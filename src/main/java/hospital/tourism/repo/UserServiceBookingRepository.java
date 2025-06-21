package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.UserServiceBooking;
import hospital.tourism.Entity.users;

public interface UserServiceBookingRepository extends JpaRepository<UserServiceBooking, Long> {


	List<UserServiceBooking> findByUserId(Long userId);
    List<UserServiceBooking> findByUser(users user);

}
