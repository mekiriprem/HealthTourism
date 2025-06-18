package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.BlogCategoryDTO;
import hospital.tourism.Dto.BlogsDTO;
import hospital.tourism.Service.BlogServiceImpl;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

	 @Autowired
	    private BlogServiceImpl blogService;

	    
	 @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<BlogsDTO> createBlog(
	            @RequestParam("authorEmail") String authorEmail,
	            @RequestParam("authorName") String authorName,
	            @RequestParam("author") String author,
	            @RequestParam("blogUrl") String blogUrl,
	            @RequestParam("metaTitle") String metaTitle,
	            @RequestParam("metaDescription") String metaDescription,
	            @RequestParam("title") String title,
	            @RequestParam("slug") String slug,
	            @RequestParam("shortDescription") String shortDescription,
	            @RequestParam("content") String content,
	            @RequestParam("categoryId") Long categoryId,
	            @RequestParam("image") MultipartFile image) {

	        BlogsDTO dto = new BlogsDTO();
	        dto.setAuthorEmail(authorEmail);
	        dto.setAuthorName(authorName);
	        dto.setAuthor(author);
	        dto.setBlogUrl(blogUrl);
	        dto.setMetaTitle(metaTitle);
	        dto.setMetaDescription(metaDescription);
	        dto.setTitle(title);
	        dto.setSlug(slug);
	        dto.setShortDescription(shortDescription);
	        dto.setContent(content);

	        // Set category as nested object
	        BlogCategoryDTO cat = new BlogCategoryDTO();
	        cat.setBlogCategoryId(categoryId);
	        dto.setCategory(cat);

	        BlogsDTO savedBlog = blogService.createBlog(dto, image);
	        return ResponseEntity.ok(savedBlog);
	    }

	    /**
	     * Get all blogs
	     */
	    @GetMapping("/all")
	    public ResponseEntity<List<BlogsDTO>> getAllBlogs() {
	        return ResponseEntity.ok(blogService.getAllBlogs());
	    }

	    /**
	     * Get blog by ID
	     */
	    @GetMapping("/{id}")
	    public ResponseEntity<BlogsDTO> getBlogById(@PathVariable Long id) {
	        return ResponseEntity.ok(blogService.getBlogById(id));
	    }

	    /**
	     * Get blogs by category name
	     */
	    @GetMapping("/category/{categoryName}")
	    public ResponseEntity<List<BlogCategoryDTO>> getBlogsByCategory(@PathVariable String categoryName) {
	        return ResponseEntity.ok(blogService.getBlogsByCategory(categoryName));
	    }
}
