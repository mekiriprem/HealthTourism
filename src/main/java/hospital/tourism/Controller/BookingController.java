package hospital.tourism.Controller;

import hospital.tourism.Dto.BookingRequest;
import hospital.tourism.Entity.Booking;
import hospital.tourism.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> bookService(
            @RequestParam Long userId,
            @RequestParam Long serviceId,
            @RequestParam String serviceType,     // e.g., "chef", "physio"
            @RequestBody BookingRequest request
    ) {
        Booking booking = bookingService.bookService(
                userId,
                serviceId,
                serviceType,
                request.getSlotInfo(),
                request.getPaymentMode(),
                request.getBookingType(),
                request.getRemarks()
        );
        return ResponseEntity.ok(booking);
    }
}
