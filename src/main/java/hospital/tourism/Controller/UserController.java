package hospital.tourism.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.UsersDTO;
import hospital.tourism.Entity.users;
import hospital.tourism.Service.UserService;
import hospital.tourism.repo.usersrepo;



@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/user")

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
    public ResponseEntity<UsersDTO> uploadUserDocuments(
            @PathVariable Long empId,
            @RequestPart(required = false) MultipartFile profilePicture,
            @RequestPart(required = false) MultipartFile prescription,
            @RequestPart(required = false) MultipartFile patientaxraysUrl,
            @RequestPart(required = false) MultipartFile patientreportsUrl
    ) throws IOException {
        UsersDTO updatedUser = userService.uploadFilesAndUpdateUser(empId, profilePicture, prescription, patientaxraysUrl, patientreportsUrl);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/get-patients/{empId}")
	public ResponseEntity<UsersDTO> getById(@PathVariable Long empId) {
    	UsersDTO res= userService.getUserById(empId);
    	        if (res != null) {
    	        	 return ResponseEntity.ok(res);
    	        } else {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	        }
	}
    
    @GetMapping("/get-doc-and-adrs/{empId}")
	public ResponseEntity<UsersDTO> getDocAndAdrs(@PathVariable Long empId) {
		UsersDTO userDocuments = userService.getAllDocuments(empId);
		if (userDocuments != null) {
			return ResponseEntity.ok(userDocuments);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
	
	@PutMapping("/update-user/{empId}")
	public ResponseEntity<UsersDTO> updateUser(@PathVariable Long empId, @RequestBody UsersDTO userDto) {
		try {
			UsersDTO updatedUser = userService.updateUserDetails(empId, userDto);
			return ResponseEntity.ok(updatedUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	//get all users pricriptions
	@GetMapping("/get-all-prescriptions")
	public ResponseEntity<?> getAllPrescriptions() {
		try {
			return ResponseEntity.ok(userService.getAllPriscriptions());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	
}
