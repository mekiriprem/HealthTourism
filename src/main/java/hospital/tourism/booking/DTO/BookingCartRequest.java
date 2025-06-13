package hospital.tourism.booking.DTO;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class BookingCartRequest {

	private Long userId;
    private String serviceType;
    private Long serviceId;
    private String serviceName;
    private double amount;
    private LocalDateTime selectedStartTime;
    private LocalDateTime selectedEndTime;
}
