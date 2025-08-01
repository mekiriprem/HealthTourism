package hospital.tourism.Controller;



import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.BlogsDTO;
import hospital.tourism.Service.BlogServiceImpl;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"},allowCredentials = "true")
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

	 @Autowired
	    private BlogServiceImpl blogService;

	 
	    @PostMapping(value = "/create", consumes = "multipart/form-data")
	    public ResponseEntity<BlogsDTO> createBlog(
	            @RequestParam("authorEmail") String authorEmail,
	            @RequestParam("authorName") String authorName,
	            @RequestParam("metaTitle") String metaTitle,
	            @RequestParam("metaDescription") String metaDescription,
	            @RequestParam("title") String title,
	            @RequestParam("shortDescription") String shortDescription,
	            @RequestParam("content") String content,
	            @RequestParam("categoryId") Long categoryId,
	            @RequestPart("image") MultipartFile image
	    ) {
	        BlogsDTO blogDTO = new BlogsDTO();
	        blogDTO.setAuthorEmail(authorEmail);
	        blogDTO.setAuthorName(authorName);
	        blogDTO.setMetaTitle(metaTitle);
	        blogDTO.setMetaDescription(metaDescription);
	        blogDTO.setTitle(title);
	        blogDTO.setShortDescription(shortDescription);
	        blogDTO.setContent(content);
	        blogDTO.setCategoryId(categoryId);

	        BlogsDTO createdBlog = blogService.createBlog(blogDTO, image);
	        return ResponseEntity.ok(createdBlog);
	    }
	    @GetMapping("/all-blogs")
		public ResponseEntity<List<BlogsDTO>> getAllBlogs() {
		List<BlogsDTO>	 blogss=blogService.getAllBlogs();
		return ResponseEntity.ok(blogss);
	}
	    
	    @PutMapping(value = "/update/{blogId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<BlogsDTO> updateBlog(
	            @PathVariable Long blogId,
	            @RequestParam("authorEmail") String authorEmail,
	            @RequestParam("authorName") String authorName,
	            @RequestParam("metaTitle") String metaTitle,
	            @RequestParam("metaDescription") String metaDescription,
	            @RequestParam("title") String title,
	            @RequestParam("shortDescription") String shortDescription,
	            @RequestParam("content") String content,
	            @RequestParam(value = "categoryId", required = false) Long categoryId,
	            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

	        BlogsDTO dto = new BlogsDTO();
	        dto.setAuthorEmail(authorEmail);
	        dto.setAuthorName(authorName);
	        dto.setMetaTitle(metaTitle);
	        dto.setMetaDescription(metaDescription);
	        dto.setTitle(title);
	        dto.setShortDescription(shortDescription);
	        dto.setContent(content);
	        dto.setCategoryId(categoryId);

	        BlogsDTO updatedBlog = blogService.updateBlog(blogId, dto, image);
	        return ResponseEntity.ok(updatedBlog);
	    }
	   @DeleteMapping("/delete/{blogId}")
		public void deleteBlog(@PathVariable Long blogId) {
			 blogService.deleteBlog(blogId);
			ResponseEntity.ok("Blog deleted successfully");
		}
	  @GetMapping("/get/{blogId}")
	          public ResponseEntity<BlogsDTO> getBlogById(@PathVariable Long blogId) {
		              BlogsDTO blog = blogService.getBlogById(blogId);
						if (blog != null) {
							return ResponseEntity.ok(blog);
						} else {
							return ResponseEntity.notFound().build(); // 404 Not Found
						}
						
	  }
}
