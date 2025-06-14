package hospital.tourism.Dto;

import lombok.Data;

@Data
public class WishlistResponseDTO {

	
	 private Long wishlistId;

	    private Long userId;
	    private String userName;
	    private String userEmail;

	    private String serviceType;       // e.g., "doctor", "spa"
	    private Long serviceId;
	    private String serviceName;
	    private String serviceDescription;
	    private String serviceImageUrl;

	    private String notes;  
}
