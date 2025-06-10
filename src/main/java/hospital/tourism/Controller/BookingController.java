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
    public ResponseEntity<BookingRequest> bookService(
            @PathVariable Long userId,
            @PathVariable Long serviceId,
            @PathVariable String serviceType,
            @RequestBody BookingRequest request
    ) {
        BookingRequest bookingResponse = bookingService.bookService(
                userId,
                serviceId,
                serviceType,
                request.getSlotInfo(),
                request.getPaymentMode(),
                request.getBookingType(),
                request.getAdditionalRemarks() // Corrected from getRemarks() to match DTO field
        );
        return ResponseEntity.ok(bookingResponse);
    }
    
    @PostMapping("/book/witout-servId/{userId}/{serviceType}")
    public ResponseEntity<BookingRequest> bookServicess(
            @PathVariable Long userId,
            
            @PathVariable String serviceType,
            @RequestBody BookingRequest request
    ) {
        BookingRequest bookingResponse = bookingService.bookServices(
                userId,
                
                serviceType,
                request.getSlotInfo(),
                request.getPaymentMode(),
                request.getBookingType(),
                request.getAdditionalRemarks() // Corrected from getRemarks() to match DTO field
        );
        return ResponseEntity.ok(bookingResponse);
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
    @PostMapping("/multiple/{userId}")
    public ResponseEntity<List<BookingRequest>> bookMultipleServices(
            @PathVariable Long userId,
            @RequestBody BookingRequest request
    ) {
        List<BookingRequest> bookingResponses = bookingService.bookMultipleServices(
                userId,
                request.getServiceTypesMultiple(),
                request.getSlotInfo(),
                request.getPaymentMode(),
                request.getBookingType(),
                request.getAdditionalRemarks()
        );

        return ResponseEntity.ok(bookingResponses);
    }
	
}
