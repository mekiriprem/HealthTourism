package hospital.tourism.Dto;

import java.util.List;

import lombok.Data;

@Data
public class LocationDTO {
    private Integer locationId;
    private String city;
    private String state;
    private String country;
    private List<HospitalDTO> hospitals;
    private List<TranslatorDTO> translators;
    private List<DiagnosticsDTO> diagnostics;
    private List<ChefDTO> chefs;

}
