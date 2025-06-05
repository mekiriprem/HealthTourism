package hospital.tourism.Dto;

import java.util.List;

import lombok.Data;

@Data
public class SlotRequestDTO {
    private String serviceType; // e.g., "chef", "doctor"
    private Long serviceId;
    private List<String> slots;
    private Long bookedBy;

    // getters and setters
}
