package hospital.tourism.Dto;


import hospital.tourism.Entity.LocationEntity;
import lombok.Data;

@Data
public class TranslatorDTO {
	private Long translatorID;
    private String translatorName;
    private String translatorDescription;
    private String translatorImage;
    private String translatorRating;
    private String translatorLanguages;
    private String status;
    private Double price;
    private String translatorAddress;
    private Integer translatorLocIdInteger;

    // Optional: Include location name or ID only (avoid full entity to prevent recursion)
    private LocationEntity location;

    

	   
}

