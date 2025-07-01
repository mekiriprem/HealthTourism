package hospital.tourism.Dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CouponDTO {

    private String code;
    private String discountType; // "PERCENTAGE" or "FIXED"
    private double discountAmount;
    private String type;
    private String name;
    private String description;
    private boolean isActive;
    private LocalDateTime validFrom;
    private LocalDateTime validTill;

    private List<CouponServiceMappingDTO> applicableServices;
}
