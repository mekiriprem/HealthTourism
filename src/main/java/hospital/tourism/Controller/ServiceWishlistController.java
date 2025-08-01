package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.WishlistResponseDTO;
import hospital.tourism.Service.ServiceWishlistService;



@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
@RestController
@RequestMapping("/api/wishlist")
public class ServiceWishlistController {

	@Autowired
    private ServiceWishlistService wishlistService;
	
	
	
	
//	 @PostMapping("/add/{userId}/{serviceType}/{serviceId}")
//	    public ResponseEntity<WishlistResponseDTO> addToWishlist(
//	            @PathVariable Long userId,
//	            @PathVariable String serviceType,
//	            @PathVariable Long serviceId
//	    ) {
//	        return ResponseEntity.ok(wishlistService.addToWishlist(userId, serviceType, serviceId));
//	    }

	 @PostMapping("/add/{userId}/{serviceType}/{serviceId}")
	    public ResponseEntity<WishlistResponseDTO> addToWishlist(
	            @PathVariable Long userId,
	            @PathVariable String serviceType,
	            @PathVariable Long serviceId
	    ) {
	        return ResponseEntity.ok(wishlistService.addToWishlist(userId, serviceType, serviceId));
	    }

	    @GetMapping("/{userId}")
	    public ResponseEntity<List<WishlistResponseDTO>> getUserWishlist(@PathVariable Long userId) {
	        return ResponseEntity.ok(wishlistService.getUserWishlist(userId));
	    }

	    @DeleteMapping("/remove/{wishlistId}")
	    public ResponseEntity<String> removeFromWishlist(@PathVariable Long wishlistId) {
	        return ResponseEntity.ok(wishlistService.removeFromWishlist(wishlistId));
	    }
}
