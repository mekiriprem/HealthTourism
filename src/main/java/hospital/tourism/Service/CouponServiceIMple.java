package hospital.tourism.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.CouponApplyRequest;
import hospital.tourism.Dto.CouponApplyResponse;
import hospital.tourism.Entity.CouponEntity;
import hospital.tourism.Entity.CouponServiceMapping;
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
	
	    private final chefsRepo chefRepository;
	    private final PhysioRepo physioRepository;
	    private final TranslatorsRepo translatorRepository;
	    private final SpaservicesRepo spaServiceRepository;
	    private final DoctorsRepo doctorRepository;
	    private final labtestsRepo labtestsRepository;
	
	 // Create a new coupon
	    public CouponEntity createCoupon(CouponEntity coupon) {
	        if (coupon.getApplicableServices() != null) {
	            for (CouponServiceMapping mapping : coupon.getApplicableServices()) {
	                mapping.setCoupon(coupon);
	            }
	        }
	        return couponRepository.save(coupon);
	    }


    // List all coupons
    public List<CouponEntity> getAllCoupons() {
        return couponRepository.findAll();
    }

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

        // Replace existing mappings
        existing.getApplicableServices().clear();
        if (updatedCoupon.getApplicableServices() != null) {
            for (CouponServiceMapping mapping : updatedCoupon.getApplicableServices()) {
                mapping.setCoupon(existing);
                existing.getApplicableServices().add(mapping);
            }
        }

        return couponRepository.save(existing);
    }

    public void deleteCoupon(Integer id) {
        if (!couponRepository.existsById(id)) {
            throw new EntityNotFoundException("Coupon not found with ID: " + id);
        }
        couponRepository.deleteById(id);
    }

    public Object getCouponByCode(String code) {
        return couponRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found with code: " + code));
    }
    
    
    
    public CouponApplyResponse applyCoupon(CouponApplyRequest req) {
        double total = 0.0;
        double paidServiceTotal = 0.0;
        boolean hasPaidService = false;
        boolean hasOnlyFreeDoctor = true;

        for (int i = 0; i < req.getServiceIds().size(); i++) {
            Long id = req.getServiceIds().get(i);
            String type = req.getServiceTypes().get(i).toLowerCase();
            double price = 0.0;

            switch (type) {
                case "spa" -> {
                    var spa = spaServiceRepository.findById(id).orElse(null);
                    if (spa != null) price = spa.getPrice();
                }
                case "chef" -> {
                    var chef = chefRepository.findById(id).orElse(null);
                    if (chef != null) price = chef.getPrice();
                }
                case "translator" -> {
                    var translator = translatorRepository.findById(id).orElse(null);
                    if (translator != null) price = translator.getPrice();
                }
                case "labtests" -> {
                    var labtest = labtestsRepository.findById(id).orElse(null);
                    if (labtest != null) price = labtest.getTestPrice();
                }
                case "physio" -> {
                    var physio = physioRepository.findById(id).orElse(null);
                    if (physio != null) price = physio.getPrice();
                }
                case "doctor" -> {
                    var doctor = doctorRepository.findById(id).orElse(null);
                    if (doctor != null) {
                        price = doctor.getPrice();
                        total += price;
                        if (price > 0) {
                            paidServiceTotal += price;
                            hasOnlyFreeDoctor = false;
                            hasPaidService = true;
                        }
                    }
                    continue; // skip doctor from paid services
                }
            }

            total += price;

            if (price > 0) {
                paidServiceTotal += price;
                hasOnlyFreeDoctor = false;
                hasPaidService = true;
            }
        }

        // ❌ Only free doctor selected
        if (!hasPaidService && hasOnlyFreeDoctor) {
            throw new IllegalArgumentException("Coupon cannot be applied to free doctor service alone.");
        }

        double discount = 0.0;

        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
            CouponEntity coupon = couponRepository.findByCode(req.getCouponCode());

            if (coupon != null && coupon.isActive()
                    && (coupon.getValidFrom() == null || !coupon.getValidFrom().isAfter(LocalDateTime.now()))
                    && (coupon.getValidTill() == null || !coupon.getValidTill().isBefore(LocalDateTime.now()))) {

                // Build sets for strict validation
                Set<String> requestKeys = new HashSet<>();
                for (int i = 0; i < req.getServiceIds().size(); i++) {
                    String key = req.getServiceTypes().get(i).toUpperCase() + ":" + req.getServiceIds().get(i);
                    requestKeys.add(key);
                }

                Set<String> couponKeys = coupon.getApplicableServices().stream()
                        .map(s -> s.getServiceType().toUpperCase() + ":" + s.getServiceId())
                        .collect(Collectors.toSet());

                // ✅ Apply only if all mapped services are present
                if (requestKeys.containsAll(couponKeys)) {
                    if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                        discount = paidServiceTotal * coupon.getDiscountAmount() / 100.0;
                    } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
                        discount = Math.min(coupon.getDiscountAmount(), paidServiceTotal);
                    }
                } else {
                    throw new IllegalArgumentException("This coupon requires all mapped services to be selected.");
                }
            }
        }

        double finalAmount = total - discount;
        return new CouponApplyResponse(total, discount, finalAmount);
    }



	public CouponEntity getCouponById(Integer id) {
		return couponRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));
	}

}
