package hospital.tourism.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponApplyResponse {
    private double totalAmount;
    private double discount;
    private double finalAmount;
}


