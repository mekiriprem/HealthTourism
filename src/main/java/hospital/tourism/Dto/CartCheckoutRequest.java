package hospital.tourism.Dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class CartCheckoutRequest {

	  private Long userId;
	    private List<Long> serviceIds;
	    private List<String> serviceTypes;
	    private String couponCode;
	    private String paymentMode;
	    private String bookingType;
	    private String additionalRemarks;
	    private LocalDateTime startTime;
	    private LocalDateTime endTime;
}
