package hospital.tourism.booking.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hospital.tourism.Entity.Booking;
import hospital.tourism.booking.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

	// Custom query methods can be added here if needed
	// For example, to find bookings by user ID or booking type
	List<BookingEntity> findByUserId(Integer userId);

	List<BookingEntity> findByBookingType(String bookingType);

	// Add more methods as per requirements
	@Query("SELECT b FROM Booking b WHERE b.chef.chefID = :chefId AND " +
		       "(:start < b.bookingEndTime AND :end > b.bookingStartTime)")
		List<Booking> findChefBookingsInTimeRange(
		        @Param("chefId") Long chefId,
		        @Param("start") LocalDateTime start,
		        @Param("end") LocalDateTime end
		);

}
