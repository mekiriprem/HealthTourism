package hospital.tourism.Controller;



import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.AdminUpdateRequest;
import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.Service.AdminServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"},allowCredentials = "true")
@RestController
@RequestMapping("/sub/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceimpl;

    // Register new sub-admin
    @PostMapping("/subadminregister")
    public ResponseEntity<?> registerSubAdmins(@RequestBody AdminEntity admin) {
        try {
        	AdminEntity savedAdmin = adminServiceimpl.registerSubAdmin(admin);
            return ResponseEntity.ok(savedAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login with email and password
    @PostMapping("/login/{adminemail}/{adminpassword}")
    public ResponseEntity<AdminEntity> loginAdmin(@PathVariable String adminemail,@PathVariable String adminpassword) {
	
    	AdminEntity admin = adminServiceimpl.loggingig(adminemail, adminpassword);
			
				return ResponseEntity.ok(admin);
	
    }
      // Create default admin for testing (remove in production)
//    @PostMapping("/create-default-admin")
//    public ResponseEntity<?> createDefaultAdmin() {
//        try {
//            // Check if admin already exists
//            Optional<AdminEntity> existingAdmin = adminServiceimpl.findByEmail("admin@gmail.com");
//            if (existingAdmin.isPresent()) {
//                return ResponseEntity.badRequest().body("Admin already exists with email: admin@gmail.com");
//            }
//            
//            AdminEntity defaultAdmin = new AdminEntity();
//            defaultAdmin.setAdminName("Super Admin");
//            defaultAdmin.setAdminEmail("admin@gmail.com");
//            defaultAdmin.setRole("admin");
//            defaultAdmin.setStatus("active");
//            
//            // Create admin with specific password
//            AdminEntity savedAdmin = adminServiceimpl.createAdminWithPassword(defaultAdmin, "admin@123");
//            return ResponseEntity.ok("Default admin created: " + savedAdmin.getAdminEmail() + " with password: admin123");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to create default admin: " + e.getMessage());
//        }
//    }
    
    // Delete sub-admin by ID
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
	public ResponseEntity<List<AdminEntity>> getAllSubAdminss() {
			return ResponseEntity.ok(adminServiceimpl.getAllSubAdmins());
		
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
    	
    	@PutMapping("/subadmin/update/{adminId}")
        public ResponseEntity<?> updateSubAdmin(
                @PathVariable Integer adminId,
                @RequestBody AdminEntity updatedAdmin) {

            try {
                AdminEntity updated = adminServiceimpl.updateSubAdmin(adminId, updatedAdmin);
                return ResponseEntity.ok(updated);
            } catch (IllegalArgumentException | EntityNotFoundException e) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Something went wrong"));
            }
        }
    	 // Get sub-admin by ID
    	 @GetMapping("/get-subadmin/{adminId}")
    	 public ResponseEntity<AdminEntity> getSubAdminById(@PathVariable Integer adminId) {
       
        	AdminEntity admin = adminServiceimpl.getSubAdminById(adminId);
            return ResponseEntity.ok(admin);
       
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
    
    	 
    	 
			/*Update the subAdmin*/
    	 
    	 @PutMapping("/profile/{id}")
    	 public ResponseEntity<AdminEntity> updateSubAdminProfile(
    	         @PathVariable("id") Integer adminId,
    	         @RequestBody AdminUpdateRequest request) {
    	     AdminEntity updatedAdmin = adminServiceimpl.updateSubAdminPersonalDetails(adminId, request);
    	     return ResponseEntity.ok(updatedAdmin);
    	 }	
    	
    	 @PutMapping("/admin/{id}/permissions/remove")
    	 public ResponseEntity<String> removeAdminPermissions(
    	         @PathVariable("id") Integer adminId,
    	         @RequestBody List<String> permissionsToRemove) {
    		 adminServiceimpl.removeSelectedPermissions(adminId, permissionsToRemove);
    	     return ResponseEntity.ok("Selected permissions removed successfully.");
    	 }
    	 
    	 
    	 @PostMapping("/forgot-password")
    	 public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    	     try {
    	         String message = adminServiceimpl.forgotPassword(email);
    	         return ResponseEntity.ok(Map.of("message", message));
    	     } catch (IllegalArgumentException e) {
    	         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    	     } catch (Exception e) {
    	         return ResponseEntity.status(500).body(Map.of("error", "Server error: " + e.getMessage()));
    	     }
    	 }

}
