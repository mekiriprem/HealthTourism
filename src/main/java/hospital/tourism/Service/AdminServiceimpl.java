package hospital.tourism.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.repo.AdminRepository;

@Service
public class AdminServiceimpl {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Method to register a new Minor Admin and send approval request to Major Admin
    public String addAdmin(AdminEntity admin) {
        String token = UUID.randomUUID().toString();
        admin.setApprovalToken(token);
        admin.setIsApproved(null);  // Pending approval by Major Admin
        adminRepository.save(admin);

        sendApprovalEmailToMajorAdmin(admin); // Send approval request to Major Admin

        return "Registration request sent to Major Admin.";
    }

    // Send approval request email to Major Admin
    private void sendApprovalEmailToMajorAdmin(AdminEntity admin) {
        try {
            String token = admin.getApprovalToken();
            String approvalLink = "http://localhost:8080/admin/approve?token=" + token;
            String rejectionLink = "http://localhost:8080/admin/reject?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("anil.n@zynlogic.com");
            message.setTo("nagaanil0329@gmail.com"); // Replace with Major Admin's email
            message.setSubject("New Admin Registration Approval Required");
            message.setText("A new admin has registered:\n\n" +
                    "Name: " + admin.getAdminName() + "\n" +
                    "Email: " + admin.getAdminEmail() + "\n\n" +
                    "To APPROVE: " + approvalLink + "\n" +
                    "To REJECT: " + rejectionLink);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending approval email: " + e.getMessage());
        }
    }

    // Automatically approve the Minor Admin
    public String approveAdmin(String token) {
        Optional<AdminEntity> optional = adminRepository.findByApprovalToken(token);
        if (optional.isPresent()) {
            AdminEntity admin = optional.get();
            admin.setIsApproved(true);  // Set as approved
            admin.setApprovalToken(null); // Clear the approval token
            adminRepository.save(admin);

            sendApprovalResultEmailToMinorAdmin(admin, true);  // Send approval email to Minor Admin

            return "Admin approved successfully!";
        } else {
            return "Invalid or expired token.";
        }
    }

    // Automatically reject the Minor Admin
    public String rejectAdmin(String token) {
        Optional<AdminEntity> optional = adminRepository.findByApprovalToken(token);
        if (optional.isPresent()) {
            AdminEntity admin = optional.get();
            admin.setIsApproved(false);  // Set as rejected
            admin.setApprovalToken(null); // Clear the approval token
            adminRepository.save(admin);

            sendApprovalResultEmailToMinorAdmin(admin, false);  // Send rejection email to Minor Admin

            return "Admin has been rejected.";
        } else {
            return "Invalid or expired token.";
        }
    }

    // Method to send email to Minor Admin after approval or rejection
    private void sendApprovalResultEmailToMinorAdmin(AdminEntity admin, boolean isApproved) {
        try {
            String subject = isApproved ? "Your Admin Registration is Approved" : "Your Admin Registration is Rejected";
            String messageText = isApproved ?
                    "Congratulations! Your admin registration has been approved." :
                    "Sorry, your admin registration has been rejected.";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("anil.n@zynlogic.com");
            message.setTo(admin.getAdminEmail()); // Send email to the Minor Admin
            message.setSubject(subject);
            message.setText(messageText);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending approval result email: " + e.getMessage());
        }
    }

    // Admin login with approval check
    public Map<String, Object> adminLogin(AdminEntity inputAdmin) {
        AdminEntity dbAdmin = adminRepository.findByAdminEmailAndAdminPassword(
                inputAdmin.getAdminEmail(), inputAdmin.getAdminPassword());

        Map<String, Object> response = new HashMap<>();

        if (dbAdmin == null) {
            response.put("status", "error");
            response.put("message", "Invalid credentials");
            return response;
        }

        if (dbAdmin.getIsApproved() == null) {
            response.put("status", "pending");
            response.put("message", "Your account is pending approval by Major Admin.");
            return response;
        }

        if (!dbAdmin.getIsApproved()) {
            response.put("status", "rejected");
            response.put("message", "Your registration was rejected by Major Admin.");
            return response;
        }

        // Successful login – generate dummy token
        response.put("status", "success");
        response.put("message", "Login Successful");
        response.put("token", UUID.randomUUID().toString()); // Replace with real JWT later
        response.put("adminName", dbAdmin.getAdminName());

        return response;
    }

}
