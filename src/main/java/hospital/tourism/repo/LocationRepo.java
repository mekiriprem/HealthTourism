package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hospital.tourism.Entity.LocationEntity;

public interface LocationRepo extends JpaRepository<LocationEntity, Integer> {
    

    @Query("""
        SELECT l FROM LocationEntity l 
        WHERE l.hospitalList IS EMPTY 
          AND l.translatorsList IS EMPTY 
          AND l.diognsticsList IS EMPTY 
          AND l.chefsList IS EMPTY 
          AND l.spalists IS EMPTY
        """)
    List<LocationEntity> findUnmappedLocations();

}
