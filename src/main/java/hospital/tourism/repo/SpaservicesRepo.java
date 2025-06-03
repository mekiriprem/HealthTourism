package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.SpaServicese;

public interface SpaservicesRepo extends JpaRepository<SpaServicese, Integer> {
    
    List<SpaServicese> findBySpaCenterSpaId(Integer spaId);

}
