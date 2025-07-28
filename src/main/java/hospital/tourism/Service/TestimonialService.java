package hospital.tourism.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.TestimonialRequest;
import hospital.tourism.Entity.Testimonial;
import hospital.tourism.repo.TestMonialsRepo;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Service
public class TestimonialService {

	@Autowired
	private TestMonialsRepo testimonialRepo;
	  @Value("${supabase.url}")
	    private String supabaseUrl;

	    @Value("${supabase.bucket}")
	    private String bucket;

	    @Value("${supabase.api.key}")
	    private String apiKey;

	    // Main logic to save testimonial with file uploads
	    public void saveTestimonialWithFiles(TestimonialRequest request) throws Exception {
	        Testimonial testimonial = new Testimonial();
	        testimonial.setTitle(request.getTitle());
	        testimonial.setDescription(request.getDescription());
	        testimonial.setHasVideo(request.isHasVideo());
	        testimonial.setHasNewspaperClip(request.isHasNewspaperClip());
	        testimonial.setUploadedAt(LocalDate.now());

	        if (request.isHasVideo() && request.getVideoFile() != null && !request.getVideoFile().isEmpty()) {
	            String videoUrl = uploadFileToSupabase(request.getVideoFile());
	            testimonial.setVideoFileName(videoUrl);
	        }

	        if (request.isHasNewspaperClip() && request.getNewspaperFile() != null && !request.getNewspaperFile().isEmpty()) {
	            String imageUrl = uploadFileToSupabase(request.getNewspaperFile());
	            testimonial.setNewspaperFileName(imageUrl);
	        }

	        testimonialRepo.save(testimonial);
	    }

	    // Upload file to Supabase and return public URL
	    public String uploadFileToSupabase(MultipartFile file) throws IOException {
	        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + uniqueFileName;

	        OkHttpClient client = new OkHttpClient();

	        RequestBody requestBody = RequestBody.create(file.getBytes(), MediaType.parse(file.getContentType()));

	        Request request = new Request.Builder()
	                .url(uploadUrl)
	                .header("Authorization", "Bearer " + apiKey)
	                .header("x-upsert", "true")
	                .post(requestBody)
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Upload failed: " + response.code() + " - " + response.message());
	            }
	        }

	        // ✅ Return public URL
	        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + uniqueFileName;
	    }
    
    //get all testimonials
	public List<Testimonial> getAllTestimonials() {
		return testimonialRepo.findAll();
	}
	
	public Testimonial getTestimonialById(Long id) {
		return testimonialRepo.findById(id).orElse(null);
	}
	
	// Update testimonial
	public Testimonial updateTestimonial(Long id, TestimonialRequest request) throws Exception {
		Testimonial testimonial = testimonialRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Testimonial not found"));

		testimonial.setTitle(request.getTitle());
		testimonial.setDescription(request.getDescription());
		testimonial.setHasVideo(request.isHasVideo());
		testimonial.setHasNewspaperClip(request.isHasNewspaperClip());

		if (request.isHasVideo() && request.getVideoFile() != null && !request.getVideoFile().isEmpty()) {
			String videoUrl = uploadFileToSupabase(request.getVideoFile());
			testimonial.setVideoFileName(videoUrl);
		}

		if (request.isHasNewspaperClip() && request.getNewspaperFile() != null
				&& !request.getNewspaperFile().isEmpty()) {
			String imageUrl = uploadFileToSupabase(request.getNewspaperFile());
			testimonial.setNewspaperFileName(imageUrl);
		}

		return testimonialRepo.save(testimonial);
	}
	// Delete testimonial
	public void deleteTestimonial(Long id) {
		Testimonial testimonial = testimonialRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Testimonial not found"));
		testimonialRepo.delete(testimonial);
	}
	
}
