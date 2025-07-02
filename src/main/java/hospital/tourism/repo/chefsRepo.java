package hospital.tourism.repo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.LocationEntity;

@Repository
public interface chefsRepo extends JpaRepository<Chefs,Long> {
    List<Chefs> findByLocation_LocationId(Integer locationId);
    
    @Query("SELECT b FROM Booking b WHERE b.chef.chefID = :chefId " +
    	       "AND (b.bookingStartTime < :end AND b.bookingEndTime > :start)")
    	List<Booking> findChefBookingsInTimeRange(@Param("chefId") Long chefId,
    	                                          @Param("start") LocalDateTime start,
    	                                          @Param("end") LocalDateTime end);

	Collection<LocationDTO> findByLocation(LocationEntity location);

}
