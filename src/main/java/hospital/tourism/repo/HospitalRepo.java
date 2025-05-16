package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Hospital;

public interface HospitalRepo extends JpaRepository<Hospital, Integer>{

}
