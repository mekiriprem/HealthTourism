package hospital.tourism.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.booking.entity.ChiefBookingEntity;
import hospital.tourism.booking.repo.BookingRepository;

@Service
public class ChiefBookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	// Add methods to handle booking logic here
	
	//book a slot
	public ChiefBookingEntity bookSlot(ChiefBookingEntity booking,Integer userId) {
		return bookingRepository.save(booking);
	}
}
