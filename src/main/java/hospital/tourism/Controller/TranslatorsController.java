package hospital.tourism.Controller;

import hospital.tourism.Dto.TranslatorDTO;
import hospital.tourism.Entity.Translators;
import hospital.tourism.Service.TranslatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/translators")
public class TranslatorsController {

    @Autowired
    private TranslatorsService translatorsService;

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addTranslator(
            @RequestParam("translatorName") String name,
            @RequestParam("translatorDescription") String description,
            @RequestParam("translatorRating") String ratingStr,
            @RequestParam("translatorLanguages") String languages,
            @RequestParam("locationId") Integer locationId,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Translator name is required");
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

            // Build Translator entity
            Translators translator = new Translators();
            translator.setTranslatorName(name);
            translator.setTranslatorDescription(description);
            translator.setTranslatorRating(ratingStr);
            translator.setTranslatorLanguages(languages);
            translator.setStatus("NUll");

            // Build storage path
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String folder = "translator-images";
            String objectPath = folder + "/" + fileName;

            // Upload to Supabase Storage via POST
            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + objectPath;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> uploadResponse = restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
            }

            // Set public image URL
            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + objectPath;
            translator.setTranslatorImage(publicImageUrl);

            Translators saved = translatorsService.saveTranslator(translator, locationId);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving translator: " + e.getMessage());
        }
    }



    // Get Translators by Location ID
    @GetMapping("/by-location/{locationId}")
    public ResponseEntity<List<Translators>> getTranslatorsByLocation(@PathVariable Integer locationId) {
        try {
            List<Translators> translators = translatorsService.getTranslatorsByLocationId(locationId);
            return ResponseEntity.ok(translators);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get All Translators
    @GetMapping("/getAll/traslators")
    public ResponseEntity<List<TranslatorDTO>> getAllTranslators() {
        try {
            List<TranslatorDTO> translators = translatorsService.getAllTranslators();
            return ResponseEntity.ok(translators);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get Translator by ID
    @GetMapping("/getone/{trId}")
	public ResponseEntity<TranslatorDTO> getonetranslator(@PathVariable Long trId) {
		try {
			TranslatorDTO translator = translatorsService.getbytraslatorId(trId);
			if (translator == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(translator);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
    @PutMapping("/translators/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        translatorsService.softDeleteTranslator(id);
        return ResponseEntity.ok("✅ Translator marked as Inactive successfully");
    }
    @PutMapping("/activate/{id}")
    public void activateTranslator(@PathVariable Long id) {
        translatorsService.activateTranslatorIfInactive(id);
    }

    @PutMapping("/update-translator/{id}")
	public ResponseEntity<TranslatorDTO> updateTranslator(@PathVariable Long id,
			@RequestBody TranslatorDTO translatorDto) {
		try {
			TranslatorDTO updatedTranslator = translatorsService.updateTraslator(id, translatorDto);
			return ResponseEntity.ok(updatedTranslator);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}