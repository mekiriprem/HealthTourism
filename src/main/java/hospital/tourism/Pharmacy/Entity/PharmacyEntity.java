package hospital.tourism.Pharmacy.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pharmacy")
public class PharmacyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "pharmacy_id")
//	private List<CartItemsEntity> cartItems;

}
