package hospital.tourism.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.CouponApplyRequest;
import hospital.tourism.Dto.CouponApplyResponse;
import hospital.tourism.Entity.CouponEntity;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.CouponRepository;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;
import hospital.tourism.repo.usersrepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CouponServiceIMple {

	@Autowired
	private CouponRepository couponRepository;
	
	  private final BookingRepo bookingRepository;
	    private final usersrepo userRepository;
	    private final chefsRepo chefRepository;
	    private final PhysioRepo physioRepository;
	    private final TranslatorsRepo translatorRepository;
	    private final SpaservicesRepo spaServiceRepository;
	    private final DoctorsRepo doctorRepository;
	    private final labtestsRepo labtestsRepository;
	
	 // Create a new coupon
    public CouponEntity createCoupon(CouponEntity coupon) {
        return couponRepository.save(coupon);
    }

    // List all coupons
    public List<CouponEntity> getAllCoupons() {
        return couponRepository.findAll();
    }

    // Update a coupon by ID
    public CouponEntity updateCoupon(Integer id, CouponEntity updatedCoupon) {
        CouponEntity existing = couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));

        existing.setCode(updatedCoupon.getCode());
        existing.setDiscountType(updatedCoupon.getDiscountType());
        existing.setDiscountAmount(updatedCoupon.getDiscountAmount());
        existing.setType(updatedCoupon.getType());
        existing.setName(updatedCoupon.getName());
        existing.setActive(updatedCoupon.isActive());
        existing.setValidFrom(updatedCoupon.getValidFrom());
        existing.setValidTill(updatedCoupon.getValidTill());
        existing.setDescription(updatedCoupon.getDescription());

        return couponRepository.save(existing);
    }

    // Delete a coupon by ID
    public void deleteCoupon(Integer id) {
        if (!couponRepository.existsById(id)) {
            throw new EntityNotFoundException("Coupon not found with ID: " + id);
        }
        couponRepository.deleteById(id);
    }

	public Object getCouponByCode(String code) {
		CouponEntity coupon = couponRepository.findByCodeAndIsActiveTrue(code)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with code: " + code));
		return null;
	}
	
	 public CouponApplyResponse applyCoupon(CouponApplyRequest req) {
	        double total = 0.0;

	        for (int i = 0; i < req.getServiceIds().size(); i++) {
	            Long id = req.getServiceIds().get(i);
	            String type = req.getServiceTypes().get(i).toLowerCase();

	            switch (type) {
	                case "spa" -> total += spaServiceRepository.findById(id).orElseThrow().getPrice();
	                case "chef" -> total += chefRepository.findById(id).orElseThrow().getPrice();
	                case "doctor" -> total += doctorRepository.findById(id).orElseThrow().getPrice();
	                case "translator" -> total += translatorRepository.findById(id).orElseThrow().getPrice();
	                case "labtests" -> total += labtestsRepository.findById(id).orElseThrow().getTestPrice();
	                case "physio" -> total += physioRepository.findById(id).orElseThrow().getPrice();
	            }
	        }

	        double discount = 0.0;

	        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
	            CouponEntity coupon = couponRepository.findByCode(req.getCouponCode());

	            if (coupon != null && coupon.isActive()
	                && (coupon.getValidFrom() == null || !coupon.getValidFrom().isAfter(LocalDateTime.now()))
	                && (coupon.getValidTill() == null || !coupon.getValidTill().isBefore(LocalDateTime.now()))) {

	                if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
	                    discount = total * coupon.getDiscountAmount() / 100.0;
	                } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
	                    discount = coupon.getDiscountAmount();
	                }
	            }
	        }

	        double finalAmount = total - discount;
	        return new CouponApplyResponse(total, discount, finalAmount);
	    }
}
