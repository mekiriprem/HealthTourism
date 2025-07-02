package hospital.tourism.Dto;

import lombok.Data;

@Data
public class LabtestsDTO {

	 private Long id;
	    private String testTitle;
	    private String testDescription;
	    private double testPrice;
	    private String testDepartment;
	    private String testImage;
	    private String status;

	    private Integer diagnosticsId;  // Foreign key reference only
	    private String diagnosticsName;
	    private String diagnosticsAddress;
	    private String diagnosticsRating;

	    private String city;
	    private String state;
	    private String country;
}
