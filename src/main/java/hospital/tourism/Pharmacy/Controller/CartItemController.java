package hospital.tourism.Pharmacy.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Pharmacy.DTO.CartItemsDTO;
import hospital.tourism.Pharmacy.DTO.PharmacyDTO;
import hospital.tourism.Pharmacy.Entity.CartItemsEntity;
import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import hospital.tourism.Pharmacy.Service.CartItemService;
@RestController
@RequestMapping("/cart-item/user")
public class CartItemController {

	
	 @Autowired
	    private CartItemService userService;

	    // ✅ 1. Get available medicines
	    @GetMapping("/capsules-user")
	    public ResponseEntity<List<PharmacyDTO>> getAvailableMedicines() {
	        return ResponseEntity.ok(userService.getAvailableMedicines());
	    }

	    // ✅ 2. Get medicine by ID
	    @GetMapping("/capsules/{id}")
	    public ResponseEntity<PharmacyEntity> getMedicineById(@PathVariable Integer id) {
	        return ResponseEntity.ok(userService.getMedicineById(id));
	    }

//	    // ✅ 3. Add to cart
//	    @PostMapping("/cart/add")
//	    public ResponseEntity<CartItemsEntity> addToCart(
//	        @RequestParam Integer userId,
//	        @RequestParam Integer madicineid,
//	        @RequestParam Integer qty) {
//	        return ResponseEntity.ok(userService.addToCart(userId, madicineid, qty));
//	    }
	    
	    @PostMapping("/cart/add")
	    public ResponseEntity<CartItemsDTO> addToCart(
	            @RequestParam Integer userId,
	            @RequestParam Integer madicineid,
	            @RequestParam Integer qty) {

	        CartItemsDTO addedItem = userService.addToCart(userId, madicineid, qty);
	        if (addedItem == null) {
	            return ResponseEntity.badRequest().build();  // or ResponseEntity.status(404).build() if medicine not found
	        }
	        return ResponseEntity.ok(addedItem);
	    }

	    // ✅ 4. View cart
	    @GetMapping("/cart/{userId}")
	    public ResponseEntity<List<CartItemsDTO>> getCart(@PathVariable Integer userId) {
	        return ResponseEntity.ok(userService.getCart(userId));
	    }

	    // ✅ 5. Remove from cart
	    @DeleteMapping("/cart/remove")
	    public ResponseEntity<String> removeFromCart(
	        @RequestParam Integer userId,
	        @RequestParam Integer madicineid) {
	        userService.removeFromCart(userId, madicineid);
	        return ResponseEntity.ok("Item removed from cart.");
	    }

	    // ✅ 6. Update cart quantity
	    @PutMapping("/cart/update")
	    public ResponseEntity<CartItemsEntity> updateCart(
	        @RequestParam Integer userId,
	        @RequestParam Integer madicineid,
	        @RequestParam Integer qty) {
	        return ResponseEntity.ok(userService.updateCartQuantity(userId, madicineid, qty));
	    }
	    
	    // ✅ 7. Clear cart multiple items
	    @DeleteMapping("/cart/clear")
		public ResponseEntity<String> clearCart(@RequestParam List<Integer> cartIds) {
			String result = userService.deleteMultipleItemsFromCart(cartIds);
			return ResponseEntity.ok(result);
		}
	    
	    //delete the single item from the cart
	    @DeleteMapping("/cart/clear/{cartId}")
		public ResponseEntity<String> clearCartById(@PathVariable Integer cartId) {
			userService.removeFromCartById(cartId);
			return ResponseEntity.ok("Item successfully deleted from the cart.");
		}
}
