package hospital.tourism.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Service.DoctorSevice;

@RestController
@RequestMapping("/api")
public class DoctorsController {

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
            @RequestParam("image") MultipartFile imageFile
    ) {
        String profilePicUrl = null;

        // Image size validation: Max 500KB
        if (imageFile.getSize() > 500 * 1024) {
            return ResponseEntity.badRequest().body("Image size should be less than 500KB");
        }

        // Save file
        if (!imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/doctors/");
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                profilePicUrl = "/uploads/doctors/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
            }
        }

        Doctors doctor = new Doctors();
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setRating(rating);
        doctor.setDescription(description);
        doctor.setDepartment(department);
        doctor.setProfilepic(profilePicUrl);

        Doctors savedDoctor = doctorService.saveDoctor(doctor, hospitalId);
        return ResponseEntity.ok(savedDoctor);
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
