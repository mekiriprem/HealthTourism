package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.tourism.Entity.Diognstics;

@Repository
public interface DiagnosticsRepo extends JpaRepository<Diognstics, Integer> {

}
