package hospital.tourism.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import jakarta.persistence.EntityNotFoundException;

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
    
   
    public List<SlotRequestDTO> updateSlots(String serviceType, Long serviceId, List<SlotRequestDTO> updateRequests) {
        List<SlotRequestDTO> responseList = new ArrayList<>();

        for (SlotRequestDTO update : updateRequests) {
            if (update.getSlotId() == null) {
                throw new IllegalArgumentException("Slot ID is required for update.");
            }

            ServiceSlot slot = serviceSlotRepo.findById(update.getSlotId())
                    .orElseThrow(() -> new EntityNotFoundException("Slot not found with ID: " + update.getSlotId()));

            // Validate the slot belongs to the correct service
            switch (serviceType.toLowerCase()) {
                case "chef" -> {
                    if (slot.getChef() == null || !slot.getChef().getChefID().equals(serviceId)) {
                        throw new IllegalArgumentException("Chef ID mismatch for slot: " + update.getSlotId());
                    }
                }
                case "doctor" -> {
                    if (slot.getDoctor() == null || !slot.getDoctor().getId().equals(serviceId)) {
                        throw new IllegalArgumentException("Doctor ID mismatch for slot: " + update.getSlotId());
                    }
                }
                case "spa" -> {
                    if (slot.getSpa() == null || !slot.getSpa().getServiceId().equals(serviceId)) {
                        throw new IllegalArgumentException("Spa ID mismatch for slot: " + update.getSlotId());
                    }
                }
                case "physio" -> {
                    if (slot.getPhysio() == null || !slot.getPhysio().getPhysioId().equals(serviceId)) {
                        throw new IllegalArgumentException("Physio ID mismatch for slot: " + update.getSlotId());
                    }
                }
                case "translator" -> {
                    if (slot.getTranslator() == null || !slot.getTranslator().getTranslatorID().equals(serviceId)) {
                        throw new IllegalArgumentException("Translator ID mismatch for slot: " + update.getSlotId());
                    }
                }
                case "labtest" -> {
                    if (slot.getLabtest() == null || !slot.getLabtest().getId().equals(serviceId)) {
                        throw new IllegalArgumentException("Labtest ID mismatch for slot: " + update.getSlotId());
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
            }

            // Update fields
            if (update.getSlotTime() != null) {
                slot.setSlotTime(update.getSlotTime());
            }

            if (update.getBookingStatus() != null) {
                slot.setBookingStatus(update.getBookingStatus());
            }

            if (update.getBookedByUserId() != null) {
                slot.setBookedByUserId(update.getBookedByUserId());
            }

            ServiceSlot updatedSlot = serviceSlotRepo.save(slot);

            SlotRequestDTO dto = new SlotRequestDTO();
            dto.setId(updatedSlot.getId());
            dto.setSlotId(updatedSlot.getSlotId());
            dto.setSlotTime(updatedSlot.getSlotTime());
            dto.setBookingStatus(updatedSlot.getBookingStatus());
            dto.setBookedByUserId(updatedSlot.getBookedByUserId());
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
    public List<SlotRequestDTO> getSlotsByServices(String serviceType) {
        List<ServiceSlot> slots = switch (serviceType.toLowerCase()) {
            case "chef" -> serviceSlotRepo.findByChefIsNotNull();
            case "doctor" -> serviceSlotRepo.findByDoctorIsNotNull();
            case "spa" -> serviceSlotRepo.findBySpaIsNotNull();
            case "physio" -> serviceSlotRepo.findByPhysioIsNotNull();
            case "translator" -> serviceSlotRepo.findByTranslatorIsNotNull();
            case "labtest" -> serviceSlotRepo.findByLabtestIsNotNull();
            default -> throw new IllegalArgumentException("Unsupported service type: " + serviceType);
        };

        return slots.stream().map(slot -> {
            SlotRequestDTO dto = new SlotRequestDTO();
            dto.setId(slot.getId());
            dto.setSlotId(slot.getId());
            dto.setSlotTime(slot.getSlotTime());
            dto.setBookingStatus(slot.getBookingStatus());

            // Set BookedByUserId if present
            dto.setBookedByUserId(
                slot.getBookedByUserId() != null ? slot.getBookedByUserId() : null
            );

            // Set slots list if present
            dto.setSlots(slot.getListSlotS());

            dto.setServiceType(serviceType.toLowerCase());

            // Set correct serviceId based on type
            switch (serviceType.toLowerCase()) {
                case "chef" -> dto.setServiceId(slot.getChef() != null ? slot.getChef().getChefID() : null);
                case "doctor" -> dto.setServiceId(slot.getDoctor() != null ? slot.getDoctor().getId() : null);
                case "spa" -> dto.setServiceId(slot.getSpa() != null ? slot.getSpa().getServiceId() : null);
                case "physio" -> dto.setServiceId(slot.getPhysio() != null ? slot.getPhysio().getPhysioId() : null);
                case "translator" -> dto.setServiceId(slot.getTranslator() != null ? slot.getTranslator().getTranslatorID() : null);
                case "labtest" -> dto.setServiceId(slot.getLabtest() != null ? slot.getLabtest().getId() : null);
            }

            return dto;
        }).collect(Collectors.toList());
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

