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

import hospital.tourism.Dto.DiagnosticsDTO;
import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.repo.DiagnosticsRepo;


@Service
public class DiagonosticsServices {

    @Autowired
    private DiagnosticsRepo diagnosticsRepo;

    @Autowired
    private hospital.tourism.repo.labtestsRepo labtestsRepo;
    
    
    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

   
    
    public Diognstics saveDiagnostics(Diognstics diognstics, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = uploadToSupabase(imageFile);
                diognstics.setDiognosticsImage(imageUrl);
            }

            diognstics.setStatus("Active");
            return diagnosticsRepo.save(diognstics);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save diagnostics with image", e);
        }
    }

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
                throw new RuntimeException("Image upload failed with HTTP status: " + statusCode);
            }
        }

        // Return public URL of uploaded image
        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + fileName;
    }

//    // Save a diagnostics center
//    public Diognstics saveDiagnostics(Diognstics diognstics) {
//        return diagnosticsRepo.save(diognstics);
//    }

    // Get all diagnostics centers
    public List<Diognstics> getAllDiagnostics() {
        return diagnosticsRepo.findAll();
    }

    // Get lab tests by diagnostics ID
    public List<Labtests> getLabtestsByDiagnosticsId(Integer diognosticsId) {
        return labtestsRepo.findByDiognostics_DiognosticsId(diognosticsId);
    }
    
    //get diagnostics by ID
    public DiagnosticsDTO getdiagnosticbyId(Integer diagId) {
		Diognstics diognstics = diagnosticsRepo.findById(diagId)
				.orElseThrow(() -> new RuntimeException("Diagnostics not found"));

		DiagnosticsDTO dto = new DiagnosticsDTO();
		dto.setDiognosticsId(diognstics.getDiognosticsId());
		dto.setDiognosticsName(diognstics.getDiognosticsName());
		dto.setDiognosticsDescription(diognstics.getDiognosticsDescription());
		dto.setDiognosticsImage(diognstics.getDiognosticsImage());
		dto.setDiognosticsrating(diognstics.getDiognosticsrating());
		dto.setDiognosticsaddress(diognstics.getDiognosticsaddress());

		return dto;
    }
    
    
    // Soft delete
    public void softDeleteDiognstics(Integer id) {
        Diognstics entity = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diognstics not found with ID: " + id));
        entity.setStatus("Inactive");
        diagnosticsRepo.save(entity);
    }

    // Reactivate
    public void activateDiognstics(Integer id) {
        Diognstics entity = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diognstics not found with ID: " + id));
        if ("Inactive".equalsIgnoreCase(entity.getStatus())) {
            entity.setStatus("Active");
            diagnosticsRepo.save(entity);
        }
    }
    // Update diagnostics
    
    public DiagnosticsDTO updateDiagnostics(Integer id, DiagnosticsDTO dto, MultipartFile imageFile) {
        // Step 1: Fetch existing diagnostics
        Diognstics existing = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnostics not found with ID: " + id));

        // Step 2: Update basic fields
        existing.setDiognosticsName(dto.getDiognosticsName());
        existing.setDiognosticsDescription(dto.getDiognosticsDescription());
        existing.setDiognosticsrating(dto.getDiognosticsrating());
        existing.setDiognosticsaddress(dto.getDiognosticsaddress());

        // Step 3: Upload and update image if new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                existing.setDiognosticsImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to Supabase", e);
            }
        }

        // Step 4: Save updated entity
        Diognstics updated = diagnosticsRepo.save(existing);

        // Step 5: Convert back to DTO
        DiagnosticsDTO updatedDto = new DiagnosticsDTO();
        updatedDto.setDiognosticsId(updated.getDiognosticsId());
        updatedDto.setDiognosticsName(updated.getDiognosticsName());
        updatedDto.setDiognosticsDescription(updated.getDiognosticsDescription());
        updatedDto.setDiognosticsImage(updated.getDiognosticsImage());
        updatedDto.setDiognosticsrating(updated.getDiognosticsrating());
        updatedDto.setDiognosticsaddress(updated.getDiognosticsaddress());

        return updatedDto;
    }

}

