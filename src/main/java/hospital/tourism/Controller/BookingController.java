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

    @PostMapping("/book/{userId}/{serviceId}/{serviceType}")
    public ResponseEntity<Booking> bookService(
            @PathVariable Long userId,
            @PathVariable Long serviceId,
            @PathVariable String serviceType,     // e.g., "chef", "physio"
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
    
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId,
            @RequestParam Long serviceId,
            @RequestParam String serviceType,
            @RequestParam List<String> slotInfo,
            @RequestParam String paymentMode,
            @RequestParam String bookingType,
            @RequestParam String remarks
    ) {
        Booking updatedBooking = bookingService.updateBooking(
                bookingId,
                userId,
                serviceId,
                serviceType,
                slotInfo,
                paymentMode,
                bookingType,
                remarks
        );

        return ResponseEntity.ok(updatedBooking);
    }
}
