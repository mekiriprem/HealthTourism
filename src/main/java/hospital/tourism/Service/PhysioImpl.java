package hospital.tourism.Service;

import java.io.InputStream;
import java.util.ArrayList;
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

import hospital.tourism.Dto.PhysioDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.Physio;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.PhysioRepo;

@Service
public class PhysioImpl {

	@Autowired
	private PhysioRepo physioRepo;
	@Autowired
	private LocationRepo locationRepo;
	 @Value("${supabase.url}")
	    private String supabaseProjectUrl;

	    @Value("${supabase.bucket}")
	    private String supabaseBucketName;

	    @Value("${supabase.api.key}")
	    private String supabaseApiKey;
	
	 // ✅ Save Physio with Image Upload
    public Physio savePhysio(PhysioDTO dto, MultipartFile imageFile) {
        LocationEntity location = locationRepo.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Physio physio = new Physio();
        physio.setPhysioName(dto.getPhysioName());
        physio.setPhysioDescription(dto.getPhysioDescription());
        physio.setRating(dto.getRating());
        physio.setAddress(dto.getAddress());
        physio.setPrice(dto.getPrice());
        physio.setLocation(location);
        physio.setStatus("Active");

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                physio.setPhysioImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to Supabase", e);
            }
        } else {
            physio.setPhysioImage(dto.getPhysioImage()); // fallback to existing
        }

        return physioRepo.save(physio);
    }

    // ✅ Upload to Supabase
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
                throw new RuntimeException("Supabase upload failed with status code: " + statusCode);
            }
        }

        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + fileName;
    }

    // Get all chefs 
    public List<PhysioDTO> getAllPhysios() {
        List<Physio> physios = physioRepo.findAll();
        List<PhysioDTO> dtoList = new ArrayList<>();

        for (Physio physio : physios) {
            PhysioDTO dto = new PhysioDTO();
            dto.setPhysioId(physio.getPhysioId());
            dto.setPhysioName(physio.getPhysioName());
            dto.setPhysioDescription(physio.getPhysioDescription());
            dto.setPhysioImage(physio.getPhysioImage());
            dto.setRating(physio.getRating());
            dto.setAddress(physio.getAddress());
            dto.setPrice(physio.getPrice());
            dto.setStatus(physio.getStatus());

            if (physio.getLocation() != null) {
                dto.setLocationId(physio.getLocation().getLocationId());
                dto.setCity(physio.getLocation().getCity());
                dto.setState(physio.getLocation().getState());
                dto.setCountry(physio.getLocation().getCountry());
            }

            dtoList.add(dto);
        }

        return dtoList;
    }




    public PhysioDTO getPhysioById(Long physioId) {
        if (physioId == null) {
            throw new IllegalArgumentException("Physio ID must not be null");
        }

        Physio physio = physioRepo.findById(physioId)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + physioId));

        PhysioDTO dto = new PhysioDTO();
        dto.setPhysioId(physio.getPhysioId());
        dto.setPhysioName(physio.getPhysioName());
        dto.setPhysioDescription(physio.getPhysioDescription());
        dto.setPhysioImage(physio.getPhysioImage());
        dto.setRating(physio.getRating());
        dto.setAddress(physio.getAddress());
        dto.setPrice(physio.getPrice());
        dto.setStatus(physio.getStatus());

        if (physio.getLocation() != null) {
            dto.setLocationId(physio.getLocation().getLocationId());
            dto.setCity(physio.getLocation().getCity());
            dto.setState(physio.getLocation().getState());
            dto.setCountry(physio.getLocation().getCountry());
        }

        return dto;
    }

    // Soft delete by setting status to "Inactive"
    public void softDeletePhysio(Long id) {
        Physio physio = physioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + id));

        physio.setStatus("Inactive");
        physioRepo.save(physio);
    }

    // Activate if currently inactive
    public void activatePhysioIfInactive(Long id) {
        Physio physio = physioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + id));

        if ("Inactive".equalsIgnoreCase(physio.getStatus())) {
            physio.setStatus("Active");
            physioRepo.save(physio);
        }
    }
    
    public PhysioDTO updatePhysio(Long physioId, PhysioDTO dto, MultipartFile imageFile) {
        // Step 1: Fetch existing physio
        Physio physio = physioRepo.findById(physioId)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + physioId));

        // Step 2: Update fields from DTO
        physio.setPhysioName(dto.getPhysioName());
        physio.setPhysioDescription(dto.getPhysioDescription());
        physio.setRating(dto.getRating());
        physio.setAddress(dto.getAddress());
        physio.setPrice(dto.getPrice());

        // Step 3: Set new location if provided
        if (dto.getLocationId() != null) {
            LocationEntity location = locationRepo.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found with ID: " + dto.getLocationId()));
            physio.setLocation(location);
        }

        // Step 4: Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                physio.setPhysioImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload physio image", e);
            }
        }

        // Step 5: Save updated physio
        Physio updatedPhysio = physioRepo.save(physio);

        // Step 6: Convert to DTO
        PhysioDTO updatedDto = new PhysioDTO();
        updatedDto.setPhysioId(updatedPhysio.getPhysioId());
        updatedDto.setPhysioName(updatedPhysio.getPhysioName());
        updatedDto.setPhysioDescription(updatedPhysio.getPhysioDescription());
        updatedDto.setPhysioImage(updatedPhysio.getPhysioImage());
        updatedDto.setRating(updatedPhysio.getRating());
        updatedDto.setAddress(updatedPhysio.getAddress());
        updatedDto.setPrice(updatedPhysio.getPrice());
        updatedDto.setStatus(updatedPhysio.getStatus());
        updatedDto.setLocationId(updatedPhysio.getLocation().getLocationId());

        return updatedDto;
    }

	
}
