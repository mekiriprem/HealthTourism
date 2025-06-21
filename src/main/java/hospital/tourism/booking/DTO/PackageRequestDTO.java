package hospital.tourism.booking.DTO;

import java.util.List;

import lombok.Data;
@Data
public class PackageRequestDTO {

	private Long id;
    private String name;
    private String description;
    private double totalPrice;
    private int durationDays;

    // Optional: List of associated service item IDs (or you can use full DTOs)
    private List<Long> serviceItemIds;
}
