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
import hospital.tourism.Dto.CouponResponseDTO;
import hospital.tourism.Dto.CouponServiceMappingDTO;
import hospital.tourism.Entity.CouponEntity;
import hospital.tourism.Entity.CouponServiceMapping;
import hospital.tourism.repo.CouponRepository;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;
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

	public CouponResponseDTO createCoupon(CouponEntity coupon) {
		if (coupon.getApplicableServices() != null && !coupon.getApplicableServices().isEmpty()) {
			for (CouponServiceMapping mapping : coupon.getApplicableServices()) {
				mapping.setCoupon(coupon);
			}
		}

		CouponEntity saved = couponRepository.save(coupon);

		// Convert to DTO response
		CouponResponseDTO dto = new CouponResponseDTO();
		dto.setId(saved.getId());
		dto.setCode(saved.getCode());
		dto.setDiscountType(saved.getDiscountType());
		dto.setDiscountAmount(saved.getDiscountAmount());
		dto.setType(saved.getType());
		dto.setName(saved.getName());
		dto.setDescription(saved.getDescription());
		dto.setActive(saved.isActive());
		dto.setValidFrom(saved.getValidFrom());
		dto.setValidTill(saved.getValidTill());

		// Convert applicableServices
		List<CouponServiceMappingDTO> mappings = saved.getApplicableServices().stream().map(service -> {
			CouponServiceMappingDTO d = new CouponServiceMappingDTO();
			d.setServiceType(service.getServiceType());

			return d;
		}).toList();

		dto.setApplicableServices(mappings);

		return dto;
	}

	// List all coupons
	public List<CouponResponseDTO> getAllCoupons() {
		return couponRepository.findAll().stream().map(coupon -> {
			CouponResponseDTO dto = new CouponResponseDTO();
			dto.setId(coupon.getId());
			dto.setCode(coupon.getCode());
			dto.setDiscountType(coupon.getDiscountType());
			dto.setDiscountAmount(coupon.getDiscountAmount());
			dto.setType(coupon.getType());
			dto.setName(coupon.getName());
			dto.setDescription(coupon.getDescription());
			dto.setActive(coupon.isActive());
			dto.setValidFrom(coupon.getValidFrom());
			dto.setValidTill(coupon.getValidTill());

			List<CouponServiceMappingDTO> mappings = coupon.getApplicableServices().stream().map(service -> {
				CouponServiceMappingDTO d = new CouponServiceMappingDTO();
				d.setServiceType(service.getServiceType());

				return d;
			}).toList();

			dto.setApplicableServices(mappings);
			return dto;
		}).toList();
	}

	public CouponResponseDTO updateCoupon(Integer id, CouponEntity updatedCoupon) {
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

		CouponEntity saved = couponRepository.save(existing);

		// Convert to DTO
		CouponResponseDTO dto = new CouponResponseDTO();
		dto.setId(saved.getId());
		dto.setCode(saved.getCode());
		dto.setDiscountType(saved.getDiscountType());
		dto.setDiscountAmount(saved.getDiscountAmount());
		dto.setType(saved.getType());
		dto.setName(saved.getName());
		dto.setDescription(saved.getDescription());
		dto.setActive(saved.isActive());
		dto.setValidFrom(saved.getValidFrom());
		dto.setValidTill(saved.getValidTill());

		List<CouponServiceMappingDTO> mappings = saved.getApplicableServices().stream().map(service -> {
			CouponServiceMappingDTO d = new CouponServiceMappingDTO();
			d.setServiceType(service.getServiceType());

			return d;
		}).toList();

		dto.setApplicableServices(mappings);
		return dto;
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

	/* public CouponApplyResponse applyCoupon(CouponApplyRequest req) {
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
	}*/

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
		                continue; // skip doctor from paid services if free
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
		
		            // Extract requested service types (ignoring IDs)
		            Set<String> requestedTypes = req.getServiceTypes().stream()
		                    .map(String::toUpperCase)
		                    .collect(Collectors.toSet());
		
		            // Extract required types from coupon
		            Set<String> requiredTypes = coupon.getApplicableServices().stream()
		                    .map(s -> s.getServiceType().toUpperCase())
		                    .collect(Collectors.toSet());
		
		            if (requestedTypes.containsAll(requiredTypes)) {
		                if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
		                    discount = paidServiceTotal * coupon.getDiscountAmount() / 100.0;
		                } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
		                    discount = Math.min(coupon.getDiscountAmount(), paidServiceTotal);
		                }
		            } else {
		                throw new IllegalArgumentException("This coupon requires all mapped service types to be selected.");
		            }
		        }
		    }
		
		    double finalAmount = total - discount;
		    return new CouponApplyResponse(total, discount, finalAmount);
		}

	/*    public CouponApplyResponse applyCoupon(CouponApplyRequest req) {
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
	                continue; // skip further discount logic for doctor
	            }
	        }
	
	        total += price;
	        if (price > 0) {
	            paidServiceTotal += price;
	            hasOnlyFreeDoctor = false;
	            hasPaidService = true;
	        }
	    }
	
	    if (!hasPaidService && hasOnlyFreeDoctor) {
	        throw new IllegalArgumentException("Coupon cannot be applied to free doctor service alone.");
	    }
	
	    double discount = 0.0;
	
	    if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
	        CouponEntity coupon = couponRepository.findByCode(req.getCouponCode());
	
	        if (coupon != null && coupon.isActive()
	                && (coupon.getValidFrom() == null || !coupon.getValidFrom().isAfter(LocalDateTime.now()))
	                && (coupon.getValidTill() == null || !coupon.getValidTill().isBefore(LocalDateTime.now()))) {
	
	            // Extract requested service types (e.g., ["SPA", "CHEF"])
	            Set<String> requestedTypes = req.getServiceTypes().stream()
	                    .map(String::toUpperCase)
	                    .collect(Collectors.toSet());
	
	            // Extract mapped service types from coupon
	            Set<String> requiredTypes = coupon.getApplicableServices().stream()
	                    .map(mapping -> mapping.getServiceType().toUpperCase())
	                    .collect(Collectors.toSet());
	
	            // Ensure exact match (coupon only applies to these types)
	            if (requestedTypes.equals(requiredTypes)) {
	                if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
	                    discount = paidServiceTotal * coupon.getDiscountAmount() / 100.0;
	                } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
	                    discount = Math.min(coupon.getDiscountAmount(), paidServiceTotal);
	                }
	            } else {
	                throw new IllegalArgumentException("This coupon can only be applied to exact service types: " + requiredTypes);
	            }
	        }
	    }
	
	    double finalAmount = total - discount;
	    return new CouponApplyResponse(total, discount, finalAmount);
	}*/

	public CouponResponseDTO getCouponById(Integer id) {
		CouponEntity coupon = couponRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));

		// Convert CouponEntity to CouponResponseDTO
		CouponResponseDTO dto = new CouponResponseDTO();
		dto.setId(coupon.getId());
		dto.setCode(coupon.getCode());
		dto.setDiscountType(coupon.getDiscountType());
		dto.setDiscountAmount(coupon.getDiscountAmount());
		dto.setType(coupon.getType());
		dto.setName(coupon.getName());
		dto.setDescription(coupon.getDescription());
		dto.setActive(coupon.isActive());
		dto.setValidFrom(coupon.getValidFrom());
		dto.setValidTill(coupon.getValidTill());

		// Map applicable services
		List<CouponServiceMappingDTO> mappingDTOs = coupon.getApplicableServices().stream().map(service -> {
			CouponServiceMappingDTO serviceDTO = new CouponServiceMappingDTO();
			serviceDTO.setServiceType(service.getServiceType());
			// Optional: include if available
			return serviceDTO;
		}).toList();

		dto.setApplicableServices(mappingDTOs);
		return dto;
	}

	// get by coupon code
	public CouponResponseDTO getCouponByCodes(String code) {
		CouponEntity coupon = couponRepository.findByCodeAndIsActiveTrue(code)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with code: " + code));

		// Convert CouponEntity to CouponResponseDTO
		CouponResponseDTO dto = new CouponResponseDTO();
		dto.setId(coupon.getId());
		dto.setCode(coupon.getCode());
		dto.setDiscountType(coupon.getDiscountType());
		dto.setDiscountAmount(coupon.getDiscountAmount());
		dto.setType(coupon.getType());
		dto.setName(coupon.getName());
		dto.setDescription(coupon.getDescription());
		dto.setActive(coupon.isActive());
		dto.setValidFrom(coupon.getValidFrom());
		dto.setValidTill(coupon.getValidTill());

		// Map applicable services
		List<CouponServiceMappingDTO> mappingDTOs = coupon.getApplicableServices().stream().map(service -> {
			CouponServiceMappingDTO serviceDTO = new CouponServiceMappingDTO();
			serviceDTO.setServiceType(service.getServiceType());
			// Optional: include if available
			return serviceDTO;
		}).toList();

		dto.setApplicableServices(mappingDTOs);
		return dto;
	}

	// inactivate coupon by id
	public void inactivateCouponById(Integer id) {
		// Find the coupon by ID and activate it
		CouponEntity coupon = couponRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));
		coupon.setActive(false);
	}

	// activate coupon by id
	public void activateCouponById(Integer id) {
		// Find the coupon by ID and activate it
		CouponEntity coupon = couponRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));
		coupon.setActive(true);
	}
}
