package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.WishlistResponseDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Entity.Translators;
import hospital.tourism.Entity.WishlistService;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.ServiceWishlistRepository;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;
import hospital.tourism.repo.usersrepo;

@Service
public class ServiceWishlistService {

	

    @Autowired private ServiceWishlistRepository wishlistRepo;
    @Autowired private usersrepo userRepo;
    @Autowired private DoctorsRepo doctorRepo;
    @Autowired private SpaservicesRepo spaRepo;
    @Autowired private PhysioRepo physioRepo;
    @Autowired private TranslatorsRepo translatorRepo;
    @Autowired private labtestsRepo labRepo;
    @Autowired private chefsRepo chefsRepo;

//    public WishlistResponseDTO addToWishlist(Long userId, String serviceType, Long serviceId) {
//
//        users user = userRepo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        WishlistService wishlist = new WishlistService();
//        wishlist.setUser(user);
//
//        switch (serviceType.toLowerCase()) {
//            case "doctor" -> wishlist.setDoctor(doctorRepo.findById(serviceId).orElseThrow());
//            case "spa" -> wishlist.setSpa(spaRepo.findById(serviceId).orElseThrow());
//            case "physio" -> wishlist.setPhysio(physioRepo.findById(serviceId).orElseThrow());
//            case "translator" -> wishlist.setTranslator(translatorRepo.findById(serviceId).orElseThrow());
//            case "labtests" -> wishlist.setLabtests(labRepo.findById(serviceId).orElseThrow());
//            case "chef" -> wishlist.setChef(chefsRepo.findById(serviceId).orElseThrow());
//            default -> throw new RuntimeException("Invalid service type: " + serviceType);
//        }
//
//        WishlistService savedWishlist = wishlistRepo.save(wishlist);
//        return convertToDTO(savedWishlist);
//    }

    
    public WishlistResponseDTO addToWishlist(Long userId, String serviceType, Long serviceId) {
        WishlistService wishlist = new WishlistService();

        users user = userRepo.findById(userId).orElseThrow(() ->
            new RuntimeException("User not found with id: " + userId));

        wishlist.setUser(user);

        switch (serviceType.toLowerCase()) {
            case "doctor" -> {
                Doctors doctor = doctorRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Doctor not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndDoctor_Id(userId, serviceId)) {
                    throw new RuntimeException("Doctor already in wishlist");
                }
                wishlist.setDoctor(doctor);
            }
            case "spa" -> {
                SpaServicese spa = spaRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Spa not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndSpa_ServiceId(userId, serviceId)) {
                    throw new RuntimeException("Spa already in wishlist");
                }
                wishlist.setSpa(spa);
            }
            case "physio" -> {
                Physio physio = physioRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Physio not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndPhysio_PhysioId(userId, serviceId)) {
                    throw new RuntimeException("Physio already in wishlist");
                }
                wishlist.setPhysio(physio);
            }
            case "translator" -> {
                Translators translator = translatorRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Translator not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndTranslator_TranslatorID(userId, serviceId)) {
                    throw new RuntimeException("Translator already in wishlist");
                }
                wishlist.setTranslator(translator);
            }
            case "labtests" -> {
                Labtests lab = labRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Labtest not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndLabtests_Id(userId, serviceId)) {
                    throw new RuntimeException("Labtest already in wishlist");
                }
                wishlist.setLabtests(lab);
            }
            case "chef" -> {
                Chefs chef = chefsRepo.findById(serviceId).orElseThrow(() ->
                    new RuntimeException("Chef not found with id: " + serviceId));
                if (wishlistRepo.existsByUser_IdAndChef_ChefID(userId, serviceId)) {
                    throw new RuntimeException("Chef already in wishlist");
                }
                wishlist.setChef(chef);
            }
            default -> throw new RuntimeException("Invalid service type: " + serviceType);
        }

        WishlistService savedWishlist = wishlistRepo.save(wishlist);
        return convertToDTO(savedWishlist);
    }


    private WishlistResponseDTO convertToDTO(WishlistService wishlist) {
        WishlistResponseDTO dto = new WishlistResponseDTO();

        dto.setWishlistId(wishlist.getId());
        dto.setUserId(wishlist.getUser().getId());
        dto.setUserName(wishlist.getUser().getName());
        dto.setUserEmail(wishlist.getUser().getEmail());
      

        if (wishlist.getDoctor() != null) {
            dto.setServiceType("doctor");
            dto.setServiceId(wishlist.getDoctor().getId());
            dto.setServiceName(wishlist.getDoctor().getName());
           dto.setServiceDescription(wishlist.getDoctor().getDescription());
            dto.setServiceImageUrl(wishlist.getDoctor().getProfilepic());
            dto.setServiceType(wishlist.getDoctor().getDepartment());
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
         
            dto.setServiceType("doctor");
        } else if (wishlist.getSpa() != null) {
            dto.setServiceType("spa");
            dto.setServiceId(wishlist.getSpa().getServiceId());
            dto.setServiceName(wishlist.getSpa().getServiceName());
            dto.setServiceDescription(wishlist.getSpa().getServiceDescription());
            dto.setServiceImageUrl(wishlist.getSpa().getServiceImage());
            
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
            dto.setBasePrice(wishlist.getSpa().getPrice());
            
        } else if (wishlist.getPhysio() != null) {
            dto.setServiceType("physio");
            dto.setServiceId(wishlist.getPhysio().getPhysioId());
            dto.setServiceName(wishlist.getPhysio().getPhysioName());
            dto.setServiceDescription(wishlist.getPhysio().getPhysioDescription());
            dto.setServiceImageUrl(wishlist.getPhysio().getPhysioImage());
            
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
            dto.setBasePrice(wishlist.getPhysio().getPrice());
           
        } else if (wishlist.getTranslator() != null) {
            dto.setServiceType("translator");
            dto.setServiceId(wishlist.getTranslator().getTranslatorID());
            dto.setServiceName(wishlist.getTranslator().getTranslatorName());
            dto.setServiceDescription(wishlist.getTranslator().getTranslatorLanguages());
            dto.setServiceImageUrl(wishlist.getTranslator().getTranslatorImage());
          
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
            dto.setBasePrice(wishlist.getTranslator().getPrice());
            
        } else if (wishlist.getLabtests() != null) {
            dto.setServiceType("labtests");
            dto.setServiceId(wishlist.getLabtests().getId());
            dto.setServiceName(wishlist.getLabtests().getTestDepartment());
            dto.setServiceDescription(wishlist.getLabtests().getTestDescription());
            dto.setServiceImageUrl(wishlist.getLabtests().getTestImage());
         
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
            dto.setBasePrice(wishlist.getLabtests().getTestPrice());
            
        } else if (wishlist.getChef() != null) {
            dto.setServiceType("chef");
            dto.setServiceId(wishlist.getChef().getChefID());
            dto.setServiceName(wishlist.getChef().getChefName());
            dto.setServiceDescription(wishlist.getChef().getChefDescription());
            dto.setServiceImageUrl(wishlist.getChef().getChefImage());
           
            dto.setWishlistId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getName());
            dto.setUserEmail(wishlist.getUser().getEmail());
            dto.setBasePrice(wishlist.getChef().getPrice());
            
        }

        return dto;
    }

    public List<WishlistResponseDTO> getUserWishlist(Long userId) {
    	        List<WishlistService> wishlistItems = wishlistRepo.findByUserId(userId);
				return wishlistItems.stream().map(this::convertToDTO).toList();
    }

    public String removeFromWishlist(Long wishlistId) {
        wishlistRepo.deleteById(wishlistId);
        return "Removed from wishlist";
    }
}
