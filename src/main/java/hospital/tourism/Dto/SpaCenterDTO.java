package hospital.tourism.Dto;

import lombok.Data;

@Data
public class SpaCenterDTO {
    private Integer spaId;
    private String spaName;
    private String spaDescription;
    private String spaImage;
    private String rating;
    private String address;
    private String Status;
    private Integer locationId;
    private String city;
    private String state;
    private String country;// only ID, not full LocationEntity
    // getters and setters
}

