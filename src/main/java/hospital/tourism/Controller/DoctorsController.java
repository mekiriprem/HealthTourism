package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Service.DoctorSevice;

@RestController
@RequestMapping("/api")
public class DoctorsController {
    
    @Autowired
    private DoctorSevice doctorService;

    @PostMapping("/doctorsupload")
    public Doctors uploadDoctor(@RequestBody Map<String, Object> doctorMap) {
        Doctors doctor = new Doctors();
        doctor.setName((String) doctorMap.get("name"));
        doctor.setEmail((String) doctorMap.get("email"));
        doctor.setRating(Double.parseDouble(doctorMap.get("rating").toString()));
        doctor.setDescription((String) doctorMap.get("description"));
        doctor.setDepartment((String) doctorMap.get("department"));
        doctor.setProfilepic((String) doctorMap.get("profilepic"));
        Integer hospitalId = Integer.parseInt(doctorMap.get("hospitalId").toString());

        return doctorService.saveDoctor(doctor, hospitalId);
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
