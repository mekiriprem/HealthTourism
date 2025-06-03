package hospital.tourism.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.ChiefBookingEntity;

public interface BookingRepository extends JpaRepository<ChiefBookingEntity, Integer> {

	// Custom query methods can be added here if needed
	// For example, to find bookings by user ID or booking type
	List<ChiefBookingEntity> findByUserId(Integer userId);

	List<ChiefBookingEntity> findByBookingType(String bookingType);

	// Add more methods as per requirements

}
