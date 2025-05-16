package hospital.tourism.Dto;

import lombok.Data;

@Data
public class SpaCenterDTO {
    private String spaName;
    private String spaDescription;
    private String spaImage;
    private String rating;
    private String address;
    private Integer locationId; // only ID, not full LocationEntity
    // getters and setters
}

