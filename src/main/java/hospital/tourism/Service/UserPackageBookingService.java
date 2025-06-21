package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.ServiceItem;
import hospital.tourism.Entity.UserServiceBooking;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.ServiceItemRepository;
import hospital.tourism.repo.UserServiceBookingRepository;
import hospital.tourism.repo.usersrepo;
@Service
public class UserPackageBookingService {

	@Autowired
	private  ServiceItemRepository serviceItemRepository;
	@Autowired
    private  UserServiceBookingRepository bookingRepository;
	@Autowired
	private usersrepo usereRepository;

	 public UserServiceBooking bookService(Long serviceId, int days, Long userId) {
	        ServiceItem service = serviceItemRepository.findById(serviceId)
	                .orElseThrow(() -> new RuntimeException("Service not found"));

	        users user = usereRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        double total = days * service.getPricePerDay();

	        UserServiceBooking booking = new UserServiceBooking();
	        booking.setServiceItem(service);
	        booking.setDurationInDays(days);
	        booking.setTotalAmount(total);
	        booking.setUser(user); // Set actual user entity

	        return bookingRepository.save(booking);
	    }

	    public List<UserServiceBooking> getUserBookings(Long userId) {
	        users user = usereRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        return bookingRepository.findByUser(user);
	    }
}
