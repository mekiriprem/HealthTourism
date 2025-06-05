package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.tourism.Entity.Chefs;

@Repository
public interface chefsRepo extends JpaRepository<Chefs,Long> {
    List<Chefs> findByLocation_LocationId(Integer locationId);
}
