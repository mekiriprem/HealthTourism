package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hospital.tourism.Entity.Hospital;

public interface HospitalRepo extends JpaRepository<Hospital, Integer>{

	// In HospitalRepository.java
	List<Hospital> findByStatus(String status);

	List<Hospital> findByStatusAndLocation_CityAndSpecializationIgnoreCase(
		    String status, String city, String specialization);

	List<Hospital> findByStatusAndLocation_City(String status, String city);
	// Search by city
	List<Hospital> findByStatusAndLocation_CityIgnoreCase(String status, String city);

	// Search by state
	List<Hospital> findByStatusAndLocation_StateIgnoreCase(String status, String state);

	// Search by city or state (combined)
	@Query("SELECT h FROM Hospital h WHERE h.status = 'Active' AND (LOWER(h.location.city) = LOWER(:search) OR LOWER(h.location.state) = LOWER(:search))")
	List<Hospital> searchByCityOrState(@Param("search") String search);
	

	@Query("SELECT h FROM Hospital h WHERE h.status = 'Active' AND h.specialization = :specialization")
	List<Hospital> findBySpecialization(String specialization);
	
	  List<Hospital> findBySpecializationAndStatus(String specialization, String status);

}
