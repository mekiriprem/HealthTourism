package hospital.tourism.Dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BookingRequest {
  
	 private Long bookingId;
	    private LocalDateTime bookingDate;
	    private Long slotIdLong;
	    private String bookingStatus;
	    private String bookingType;
	    private double bookingAmount;
	    private String paymentMode;
	    private String paymentStatus;
	    private String discountApplied;
	    private List<String> slotInfo;
	    // For input only (optional usage)
	    private List<String> serviceTypesMultiple; // e.g., ["Physio", "Translator", "Spa", "Doctor", "LabTest", "Chef"];

	    private String additionalRemarks;

	    // Associated entity IDs or names
	    private Long physioId;
	    private String physioName;

	    private Long translatorId;
	    private String translatorName;

	    private Long spaId;
	    private String spaName;

	    private Long doctorId;
	    private String doctorName;

	    private Long labtestId;
	    private String labtestName;

	    private Long userId;
	    private String userName;

	    private Long chefId;
	    private String chefName;
}
