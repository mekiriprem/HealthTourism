package hospital.tourism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Entity.users;
import hospital.tourism.Service.UserService;
import hospital.tourism.repo.usersrepo;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8082")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private usersrepo userRepository;

    // ✅ Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody users user) {
        try {
            users savedUser = userService.registerUser(user);
            return ResponseEntity.ok("Registration successful. Please check your email for verification link.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Your email is already registered, use another email. Thank you.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred. Please try again.");
        }
    }


    // ✅ Verify Email
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        users user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token."));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    // ✅ Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody users loginRequest) {
        users user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok( user);
    }
    
    @PostMapping(value = "/upload-files/{empId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFilesAndAddress(
            @PathVariable Long empId,
            @RequestPart(required = false) MultipartFile profilePicture,
            @RequestPart(required = false) MultipartFile prescription,
            @RequestPart(required = false) MultipartFile patientaxraysUrl,
            @RequestPart(required = false) MultipartFile patientreportsUrl,
            @RequestParam(required = false) String address
    ) {
        try {
            users updatedUser = userService.updateUserFilesAndAddress(empId, profilePicture, prescription,patientaxraysUrl,patientreportsUrl, address);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/get-patients/{empId}")
	public ResponseEntity<?> getById(@PathVariable Long empId) {
		try {
			users user = userRepository.findById(empId)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));
			return ResponseEntity.ok(user);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}
    
    @GetMapping("/get-doc-and-adrs/{empId}")
	public ResponseEntity<?> getDocAndAdrs(@PathVariable Long empId) {
		try {
			users user = userRepository.findById(empId)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));
			return ResponseEntity.ok(user);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@GetMapping("/get-all-users")
	
	public ResponseEntity<?> getAllUsers() {
		try {
			return ResponseEntity.ok(userService.getAllUsers());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}


}
