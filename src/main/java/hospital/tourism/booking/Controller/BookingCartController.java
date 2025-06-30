package hospital.tourism.booking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.booking.DTO.BookingCartRequest;
import hospital.tourism.booking.entity.BookingCart;
import hospital.tourism.booking.service.BookingCartService;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
public class BookingCartController {

	
	 @Autowired
	    private BookingCartService cartService;

	    @PostMapping("/add")
	    public ResponseEntity<String> addToCart(@RequestBody BookingCartRequest request) {
	        cartService.addToCart(request);
	        return ResponseEntity.ok("Service added to cart");
	    }

	    @GetMapping("/{userId}")
	    public ResponseEntity<List<BookingCartRequest>> viewCart(@PathVariable Long userId) {
	        return ResponseEntity.ok(cartService.getUserCartDTO(userId));
	    }

	    @PostMapping("/checkout/{userId}")
	    public ResponseEntity<String> checkout(@PathVariable Long userId) {
	        cartService.confirmBookings(userId);
	        return ResponseEntity.ok("All bookings confirmed and paid");
	    }
}
