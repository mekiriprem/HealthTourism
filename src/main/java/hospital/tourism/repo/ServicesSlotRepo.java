package hospital.tourism.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.tourism.Entity.ServiceSlot;

@Repository
public interface ServicesSlotRepo extends JpaRepository<ServiceSlot, Long> {

    List<ServiceSlot> findByChef_ChefID(Long id);
    List<ServiceSlot> findByDoctor_id(Long id);
    List<ServiceSlot> findBySpa_ServiceId(Long id);
    List<ServiceSlot> findByPhysio_PhysioId(Long id);
    List<ServiceSlot> findByTranslator_TranslatorID(Long id);
    List<ServiceSlot> findByLabtest_id(Long id);
    
}

