package hospital.tourism.Pharmacy.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Pharmacy.Entity.PharmacyEntity;

public interface ImadicineRepository extends JpaRepository<PharmacyEntity, Integer> {

	public List<PharmacyEntity> findByMedicineQuantityLessThan(Integer quantity);
	
	 // Find medicines expiring before a specific date
    List<PharmacyEntity> findByMedicineExpiryDateBefore(LocalDateTime date);

    // Find medicines expiring between two dates
    List<PharmacyEntity> findByMedicineExpiryDateBetween(LocalDateTime start, LocalDateTime end);

	public List<PharmacyEntity> findTop5ByOrderByMedicinePriceDesc();



}
