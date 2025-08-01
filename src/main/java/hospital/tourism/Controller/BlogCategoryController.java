package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import hospital.tourism.Dto.BlogCategoryDTO;
import hospital.tourism.Service.BlogCategoryService;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
@RestController
@RequestMapping("/api/blog-categories")
public class BlogCategoryController {


    @Autowired
    private BlogCategoryService blogCategoryService;

    // Create a new blog category with image
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogCategoryDTO> createCategory(
            @RequestParam("blogCategoryName") String blogCategoryName,
            @RequestParam("blogCategoryDescription") String blogCategoryDescription,
            @RequestParam("blogCategoryCreatedBy") String blogCategoryCreatedBy,
            @RequestParam("blogCategoryCreatedDate") String blogCategoryCreatedDate,
            @RequestParam("image") MultipartFile image) {

        BlogCategoryDTO dto = new BlogCategoryDTO();
        dto.setBlogCategoryName(blogCategoryName);
        dto.setBlogCategoryDescription(blogCategoryDescription);
        dto.setBlogCategoryCreatedBy(blogCategoryCreatedBy);
        dto.setBlogCategoryCreatedDate(blogCategoryCreatedDate);

        BlogCategoryDTO savedCategory = blogCategoryService.createCategoryWithImage(dto, image);
        return ResponseEntity.ok(savedCategory);
    }
    // Get all blog categories
    @GetMapping("/getAll/categories")
    public ResponseEntity<List<BlogCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(blogCategoryService.getAllCategories());
    }

    // Get a blog category by ID
    @GetMapping("/{id}")
    public ResponseEntity<BlogCategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(blogCategoryService.getCategoryById(id));
    }

    // Delete a blog category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        blogCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    // update a blog category by ID
    @PostMapping("/{id}")
	public ResponseEntity<BlogCategoryDTO> updateCategory(@PathVariable Long id,
			@RequestParam("blogCategoryName") String blogCategoryName,
			@RequestParam("blogCategoryDescription") String blogCategoryDescription,
			@RequestParam("blogCategoryCreatedBy") String blogCategoryCreatedBy,
			@RequestParam("blogCategoryCreatedDate") String blogCategoryCreatedDate,
			@RequestParam(value = "image", required = false) MultipartFile image) {

		BlogCategoryDTO dto = new BlogCategoryDTO();
		dto.setBlogCategoryId(id);
		dto.setBlogCategoryName(blogCategoryName);
		dto.setBlogCategoryDescription(blogCategoryDescription);
		dto.setBlogCategoryCreatedBy(blogCategoryCreatedBy);
		dto.setBlogCategoryCreatedDate(blogCategoryCreatedDate);

		BlogCategoryDTO updatedCategory = blogCategoryService.createCategoryWithImage(dto, image);
		return ResponseEntity.ok(updatedCategory);
	}
    
}
