package hospital.tourism.Dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
  
    private String bookingType;
    private String remarks;
    private String Bokkedby;
    private Long bookingId;
    private String serviceType;
    private String paymentMode;
    private String bookingStatus;
    private double bookingAmount;
    private String bookedByName;
    private List<String> slotInfo;
}
