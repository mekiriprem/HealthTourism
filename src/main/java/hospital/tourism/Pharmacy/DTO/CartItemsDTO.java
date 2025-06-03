package hospital.tourism.Pharmacy.DTO;

import lombok.Data;

@Data
public class CartItemsDTO {
	 private Integer cartId;
	    private Integer userId;
	    private Integer madicineid;
	    private String medicineName;
	    private String medicineImage;
	    private double medicinePrice;
	    private Integer quantity;
}
