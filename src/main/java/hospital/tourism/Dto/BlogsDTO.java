package hospital.tourism.Dto;

import lombok.Data;

@Data
public class BlogsDTO {
	private Long blogId;
    private String authorEmail;
    private String authorName;
    private String metaTitle;
    private String metaDescription;
    private String title;
    private String shortDescription;
    private String content;
    private String coverImage;

    private Long categoryId;       // Extracted from BlogCategory
    private String categoryName;
}
