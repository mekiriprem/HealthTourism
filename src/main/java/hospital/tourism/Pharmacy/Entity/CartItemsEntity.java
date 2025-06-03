package hospital.tourism.Pharmacy.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items")
public class CartItemsEntity {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
	private Integer cartId;
	
	
	private Integer userId; // Can be skipped if single user or mock value

    private Integer madicineid;

    private String medicineName;
    private String medicineImage;
    private double medicinePrice;
 
//	@ManyToOne
//	@JoinColumn(name = "pharmacy_id") // or any appropriate FK
//	private PharmacyEntity pharmacyEntity;
// // Reference to the PharmacyEntity
	 
	private Integer quantity; // Quantity of the item in the cart
}
