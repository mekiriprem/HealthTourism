package hospital.tourism.booking.DTO;

import java.time.LocalDate;

import lombok.Data;
@Data
public class BookingPackageDTO {

	private Long id;

    private Long userId;               // Only the ID to avoid circular references
    private Long servicePackageId;     // Only the ID to keep it lightweight

    private LocalDate bookingDate;
    private String status;             // e.g., PENDING, CONFIRMED
    private double totalPrice;
    
    private String userName;
    private Long phNumber;
    private String email;
    private String address;
    private String servicePackageName;
    private String servicePackageDescription;
    private String servicePackageImageUrl;
    private String servicePackageCategory;
    private String servicePackageType;
    private String servicePackageDuration;
    private String servicePackagePrice;
    private String servicePackageStatus; // e.g., ACTIVE, INACTIVE
    
}
