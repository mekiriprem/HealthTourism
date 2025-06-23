package hospital.tourism.booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.users;
import hospital.tourism.booking.DTO.BookingPackageDTO;
import hospital.tourism.booking.DTO.PackageServiceItemDTO;
import hospital.tourism.booking.DTO.ServicePackageDTO;
import hospital.tourism.booking.entity.BookingPackage;
import hospital.tourism.booking.entity.ServicePackage;
import hospital.tourism.booking.repo.BookingRepositoryPackage;
import hospital.tourism.booking.repo.ServicePackageRepository;
import hospital.tourism.repo.usersrepo;
@Service
public class UserPackageService {

	 @Autowired
	    private BookingRepositoryPackage bookingRepository;

	    @Autowired
	    private ServicePackageRepository packageRepo;

	    @Autowired
	    private usersrepo userRepo;

	    public BookingPackageDTO bookPackage(Long userId, Long packageId) {
	        users user = userRepo.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

	        ServicePackage sp = packageRepo.findById(packageId)
	                .orElseThrow(() -> new RuntimeException("Package not found with ID: " + packageId));

	        BookingPackage booking = new BookingPackage();
	        booking.setUser(user);
	        booking.setServicePackage(sp);
	        booking.setBookingDate(LocalDate.now());
	        booking.setStatus("PENDING");
	        booking.setTotalPrice(sp.getTotalPrice());

	        BookingPackage savedBooking = bookingRepository.save(booking);

	        // 🔁 Convert to DTO
	        BookingPackageDTO dto = new BookingPackageDTO();
	        dto.setId(savedBooking.getId());
	        dto.setUserId(savedBooking.getUser().getId());
	        dto.setServicePackageId(savedBooking.getServicePackage().getId());
	        dto.setBookingDate(savedBooking.getBookingDate());
	        dto.setStatus(savedBooking.getStatus());
	        dto.setTotalPrice(savedBooking.getTotalPrice());

	        return dto;
	    }

	    public List<ServicePackageDTO> listPackages() {
	        List<ServicePackage> packages = packageRepo.findAll();

	        return packages.stream().map(sp -> {
	            ServicePackageDTO dto = new ServicePackageDTO();
	            dto.setId(sp.getId());
	            dto.setName(sp.getName());
	            dto.setDescription(sp.getDescription());
	            dto.setTotalPrice(sp.getTotalPrice());
	            dto.setDurationDays(sp.getDurationDays());

	            // Convert serviceItems to DTOs
	            List<PackageServiceItemDTO> itemDTOs = sp.getServiceItems().stream().map(psi -> {
	                PackageServiceItemDTO psiDTO = new PackageServiceItemDTO();
	                psiDTO.setId(psi.getId());
	                psiDTO.setServiceItemId(psi.getServiceItem().getId());
	                psiDTO.setServicePackageId(sp.getId());
	                return psiDTO;
	            }).toList();

	            dto.setServiceItems(itemDTOs);

	            return dto;
	        }).toList();
	    }

	    public List<BookingPackageDTO> listBookingsByUser(Long userId) {
	        users user = userRepo.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

	        List<BookingPackage> bookings = bookingRepository.findByUser(user);

	        return bookings.stream().map(booking -> {
	            BookingPackageDTO dto = new BookingPackageDTO();
	            dto.setId(booking.getId());
	            dto.setUserId(user.getId());
	            dto.setUserName(user.getName()); // Assuming users has a getName() method
	            dto.setPhNumber(user.getMobilenum()); // Assuming users has a getPhoneNumber() method
	            dto.setEmail(user.getEmail()); // Assuming users has a getEmail() method
	            
	            dto.setServicePackageId(booking.getServicePackage().getId());
	            dto.setBookingDate(booking.getBookingDate());
	            dto.setStatus(booking.getStatus());
	            dto.setTotalPrice(booking.getTotalPrice());
	            dto.setServicePackageName(booking.getServicePackage().getName());
	            dto.setServicePackageDescription(booking.getServicePackage().getDescription());
	            dto.setServicePackageImageUrl(booking.getServicePackage().getImageUrl());
	            
	            return dto;
	        }).toList();
	        
	    }

	    
}
