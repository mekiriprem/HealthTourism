package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.TestimonialRequest;
import hospital.tourism.Entity.Testimonial;
import hospital.tourism.Service.TestimonialService;

@RestController
@RequestMapping("/testimonials")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"},allowCredentials = "true")
public class TestMonialsController {

	@Autowired
	private TestimonialService testimonialService;
	
	
	 @PostMapping("/upload")
	    public ResponseEntity<String> uploadTestimonial(
	            @RequestParam String title,
	            @RequestParam String description,
	            @RequestParam boolean hasVideo,
	            @RequestParam boolean hasNewspaperClip,
	            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
	            @RequestParam(value = "newspaperFile", required = false) MultipartFile newspaperFile
	    ) {
	        try {
	            TestimonialRequest request = new TestimonialRequest();
	            request.setTitle(title);
	            request.setDescription(description);
	            request.setHasVideo(hasVideo);
	            request.setHasNewspaperClip(hasNewspaperClip);
	            request.setVideoFile(videoFile);
	            request.setNewspaperFile(newspaperFile);

	            testimonialService.saveTestimonialWithFiles(request);
	            return ResponseEntity.ok("Testimonial uploaded successfully.");

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.internalServerError()
	                    .body("Upload failed: " + e.getMessage());
	        }
	    }
	 //get all testimonials
	
		@GetMapping("/all")
		public ResponseEntity<?> getAllTestimonials() {
			try {
				List<Testimonial> testimonials = testimonialService.getAllTestimonials();
				return ResponseEntity.ok(testimonials);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body("Failed to retrieve testimonials: " + e.getMessage());
			}
		}
		@GetMapping("/get/{id}")
		public ResponseEntity<?> getTestimonialById(@PathVariable Long id) {
			try {
				Testimonial testimonial = testimonialService.getTestimonialById(id);
				if (testimonial != null) {
					return ResponseEntity.ok(testimonial);
				} else {
					return ResponseEntity.notFound().build();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body("Failed to retrieve testimonial: " + e.getMessage());
			}
		}
		@PostMapping("/update/{id}")
		public ResponseEntity<String> updateTestimonial(@PathVariable Long id, @RequestParam String title,
				@RequestParam String description, @RequestParam boolean hasVideo,
				@RequestParam boolean hasNewspaperClip,
				@RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
				@RequestParam(value = "newspaperFile", required = false) MultipartFile newspaperFile) {
			try {
				TestimonialRequest request = new TestimonialRequest();
				request.setTitle(title);
				request.setDescription(description);
				request.setHasVideo(hasVideo);
				request.setHasNewspaperClip(hasNewspaperClip);
				request.setVideoFile(videoFile);
				request.setNewspaperFile(newspaperFile);

				testimonialService.updateTestimonial(id, request);
				return ResponseEntity.ok("Testimonial updated successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body("Update failed: " + e.getMessage());
			}
		}
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<String> deleteTestimonial(@PathVariable Long id) {
			try {
				testimonialService.deleteTestimonial(id);
				return ResponseEntity.ok("Testimonial deleted successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body("Deletion failed: " + e.getMessage());
			}
		}
}
