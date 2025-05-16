package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.LocationEntity;

public interface LocationRepo extends JpaRepository<LocationEntity, Integer> {

}
