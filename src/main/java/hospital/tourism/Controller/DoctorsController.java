package hospital.tourism.Controller;

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

import hospital.tourism.Dto.DoctorsDTO;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Service.DoctorSevice;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
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


//    @PostMapping("/doctorsupload")
//    public ResponseEntity<?> uploadDoctor(
//            @RequestParam("name") String name,
//            @RequestParam("email") String email,
//            @RequestParam("rating") double rating,
//            @RequestParam("description") String description,
//            @RequestParam("department") String department,
//            @RequestParam("hospitalId") int hospitalId,
//            @RequestPart("image") MultipartFile imageFile
//    ) {
//        try {
//            // Validate image size (max 500KB)
//            if (imageFile.getSize() > 500 * 1024) {
//                return ResponseEntity.badRequest().body("Image size should be less than 500KB");
//            }
//
//            if (name == null || name.isEmpty()) return ResponseEntity.badRequest().body("Doctor name is required");
//            if (email == null || email.isEmpty()) return ResponseEntity.badRequest().body("Email is required");
//
//            // Generate image file name
//            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
//            String folder = "doctor-images";
//            String objectPath = folder + "/" + fileName;
//
//            // Upload to Supabase
//            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + objectPath;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + supabaseApiKey);
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//
//            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
//            RestTemplate restTemplate = new RestTemplate();
//
//            ResponseEntity<String> uploadResponse = restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);
//
//            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
//            }
//
//            // Public image URL
//            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + objectPath;
//
//            // Prepare DoctorsDTO
//            DoctorsDTO doctorDto = new DoctorsDTO();
//            doctorDto.setName(name);
//            doctorDto.setEmail(email);
//            doctorDto.setRating(rating);
//            doctorDto.setDescription(description);
//            doctorDto.setDepartment(department);
//            doctorDto.setProfilepic(publicImageUrl);
//            doctorDto.setStatus("active"); // Set default status
//
//            // Save and return response
//            DoctorsDTO savedDoctor = doctorService.saveDoctor(doctorDto, hospitalId);
//            return ResponseEntity.ok(savedDoctor);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error uploading doctor: " + e.getMessage());
//        }
//    }

    @GetMapping("/doctors")
    public List<Doctors> getDoctors() {
        return doctorService.getAllDoctors();
    }
    
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorsDTO> getDoctorById(@PathVariable Long id) {
        DoctorsDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteDoctor(@PathVariable Long id) {
        doctorService.softDeleteDoctor(id);
        return ResponseEntity.ok("Doctor with ID " + id + " has been soft deleted.");
    }
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreDoctor(@PathVariable Long id) {
        doctorService.restoreDoctor(id);
        return ResponseEntity.ok("Doctor with ID " + id + " has been restored to active.");
    }
    @GetMapping("/doctors/status/{status}")
	public ResponseEntity<List<Doctors>> doctorsByStatus(@PathVariable String status) {
		List<Doctors> doctors = doctorService.getDoctorsByStatus(status);
		return ResponseEntity.ok(doctors);
	}
    
    //update doctor
    @PutMapping("/doctors/update/{id}")
    public ResponseEntity<DoctorsDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorsDTO doctorDTO) {
        DoctorsDTO updated = doctorService.updateDoctor(id, doctorDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/doctors/deletebyid/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor with ID " + id + " has been deleted.");
    }

    @GetMapping("/doctors/By/{doctorId}")
	public ResponseEntity<DoctorsDTO> getDoctorByIds(@PathVariable Long doctorId) {
		DoctorsDTO doctor = doctorService.getDoctorById(doctorId);
		return ResponseEntity.ok(doctor);
	}
    
    // Get doctors by hospital ID
    @GetMapping("/doctors/hospital/{hospitalId}")
    public ResponseEntity<List<Doctors>> getDoctorsByHospitalId(@PathVariable Integer hospitalId) {
        List<Doctors> doctors = doctorService.getDoctorsByHospitalId(hospitalId);
        return ResponseEntity.ok(doctors);
    }
}
