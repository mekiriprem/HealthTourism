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

import hospital.tourism.Dto.SpaServiceDTO;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Service.SpaServicessImpl;

@RestController
@RequestMapping("/spaServices")
public class SpaServicesController {

	
    @Autowired
    private SpaServicessImpl spaServicessImpl;

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSpaService(
            @RequestParam("serviceName") String serviceName,
            @RequestParam("description") String description,
            @RequestParam("spaCenterId") Integer spaCenterId,
            @RequestParam("spaPrice") Double spaPrice,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is required");
            }

            // Upload to Supabase
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/spa-service-images/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
            ResponseEntity<String> uploadResponse = new RestTemplate().exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
            }

            // Create DTO
            String imageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/spa-service-images/" + fileName;
            SpaServiceDTO dto = new SpaServiceDTO();
            dto.setServiceName(serviceName);
            dto.setServiceDescription(description);
            dto.setPrice(spaPrice);
            dto.setSpaCenterId(spaCenterId);
            dto.setServiceImage(imageUrl);

            // Save service
            SpaServicese savedEntity = spaServicessImpl.saveSpaService(dto);
            SpaServiceDTO responseDTO = convertToDTO(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading spa service: " + e.getMessage());
        }
    }



	
	
    @GetMapping("/bySpaCenter/{spaId}")
    public ResponseEntity<List<SpaServiceDTO>> getServicesBySpaId(@PathVariable Long spaId) {
        List<SpaServicese> services = spaServicessImpl.getServicesBySpaCenterId(spaId);

        List<SpaServiceDTO> dtos = services.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

//     Converts SpaService entity to SpaServiceDTO
    private SpaServiceDTO convertToDTO(SpaServicese spaService) {
        SpaServiceDTO dto = new SpaServiceDTO();
        dto.setServiceName(spaService.getServiceName());
        dto.setServiceDescription(spaService.getServiceDescription());
        dto.setServiceImage(spaService.getServiceImage());
        dto.setRating(spaService.getRating());
        dto.setPrice(spaService.getPrice());   	
        return dto;
    }
    @GetMapping("/getAll/spaServices")
	public ResponseEntity<List<SpaServiceDTO>> getAllSpaServices() {
		List<SpaServiceDTO> services = spaServicessImpl.getAllServicesss();
		return ResponseEntity.ok(services);
	}
    
    @GetMapping("spa/{spaId}")
	public ResponseEntity<SpaServiceDTO> getSpaServiceById(@PathVariable Long spaId) {
		SpaServiceDTO spaService = spaServicessImpl.getoneRecordById(spaId);
		return ResponseEntity.ok(spaService);
	}
    @PutMapping("/updateSpaService/{spaId}")
	public String deleteSoftly(@PathVariable Long spaId) {
		 spaServicessImpl.softDeleteSpaService(spaId);
		return "deleted successfully";
	}
    
    @PutMapping("/activate/{id}")
    public String activateSpaService(@PathVariable Long id) {
        spaServicessImpl.activateIfInactive(id);
       return "Spa Service with ID " + id + " has been activated successfully.";
    }
    @PutMapping("/update-spaService/{id}")
	public ResponseEntity<SpaServiceDTO> updateSpaService(@PathVariable Long id,
			@RequestBody SpaServiceDTO spaServiceDto) {
		SpaServiceDTO updatedSpaService = spaServicessImpl.updateSpaService(id, spaServiceDto);
		return ResponseEntity.ok(updatedSpaService);
	}
    
    @GetMapping("/getSpaService/{id}")
	public ResponseEntity<SpaServiceDTO> getSpaServiceByIds(@PathVariable Long id) {
		SpaServiceDTO spaService = spaServicessImpl.getoneRecordById(id);
		return ResponseEntity.ok(spaService);
	}
}