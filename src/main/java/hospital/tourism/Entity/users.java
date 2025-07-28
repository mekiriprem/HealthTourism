package hospital.tourism.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
// or "patients"
public class users {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    @Column(nullable = false, unique = true)
	    private String email;

	    @Column(nullable = false)
	    private long mobilenum;

	    @Column(nullable = false)
	    private String country;

	    @Column(nullable = false)
	    private String password;

	    @Column(nullable = false)
	    private String role = "PATIENT";

	    @Column(nullable = false)
	    private boolean emailVerified = false;

	    @Column(unique = true)
	    private String verificationToken;

	    @Column(name = "profile_picture_url")
	    private String profilePictureUrls;

	    @ElementCollection
	    @CollectionTable(name = "user_prescriptions", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "url")
	    private List<String> prescriptionUrls;

	    @ElementCollection
	    @CollectionTable(name = "user_xrays", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "url")
	    private List<String> patientAxraysUrls;

	    @ElementCollection
	    @CollectionTable(name = "user_reports", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "url")
	    private List<String> patientReportsUrls;

	    private String address;

	    @OneToMany(mappedBy = "user")
	    @JsonIgnoreProperties("user")
	    private List<Booking> bookings;

	    @Column(name = "package_booking_id")
	    private String packageBookingId;
	    
	    @Column(name = "reset_token")
	    private String resetToken;



}