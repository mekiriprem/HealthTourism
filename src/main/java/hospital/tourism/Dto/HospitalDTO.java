package hospital.tourism.Dto;



import lombok.Data;

@Data
public class HospitalDTO {
	 private Integer hospitalId;
	    private String hospitalName;
	    private String hospitalDescription;
	    private String hospitalImage;
	    private String rating;
	    private String address;
	    private String status ; // Default status is ACTIVE;
	    private String specialization;

	    // Optional: Only include minimal location info to avoid recursion
	    private Integer hospitallocationId;
	    private String hospitallocationName;
		        private String city;
		                private String state;
		                private String country; // Only ID, not full LocationEntity

	    
}

