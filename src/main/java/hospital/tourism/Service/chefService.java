package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.ServicesSlotRepo;
import hospital.tourism.repo.chefsRepo;

@Service
public class chefService {

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private chefsRepo chefsRepo;
    @Autowired
    private ServicesSlotRepo serviceSlotRepo;
    
    @Autowired
    private BookingRepo bookingRepository;

//    // Save a chef with location mapping
//    public ChefDTO saveChef(ChefDTO chefDto, Integer locationId) {
//        LocationEntity location = locationRepo.findById(locationId)
//            .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));
//
//        Chefs chefs = new Chefs();
//        chefs.setChefName(chefDto.getChefName());
//        chefs.setChefDescription(chefDto.getChefDescription());
//        chefs.setChefImage(chefDto.getChefImage());
//        chefs.setChefRating(chefDto.getChefRating());
//        chefs.setExperience(chefDto.getExperience());
//        chefs.setStyles(chefDto.getStyles());
//        chefs.setStatus(chefDto.getStatus());
//        chefs.setPrice(chefDto.getPrice());
//        chefs.setLocation(location);
//
//        if (chefDto.getSlots() != null && !chefDto.getSlots().isEmpty()) {
//            List<ServiceSlot> slotEntities = new ArrayList<>();
//            for (SlotRequestDTO slotDto : chefDto.getSlots()) {
//                ServiceSlot slot = new ServiceSlot();
//                slot.setSlotTime(slotDto.getSlotTime());
//                slot.setBookingStatus(slotDto.getBookingStatus());
//                slot.setChef(chefs);
//                slotEntities.add(slot);
//            }
//            chefs.setSlots(slotEntities);
//        }
//
//        Chefs savedChef = chefsRepo.save(chefs);
//
//        ChefDTO savedChefDto = new ChefDTO();
//        savedChefDto.setChefID(savedChef.getChefID());
//        savedChefDto.setChefName(savedChef.getChefName());
//        savedChefDto.setChefDescription(savedChef.getChefDescription());
//        savedChefDto.setChefImage(savedChef.getChefImage());
//        savedChefDto.setChefRating(savedChef.getChefRating());
//        savedChefDto.setExperience(savedChef.getExperience());
//        savedChefDto.setStyles(savedChef.getStyles());
//        savedChefDto.setStatus(savedChef.getStatus());
//        savedChefDto.setPrice(savedChef.getPrice());
//        savedChef.setStatus(savedChef.getStatus());
//      
//
//        List<SlotRequestDTO> slotDtos = new ArrayList<>();
//        if (savedChef.getSlots() != null) {
//            for (ServiceSlot slot : savedChef.getSlots()) {
//            	SlotRequestDTO slotDto = new SlotRequestDTO();
//                slotDto.setId(slot.getId());
//                slotDto.setSlotTime(slot.getSlotTime());
//                slotDto.setBookingStatus(slot.getBookingStatus());
//                slotDtos.add(slotDto);
//            }
//        }
//        savedChefDto.setSlots(slotDtos);
//
//        return savedChefDto;
//    }
    
    
    
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

        Chefs savedChef = chefsRepo.save(chefs);

        ChefDTO savedChefDto = new ChefDTO();
        savedChefDto.setChefID(savedChef.getChefID());
        savedChefDto.setChefName(savedChef.getChefName());
        savedChefDto.setChefDescription(savedChef.getChefDescription());
        savedChefDto.setChefImage(savedChef.getChefImage());
        savedChefDto.setChefRating(savedChef.getChefRating());
        savedChefDto.setExperience(savedChef.getExperience());
        savedChefDto.setStyles(savedChef.getStyles());

        savedChefDto.setPrice(savedChef.getPrice());
        savedChefDto.setLocation(location);
       

        return savedChefDto;
    }

    

    // Get all chefs by location ID
    public List<Chefs> getChefsByLocationId(Integer locationId) {
        return chefsRepo.findByLocation_LocationId(locationId);
    }

//  
    
    
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

        // Optional: Include location info
     

        return chefDto;
    }

    public void softDeleteChef(Long id) {
        Chefs chef = chefsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef not found with ID: " + id));
        chef.setStatus("Inactive");
        chefsRepo.save(chef);
    }

    public void activateChef(Long id) {
        Chefs chef = chefsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef not found with ID: " + id));
        if ("Inactive".equalsIgnoreCase(chef.getStatus())) {
            chef.setStatus("Active");
            chefsRepo.save(chef);
        }
    }
    
	

}
