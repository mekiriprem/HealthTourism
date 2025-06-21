package hospital.tourism.booking.DTO;

import java.util.List;

import lombok.Data;
@Data
public class ServicePackageDTO {

	
	private Long id;
    private String name;
    private String description;
    private double totalPrice;
    private int durationDays;
    private List<PackageServiceItemDTO> serviceItems;
}
