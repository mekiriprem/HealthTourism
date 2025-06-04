package hospital.tourism.booking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.booking.entity.BookingEntity;
import hospital.tourism.booking.service.BookingService;
import hospital.tourism.repo.chefsRepo;

@RequestMapping("/booking")
@RestController
public class BookingController {

	
	
	 	@Autowired
	    private BookingService bookingService;

	    
	    @PostMapping("/chef/{userId}/{chefId}")
	    public ResponseEntity<BookingEntity> bookChef(
	            @PathVariable Long userId,
	            @PathVariable Integer chefId,
	            @RequestBody BookingEntity bookingRequest) {

	        BookingEntity bookedChef = bookingService.bookChef(bookingRequest, userId, chefId);
	        return ResponseEntity.ok(bookedChef);
	    }
	    @GetMapping("/aaaa")
	    public String dattt() {
	    	return "hi anil";
	    }
}
