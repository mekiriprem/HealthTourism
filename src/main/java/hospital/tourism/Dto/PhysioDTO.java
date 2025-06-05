package hospital.tourism.Dto;

import lombok.Data;

@Data
public class PhysioDTO {
    private Long physioId;
    private String physioName;
    private String physioDescription;
    private String physioImage;
    private String rating;
    private String address;
    private Double price;
    private String Status;
    private Integer locationId; // Only the ID of the Location, not the full entity
}