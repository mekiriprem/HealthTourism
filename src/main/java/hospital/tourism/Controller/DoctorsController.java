package hospital.tourism.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Service.DoctorSevice;

@RestController
@RequestMapping("/api")
public class DoctorsController {
    
    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @Autowired
    private DoctorSevice doctorService;

    @PostMapping("/doctorsupload")
    public ResponseEntity<?> uploadDoctor(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("rating") double rating,
            @RequestParam("description") String description,
            @RequestParam("department") String department,
            @RequestParam("hospitalId") int hospitalId,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            // Validate image size (max 500KB)
            if (imageFile.getSize() > 500 * 1024) {
                return ResponseEntity.badRequest().body("Image size should be less than 500KB");
            }

            // Validate required fields
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor name is required");
            }
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            // Generate file name
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String folder = "doctor-images";
            String objectPath = folder + "/" + fileName;

            // Upload to Supabase
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

            // Build public URL
            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + objectPath;

            // Save doctor entity
            Doctors doctor = new Doctors();
            doctor.setName(name);
            doctor.setEmail(email);
            doctor.setRating(rating);
            doctor.setDescription(description);
            doctor.setDepartment(department);
            doctor.setProfilepic(publicImageUrl);
            doctor.setStatus("Null");

            Doctors savedDoctor = doctorService.saveDoctor(doctor, hospitalId);
            return ResponseEntity.ok(savedDoctor);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading doctor: " + e.getMessage());
        }
    }



    @GetMapping("/doctors")
    public List<Doctors> getDoctors() {
        return doctorService.getAllDoctors();
    }
    
    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctors> getDoctorById(@PathVariable Long id) {
        Doctors doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

}
