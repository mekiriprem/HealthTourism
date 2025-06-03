package hospital.tourism.Pharmacy.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Pharmacy.DTO.PharmacyDashboardResponse;
import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import hospital.tourism.Pharmacy.Repository.ImadicineRepository;

@Service
public class PharmacyService {
@Autowired
	private ImadicineRepository imadicineRepository;

		//add madicines
		public PharmacyEntity addMadicine(PharmacyEntity pharmacyEntity) {
				return imadicineRepository.save(pharmacyEntity);
		}
		//get All Madicines
		public List<PharmacyEntity> getAllMadicines() {
			return imadicineRepository.findAll();
		}
		
		//Delete Madicine
		public String  deleteMadicine(Integer madicineId) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				imadicineRepository.deleteById(madicineId);
				return "Madicine deleted Sucessfully with Id : " + madicineId;
			}
			else {
				return "Madicine not found with id: " + madicineId;
			}
			
		}
		//update Madicine
		public PharmacyEntity updateMadicine(Integer madiceid, PharmacyEntity pharmacyEntity) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madiceid);
			if (optionalMadicine.isPresent()) {
				PharmacyEntity existingMadicine = optionalMadicine.get();
				existingMadicine.setMedicineName(pharmacyEntity.getMedicineName());
				existingMadicine.setMedicineType(pharmacyEntity.getMedicineType());
				existingMadicine.setMedicineDescription(pharmacyEntity.getMedicineDescription());
				existingMadicine.setMedicinePrice(pharmacyEntity.getMedicinePrice());
				existingMadicine.setMedicineQuantity(pharmacyEntity.getMedicineQuantity());
				existingMadicine.setMedicineExpiryDate(pharmacyEntity.getMedicineExpiryDate());
				existingMadicine.setMedicineManufacturer(pharmacyEntity.getMedicineManufacturer());
				existingMadicine.setMedicineImage(pharmacyEntity.getMedicineImage());
				existingMadicine.setMedicineCategory(pharmacyEntity.getMedicineCategory());

				return imadicineRepository.save(existingMadicine);
			} else {
				throw new RuntimeException("Madicine not found with id: " + pharmacyEntity.getMadicineid());
			}
		}
		
		// Get Madicine by Id
		public PharmacyEntity getMadicineById(Integer madicineId) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				return optionalMadicine.get();
			} else {
				throw new RuntimeException("Madicine not found with id: " + madicineId);
			}
		}
		
		//update the date 
		
		public PharmacyEntity updateMadicineDate(Integer madicineId, LocalDateTime newExpiryDate) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				PharmacyEntity existingMadicine = optionalMadicine.get();
				existingMadicine.setMedicineExpiryDate(newExpiryDate);
				return imadicineRepository.save(existingMadicine);
			} else {
				throw new RuntimeException("Madicine not found with id: " + madicineId);
			}
		}
		
		
	    // Method to get medicines with low stock (less than 10)
		public List<PharmacyEntity> getLowStockMedicines() {
		    return imadicineRepository.findByMedicineQuantityLessThan(100);
		}
		
		
	
		public List<PharmacyEntity> getExpiredMedicines() {
		    return imadicineRepository.findByMedicineExpiryDateBefore(LocalDateTime.now());
		}

		// Method to get medicines that are near expiry (within the next 30 days)
		public List<PharmacyEntity> getNearExpiryMedicines() {
		    LocalDateTime now = LocalDateTime.now();
		    LocalDateTime in30Days = now.plusDays(30);
		    return imadicineRepository.findByMedicineExpiryDateBetween(now, in30Days);
		}

		  public PharmacyDashboardResponse getPharmacyDashboard() {
		        PharmacyDashboardResponse response = new PharmacyDashboardResponse();

		        LocalDateTime now = LocalDateTime.now();
		        LocalDateTime in30Days = now.plusDays(30);

		        response.setAllMedicines(imadicineRepository.findAll());
		        response.setLowStockMedicines(imadicineRepository.findByMedicineQuantityLessThan(100));
		        response.setExpiredMedicines(imadicineRepository.findByMedicineExpiryDateBefore(now));
		        response.setNearExpiryMedicines(imadicineRepository.findByMedicineExpiryDateBetween(now, in30Days));

		        return response;
		    }
		  //show top 5 medicines by price
		  
			public List<PharmacyEntity> getTop5MedicinesByPrice() {
            return imadicineRepository.findTop5ByOrderByMedicinePriceDesc();
			}
}
