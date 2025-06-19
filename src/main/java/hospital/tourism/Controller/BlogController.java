package hospital.tourism.Controller;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import hospital.tourism.Dto.BlogsDTO;
import hospital.tourism.Service.BlogServiceImpl;



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
	    
}
