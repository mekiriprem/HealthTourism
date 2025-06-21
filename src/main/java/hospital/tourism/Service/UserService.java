package hospital.tourism.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.UsersDTO;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.usersrepo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
	    
	    public UsersDTO uploadFilesAndUpdateUser(Long empId,
                MultipartFile profilePicture,
                MultipartFile prescription,
                MultipartFile patientaxrays,
                MultipartFile patientreports) {

	    	users user = userRepository.findById(empId)
	    			.orElseThrow(() -> new RuntimeException("User not found with ID: " + empId));

	    	if (profilePicture != null && !profilePicture.isEmpty()) {
	    		String url = uploadFileToSupabase(profilePicture, "profile_pictures", empId);
	    		if (user.getProfilePictureUrls() == null) {
	    			user.setProfilePictureUrls(new ArrayList<>());
	    		}
	    		user.getProfilePictureUrls().add(url);
	    	}

	    	if (prescription != null && !prescription.isEmpty()) {
	    		String url = uploadFileToSupabase(prescription, "prescriptions", empId);
	    		if (user.getPrescriptionUrls() == null) {
	    			user.setPrescriptionUrls(new ArrayList<>());
	    		}
	    		user.getPrescriptionUrls().add(url);
	    	}

	    	if (patientaxrays != null && !patientaxrays.isEmpty()) {
	    		String url = uploadFileToSupabase(patientaxrays, "xray_files", empId);
	    		if (user.getPatientAxraysUrls() == null) {
	    			user.setPatientAxraysUrls(new ArrayList<>());
	    		}
	    		user.getPatientAxraysUrls().add(url);
	    	}

	    	if (patientreports != null && !patientreports.isEmpty()) {
	    		String url = uploadFileToSupabase(patientreports, "reports", empId);
	    		if (user.getPatientReportsUrls() == null) {
	    			user.setPatientReportsUrls(new ArrayList<>());
	    		}
	    		user.getPatientReportsUrls().add(url);
	    	}

	    	userRepository.save(user);
	    	return mapToDTO(user);
	    }

	    private String uploadFileToSupabase(MultipartFile file, String folder, Long empId) {
	        try {
	            String fileName = "emp" + empId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename().replace(" ", "_");
	            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + folder + "/" + fileName;

	            OkHttpClient client = new OkHttpClient();
	            RequestBody body = RequestBody.create(file.getBytes(), MediaType.parse(file.getContentType()));

	            Request request = new Request.Builder()
	                    .url(uploadUrl)
	                    .header("apikey", supabaseApiKey)
	                    .header("Authorization", "Bearer " + supabaseApiKey)
	                    .put(body)
	                    .build();

	            Response response = client.newCall(request).execute();

	            if (!response.isSuccessful()) {
	                throw new IOException("Failed to upload to Supabase: " + response);
	            }

	            return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + folder + "/" + fileName;
	        } catch (Exception e) {
	            throw new RuntimeException("File upload failed", e);
	        }
	    }

	    private UsersDTO mapToDTO(users user) {
	        UsersDTO dto = new UsersDTO();
	        dto.setId(user.getId());
	        dto.setName(user.getName());
	        dto.setEmail(user.getEmail());
	        dto.setMobilenum(user.getMobilenum());
	        dto.setCountry(user.getCountry());
	        dto.setRole(user.getRole());
	        dto.setEmailVerified(user.isEmailVerified());
	        dto.setVerificationToken(user.getVerificationToken());
	        dto.setAddress(user.getAddress());
	        dto.setPackageBookingId(user.getPackageBookingId());

	        dto.setProfilePictureUrls(user.getProfilePictureUrls());
	        dto.setPrescriptionUrls(user.getPrescriptionUrls());
	        dto.setPatientAxraysUrls(user.getPatientAxraysUrls());
	        dto.setPatientReportsUrls(user.getPatientReportsUrls());

	        return dto;
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
            dto.setProfilePictureUrls(user.getProfilePictureUrls());
            dto.setPrescriptionUrls(user.getPrescriptionUrls());
            dto.setPatientAxraysUrls(user.getPatientAxraysUrls());
            dto.setPatientReportsUrls(user.getPatientReportsUrls());
            dto.setAddress(user.getAddress());
            // If you have a bookingIds field in UsersDTO, you can set it here
            // dto.setBookingIds(user.getBookings().stream().map(booking -> booking.getId()).collect(Collectors.toList()));
            return dto;
            }


	    public boolean emailExists(Long empId) {
	        return userRepository.existsById(empId);
	    }
	    


	    public UsersDTO getUserById(Long empId) {
	        users user = userRepository.findById(empId)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + empId));

	        UsersDTO dto = new UsersDTO();
	           dto.setId(user.getId());
	           	dto.setName(user.getName());
	           	 dto.setEmail(user.getEmail());
	           	 dto.setMobilenum(user.getMobilenum());
	           	 dto.setCountry(user.getCountry());
	           	 dto.setRole(user.getRole());
	           	 dto.setEmailVerified(user.isEmailVerified());
	           	 dto.setAddress(user.getAddress());
	           	 	dto.setProfilePictureUrls(user.getProfilePictureUrls() );
	           	 	dto.setPrescriptionUrls(user.getPrescriptionUrls() );
	           	 	dto.setPatientAxraysUrls(user.getPatientAxraysUrls() );
	           	 	dto.setPatientReportsUrls(user.getPatientReportsUrls() );
	           	 	return dto;
	           	 	}
	    
	    
	    
	    public UsersDTO getAllDocuments(Long empId) {
	        users user = userRepository.findById(empId)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + empId));

	        UsersDTO dto = new UsersDTO();
	        dto.setId(user.getId());
	        dto.setName(user.getName());
	        dto.setEmail(user.getEmail());
	        dto.setMobilenum(user.getMobilenum());
	        dto.setCountry(user.getCountry());
	        dto.setRole(user.getRole());
	        dto.setEmailVerified(user.isEmailVerified());
	        dto.setAddress(user.getAddress());

	        dto.setProfilePictureUrls(user.getProfilePictureUrls() != null ? user.getProfilePictureUrls() : new ArrayList<>());
	        dto.setPrescriptionUrls(user.getPrescriptionUrls() != null ? user.getPrescriptionUrls() : new ArrayList<>());
	        dto.setPatientAxraysUrls(user.getPatientAxraysUrls() != null ? user.getPatientAxraysUrls() : new ArrayList<>());
	        dto.setPatientReportsUrls(user.getPatientReportsUrls() != null ? user.getPatientReportsUrls() : new ArrayList<>());

	        return dto;
	    }

			public users getAllPatients() {
				return userRepository.findAll()
						.stream().filter(user -> "PATIENT".equalsIgnoreCase(user.getRole())).findFirst()
						.orElseThrow(() -> new IllegalArgumentException("No patients found"));
			}
			
			
			
			public UsersDTO updateUserDetails(Long empId, UsersDTO userDto) {
				users user = userRepository.findById(empId)
						.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + empId));

				user.setName(userDto.getName());
				user.setEmail(userDto.getEmail());
				user.setMobilenum(userDto.getMobilenum());
				user.setCountry(userDto.getCountry());
				user.setAddress(userDto.getAddress());

				users updatedUser = userRepository.save(user);
				return mapToDTO(updatedUser);
			}
			
			//getAllusers
			public List<UsersDTO>getAllPriscriptions(){
				List<users> users = userRepository.findAll();
				return users.stream().map(user -> {
					UsersDTO dto = new UsersDTO();
					dto.setId(user.getId());
					dto.setName(user.getName());
					dto.setEmail(user.getEmail());
					dto.setMobilenum(user.getMobilenum());
					dto.setCountry(user.getCountry());
			
					dto.setPrescriptionUrls(
							user.getPrescriptionUrls() != null ? user.getPrescriptionUrls() : new ArrayList<>());
					return dto;
				}).collect(Collectors.toList());
			}
			
}
		
