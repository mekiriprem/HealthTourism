package hospital.tourism.Controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.HospitalDTO;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Hospital;
import hospital.tourism.Service.HospitalService;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @Autowired
    private HospitalService hospitalService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addHospital(
            @RequestParam("hospitalName") String name,
            @RequestParam("hospitalDescription") String description,
            @RequestParam("hospitalRating") String ratingStr,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("hospitalAdress") String address,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            // Validations
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Hospital name is required");
            }
            if (locationId == null) {
                return ResponseEntity.badRequest().body("Location ID is required");
            }
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is required");
            }

            // Parse rating
            Double rating;
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 5) {
                    return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid rating format");
            }

            // Build Hospital entity
            Hospital hospital = new Hospital();
            hospital.setHositalName(name);  // ✅ Fixed typo here
            hospital.setHospitalDescription(description);
            hospital.setRating(ratingStr);
            hospital.setAddress(address);
            hospital.setStatus("null");

            // Upload image to Supabase
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/hospital-images/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
            ResponseEntity<String> uploadResponse = new RestTemplate().exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
            }

            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/hospital-images/" + fileName;
            hospital.setHospitalImage(publicImageUrl);

            Hospital savedHospital = hospitalService.saveHospital(hospital, locationId);
            return ResponseEntity.ok(savedHospital);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving hospital: " + e.getMessage());
        }
    }


    @GetMapping("/getall/hospitals")
    public List<HospitalDTO> getAllHospitals() {
        return hospitalService.getAllHospitalsAsDto();
    }

    
    @GetMapping("/{hospitalId}")
    public List<Doctors> getDoctorsByHospital(@PathVariable Integer hospitalId) {
        return hospitalService.getDoctorsByHospitalId(hospitalId);
    }

}