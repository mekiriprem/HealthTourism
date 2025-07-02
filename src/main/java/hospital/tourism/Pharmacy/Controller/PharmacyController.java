package hospital.tourism.Pharmacy.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Pharmacy.DTO.PharmacyDashboardResponse;
import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import hospital.tourism.Pharmacy.Service.PharmacyService;

@RestController
@RequestMapping("/pharmacy")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
public class PharmacyController {

	@Autowired
	private PharmacyService pharmacyService;
	
	// Add methods to handle requests for pharmacy operations
	@PostMapping("/addMadicine")
	public ResponseEntity<PharmacyEntity> addMadicine(
	        @RequestParam("medicineName") String name,
	        @RequestParam("medicineType") String type,
	        @RequestParam("medicineDescription") String description,
	        @RequestParam("medicinePrice") double price,
	        @RequestParam("medicineQuantity") Integer quantity,
	        @RequestParam("medicineExpiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDate,
	        @RequestParam("medicineManufacturer") String manufacturer,
	        @RequestParam("medicineCategory") String category,
	        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
	) {
	    PharmacyEntity pharmacyEntity = new PharmacyEntity();
	    pharmacyEntity.setMedicineName(name);
	    pharmacyEntity.setMedicineType(type);
	    pharmacyEntity.setMedicineDescription(description);
	    pharmacyEntity.setMedicinePrice(price);
	    pharmacyEntity.setMedicineQuantity(quantity);
	    pharmacyEntity.setMedicineExpiryDate(expiryDate);
	    pharmacyEntity.setMedicineManufacturer(manufacturer);
	    pharmacyEntity.setMedicineCategory(category);

	    PharmacyEntity savedEntity = pharmacyService.addMadicine(pharmacyEntity, imageFile);
	    return ResponseEntity.ok(savedEntity);
	}

	
	//get All Madicines
	@GetMapping("/getAllMadicines")
	public ResponseEntity<List<PharmacyEntity>> getAllMadicines() {
		List<PharmacyEntity> madicines = pharmacyService.getAllMadicines();
		return ResponseEntity.ok(madicines);
	}
	//Delete Madicine
	@DeleteMapping("/deleteMadicine/{madicineId}")
	public ResponseEntity<String> deleteMadicine(@PathVariable Integer madicineId) {
		String msg =pharmacyService.deleteMadicine(madicineId);
		return ResponseEntity.ok(msg);
	}
	
	@PutMapping("/updateMadicine/{madicineId}")
	public ResponseEntity<PharmacyEntity> updateMadicine(
	        @PathVariable Integer madicineId,
	        @RequestParam("medicineName") String name,
	        @RequestParam("medicineType") String type,
	        @RequestParam("medicineDescription") String description,
	        @RequestParam("medicinePrice") double price,
	        @RequestParam("medicineQuantity") Integer quantity,
	        @RequestParam("medicineExpiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDate,
	        @RequestParam("medicineManufacturer") String manufacturer,
	        @RequestParam("medicineCategory") String category,
	        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
	) {
	    PharmacyEntity pharmacyEntity = new PharmacyEntity();
	    pharmacyEntity.setMedicineName(name);
	    pharmacyEntity.setMedicineType(type);
	    pharmacyEntity.setMedicineDescription(description);
	    pharmacyEntity.setMedicinePrice(price);
	    pharmacyEntity.setMedicineQuantity(quantity);
	    pharmacyEntity.setMedicineExpiryDate(expiryDate);
	    pharmacyEntity.setMedicineManufacturer(manufacturer);
	    pharmacyEntity.setMedicineCategory(category);

	    PharmacyEntity updatedEntity = pharmacyService.updateMadicine(madicineId, pharmacyEntity, imageFile);
	    return ResponseEntity.ok(updatedEntity);
	}

	
	// Get Madicine by Id
	@GetMapping("/getMadicineById/{madicineId}")
	public ResponseEntity<PharmacyEntity> getMadicineById(@PathVariable Integer madicineId) {
		PharmacyEntity pharmacyEntity = pharmacyService.getMadicineById(madicineId);
		return ResponseEntity.ok(pharmacyEntity);
	}
	
	//update the date 
	@PutMapping("/updateMadicineDate/{madicineId}")
	public ResponseEntity<PharmacyEntity> updateMadicineDate(@PathVariable Integer madicineId,
			@Param("medicineExpiryDate") LocalDateTime medicineExpiryDate) {
		PharmacyEntity updatedEntity = pharmacyService.updateMadicineDate(madicineId, medicineExpiryDate);
		return ResponseEntity.ok(updatedEntity);
	}
	@GetMapping("/medicines/low-stock")
	public List<PharmacyEntity> getLowStockMedicines() {
	    return pharmacyService.getLowStockMedicines();
	}
	
	@GetMapping("/medicines/expired")
	public List<PharmacyEntity> getExpiredMedicines() {
	    return pharmacyService.getExpiredMedicines();
	}

	@GetMapping("/medicines/near-expiry")
	public List<PharmacyEntity> getNearExpiryMedicines() {
	    return pharmacyService.getNearExpiryMedicines();
	}
	 @GetMapping("/dashboard")
	    public ResponseEntity<PharmacyDashboardResponse> getPharmacyDashboard() {
	        PharmacyDashboardResponse dashboard = pharmacyService.getPharmacyDashboard();
	        return new ResponseEntity<>(dashboard, HttpStatus.OK);
	    }
	 //get 5 medicines by price
	 @GetMapping("/medicines/top5")
		public ResponseEntity<List<PharmacyEntity>> getTop5MedicinesByPrice() {
			List<PharmacyEntity> topMedicines = pharmacyService.getTop5MedicinesByPrice();
			return new ResponseEntity<>(topMedicines, HttpStatus.OK);
		}

}

