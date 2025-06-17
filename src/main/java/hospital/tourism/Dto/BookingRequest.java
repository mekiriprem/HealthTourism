package hospital.tourism.Dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BookingRequest {
  
	 private Long bookingId;
	    private LocalDateTime bookingDate;

	    private Long slotIdLong;
	    private String bookingStatus;
	    
	    private double bookingAmount;

	 
	    private String paymentStatus;
	    private String discountApplied;

	    private LocalDateTime bookingStartTime;
	    private LocalDateTime bookingEndTime;

	    private String additionalRemarks;
	   
	    private String serviceTypes;
	    private String serviceName;
	    private List<Double> bookingAmounts;
	    private LocalDateTime bookingStartDate;
	    private LocalDateTime bookingEndDate;
	    private String paymentMode;
	    private String bookingType;
	    private String remarks;
	    private double bookingtotalAmount;

	    // Related service IDs (optional: include names if needed)
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

	    private Long chefId;
	    private String chefName;

	    private Long userId;
	    private String userName;
	    
	    
		private  List<String> ServiceTypesMultiple;
		
		@Column(name = "package_booking_id")
		private String packageBookingId;

		
	
}
