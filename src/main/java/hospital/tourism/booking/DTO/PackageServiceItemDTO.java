package hospital.tourism.booking.DTO;

import lombok.Data;

@Data
public class PackageServiceItemDTO {

	 private Long id;

	    private Long servicePackageId;   // just the ID to avoid full object
	    private Long serviceItemId;
}
