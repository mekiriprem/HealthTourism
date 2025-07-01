package hospital.tourism.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.CouponApplyRequest;
import hospital.tourism.Dto.CouponApplyResponse;
import hospital.tourism.Dto.CouponResponseDTO;
import hospital.tourism.Entity.CouponEntity;
import hospital.tourism.Service.CouponServiceIMple;
import hospital.tourism.repo.CouponRepository;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
	
	  @Autowired
	  private  CouponServiceIMple couponService;
	  
	  @Autowired
		private CouponRepository couponRepository;

	    @PostMapping
	    public ResponseEntity<CouponResponseDTO> createCoupon(@RequestBody CouponEntity coupon) {
	        return ResponseEntity.ok(couponService.createCoupon(coupon));
	    }

	    @GetMapping
	    public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {
	        return ResponseEntity.ok(couponService.getAllCoupons());
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<CouponResponseDTO> updateCoupon(@PathVariable Integer id, @RequestBody CouponEntity coupon) {
	        return ResponseEntity.ok(couponService.updateCoupon(id, coupon));
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
	        couponService.deleteCoupon(id);
	        return ResponseEntity.noContent().build();
	    }
	    @GetMapping("/{code}")
		public ResponseEntity<CouponResponseDTO> getCouponByCode(@PathVariable String code) {
			 couponService.getCouponByCodes(code);
			return ResponseEntity.ok(couponService.getCouponByCodes(code));
		}
	    
	    @GetMapping("/validate")
	    public ResponseEntity<?> validateCoupon(@RequestParam String code, @RequestParam double total) {
	        CouponEntity coupon = couponRepository.findByCode(code);

	        if (coupon == null || !coupon.isActive() ||
	            (coupon.getValidFrom() != null && coupon.getValidFrom().isAfter(LocalDateTime.now())) ||
	            (coupon.getValidTill() != null && coupon.getValidTill().isBefore(LocalDateTime.now()))) {
	            return ResponseEntity.badRequest().body("Invalid or expired coupon");
	        }

	        double discount = 0.0;
	        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
	            discount = total * coupon.getDiscountAmount() / 100.0;
	        } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
	            discount = coupon.getDiscountAmount();
	        }

	        return ResponseEntity.ok(Map.of(
	            "discount", discount,
	            "finalAmount", total - discount
	        ));
	    }
	    
//	    @PostMapping("/apply")
//	    public ResponseEntity<CouponApplyResponse> applyCoupon(@RequestBody CouponApplyRequest req) {
//	        double total = 0.0;
//
//	        for (int i = 0; i < req.getServiceIds().size(); i++) {
//	            Long id = req.getServiceIds().get(i);
//	            String type = req.getServiceTypes().get(i).toLowerCase();
//
//	            switch (type) {
//	                case "spa" -> total += spaRepo.findById(id).orElseThrow().getPrice();
//	                case "chef" -> total += chefRepo.findById(id).orElseThrow().getPrice();
//	                case "doctor" -> total += doctorRepo.findById(id).orElseThrow().getPrice();
//	                case "translator" -> total += translatorRepo.findById(id).orElseThrow().getPrice();
//	                case "labtests" -> total += labRepo.findById(id).orElseThrow().getPrice();
//	                case "physio" -> total += physioRepo.findById(id).orElseThrow().getPrice();
//	            }
//	        }
//
//	        double discount = 0.0;
//
//	        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
//	            CouponEntity coupon = couponRepository.findByCode(req.getCouponCode());
//
//	            if (coupon != null && coupon.isActive()
//	                && (coupon.getValidFrom() == null || !coupon.getValidFrom().isAfter(LocalDateTime.now()))
//	                && (coupon.getValidTill() == null || !coupon.getValidTill().isBefore(LocalDateTime.now()))) {
//
//	                if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
//	                    discount = total * coupon.getDiscountAmount() / 100.0;
//	                } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
//	                    discount = coupon.getDiscountAmount();
//	                }
//	            }
//	        }
//
//	        double finalAmount = total - discount;
//	        return ResponseEntity.ok(new CouponApplyResponse(total, discount, finalAmount));
//	    }
	    
	    @PostMapping("/apply")
	    public ResponseEntity<CouponApplyResponse> applyCoupons(@RequestBody CouponApplyRequest req) {
	        CouponApplyResponse response = couponService.applyCoupon(req);
	        return ResponseEntity.ok(response);
	    }
	   @PutMapping("/active/{id}")
		public void activateCoupon(@PathVariable Integer id) {
		   couponService.activateCouponById(id);
		}

		@PutMapping("/deactivate/{id}")
		public void deactivateCoupon(@PathVariable Integer id) {
			couponService.inactivateCouponById(id);
		}
}

