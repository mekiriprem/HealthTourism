package hospital.tourism.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.users;
import hospital.tourism.repo.usersrepo;

@Service
public class UserService {
	
			@Autowired
			private usersrepo userRepository;
	 	@Autowired
	    private JavaMailSender mailSender;

	    public users registerUser(users user) {
	        user.setVerificationToken(UUID.randomUUID().toString());
	        user.setEmailVerified(false);
	        users savedUser = userRepository.save(user);
	        sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
	        return savedUser;
	    }
	    private void sendVerificationEmail(String toEmail, String token) {
	        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject("Verify your email");
	        message.setText("Click the link to verify your email: " + verificationUrl);
	        message.setFrom("premmekiri22@gmail.com");
	        mailSender.send(message);
	    }
	    
	    public users loginUser(String email, String password) {
	        users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	        // ✅ Check email verified
	        if (!user.isEmailVerified()) {
	            throw new RuntimeException("Please verify your email before logging in.");
	        }

	        // ✅ Validate password
	        if (!user.getPassword().equals(password)) {
	            throw new RuntimeException("Invalid email or password.");
	        }
	    

		return user;
	}
	    public List<users> getallUsers() {
	        return userRepository.findAll();
	    }

}
		
