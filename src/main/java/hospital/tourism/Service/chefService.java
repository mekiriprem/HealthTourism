package hospital.tourism.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

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
    
    
    
    // ✅ Save chef with image upload
    public ChefDTO saveChef(ChefDTO chefDto, Integer locationId, MultipartFile imageFile) {
        // Step 1: Get Location
        LocationEntity location = locationRepo.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));

        // Step 2: Map DTO to Entity
        Chefs chefs = new Chefs();
        chefs.setChefName(chefDto.getChefName());
        chefs.setChefDescription(chefDto.getChefDescription());
        chefs.setChefRating(chefDto.getChefRating());
        chefs.setExperience(chefDto.getExperience());
        chefs.setStyles(chefDto.getStyles());
        chefs.setStatus(chefDto.getStatus());
        chefs.setPrice(chefDto.getPrice());
        chefs.setLocation(location);

        // Step 3: Upload Image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                chefs.setChefImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload chef image", e);
            }
        } else {
            chefs.setChefImage(chefDto.getChefImage()); // fallback if DTO already has a URL
        }

        // Step 4: Save Entity
        Chefs savedChef = chefsRepo.save(chefs);

        // Step 5: Map back to DTO
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
        savedChefDto.setLocation(location);

        return savedChefDto;
    }

    // ✅ Supabase Image Upload Logic
    private String uploadToSupabase(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + fileName;

        HttpPost post = new HttpPost(uploadUrl);
        post.setHeader("apikey", supabaseApiKey);
        post.setHeader("Authorization", "Bearer " + supabaseApiKey);
        post.setHeader("Content-Type", file.getContentType());

        try (InputStream inputStream = file.getInputStream();
             CloseableHttpClient client = HttpClients.createDefault()) {

            post.setEntity(new InputStreamEntity(inputStream));
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200 && statusCode != 201) {
                throw new RuntimeException("Supabase image upload failed with status: " + statusCode);
            }
        }

        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + fileName;
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

            if (chef.getLocation() != null) {
                dto.setLocationId(chef.getLocation().getLocationId());
                dto.setCity(chef.getLocation().getCity());
                dto.setState(chef.getLocation().getState());
                dto.setCountry(chef.getLocation().getCountry());
            }

            return dto;
        }).toList();
    }

    public ChefDTO getChefById(Long chefId) {
        Chefs chef = chefsRepo.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef not found with ID: " + chefId));

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

        if (chef.getLocation() != null) {
            dto.setLocationId(chef.getLocation().getLocationId());
            dto.setCity(chef.getLocation().getCity());
            dto.setState(chef.getLocation().getState());
            dto.setCountry(chef.getLocation().getCountry());
        }

        
        return dto;
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
    
    public ChefDTO updateChef(Long chefId, ChefDTO chefDto, MultipartFile imageFile) {
        // Step 1: Fetch existing chef
        Chefs chef = chefsRepo.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef not found with ID: " + chefId));

        // Step 2: Update fields
        chef.setChefName(chefDto.getChefName());
        chef.setChefDescription(chefDto.getChefDescription());
        chef.setChefRating(chefDto.getChefRating());
        chef.setExperience(chefDto.getExperience());
        chef.setStyles(chefDto.getStyles());
        chef.setStatus(chefDto.getStatus());
        chef.setPrice(chefDto.getPrice());

        // Step 3: Replace image only if new one is uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                chef.setChefImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload new chef image", e);
            }
        }

        // Step 4: Save updated chef
        Chefs updatedChef = chefsRepo.save(chef);

        // Step 5: Convert to DTO
        ChefDTO updatedChefDto = new ChefDTO();
        updatedChefDto.setChefID(updatedChef.getChefID());
        updatedChefDto.setChefName(updatedChef.getChefName());
        updatedChefDto.setChefDescription(updatedChef.getChefDescription());
        updatedChefDto.setChefImage(updatedChef.getChefImage());
        updatedChefDto.setChefRating(updatedChef.getChefRating());
        updatedChefDto.setExperience(updatedChef.getExperience());
        updatedChefDto.setStyles(updatedChef.getStyles());
        updatedChefDto.setStatus(updatedChef.getStatus());
        updatedChefDto.setPrice(updatedChef.getPrice());

        return updatedChefDto;
    }


}
