package hospital.tourism.Dto;

import java.util.List;

import jakarta.persistence.Column;
import lombok.Data;
@Data
public class UsersDTO {

	 private Long id;
	    private String name;
	    private String email;
	    private long mobilenum;
	    private String country;
	    private String password;
	    private String role;
	    private boolean emailVerified;
	    private String verificationToken;

	    private String profilePictureUrls;
	    private List<String> prescriptionUrls;
	    private List<String> patientAxraysUrls;
	    private List<String> patientReportsUrls;

	    private String address;
	    private String packageBookingId;
}
