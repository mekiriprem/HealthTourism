package hospital.tourism.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicLocationDTO {
    private Integer locationId;
    private String city;
    private String state;
    private String country;
}

