package hospital.tourism.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.WishlistService;

public interface ServiceWishlistRepository extends JpaRepository<WishlistService, Long> {

	 List<WishlistService> findByUserId(Long userId);

	 boolean existsByUser_IdAndDoctor_Id(Long userId, Long doctorId);

	    boolean existsByUser_IdAndSpa_ServiceId(Long userId, Long spaId);

	    boolean existsByUser_IdAndPhysio_PhysioId(Long userId, Long physioId);

	    boolean existsByUser_IdAndTranslator_TranslatorID(Long userId, Long translatorId);

	    boolean existsByUser_IdAndLabtests_Id(Long userId, Long labId);

	    boolean existsByUser_IdAndChef_ChefID(Long userId, Long chefId);

	    
	 
	 
}
