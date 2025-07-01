package hospital.tourism.Dto;

import java.util.List;

import lombok.Data;

@Data
public class CouponApplyRequest {
    private List<Long> serviceIds;
    private List<String> serviceTypes;
    private String couponCode;
}
