package hospital.tourism.Dto;

import java.util.List;

import hospital.tourism.Entity.LocationEntity;
import lombok.Data;

@Data
public class ChefDTO {
	private Long chefID;  // optional for create, useful for update

    private String chefName;
    private String chefDescription;
    private String chefImage;
    private String chefRating;
    private String experience;
    private String styles;
    private String Status;
    private double price;

    private Integer locationId;
    private String city;
    private String state;
    private String country;// Instead of whole LocationEntity, just pass location ID

    private LocationEntity location; // Optional: Include full location entity if needed


}

