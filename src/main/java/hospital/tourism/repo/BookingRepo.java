package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Booking;

public interface BookingRepo extends JpaRepository<Booking, Long> {

}
