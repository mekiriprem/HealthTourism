package hospital.tourism.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

        String normalizedEmail = subAdmin.getAdminEmail().trim().toLowerCase();
        subAdmin.setAdminEmail(normalizedEmail);

        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Registration failed: Invalid email format for {}", normalizedEmail);
            throw new IllegalArgumentException("Invalid email format");
        }

        if (subAdmin.getAdminName() == null || subAdmin.getAdminName().isEmpty()) {
            logger.warn("Registration failed: Name is required");
            throw new IllegalArgumentException("Name is required");
        }

        Optional<AdminEntity> existingAdmin = adminRepository.findByAdminEmail(normalizedEmail);
        if (existingAdmin.isPresent()) {
            logger.warn("Registration failed: Email already registered - {}", normalizedEmail);
            throw new IllegalArgumentException("Email already registered");
        }

        // Generate temporary password
        String resetToken = UUID.randomUUID().toString().substring(0, 8);
        subAdmin.setAdminPassword(passwordEncoder.encode(resetToken));

        try {
            logger.info("Registering sub-admin with email: {}", normalizedEmail);
            AdminEntity savedAdmin = adminRepository.save(subAdmin);
            logger.info("Sub-admin registered successfully: {}", normalizedEmail);
            sendRegistrationEmail(normalizedEmail, savedAdmin.getAdminName(), resetToken);
            return savedAdmin;
        } catch (DataIntegrityViolationException e) {
            logger.error("Email conflict on save: {}", normalizedEmail);
            throw new IllegalArgumentException("Email already registered");
        }
    }

    /**
     * Login method with lockout logic
     */
    public AdminEntity login(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        if (lockoutTimestamps.containsKey(normalizedEmail)) {
            long lockTime = lockoutTimestamps.get(normalizedEmail);
            if (System.currentTimeMillis() - lockTime < LOCKOUT_DURATION) {
                throw new IllegalStateException("Account is temporarily locked due to multiple failed login attempts. Please try again later.");
            } else {
                lockoutTimestamps.remove(normalizedEmail);
                loginAttempts.remove(normalizedEmail);
            }
        }

        Optional<AdminEntity> optionalAdmin = adminRepository.findByAdminEmail(normalizedEmail);
        if (optionalAdmin.isEmpty()) {
            incrementFailedAttempts(normalizedEmail);
            throw new IllegalArgumentException("Invalid email or password");
        }

        AdminEntity admin = optionalAdmin.get();
        if (!passwordEncoder.matches(password, admin.getAdminPassword())) {
            incrementFailedAttempts(normalizedEmail);
            throw new IllegalArgumentException("Invalid email or password");
        }

        loginAttempts.remove(normalizedEmail);
        lockoutTimestamps.remove(normalizedEmail);

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
    
	/**
	 * Update sub-admin details
	 */
    @Transactional
    public AdminEntity updateSubAdmin(AdminEntity subAdmin) {
        if (subAdmin.getAdminId() == null) {
            logger.warn("Update failed: Admin ID is required");
            throw new IllegalArgumentException("Admin ID is required");
        }

        Optional<AdminEntity> existingAdmin = adminRepository.findById(subAdmin.getAdminId());
        if (existingAdmin.isPresent()) {
            AdminEntity admn = existingAdmin.get();
            admn.setAdminName(subAdmin.getAdminName());
            admn.setAdminEmail(subAdmin.getAdminEmail());
            admn.setAdminPassword(subAdmin.getAdminPassword());
            return adminRepository.save(admn); // Save and return updated entity
        } else {
            logger.warn("Update failed: Admin with ID {} not found", subAdmin.getAdminId());
            throw new EntityNotFoundException("Admin not found with ID: " + subAdmin.getAdminId());
        }
    }

        /**
         * Delete sub-admin by ID
         * */
         @Transactional
         public void deleteSubAdmin(Integer adminId) {
				if (adminId == null) {
					logger.warn("Delete failed: Admin ID is required");
					throw new IllegalArgumentException("Admin ID is required");
				}
				Optional<AdminEntity> existingAdmin = adminRepository.findById(adminId);
				if (existingAdmin.isPresent()) {
					adminRepository.deleteById(adminId);
					logger.info("Sub-admin with ID {} deleted successfully", adminId);
				} else {
					logger.warn("Delete failed: Admin with ID {} not found", adminId);
					throw new EntityNotFoundException("Admin not found with ID: " + adminId);
				}
         }
         
         /**
          * get all sub-admins
          * 
          */
         public List<AdminEntity> getAllSubAdmins() {
             return adminRepository.findAll();         
         }
         
		public AdminEntity getSubAdminById(Integer adminId) {
            return adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Sub-admin not found with ID: " + adminId));
		}
		
		
}
