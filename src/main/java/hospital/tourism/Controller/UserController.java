package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Entity.Translators;
import hospital.tourism.Entity.users;
import hospital.tourism.Service.UserService;
import hospital.tourism.repo.usersrepo;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private usersrepo userRepository;

    // ✅ Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody users user) {
        users savedUser = userService.registerUser(user);
        return ResponseEntity.ok("Registration successful. Please check your email for verification link.");
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
        return ResponseEntity.ok("Login successful. Welcome " + user.getName() + "!");
    }
    
    @GetMapping
    public List<users> getAllUsers() {
        return userService.getallUsers();
    }
    
    
}
