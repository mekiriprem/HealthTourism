package hospital.tourism.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.Physio;

public interface PhysioRepo extends JpaRepository<Physio, Long> {

	Collection<LocationDTO> findByLocation(LocationEntity location);
	// Custom query methods can be defined here if needed

}
