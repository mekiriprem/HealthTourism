package hospital.tourism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.Service.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "http://localhost:8082")
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
    
    @PutMapping("/update-subadmin")
    public ResponseEntity<?> updateSubAdmin(@RequestBody AdminEntity subAdmin) {
        try {
            AdminEntity updatedAdmin = adminServiceimpl.updateSubAdmin(subAdmin);
            return ResponseEntity.ok(updatedAdmin);
           } catch (IllegalArgumentException e) {
        	               return ResponseEntity.badRequest().body(e.getMessage());
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
			log.error("Sub-admin status updated successfully: {}", updatedAdmin);
            return ResponseEntity.ok(updatedAdmin);
            } catch (IllegalArgumentException e) {
            	return ResponseEntity.badRequest().body(e.getMessage());
            }
    	}
    	
    	 @PutMapping("/update-subadmin/{adminId}")
    	    public AdminEntity updateSubAdmin(
    	            @PathVariable Integer adminId,
    	            @RequestBody AdminEntity updatedAdmin
    	    ) {
    	        return adminServiceimpl.updateSubAdmin(adminId, updatedAdmin);
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
    
}
