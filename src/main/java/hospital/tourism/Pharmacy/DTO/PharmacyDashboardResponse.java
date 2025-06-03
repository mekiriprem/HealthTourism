package hospital.tourism.Pharmacy.DTO;

import java.util.List;

import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import lombok.Data;

@Data
public class PharmacyDashboardResponse {
	private List<PharmacyEntity> allMedicines;
    private List<PharmacyEntity> lowStockMedicines;
    private List<PharmacyEntity> expiredMedicines;
    private List<PharmacyEntity> nearExpiryMedicines;
}
