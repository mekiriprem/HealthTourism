package hospital.tourism.Dto;

import java.util.List;

import lombok.Data;

@Data
public class SlotRequestDTO {
	
	private Long id;
    private List<String> slots;
    private Long slotId;
    private String slotTime;
    private String bookingStatus;
    private Long BookedByUserId;
    private String serviceType;
    private Long serviceId;

    // ✅ Default constructor
    public SlotRequestDTO() {
    }
}
