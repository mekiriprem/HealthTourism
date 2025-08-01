package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Doctors;

public interface DoctorsRepo  extends JpaRepository<Doctors, Long>{
    
    List<Doctors> findByHospital_HospitalId(Integer hospitalId);
   
    List<Doctors> findByStatus(String status);

	List<Doctors> findByHospitalHospitalId(int hospitalId);  

}
