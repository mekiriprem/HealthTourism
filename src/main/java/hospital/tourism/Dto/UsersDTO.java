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

    private String role;

    private boolean emailVerified;

    private String profilePictureUrl;

    private String prescriptionUrl;

    private String patientaxraysUrl;

    private String patientreportsUrl;

    private String address;

    @Column(name = "package_booking_id")
    private String packageBookingId;
    
    private List<Long> bookingIds;
}
