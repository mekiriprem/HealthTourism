package hospital.tourism.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.SpaCenter;

public interface SpaCenterRepo extends JpaRepository<SpaCenter, Integer> {

	Collection<LocationEntity> findByLocation(LocationEntity location);

}
