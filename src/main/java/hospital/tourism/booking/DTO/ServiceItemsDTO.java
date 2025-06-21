package hospital.tourism.booking.DTO;

import lombok.Data;

@Data
public class ServiceItemsDTO {

	private Long id;
    private String name;
    private String description;
    private String type; // SPA, DOCTOR, HOTEL, etc.
    private double price;
}
