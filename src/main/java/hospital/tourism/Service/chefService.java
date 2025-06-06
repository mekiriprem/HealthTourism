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
import hospital.tourism.repo.chefsRepo;

@Service
public class chefService {

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private chefsRepo chefsRepo;

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

    // Get all chefs
    public List<Chefs> getAllChefs() {
        return chefsRepo.findAll();
    }
}
