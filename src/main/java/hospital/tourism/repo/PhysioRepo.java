package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Physio;

public interface PhysioRepo extends JpaRepository<Physio, Integer> {
	// Custom query methods can be defined here if needed

}
