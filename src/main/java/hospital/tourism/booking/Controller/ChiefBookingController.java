package hospital.tourism.booking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.booking.entity.ChiefBookingEntity;
import hospital.tourism.booking.service.ChiefBookingService;
import hospital.tourism.repo.chefsRepo;

@RequestMapping("/chief-booking")
@RestController
public class ChiefBookingController {

	@Autowired
	private ChiefBookingService chiefBookingService;
	
	@Autowired
	private chefsRepo chefRepository;
	@PostMapping("/bookChef")
	public ResponseEntity<?> bookChef(@RequestBody ChiefBookingEntity booking) {
	    // Fetch the chef from DB using ID passed from client
	    Chefs chef = chefRepository.findById(booking.getChef().getChefID())
	                   .orElseThrow(() -> new RuntimeException("Chef not found"));

	    booking.setChef(chef); // set the valid chef

	    chiefBookingService.bookSlot(booking, booking.getUserId());
	    return ResponseEntity.ok("Booking successful");
	}
}
