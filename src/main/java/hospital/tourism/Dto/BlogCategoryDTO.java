package hospital.tourism.Dto;

import java.util.List;

import lombok.Data;

@Data
public class BlogCategoryDTO {

	
	private Long blogCategoryId;
    private String blogCategoryName;
    private String blogCategoryDescription;
    private String blogCategoryImageUrl;
    private String blogCategoryCreatedBy;
    private String blogCategoryCreatedDate;
    
    private List<SimpleBlogDTO> blogs;

    // Inner DTO for simplicity
    public static class SimpleBlogDTO {
        private Long blogId;
        private String title;
        private String slug;
    }
}
