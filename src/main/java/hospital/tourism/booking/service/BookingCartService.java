package hospital.tourism.booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Entity.Translators;
import hospital.tourism.booking.DTO.BookingCartRequest;
import hospital.tourism.booking.entity.BookingCart;
import hospital.tourism.booking.repo.BookingCartRepository;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;
import hospital.tourism.repo.usersrepo;

@Service
public class BookingCartService {

	
	 @Autowired
	    private BookingCartRepository cartRepo;

	    @Autowired
	    private usersrepo userRepo;
	    
	    
	    
	    @Autowired
	    private DoctorsRepo doctorRepository;

	    @Autowired
	    private SpaservicesRepo spaRepository;

	    @Autowired
	    private PhysioRepo physioRepository;

	    @Autowired
	    private TranslatorsRepo translatorRepository;

	    @Autowired
	    private labtestsRepo labTestsRepository;

	    @Autowired
	    private chefsRepo chefRepository;

	    @Autowired
	    private BookingRepo bookingRepository;


	    public void addToCart(BookingCartRequest request) {
	        BookingCart cart = new BookingCart();
	        cart.setUser(userRepo.findById(request.getUserId()).orElseThrow());
	        cart.setServiceType(request.getServiceType());
	        cart.setServiceId(request.getServiceId());
	        cart.setServiceName(request.getServiceName());
	        cart.setAmount(request.getAmount());
	        cart.setSelectedStartTime(request.getSelectedStartTime());
	        cart.setSelectedEndTime(request.getSelectedEndTime());
	        cart.setConfirmed(false);
	        cartRepo.save(cart);
	    }

	    public List<BookingCartRequest> getUserCartDTO(Long userId) {
	        List<BookingCart> carts = cartRepo.findByUserIdAndIsConfirmedFalse(userId);
	        return carts.stream().map(cart -> {
	        	BookingCartRequest dto = new BookingCartRequest();
	            dto.setServiceType(cart.getServiceType());
	            dto.setServiceName(cart.getServiceName());
	            dto.setAmount(cart.getAmount());
	            dto.setSelectedStartTime(cart.getSelectedStartTime());
	            dto.setSelectedEndTime(cart.getSelectedEndTime());
	            return dto;
	        }).collect(Collectors.toList());
	    }


	    public void confirmBookings(Long userId) {
	        List<BookingCart> carts = cartRepo.findByUserIdAndIsConfirmedFalse(userId);

	        for (BookingCart cart : carts) {
	            Booking booking = new Booking();
	            booking.setUser(cart.getUser());
	            booking.setBookingStatus("Confirmed");
	            booking.setBookingType("Cost");
	            booking.setBookingAmount(cart.getAmount());
	            booking.setBookingStartTime(cart.getSelectedStartTime());
	            booking.setBookingEndTime(cart.getSelectedEndTime());
	            booking.setPaymentStatus("Paid");
	            booking.setPaymentMode("UPI");

	            switch (cart.getServiceType()) {
	            case "doctor" -> {
	                Doctors doctor = doctorRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setDoctors(doctor);
	            }
	            case "spa" -> {
	                SpaServicese spa = spaRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setSpa(spa);
	            }
	            case "physio" -> {
	                Physio physio = physioRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setPhysio(physio);
	            }
	            case "translator" -> {
	                Translators translator = translatorRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setTranslator(translator);
	            }
	            case "labtests" -> {
	                Labtests labtests = labTestsRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setLabtests(labtests);
	            }
	            case "chef" -> {
	                Chefs chef = chefRepository.findById(cart.getServiceId()).orElse(null);
	                booking.setChef(chef);
	            }
	        }


	            // Save booking here...
	            // bookingRepo.save(booking);

	            // Mark cart as confirmed
	            cart.setConfirmed(true);
	            cartRepo.save(cart);
	        }
	    }
}
