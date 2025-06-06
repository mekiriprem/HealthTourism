package hospital.tourism.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.SlotRequestDTO;
import hospital.tourism.Entity.ServiceSlot;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.ServicesSlotRepo;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;

@Service
public class SlotService {

    @Autowired private chefsRepo chefsRepo;
    @Autowired private DoctorsRepo doctorsRepo;
    @Autowired private SpaservicesRepo spaRepo;
    @Autowired private PhysioRepo physioRepo;
    @Autowired private TranslatorsRepo translatorsRepo;
    @Autowired private labtestsRepo labtestsRepo;
    @Autowired private ServicesSlotRepo serviceSlotRepo;

//    public ServiceSlot addSlots(String serviceType, Long serviceId, List<String> slotTimes,Long bookedBy) {
//        for (String time : slotTimes) {
//            ServiceSlot slot = new ServiceSlot();
//            slot.setSlotTime(time);  // ✅ Corrected this line
//            slot.setBookingStatus("AVAILABLE");
//            slot.setBookedBy(bookedBy);
//
//            switch (serviceType.toLowerCase()) {
//                case "chef" -> slot.setChef(chefsRepo.findById(serviceId).orElseThrow());
//                case "doctor" -> slot.setDoctor(doctorsRepo.findById(serviceId).orElseThrow());
//                case "spa" -> slot.setSpa(spaRepo.findById(serviceId).orElseThrow());
//                case "physio" -> slot.setPhysio(physioRepo.findById(serviceId).orElseThrow());
//                case "translator" -> slot.setTranslator(translatorsRepo.findById(serviceId).orElseThrow());
//                case "labtest" -> slot.setLabtest(labtestsRepo.findById(serviceId).orElseThrow());
//                default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
//            }
//
//            serviceSlotRepo.save(slot); // ✅ Save each slot one-by-one
//        }
//		return null;
//    }
//    
    public List<SlotRequestDTO> addSlots(String serviceType, Long serviceId, List<String> slotTimes, Long bookedBy) {
        List<SlotRequestDTO> responseList = new ArrayList<>();

        for (String time : slotTimes) {
            ServiceSlot slot = new ServiceSlot();
            slot.setSlotTime(time);
            slot.setBookingStatus("AVAILABLE");
            slot.setBookedByUserId(bookedBy);

            switch (serviceType.toLowerCase()) {
                case "chef" -> slot.setChef(chefsRepo.findById(serviceId).orElseThrow());
                case "doctor" -> slot.setDoctor(doctorsRepo.findById(serviceId).orElseThrow());
                case "spa" -> slot.setSpa(spaRepo.findById(serviceId).orElseThrow());
                case "physio" -> slot.setPhysio(physioRepo.findById(serviceId).orElseThrow());
                case "translator" -> slot.setTranslator(translatorsRepo.findById(serviceId).orElseThrow());
                case "labtest" -> slot.setLabtest(labtestsRepo.findById(serviceId).orElseThrow());
                default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
            }

            ServiceSlot savedSlot = serviceSlotRepo.save(slot);

            // ✅ Populate DTO manually
            SlotRequestDTO dto = new SlotRequestDTO();
            dto.setId(savedSlot.getId());
            dto.setSlotId(savedSlot.getSlotId());
            dto.setSlotTime(savedSlot.getSlotTime());
            dto.setBookingStatus(savedSlot.getBookingStatus());
            dto.setBookedByUserId(savedSlot.getBookedByUserId());
            dto.setServiceType(serviceType.toLowerCase());
            dto.setServiceId(serviceId);

            responseList.add(dto);
        }

        return responseList;
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

    //public view All Slots
    public List<SlotRequestDTO> getAllSlotses() {
        List<ServiceSlot> slots = serviceSlotRepo.findAll();
        List<SlotRequestDTO> dtos = new ArrayList<>();

        for (ServiceSlot slot : slots) {
            String serviceType = null;
            Long serviceId = null;

            if (slot.getChef() != null) {
                serviceType = "chef";
                serviceId = slot.getChef().getChefID();
            } else if (slot.getDoctor() != null) {
                serviceType = "doctor";
                serviceId = slot.getDoctor().getId();
            } else if (slot.getSpa() != null) {
                serviceType = "spa";
                serviceId = slot.getSpa().getServiceId();
            } else if (slot.getPhysio() != null) {
                serviceType = "physio";
                serviceId = slot.getPhysio().getPhysioId();
            } else if (slot.getTranslator() != null) {
                serviceType = "translator";
                serviceId = slot.getTranslator().getTranslatorID();
            } else if (slot.getLabtest() != null) {
                serviceType = "labtest";
                serviceId = slot.getLabtest().getId();
            }

            SlotRequestDTO dto = new SlotRequestDTO();
            dto.setId(slot.getId());
            dto.setSlotId(slot.getId());
            dto.setSlotTime(slot.getSlotTime());
            dto.setBookingStatus(slot.getBookingStatus());
            dto.setBookedByUserId(slot.getBookedByUserId());
            dto.setServiceType(serviceType);
            dto.setServiceId(serviceId);

            dtos.add(dto);
        }

        return dtos;
    }

}

