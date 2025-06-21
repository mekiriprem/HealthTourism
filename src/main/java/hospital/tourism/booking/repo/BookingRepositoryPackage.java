package hospital.tourism.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.users;
import hospital.tourism.booking.entity.BookingPackage;

public interface BookingRepositoryPackage extends JpaRepository<Booking, Long> {

	BookingPackage save(BookingPackage booking);

	List<BookingPackage> findByUser(users user);

}
