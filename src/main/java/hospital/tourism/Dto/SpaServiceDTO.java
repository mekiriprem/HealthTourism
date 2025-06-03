package hospital.tourism.Dto;

import lombok.Data;

@Data
public class SpaServiceDTO {
    private String serviceName;
    private String serviceDescription;
    private String serviceImage;
    private String rating;
    private Double price;
    private Integer spaCenterId;  // ID of existing SpaCenter
    // getters and setters
}
