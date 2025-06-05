// package: hospital.tourism.repo

package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.tourism.Entity.Translators;

@Repository
public interface TranslatorsRepo extends JpaRepository<Translators, Long> {
    List<Translators> findByLocation_LocationId(Integer locationId);
}

