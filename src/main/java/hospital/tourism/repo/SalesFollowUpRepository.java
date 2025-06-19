package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.SalesFollowUp;

public interface SalesFollowUpRepository  extends JpaRepository<SalesFollowUp, Long>{
    List<SalesFollowUp> findBySalesTeam_Id(Long salesId);
    List<SalesFollowUp> findByBooking_Id(Long bookingId);
    
    List<SalesFollowUp> findByBooking_BookingIdOrderByFollowUpDateDesc(Long bookingId);


}
