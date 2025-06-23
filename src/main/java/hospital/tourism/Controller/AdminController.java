package hospital.tourism.Controller;

import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.Service.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000"})
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceimpl;

    // Register new sub-admin
    @PostMapping("/subadminregister")
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
    public ResponseEntity<?> loginAdmin(@RequestBody AdminEntity loginRequest) {
        try {
            log.info("Login attempt for email: {}", loginRequest.getAdminEmail());
            
            if (loginRequest.getAdminEmail() == null || loginRequest.getAdminEmail().trim().isEmpty()) {
                log.warn("Login failed: Email is required");
                return ResponseEntity.badRequest().body("Email is required");
            }
            
            if (loginRequest.getAdminPassword() == null || loginRequest.getAdminPassword().trim().isEmpty()) {
                log.warn("Login failed: Password is required");
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            AdminEntity admin = adminServiceimpl.login(
                loginRequest.getAdminEmail(),
                loginRequest.getAdminPassword()
            );
            log.info("Admin logged in successfully: {}", admin.getAdminEmail());
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException e) {
            log.error("Login failed for email {}: {}", loginRequest.getAdminEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
      // Create default admin for testing (remove in production)
    @PostMapping("/create-default-admin")
    public ResponseEntity<?> createDefaultAdmin() {
        try {
            // Check if admin already exists
            Optional<AdminEntity> existingAdmin = adminServiceimpl.findByEmail("admin@gmail.com");
            if (existingAdmin.isPresent()) {
                return ResponseEntity.badRequest().body("Admin already exists with email: admin@gmail.com");
            }
            
            AdminEntity defaultAdmin = new AdminEntity();
            defaultAdmin.setAdminName("Super Admin");
            defaultAdmin.setAdminEmail("admin@gmail.com");
            defaultAdmin.setRole("admin");
            defaultAdmin.setStatus("active");
            
            // Create admin with specific password
            AdminEntity savedAdmin = adminServiceimpl.createAdminWithPassword(defaultAdmin, "admin@123");
            return ResponseEntity.ok("Default admin created: " + savedAdmin.getAdminEmail() + " with password: admin123");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create default admin: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete-subadmin/{adminId}")
    public ResponseEntity<?> deleteSubAdmin(@PathVariable Integer adminId) {
        try {
            adminServiceimpl.deleteSubAdmin(adminId);
            return ResponseEntity.ok("Sub-admin with ID " + adminId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get-all-subadmins")
	public ResponseEntity<?> getAllSubAdmins() {
		try {
			return ResponseEntity.ok(adminServiceimpl.getAllSubAdmins());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error retrieving sub-admins: " + e.getMessage());
		}
	}
    
    //soft delete sub-admin by ID
    @DeleteMapping("/soft-delete-subadmin/{adminId}")
	public ResponseEntity<?> softDeleteSubAdmin(@PathVariable Integer adminId) {
		try {
			adminServiceimpl.softDeleteSubAdmin(adminId);
			return ResponseEntity.ok("Sub-admin with ID " + adminId + " soft deleted successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
    //get all sub-admins
    @GetMapping("/get-all")
	public ResponseEntity<?> getAllAdmins() {
		try {
			return ResponseEntity.ok(adminServiceimpl.getAllAdmins());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error retrieving admins: " + e.getMessage());
		}
	}
    
    //status update sub-admin
    	@PutMapping("/update-status/active/{adminId}")
    	    public ResponseEntity<?> updateSubAdminStatus(@PathVariable Integer adminId) {
    		        try {
            AdminEntity updatedAdmin = adminServiceimpl.updateSubAdminStatus(adminId);
			if (updatedAdmin == null) {
				return ResponseEntity.notFound().build();
			}
			log.info("Sub-admin status updated successfully: {}", updatedAdmin);
            return ResponseEntity.ok(updatedAdmin);
            } catch (IllegalArgumentException e) {
            	return ResponseEntity.badRequest().body(e.getMessage());
            }
    	}
    	
    	 @PutMapping("/update-subadmin/{adminId}")
    	    public ResponseEntity<?> updateSubAdmin(
    	            @PathVariable Integer adminId,
    	            @RequestBody AdminEntity updatedAdmin
    	    ) {
    	        try {
    	        	AdminEntity result = adminServiceimpl.updateSubAdmin(adminId, updatedAdmin);
    	        	return ResponseEntity.ok(result);
				} catch (Exception e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
    	    }
    	 // Get sub-admin by ID
    	 @GetMapping("/get-subadmin/{adminId}")
    	 public ResponseEntity<?> getSubAdminById(@PathVariable Integer adminId) {
        try {
            AdminEntity admin = adminServiceimpl.getSubAdminById(adminId);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    	 }
    
    	 // Reset a sub-admin's password
    	 @PostMapping("/reset-password/{adminId}")
    	 public ResponseEntity<?> resetSubAdminPassword(@PathVariable Integer adminId) {
        try {
            String newPassword = adminServiceimpl.resetSubAdminPassword(adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Password reset successful",
                "newPassword", newPassword
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error resetting password: " + e.getMessage());
        }
    	 }
    
    	 // Get sub-admin by Employee ID
    	 @GetMapping("/get-subadmin-by-employee/{employeeId}")
    	 public ResponseEntity<?> getSubAdminByEmployeeId(@PathVariable String employeeId) {
        try {
            AdminEntity admin = adminServiceimpl.getSubAdminByEmployeeId(employeeId);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    	 }
    
}
