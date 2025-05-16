package hospital.tourism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.Service.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceimpl;

    // Register new sub-admin
    @PostMapping("/register")
    public ResponseEntity<?> registerSubAdmin(@RequestBody AdminEntity admin) {
        try {
            AdminEntity savedAdmin = adminServiceimpl.registerSubAdmin(admin);
            return ResponseEntity.ok(savedAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login with email and password
    @PostMapping("/login")
    public ResponseEntity<AdminEntity> loginAdmin(@RequestBody AdminEntity loginRequest) {
      
            AdminEntity admin = adminServiceimpl.login(
                loginRequest.getAdminEmail(),
                loginRequest.getAdminPassword()
                
            );
            log.info("Admin logged in successfully: {}", admin);
            return ResponseEntity.ok(admin);
       
    }

}
