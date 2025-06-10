package hospital.tourism.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Dto.SlotRequestDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.ServiceSlot;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.ServicesSlotRepo;
import hospital.tourism.repo.chefsRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class chefService {

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private chefsRepo chefsRepo;
    @Autowired
    private ServicesSlotRepo serviceSlotRepo;

    // Save a chef with location mapping
    public ChefDTO saveChef(ChefDTO chefDto, Integer locationId) {
        LocationEntity location = locationRepo.findById(locationId)
            .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));

        Chefs chefs = new Chefs();
        chefs.setChefName(chefDto.getChefName());
        chefs.setChefDescription(chefDto.getChefDescription());
        chefs.setChefImage(chefDto.getChefImage());
        chefs.setChefRating(chefDto.getChefRating());
        chefs.setExperience(chefDto.getExperience());
        chefs.setStyles(chefDto.getStyles());
        chefs.setStatus(chefDto.getStatus());
        chefs.setPrice(chefDto.getPrice());
        chefs.setLocation(location);

        if (chefDto.getSlots() != null && !chefDto.getSlots().isEmpty()) {
            List<ServiceSlot> slotEntities = new ArrayList<>();
            for (SlotRequestDTO slotDto : chefDto.getSlots()) {
                ServiceSlot slot = new ServiceSlot();
                slot.setSlotTime(slotDto.getSlotTime());
                slot.setBookingStatus(slotDto.getBookingStatus());
                slot.setChef(chefs);
                slotEntities.add(slot);
            }
            chefs.setSlots(slotEntities);
        }

        Chefs savedChef = chefsRepo.save(chefs);

        ChefDTO savedChefDto = new ChefDTO();
        savedChefDto.setChefID(savedChef.getChefID());
        savedChefDto.setChefName(savedChef.getChefName());
        savedChefDto.setChefDescription(savedChef.getChefDescription());
        savedChefDto.setChefImage(savedChef.getChefImage());
        savedChefDto.setChefRating(savedChef.getChefRating());
        savedChefDto.setExperience(savedChef.getExperience());
        savedChefDto.setStyles(savedChef.getStyles());
        savedChefDto.setStatus(savedChef.getStatus());
        savedChefDto.setPrice(savedChef.getPrice());
        savedChef.setStatus(savedChef.getStatus());
      

        List<SlotRequestDTO> slotDtos = new ArrayList<>();
        if (savedChef.getSlots() != null) {
            for (ServiceSlot slot : savedChef.getSlots()) {
            	SlotRequestDTO slotDto = new SlotRequestDTO();
                slotDto.setId(slot.getId());
                slotDto.setSlotTime(slot.getSlotTime());
                slotDto.setBookingStatus(slot.getBookingStatus());
                slotDtos.add(slotDto);
            }
        }
        savedChefDto.setSlots(slotDtos);

        return savedChefDto;
    }
    

    // Get all chefs by location ID
    public List<Chefs> getChefsByLocationId(Integer locationId) {
        return chefsRepo.findByLocation_LocationId(locationId);
    }

    public List<ChefDTO> getAllChefs() {
        List<Chefs> chefs = chefsRepo.findAll();

        return chefs.stream().map(chef -> {
            ChefDTO dto = new ChefDTO();
            dto.setChefID(chef.getChefID());
            dto.setChefName(chef.getChefName());
            dto.setChefDescription(chef.getChefDescription());
            dto.setChefImage(chef.getChefImage());
            dto.setChefRating(chef.getChefRating());
            dto.setExperience(chef.getExperience());
            dto.setStyles(chef.getStyles());
            dto.setStatus(chef.getStatus());
            dto.setPrice(chef.getPrice());

         
            if (chef.getSlots() != null) {
                List<SlotRequestDTO> slotDTOs = chef.getSlots().stream().map(slot -> {
                    SlotRequestDTO slotDTO = new SlotRequestDTO();
                    slotDTO.setId(slot.getId());
                    slotDTO.setSlotId(slot.getSlotId());
                    slotDTO.setSlotTime(slot.getSlotTime());
                    slotDTO.setBookingStatus(slot.getBookingStatus());
                    slotDTO.setBookedByUserId(slot.getBookedByUserId());
                    return slotDTO;
                }).toList();
                dto.setSlots(slotDTOs);
            }

            return dto;
        }).toList();
    }
    
	public ChefDTO getChefById(Long chefId) {
		Chefs chef = chefsRepo.findById(chefId)
				.orElseThrow(() -> new RuntimeException("Chef not found with ID: " + chefId));

		ChefDTO chefDto = new ChefDTO();
		chefDto.setChefID(chef.getChefID());
		chefDto.setChefName(chef.getChefName());
		chefDto.setChefDescription(chef.getChefDescription());
		chefDto.setChefImage(chef.getChefImage());
		chefDto.setChefRating(chef.getChefRating());
		chefDto.setExperience(chef.getExperience());
		chefDto.setStyles(chef.getStyles());
		chefDto.setStatus(chef.getStatus());
		chefDto.setPrice(chef.getPrice());

		if (chef.getSlots() != null) {
			List<SlotRequestDTO> slotDtos = chef.getSlots().stream().map(slot -> {
				SlotRequestDTO slotDto = new SlotRequestDTO();
				slotDto.setId(slot.getId());
				slotDto.setSlotId(slot.getSlotId());
				slotDto.setSlotTime(slot.getSlotTime());
				slotDto.setBookingStatus(slot.getBookingStatus());
				slotDto.setBookedByUserId(slot.getBookedByUserId());
				return slotDto;
			}).toList();
			chefDto.setSlots(slotDtos);
		}

		return chefDto;
	}
	
	@Transactional
	public List<SlotRequestDTO> updateChefSlots(Long chefId, List<SlotRequestDTO> slotRequests) {
	    Chefs chef = chefsRepo.findById(chefId)
	            .orElseThrow(() -> new EntityNotFoundException("Chef not found with ID: " + chefId));

	    List<SlotRequestDTO> updatedSlotDTOs = new ArrayList<>();

	    for (SlotRequestDTO slotDto : slotRequests) {
	        ServiceSlot slot = serviceSlotRepo.findById(slotDto.getId())
	                .orElseThrow(() -> new EntityNotFoundException("Slot not found with ID: " + slotDto.getId()));

	        if (slotDto.getSlotTime() != null) {
	            slot.setSlotTime(slotDto.getSlotTime());
	        }

	        if (slotDto.getBookingStatus() != null) {
	            slot.setBookingStatus(slotDto.getBookingStatus());
	        }

	        if (slotDto.getBookedByUserId() != null) {
	            slot.setBookedByUserId(slotDto.getBookedByUserId());
	        }

	        slot.setChef(chef); // Reassign to ensure linkage

	        ServiceSlot updatedSlot = serviceSlotRepo.save(slot);

	        // Convert back to DTO
	        SlotRequestDTO updatedDto = new SlotRequestDTO();
	        updatedDto.setId(updatedSlot.getId());
	        updatedDto.setSlotTime(updatedSlot.getSlotTime());
	        updatedDto.setBookingStatus(updatedSlot.getBookingStatus());
	        updatedDto.setBookedByUserId(updatedSlot.getBookedByUserId());
	        updatedDto.setSlotId(updatedSlot.getSlotId());
	        updatedDto.setServiceType("chef");
	        updatedDto.setServiceId(chefId);

	        updatedSlotDTOs.add(updatedDto);
	    }

	    return updatedSlotDTOs;
	}

}
