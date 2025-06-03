package hospital.tourism.booking.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ChiefRequestDTO {
	private Integer chefId;
    private Integer userId;
    private String bookingType; // "Cost" or "NoCost"
    private List<String> selectedDurations; 
}
