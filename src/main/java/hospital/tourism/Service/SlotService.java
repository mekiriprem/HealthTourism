package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.ServiceSlot;
import hospital.tourism.repo.*;

@Service
public class SlotService {

    @Autowired private chefsRepo chefsRepo;
    @Autowired private DoctorsRepo doctorsRepo;
    @Autowired private SpaservicesRepo spaRepo;
    @Autowired private PhysioRepo physioRepo;
    @Autowired private TranslatorsRepo translatorsRepo;
    @Autowired private labtestsRepo labtestsRepo;
    @Autowired private ServicesSlotRepo serviceSlotRepo;

    public void addSlots(String serviceType, Long serviceId, List<String> slotTimes,Long bookedBy) {
        for (String time : slotTimes) {
            ServiceSlot slot = new ServiceSlot();
            slot.setSlotTime(time);  // ✅ Corrected this line
            slot.setBookingStatus("AVAILABLE");
            slot.setBookedBy(bookedBy);

            switch (serviceType.toLowerCase()) {
                case "chef" -> slot.setChef(chefsRepo.findById(serviceId).orElseThrow());
                case "doctor" -> slot.setDoctor(doctorsRepo.findById(serviceId).orElseThrow());
                case "spa" -> slot.setSpa(spaRepo.findById(serviceId).orElseThrow());
                case "physio" -> slot.setPhysio(physioRepo.findById(serviceId).orElseThrow());
                case "translator" -> slot.setTranslator(translatorsRepo.findById(serviceId).orElseThrow());
                case "labtest" -> slot.setLabtest(labtestsRepo.findById(serviceId).orElseThrow());
                default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
            }

            serviceSlotRepo.save(slot); // ✅ Save each slot one-by-one
        }
    }
    
    public List<ServiceSlot> getSlotsByService(String serviceType, Long serviceId) {
        return switch (serviceType.toLowerCase()) {
            case "chef" -> serviceSlotRepo.findByChef_ChefID(serviceId);
            case "doctor" -> serviceSlotRepo.findByDoctor_id(serviceId);
            case "spa" -> serviceSlotRepo.findBySpa_ServiceId(serviceId);
            case "physio" -> serviceSlotRepo.findByPhysio_PhysioId(serviceId);
            case "translator" -> serviceSlotRepo.findByTranslator_TranslatorID(serviceId);
            case "labtest" -> serviceSlotRepo.findByLabtest_id(serviceId);
            default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
        };
    }

}

