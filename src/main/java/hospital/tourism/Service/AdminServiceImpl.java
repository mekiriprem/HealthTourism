package hospital.tourism.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.repo.AdminRepository;

@Service
public class AdminServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 15 * 60 * 1000; // 15 minutes

    private final Map<String, Integer> loginAttempts = new HashMap<>();
    private final Map<String, Long> lockoutTimestamps = new HashMap<>();

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new sub-admin with a temporary password sent via email
     */
    @Transactional
    public AdminEntity registerSubAdmin(AdminEntity subAdmin) {
        if (subAdmin.getAdminEmail() == null || subAdmin.getAdminEmail().isEmpty()) {
            logger.warn("Registration failed: Email is required");
            throw new IllegalArgumentException("Email is required");
        }

        if (!subAdmin.getAdminEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Registration failed: Invalid email format for {}", subAdmin.getAdminEmail());
            throw new IllegalArgumentException("Invalid email format");
        }

        if (subAdmin.getAdminName() == null || subAdmin.getAdminName().isEmpty()) {
            logger.warn("Registration failed: Name is required");
            throw new IllegalArgumentException("Name is required");
        }

        Optional<AdminEntity> existingAdmin = adminRepository.findByAdminEmail(subAdmin.getAdminEmail());
        if (existingAdmin.isPresent()) {
            logger.warn("Registration failed: Email already registered - {}", subAdmin.getAdminEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        // Generate temporary password (resetToken)
        String resetToken = UUID.randomUUID().toString().substring(0, 8); // Shorten for usability
        subAdmin.setAdminPassword(passwordEncoder.encode(resetToken)); // Store encoded password

        logger.info("Registering sub-admin with email: {}", subAdmin.getAdminEmail());
        AdminEntity savedAdmin = adminRepository.save(subAdmin);
        logger.info("Sub-admin registered successfully: {}", subAdmin.getAdminEmail());

        sendRegistrationEmail(savedAdmin.getAdminEmail(), savedAdmin.getAdminName(), resetToken);

        return savedAdmin;
    }

    /**
     * Login method with lockout logic
     */
    public AdminEntity login(String email, String password) {
        if (lockoutTimestamps.containsKey(email)) {
            long lockTime = lockoutTimestamps.get(email);
            if (System.currentTimeMillis() - lockTime < LOCKOUT_DURATION) {
                throw new IllegalStateException("Account is temporarily locked due to multiple failed login attempts. Please try again later.");
            } else {
                // Lock expired, remove lock
                lockoutTimestamps.remove(email);
                loginAttempts.remove(email);
            }
        }

        Optional<AdminEntity> optionalAdmin = adminRepository.findByAdminEmail(email);
        if (optionalAdmin.isEmpty()) {
            incrementFailedAttempts(email);
            throw new IllegalArgumentException("Invalid email or password");
        }

        AdminEntity admin = optionalAdmin.get();

        if (!passwordEncoder.matches(password, admin.getAdminPassword())) {
            incrementFailedAttempts(email);
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Successful login, reset attempts
        loginAttempts.remove(email);
        lockoutTimestamps.remove(email);

        return admin;
    }

    private void incrementFailedAttempts(String email) {
        int attempts = loginAttempts.getOrDefault(email, 0);
        attempts++;
        loginAttempts.put(email, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockoutTimestamps.put(email, System.currentTimeMillis());
            logger.warn("Account locked due to too many failed attempts: {}", email);
        }
    }

    private void sendRegistrationEmail(String email, String name, String resetToken) {
        if (email.contains("\n") || email.contains("\r") || name.contains("\n") || name.contains("\r")) {
            logger.warn("Failed to send email: Invalid email or name format - {}", email);
            throw new IllegalArgumentException("Invalid email or name format");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Sub-Admin Registration - Hospital Tourism");

        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your sub-admin account has been successfully created.\n\n" +
            "Login Email: %s\n" +
            "Temporary Password: %s\n\n" +
            "Please log in and change your password immediately.\n\n" +
            "Best regards,\n" +
            "Hospital Tourism Team",
            name, email, resetToken
        ));

        try {
            mailSender.send(message);
            logger.info("Registration email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send registration email to {}: {}", email, e.getMessage());
        }
    }
}
