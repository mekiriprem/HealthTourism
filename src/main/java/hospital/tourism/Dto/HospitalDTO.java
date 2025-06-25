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
	    private String status;

	    // Optional: Only include minimal location info to avoid recursion
	    private Integer hospitallocationId;
	    private String hospitallocationName;

}

