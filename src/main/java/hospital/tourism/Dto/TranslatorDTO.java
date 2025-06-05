package hospital.tourism.Dto;


import lombok.Data;

@Data
public class TranslatorDTO {
    private Long translatorID;
    private String translatorName;
    private String translatorDescription;
    private String translatorImage;
    private String translatorRating;
    private String translatorLanguages;
}

