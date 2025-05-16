package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Doctors;

public interface DoctorsRepo  extends JpaRepository<Doctors, Long>{

}
