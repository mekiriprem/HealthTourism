package hospital.tourism.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.BookingRequest;
import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Booking;
import hospital.tourism.Service.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

//    @PostMapping("/book/{userId}/{serviceId}/{serviceType}")
//    public ResponseEntity<BookingRequest> bookService(
//            @PathVariable Long userId,
//            @PathVariable Long serviceId,
//            @PathVariable String serviceType,
//            @RequestBody BookingRequest request
//    ) {
//        BookingRequest bookingResponse = bookingService.bookService(
//                userId,
//                serviceId,
//                serviceType,
//                request.getBookingStartTime(),
//                request.getBookingEndTime(),
//                request.getPaymentMode(),
//                request.getBookingType(),
//                request.getAdditionalRemarks() // Corrected from getRemarks() to match DTO field
//        );
//        return ResponseEntity.ok(bookingResponse);
//    }
    @PostMapping("/book-service/{userId}/{serviceType}")
    public ResponseEntity<BookingRequest> bookSingleService(
            @PathVariable Long userId,
            @RequestBody BookingRequest request,
            @PathVariable String serviceType
            
    ) {
        BookingRequest booking = bookingService.bookServices(
                userId,
                serviceType,
                request.getBookingStartTime(),
                request.getBookingEndTime(),
                request.getPaymentMode(),
                request.getBookingType(),
                request.getBookingAmount(),
                request.getRemarks(),
                request.getChefId(),
                request.getPhysioId(),
                request.getTranslatorId(),
                request.getSpaId(),
                request.getDoctorId(),
                request.getLabtestId()
        );

        return ResponseEntity.ok(booking);
    }
    
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId,
            @RequestParam Long serviceId,
            @RequestParam String serviceType,
            @RequestParam LocalDateTime bookingstartDate,
            @RequestParam LocalDateTime bookingEndDate,
            @RequestParam String paymentMode,
            @RequestParam String bookingType,
            @RequestParam String remarks
    ) {
        Booking updatedBooking = bookingService.updateBooking(
                bookingId,
                userId,
                serviceId,
                serviceType,
               bookingstartDate,
                   bookingEndDate,
                paymentMode,
                bookingType,
                remarks
        );

        return ResponseEntity.ok(updatedBooking);
    }
//    @PostMapping("/multiple/{userId}")
//    public ResponseEntity<List<BookingRequest>> bookMultipleServices(
//            @PathVariable Long userId,
//            @RequestBody BookingRequest request
//    ) {
//        List<BookingRequest> bookingResponses = bookingService.bookMultipleServices(
//                userId,
//                request.getServiceTypesMultiple(),
//                request.getBookingStartTime(),
//                request.getBookingEndTime(),
//                request.getPaymentMode(),
//                request.getBookingType(),
//                request.getAdditionalRemarks()
//        );
//
//        return ResponseEntity.ok(bookingResponses);
//    }
// 
    
    
    
    
//    @PostMapping("/book-package/{userId}")
//    public ResponseEntity<List<BookingRequest>> bookServicePackage(
//            @PathVariable Long userId,
//            @RequestBody BookingRequest request
//    ) {
//        List<BookingRequest> bookings = bookingService.bookServicePackage(
//                userId,
//                request.getServiceTypes(),
//                request.getBookingAmount(),
//                request.getBookingDate(),
//                request.getBookingEndTime(),
//                request.getPaymentMode(),
//                request.getBookingType(),
//                request.getRemarks()
//        );
//        return ResponseEntity.ok(bookings);
//    }
    @GetMapping("/available-chefs/{start}/{end}")
    public ResponseEntity<List<ChefDTO>> getAvailableChefs(
            @PathVariable("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @PathVariable("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<ChefDTO> availableChefs = bookingService.getAvailableChefs(start, end);
        return ResponseEntity.ok(availableChefs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingRequest>> getUserBookings(@PathVariable Long userId) {
        List<BookingRequest> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

}
