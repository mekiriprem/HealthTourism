package hospital.tourism.Dto;

import lombok.Data;

@Data
public class PhysioDTO {
    private Integer physioId;
    private String physioName;
    private String physioDescription;
    private String physioImage;
    private String rating;
    private String address;
    private String price;
    private Integer locationId; // Only the ID of the Location, not the full entity
}