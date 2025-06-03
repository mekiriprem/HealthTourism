package hospital.tourism.Pharmacy.DTO;

import java.time.LocalDateTime;
import java.util.List;

import hospital.tourism.Pharmacy.Entity.CartItemsEntity;
import lombok.Data;

@Data
public class PharmacyDTO {

	private Integer madicineid;
    private String medicineName;
    private String medicineType;
    private String medicineDescription;
    private double medicinePrice;
    private Integer medicineQuantity;
    private LocalDateTime medicineExpiryDate;
    private String medicineManufacturer;
    private String medicineImage;
    private String medicineCategory;
//	private List<CartItemsEntity> cartItems;
}
