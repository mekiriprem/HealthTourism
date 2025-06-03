package hospital.tourism.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseUploadService {

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename());
        String storagePath = folder + "/" + fileName;
        String uploadUrl = supabaseProjectUrl + "/storage/v1/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseApiKey);
        headers.set("Content-Type", "application/octet-stream");
        headers.set("x-upsert", "true"); // optional: allows overwriting
        headers.set("Content-Profile", "public");

        uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + storagePath;

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("Failed to upload file: " + response.getStatusCode());
        }

        return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + storagePath;
    }

}
