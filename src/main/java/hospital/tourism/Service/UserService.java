package hospital.tourism.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.UsersDTO;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.usersrepo;

@Service
public class UserService {
	
			@Autowired
			private usersrepo userRepository;
	 	@Autowired
	    private JavaMailSender mailSender;
	 	@Value("${supabase.url}")
	    private String supabaseProjectUrl;

	    @Value("${supabase.bucket}")
	    private String supabaseBucketName;

	    @Value("${supabase.api.key}")
	    private String supabaseApiKey;
	    public users registerUser(users user) {
	        user.setVerificationToken(UUID.randomUUID().toString());
	        user.setEmailVerified(false);
	        users savedUser = userRepository.save(user);
	        sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
	        return savedUser;
	    }
	    private void sendVerificationEmail(String toEmail, String token) {
	        String verificationUrl = "http://localhost:8080/user/verify?token=" + token;
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
	    

	    
	    
	    public List<UsersDTO> getAllUsers() {
	        List<users> userList = userRepository.findAll();

	        return userList.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

		private UsersDTO convertToDTO(users user) {
            UsersDTO dto = new UsersDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setMobilenum(user.getMobilenum());
            dto.setCountry(user.getCountry());
            dto.setRole(user.getRole());
            dto.setEmailVerified(user.isEmailVerified());
            dto.setProfilePictureUrl(user.getProfilePictureUrl());
            dto.setPrescriptionUrl(user.getPrescriptionUrl());
            dto.setPatientaxraysUrl(user.getPatientaxraysUrl());
            dto.setPatientreportsUrl(user.getPatientreportsUrl());
            dto.setAddress(user.getAddress());
            // If you have a bookingIds field in UsersDTO, you can set it here
            // dto.setBookingIds(user.getBookings().stream().map(booking -> booking.getId()).collect(Collectors.toList()));
            return dto;
            }


	    public boolean emailExists(Long empId) {
	        return userRepository.existsById(empId);
	    }
	    
	    
	    
	    public String uploadFile(MultipartFile file, String folder, Long empId) throws IOException {
	        String fileName = "emp" + empId + "_" + UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename());
	        String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + folder + "/" + fileName;

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + supabaseApiKey);
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

	        if (!response.getStatusCode().is2xxSuccessful()) {
	            throw new IOException("Failed to upload file: " + response.getStatusCode());
	        }

	        // Return public URL of uploaded file
	        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + folder + "/" + fileName;
	    }

	    // Update user with new files URLs and address
	    public users updateUserFilesAndAddress(Long empId, MultipartFile profilePicture, MultipartFile prescription,MultipartFile patientaxraysUrl,MultipartFile patientreportsUrl, String address) throws IOException {
	        Optional<users> optionalUser = userRepository.findById(empId);
	        if (optionalUser.isEmpty()) {
	            throw new IllegalArgumentException("User not found with id: " + empId);
	        }

	        users user = optionalUser.get();

	        if (profilePicture != null && !profilePicture.isEmpty()) {
	            String profileUrl = uploadFile(profilePicture, "profile-pictures", empId);
	            user.setProfilePictureUrl(profileUrl);
	        }

			if (patientaxraysUrl != null && !patientaxraysUrl.isEmpty()) {
				String axraysUrl = uploadFile(patientaxraysUrl, "patient-axrays", empId);
				user.setPatientaxraysUrl(axraysUrl);
			}
			if (patientreportsUrl != null && !patientreportsUrl.isEmpty()) {
				String reportsUrl = uploadFile(patientreportsUrl, "patient-reports", empId);
				user.setPatientreportsUrl(reportsUrl);
				
			}
	        if (prescription != null && !prescription.isEmpty()) {
	            String prescriptionUrl = uploadFile(prescription, "prescriptions", empId);
	            user.setPrescriptionUrl(prescriptionUrl);
	        }

	        if (address != null && !address.isEmpty()) {
	            user.setAddress(address);
	        }

	        return userRepository.save(user);
	    }
	    
	    public users getUserById(Long empId) {
			return userRepository.findById(empId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + empId));
	     }
	    
	    
	    
	    
			public users getUploadedDocAndAddress(Long empId) {
				return userRepository.findById(empId)
						.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + empId));
			}
			
			public users getAllPatients() {
				return userRepository.findAll()
						.stream().filter(user -> "PATIENT".equalsIgnoreCase(user.getRole())).findFirst()
						.orElseThrow(() -> new IllegalArgumentException("No patients found"));
			}
}
		
