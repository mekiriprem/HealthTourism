package hospital.tourism.Dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private List<String> slotInfo;
    private String paymentMode;
    private String bookingType;
    private String remarks;
    private String Bokkedby;
}
