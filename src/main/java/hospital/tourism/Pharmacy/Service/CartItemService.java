package hospital.tourism.Pharmacy.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Pharmacy.DTO.CartItemsDTO;
import hospital.tourism.Pharmacy.DTO.PharmacyDTO;
import hospital.tourism.Pharmacy.Entity.CartItemsEntity;
import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import hospital.tourism.Pharmacy.Repository.ICartRepository;
import hospital.tourism.Pharmacy.Repository.ImadicineRepository;

@Service
public class CartItemService {


    @Autowired
    private ImadicineRepository madicineRepository;

    @Autowired
    private ICartRepository cartRepository;

//    // ✅ 1. View all valid (non-expired, in-stock) medicines
//    public List<PharmacyEntity> getAvailableMedicines() {
//        return madicineRepository.findAll().stream()
//            .filter(m -> m.getMedicineQuantity() > 0 &&
//                         m.getMedicineExpiryDate().isAfter(LocalDateTime.now()))
//            .collect(Collectors.toList());
//    }
    public List<PharmacyDTO> getAvailableMedicines() {
        return madicineRepository.findAll().stream()
            .filter(m -> m.getMedicineQuantity() > 0)
            .map(m -> {
                PharmacyDTO dto = new PharmacyDTO();
                dto.setMadicineid(m.getMadicineid());
                dto.setMedicineName(m.getMedicineName());
                dto.setMedicineType(m.getMedicineType());
                dto.setMedicineDescription(m.getMedicineDescription());
                dto.setMedicinePrice(m.getMedicinePrice());
                dto.setMedicineQuantity(m.getMedicineQuantity());
                dto.setMedicineExpiryDate(m.getMedicineExpiryDate());
                dto.setMedicineManufacturer(m.getMedicineManufacturer());
                dto.setMedicineImage(m.getMedicineImage());
                dto.setMedicineCategory(m.getMedicineCategory());
                return dto;
            })
            .collect(Collectors.toList());
    }



    // ✅ 2. View single medicine
    public PharmacyEntity getMedicineById(Integer id) {
        return madicineRepository.findById(id).orElse(null);
    }

    // ✅ 3. Add to cart
//    public CartItemsEntity addToCart(Integer userId, Integer madicineid, Integer qty) {
//        PharmacyEntity med = madicineRepository.findById(madicineid).orElse(null);
//        if (med == null || med.getMedicineQuantity() <= 0) return null;
//
//        Optional<CartItemsEntity> existing = cartRepository.findByUserIdAndMadicineid(userId, madicineid);
//
//        if (existing.isPresent()) {
//            CartItemsEntity item = existing.get();
//            item.setQuantity(item.getQuantity() + qty);
//            return cartRepository.save(item);
//        }
//
//        CartItemsEntity newItem = new CartItemsEntity();
//        newItem.setUserId(userId);
//        newItem.setMadicineid(madicineid);
//        newItem.setMedicineName(med.getMedicineName());
//        newItem.setMedicineImage(med.getMedicineImage());
//        newItem.setMedicinePrice(med.getMedicinePrice());
//        newItem.setQuantity(qty);
//        newItem.setPharmacyEntity(med);
//        return cartRepository.save(newItem);
//    }
    
    public CartItemsDTO addToCart(Integer userId, Integer madicineid, Integer qty) {
        PharmacyEntity med = madicineRepository.findById(madicineid).orElse(null);
        if (med == null || med.getMedicineQuantity() <= 0) return null;

        Optional<CartItemsEntity> existing = cartRepository.findByUserIdAndMadicineid(userId, madicineid);

        CartItemsEntity savedItem;

        if (existing.isPresent()) {
            CartItemsEntity item = existing.get();
            item.setQuantity(item.getQuantity() + qty);
            savedItem = cartRepository.save(item);
        } else {
            CartItemsEntity newItem = new CartItemsEntity();
            newItem.setUserId(userId);
            newItem.setMadicineid(madicineid);
            newItem.setMedicineName(med.getMedicineName());
            newItem.setMedicineImage(med.getMedicineImage());
            newItem.setMedicinePrice(med.getMedicinePrice());
            newItem.setQuantity(qty);
            
            savedItem = cartRepository.save(newItem);
        }

        // Map entity to DTO
        CartItemsDTO dto = new CartItemsDTO();
        dto.setCartId(savedItem.getCartId());
        dto.setUserId(savedItem.getUserId());
        dto.setMadicineid(savedItem.getMadicineid());
        dto.setMedicineName(savedItem.getMedicineName());
        dto.setMedicineImage(savedItem.getMedicineImage());
        dto.setMedicinePrice(savedItem.getMedicinePrice());
        dto.setQuantity(savedItem.getQuantity());

        return dto;
    }



    public List<CartItemsDTO> getCart(Integer userId) {
		return cartRepository.findByUserId(userId).stream().map(item -> {
			CartItemsDTO dto = new CartItemsDTO();
			dto.setCartId(item.getCartId());
			dto.setUserId(item.getUserId());
			dto.setMadicineid(item.getMadicineid());
			dto.setMedicineName(item.getMedicineName());
			dto.setMedicineImage(item.getMedicineImage());
			dto.setMedicinePrice(item.getMedicinePrice());
			dto.setQuantity(item.getQuantity());
			return dto;
		}).collect(Collectors.toList());
    }

    // ✅ 5. Remove from cart
    public void removeFromCart(Integer userId, Integer madicineid) {
        cartRepository.deleteByUserIdAndMadicineid(userId, madicineid);
    }

    // ✅ 6. Update cart quantity
    public CartItemsEntity updateCartQuantity(Integer userId, Integer madicineid, Integer qty) {
        CartItemsEntity item = cartRepository.findByUserIdAndMadicineid(userId, madicineid).orElse(null);
        if (item == null) return null;
        item.setQuantity(qty);
        return cartRepository.save(item);
    }
    
    //delete the multiple items from the cart
	public String deleteMultipleItemsFromCart(List<Integer> cartIds) {
		List<CartItemsEntity> itemsToDelete = cartRepository.findAllById(cartIds);
		if (itemsToDelete.isEmpty()) {
			return "No items found for the provided cart IDs.";
		}

		cartRepository.deleteAll(itemsToDelete);
		return "Items successfully deleted from the cart.";
	}
	//delete the single item from the cart
	
	public String removeFromCartById(Integer cartId) {
		Optional<CartItemsEntity> item = cartRepository.findById(cartId);
		if (item.isPresent()) {
			cartRepository.delete(item.get());
			return "Item successfully deleted from the cart.";
		} else {
			return "Item not found in the cart.";
		}
	}
	//get quantity of the cart items
	public int getCartItemCount(Integer userId) {
		List<CartItemsEntity> cartItems = cartRepository.findByUserId(userId);
		return cartItems.stream().mapToInt(CartItemsEntity::getQuantity).sum();
	}
}
