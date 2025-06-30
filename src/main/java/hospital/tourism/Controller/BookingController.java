package hospital.tourism.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/api/AddToCart")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;    @PostMapping("/addToCart/{userId}/{serviceType}")
    public ResponseEntity<?> bookService(
            @PathVariable Long userId,
            @PathVariable String serviceType,
            @RequestBody BookingRequest request
    ) {
        try {
            BookingRequest response = bookingService.addToCart(
                    userId,
                    serviceType,
                    request.getBookingStartTime(),
                    request.getBookingEndTime(),
                    request.getBookingType(),
                    request.getPaymentMode(),
                    request.getBookingAmount(),
                    request.getAdditionalRemarks(),
                    request.getChefId(),
                    request.getPhysioId(),
                    request.getTranslatorId(),
                    request.getSpaId(),
                    request.getDoctorId(),
                    request.getLabtestId(),
                    request.getBookingStatus()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Handle time slot conflict
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "TIME_SLOT_CONFLICT");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "INVALID_REQUEST");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (EntityNotFoundException e) {
            // Handle not found entities
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "ENTITY_NOT_FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Handle any other unexpected errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
    
      @PutMapping("/bookings/update-payment-status")
    public ResponseEntity<?> updateBookings(@RequestBody List<Long> bookingIds) {
        try {
            List<BookingRequest> result = bookingService.updatePaymentStatusToPaid(bookingIds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to update payment status: " + e.getMessage());
            errorResponse.put("error", "PAYMENT_UPDATE_FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    
    @GetMapping("/admin/bookings/success")
    public ResponseEntity<List<BookingRequest>> getSuccessfulBookingsForAdmin() {
        List<BookingRequest> successfulBookings = bookingService.getAllSuccessfulBookings();
        return ResponseEntity.ok(successfulBookings);
    }
    @GetMapping("/user-successful/{userId}")
    public ResponseEntity<List<BookingRequest>> getSuccessfulBookingsByUserId(@PathVariable Long userId) {
        List<BookingRequest> bookings = bookingService.getSuccessfulBookingsByUserId(userId);

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(bookings); // 200 OK
    }

    @DeleteMapping("/delete/{bookingId}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
		 bookingService.deleteByBookingId(bookingId);
		return ResponseEntity.ok("Booking deleted successfully");
		
	}

    @DeleteMapping("/delete-multiple")
	public ResponseEntity<?> deleteBymultipleBookingIds(@RequestBody List<Long> bookingIds) {
		bookingService.deleteByMultipleBookingIds(bookingIds);
		return ResponseEntity.ok("Bookings deleted successfully");
	}

}
