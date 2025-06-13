package hospital.tourism.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hospital.tourism.Entity.Booking;

public interface BookingRepo extends JpaRepository<Booking, Long> {



	@Query("SELECT b FROM Booking b WHERE b.physio.physioId = :id AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findPhysioBookingsInTimeRange(Long id, LocalDateTime start, LocalDateTime end);

	@Query("SELECT b FROM Booking b WHERE b.translator.translatorID = :id AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findTranslatorBookingsInTimeRange(Long id, LocalDateTime start, LocalDateTime end);

	@Query("SELECT b FROM Booking b WHERE b.spa.serviceId = :id AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findSpaBookingsInTimeRange(Long id, LocalDateTime start, LocalDateTime end);

	@Query("SELECT b FROM Booking b WHERE b.doctors.id = :id AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findDoctorBookingsInTimeRange(Long id, LocalDateTime start, LocalDateTime end);

	@Query("SELECT b FROM Booking b WHERE b.labtests.id = :id AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findLabTestBookingsInTimeRange(Long id, LocalDateTime start, LocalDateTime end);
	@Query("SELECT b FROM Booking b WHERE b.chef.chefID = :chefId AND b.bookingStartTime < :end AND b.bookingEndTime > :start")
	List<Booking> findChefBookingsInTimeRange(@Param("chefId") Long chefId,
	                                          @Param("start") LocalDateTime start,
	                                          @Param("end") LocalDateTime end);
	
	
	 List<Booking> findByUserId(Long userId);




}
