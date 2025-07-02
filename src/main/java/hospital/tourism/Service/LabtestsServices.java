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

import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.repo.DiagnosticsRepo;


@Service
public class LabtestsServices {

    @Autowired
    private hospital.tourism.repo.labtestsRepo labtestsRepo;

    @Autowired
    private DiagnosticsRepo diagnosticsRepo;
    
    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    public Labtests saveLabtest(Labtests labtests, MultipartFile imageFile, Integer diognosticsId) {
        Diognstics diognstics = diagnosticsRepo.findById(diognosticsId)
                .orElseThrow(() -> new RuntimeException("Diagnostics center not found with ID: " + diognosticsId));
        labtests.setDiognostics(diognstics);
        labtests.setStatus("Active");

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = uploadToSupabase(imageFile);
                labtests.setTestImage(imageUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload labtest image", e);
        }

        return labtestsRepo.save(labtests);
    }

    private String uploadToSupabase(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String cleanFileName = originalName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        String fileName = UUID.randomUUID() + "_" + cleanFileName;

        String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + fileName;

        HttpPost post = new HttpPost(uploadUrl);
        post.setHeader("apikey", supabaseApiKey);
        post.setHeader("Authorization", "Bearer " + supabaseApiKey);
        post.setHeader("Content-Type", file.getContentType());
        post.setHeader("x-upsert", "true");

        try (InputStream inputStream = file.getInputStream();
             CloseableHttpClient client = HttpClients.createDefault()) {

            post.setEntity(new InputStreamEntity(inputStream));
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200 && statusCode != 201) {
                throw new RuntimeException("Image upload failed: HTTP " + statusCode);
            }
        }

        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + fileName;
    }


    // Get all lab tests
    public List<Labtests> getAllLabtests() {
        return labtestsRepo.findAll();
    }

    // Get lab test by ID
    public Labtests getLabById(Long id) {
        return labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));
    }
    
    public void softDeleteLabtest(Long id) {
        Labtests labtest = labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));

        labtest.setStatus("Inactive");
        labtestsRepo.save(labtest);
    }

    // Activate lab test if inactive
    public void activateLabtestIfInactive(Long id) {
        Labtests labtest = labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));

        if ("Inactive".equalsIgnoreCase(labtest.getStatus())) {
            labtest.setStatus("Active");
            labtestsRepo.save(labtest);
        }
    }
    
    public Labtests updateLabtest(Long id, Labtests updatedLabtest, MultipartFile imageFile) {
        System.out.println("=== SERVICE UPDATE LABTEST ===");
        System.out.println("Service received ID: " + id);
        System.out.println("Service received labtest: " + updatedLabtest);
        System.out.println("==============================");

        Labtests existingLabtest = labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));

        existingLabtest.setTestTitle(updatedLabtest.getTestTitle());
        existingLabtest.setTestDescription(updatedLabtest.getTestDescription());
        existingLabtest.setTestPrice(updatedLabtest.getTestPrice());
        existingLabtest.setTestDepartment(updatedLabtest.getTestDepartment());

        // Update status if provided
        if (updatedLabtest.getStatus() != null) {
            existingLabtest.setStatus(updatedLabtest.getStatus());
        }

        // ✅ Replace image if a new file is uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = uploadToSupabase(imageFile);
                existingLabtest.setTestImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Image update failed", e);
            }
        }

        Labtests saved = labtestsRepo.save(existingLabtest);
        System.out.println("Successfully updated labtest: " + saved.getId());
        return saved;
    }

}
