package hospital.tourism.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlogsDTO {

	 private Long blogId;

	    private String authorEmail;
	    private String authorName;

	    private String blogUrl;
	    private String metaTitle;
	    private String metaDescription;

	    private String title;
	    private String slug;
	    private String shortDescription;
	    private String content;
	    private String coverImage;

	    private String author;

	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

	    private BlogCategoryDTO category; // nested DTO
}
