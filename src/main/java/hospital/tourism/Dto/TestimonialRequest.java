package hospital.tourism.Dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class TestimonialRequest {

	 	private String title;
	    private String description;
	    private boolean hasVideo;
	    private boolean hasNewspaperClip;
	    private MultipartFile videoFile;
	    private MultipartFile newspaperFile;
}
