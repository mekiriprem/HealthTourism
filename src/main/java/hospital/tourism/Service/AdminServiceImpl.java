package hospital.tourism.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                logger.warn("Account locked for {}. Try again later.", normalizedEmail);
                throw new IllegalArgumentException("Account locked. Please try again later.");
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

        // Role-based access check
        if ("subadmin".equalsIgnoreCase(admin.getRole())) {
            // Optional: Add specific permission checks for sub-admins if needed
            if (admin.getPermissions() == null || admin.getPermissions().isEmpty()) {
                logger.warn("Sub-admin {} has no permissions.", normalizedEmail);
                // Depending on requirements, you might want to deny login if no permissions are assigned
            }
        } else if (!"admin".equalsIgnoreCase(admin.getRole())) {
            logger.warn("Login failed: Unknown role for user {}", normalizedEmail);
            throw new IllegalArgumentException("Access denied: Invalid role");
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
         * Delete admin or sub-admin by ID
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
				logger.info("Admin with ID {} deleted successfully", adminId);
			} else {
				logger.warn("Delete failed: Admin with ID {} not found", adminId);
				throw new EntityNotFoundException("Admin not found with ID: " + adminId);
			}
         }         /**
          * get all sub-admins (only users with role = 'subadmin')
          * 
          */
         public List<AdminEntity> getAllSubAdmins() {
             return adminRepository.findAll().stream()
                     .filter(admin -> "subadmin".equalsIgnoreCase(admin.getRole()))
                     .collect(Collectors.toList());         
         }
         
	
		
		//soft delete sub-admin by ID
		@Transactional
		public void softDeleteSubAdmin(Integer adminId) {
			if (adminId == null) {
				logger.warn("Soft delete failed: Admin ID is required");
				throw new IllegalArgumentException("Admin ID is required");
			}
			Optional<AdminEntity> existingAdmin = adminRepository.findById(adminId);
			if (existingAdmin.isPresent()) {
				adminRepository.deleteById(adminId);
				logger.info("Sub-admin with ID {} soft deleted successfully", adminId);
			} else {
				logger.warn("Soft delete failed: Admin with ID {} not found", adminId);
				throw new EntityNotFoundException("Admin not found with ID: " + adminId);
			}
		}
		
		//List<AdminEntity> allAdmins = adminRepository.findAllAdmins();
		public List<AdminEntity> getAllAdmins() {
			return adminRepository.findAllAdmins();
		}
		
		//update sub-admin status
		@Transactional
		public AdminEntity updateSubAdminStatus(Integer adminId) {
			
			Optional<AdminEntity> existingAdmin = adminRepository.findById(adminId);
			if (existingAdmin.isPresent()) {
                AdminEntity admin = existingAdmin.get();
                admin.setStatus("active");
                return adminRepository.save(admin);
            } else {
				throw new EntityNotFoundException("Admin not found with ID: " + adminId);
			}
		}
		
		public AdminEntity updateSubAdmin(Integer adminId, AdminEntity updatedAdmin) {
	        Optional<AdminEntity> optionalAdmin = adminRepository.findById(adminId);
	        if (optionalAdmin.isPresent()) {
	            AdminEntity existingAdmin = optionalAdmin.get();
	            existingAdmin.setAdminName(updatedAdmin.getAdminName());
	            existingAdmin.setAdminEmail(updatedAdmin.getAdminEmail());
	            existingAdmin.setEmployeeId(updatedAdmin.getEmployeeId());
	            existingAdmin.setPermissions(updatedAdmin.getPermissions() != null ? updatedAdmin.getPermissions() : Collections.emptyList());

	            // Do not update password here unless a specific flow for it exists
	            return adminRepository.save(existingAdmin);
	        } else {
	            throw new EntityNotFoundException("Admin not found with ID: " + adminId);
	        }
	    }
				//get sub-admin by id
		public AdminEntity getSubAdminById(Integer adminId) {
			AdminEntity admin = adminRepository.findById(adminId)
					.orElseThrow(() -> new EntityNotFoundException("Sub-admin not found with ID: " + adminId));
			
			// Verify that this is actually a sub-admin
			if (!"subadmin".equalsIgnoreCase(admin.getRole())) {
				throw new IllegalArgumentException("Admin with ID " + adminId + " is not a sub-admin");
			}
			
			return admin;
		}
		
		//get sub-admin by employee ID
		public AdminEntity getSubAdminByEmployeeId(String employeeId) {
			if (employeeId == null || employeeId.trim().isEmpty()) {
				throw new IllegalArgumentException("Employee ID is required");
			}
			
			List<AdminEntity> allAdmins = adminRepository.findAll();
			Optional<AdminEntity> admin = allAdmins.stream()
					.filter(a -> employeeId.equals(a.getEmployeeId()) && "subadmin".equalsIgnoreCase(a.getRole()))
					.findFirst();
			
			if (admin.isEmpty()) {
				throw new EntityNotFoundException("Sub-admin not found with Employee ID: " + employeeId);
			}
			
			return admin.get();
		}
		
		/**
         * Find admin by email (public method for controller use)
         */
        public Optional<AdminEntity> findByEmail(String email) {
            String normalizedEmail = email.trim().toLowerCase();
            return adminRepository.findByAdminEmail(normalizedEmail);
        }

        /**
         * Create admin with specific password (for default admin creation)
         */
        @Transactional
        public AdminEntity createAdminWithPassword(AdminEntity admin, String password) {
            if (admin.getAdminEmail() == null || admin.getAdminEmail().isEmpty()) {
                logger.warn("Admin creation failed: Email is required");
                throw new IllegalArgumentException("Email is required");
            }

            String normalizedEmail = admin.getAdminEmail().trim().toLowerCase();
            admin.setAdminEmail(normalizedEmail);

            if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                logger.warn("Admin creation failed: Invalid email format for {}", normalizedEmail);
                throw new IllegalArgumentException("Invalid email format");
            }

            if (admin.getAdminName() == null || admin.getAdminName().isEmpty()) {
                logger.warn("Admin creation failed: Name is required");
                throw new IllegalArgumentException("Name is required");
            }

            if (password == null || password.isEmpty()) {
                logger.warn("Admin creation failed: Password is required");
                throw new IllegalArgumentException("Password is required");
            }

            Optional<AdminEntity> existingAdmin = adminRepository.findByAdminEmail(normalizedEmail);
            if (existingAdmin.isPresent()) {
                logger.warn("Admin creation failed: Email already registered - {}", normalizedEmail);
                throw new IllegalArgumentException("Email already registered");
            }

            // Set the password with proper encoding
            admin.setAdminPassword(passwordEncoder.encode(password));

            try {
                logger.info("Creating admin with email: {}", normalizedEmail);
                AdminEntity savedAdmin = adminRepository.save(admin);
                logger.info("Admin created successfully: {}", normalizedEmail);
                return savedAdmin;
            } catch (DataIntegrityViolationException e) {
                logger.error("Email conflict on save: {}", normalizedEmail);
                throw new IllegalArgumentException("Email already registered");
            }
        }
	
        /**
         * Reset a sub-admin's password to a new random password
         * This will generate a new password, update it in the database, and return the new password
         */
        @Transactional
        public String resetSubAdminPassword(Integer adminId) {
            if (adminId == null) {
                logger.warn("Password reset failed: Admin ID is required");
                throw new IllegalArgumentException("Admin ID is required");
            }
            
            Optional<AdminEntity> optionalAdmin = adminRepository.findById(adminId);
            if (optionalAdmin.isEmpty()) {
                logger.warn("Password reset failed: Admin with ID {} not found", adminId);
                throw new EntityNotFoundException("Admin not found with ID: " + adminId);
            }
            
            AdminEntity admin = optionalAdmin.get();
            
            // Generate a new random password
            String newPassword = UUID.randomUUID().toString().substring(0, 8);
            
            // Update the password in the database
            admin.setAdminPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
            
            logger.info("Password reset successful for admin ID: {}", adminId);
            
            // Send email with the new password if email service is available
            try {
                sendPasswordResetEmail(admin.getAdminEmail(), admin.getAdminName(), newPassword);
            } catch (Exception e) {
                logger.error("Failed to send password reset email: {}", e.getMessage());
                // We continue even if email fails since we'll return the password to the admin
            }
            
            return newPassword;
        }
        
        /**
         * Send password reset email
         */
        private void sendPasswordResetEmail(String email, String name, String newPassword) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset - Hospital Tourism");
            
            message.setText(String.format(
                "Dear %s,\n\n" +
                "Your password has been reset.\n\n" +
                "Login Email: %s\n" +
                "New Password: %s\n\n" +
                "Please log in and change your password immediately.\n\n" +
                "Best regards,\n" +
                "Hospital Tourism Team",
                name, email, newPassword
            ));
            
            mailSender.send(message);
            logger.info("Password reset email sent to: {}", email);
        }
}
