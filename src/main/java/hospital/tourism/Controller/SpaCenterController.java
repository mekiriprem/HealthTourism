package hospital.tourism.Controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.SpaCenterDTO;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.Service.SpaCenterImpl;
import hospital.tourism.Service.TranslatorsService;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/spaCenter")
public class SpaCenterController {

    @Autowired
    private SpaCenterImpl spaCenterService;

    @Autowired
    private TranslatorsService translatorsService;

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveSpaCenter(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("rating") String ratingStr,
            @RequestParam("address") String address,
            @RequestParam("locationId") Integer locationId,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            // Validation
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Spa center name is required");
            }
            if (locationId == null) {
                return ResponseEntity.badRequest().body("Location ID is required");
            }
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is required");
            }

            // Parse and validate rating
            Double rating;
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 5) {
                    return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid rating format");
            }

            // Upload image to Supabase
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/spa-images/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
            ResponseEntity<String> uploadResponse = new RestTemplate().exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
            }

            // Create DTO with image URL
            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/spa-images/" + fileName;

            SpaCenterDTO spaCenterDTO = new SpaCenterDTO();
            spaCenterDTO.setSpaName(name);
            spaCenterDTO.setSpaDescription(description);
            spaCenterDTO.setAddress(address);
            spaCenterDTO.setRating(ratingStr);
            spaCenterDTO.setLocationId(locationId);
            spaCenterDTO.setSpaImage(publicImageUrl);
            spaCenterDTO.setStatus("Active"); // Default status

            SpaCenter savedSpaCenter = spaCenterService.saveSpaCenter(spaCenterDTO);
            return ResponseEntity.ok(savedSpaCenter);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving spa center: " + e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<SpaCenterDTO>> getAllSpaCenters() {
        List<SpaCenter> spaCenters = spaCenterService.getAllSpaCenters();
        List<SpaCenterDTO> dtos = spaCenters.stream().map(spa -> {
            SpaCenterDTO dto = new SpaCenterDTO();
            dto.setSpaId(spa.getSpaId());
            dto.setSpaName(spa.getSpaName());
            dto.setStatus(spa.getStatus());
            dto.setRating(spa.getRating());
            dto.setAddress(spa.getAddress());
            dto.setSpaDescription(spa.getSpaDescription());
            dto.setSpaImage(spa.getSpaImage()); // ✅ ADD THIS LINE
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    
    //update spa center
    @PutMapping("/update-spaCenter/{id}")
	public ResponseEntity<SpaCenterDTO> updateSpaCenter(@PathVariable Integer id,
			@RequestBody SpaCenterDTO spaCenterDTO) {
		try {
			SpaCenterDTO updatedSpaCenter = spaCenterService.updateSpaCenter(id, spaCenterDTO);
			return ResponseEntity.ok(updatedSpaCenter);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
    
    // Soft delete spa center
    @PutMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDeleteSpaCenter(@PathVariable Integer id) {
		try {
			spaCenterService.softDeleteSpaCenter(id);
			return ResponseEntity.ok("Spa Center soft deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	// Activate spa center
	@PutMapping("/activate/{id}")
	public ResponseEntity<String> activateSpaCenter(@PathVariable Integer id) {
		try {
			spaCenterService.activateSpaCenter(id);
			return ResponseEntity.ok("Spa Center activated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	// Get spa center by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getSpaCenterById(@PathVariable Integer id) {
		try {
			 spaCenterService.activateSpaCenter(id);
			return ResponseEntity.ok("Spa Center activated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
   
}