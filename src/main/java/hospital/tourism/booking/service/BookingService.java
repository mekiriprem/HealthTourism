package hospital.tourism.booking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.users;
import hospital.tourism.booking.entity.BookingEntity;
import hospital.tourism.booking.repo.BookingRepository;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.usersrepo;



@Service
public class BookingService {
	
	  @Autowired
	    private BookingRepository bookingRepository;

	    @Autowired
	    private usersrepo userRepository;

	    @Autowired
	    private chefsRepo chefRepository;

	
	    public BookingEntity bookChef(BookingEntity bookingRequest, Long userId, Integer chefId) {
	        Optional<users> userOpt = userRepository.findById(userId);
	        Optional<Chefs> chefOpt = chefRepository.findById(chefId);

	        if (userOpt.isEmpty() || chefOpt.isEmpty()) {
	            throw new RuntimeException("User or Chef not found");
	        }

	        bookingRequest.setUser(userOpt.get());
	        bookingRequest.setChef(chefOpt.get());
	        bookingRequest.setPhysios(null);
	        bookingRequest.setTranslator(null);
	        bookingRequest.setBookingStatus("Pending");

	        return bookingRepository.save(bookingRequest);
	    }
	
}
