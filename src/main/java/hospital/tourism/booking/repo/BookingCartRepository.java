package hospital.tourism.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.BookingCart;

public interface BookingCartRepository extends JpaRepository<BookingCart, Integer> {
	 List<BookingCart> findByUserIdAndIsConfirmedFalse(Long userId);
}
