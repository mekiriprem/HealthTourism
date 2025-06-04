package hospital.tourism.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

	// Custom query methods can be added here if needed
	// For example, to find bookings by user ID or booking type
	List<BookingEntity> findByUserId(Integer userId);

	List<BookingEntity> findByBookingType(String bookingType);

	// Add more methods as per requirements

}
