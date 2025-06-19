package hospital.tourism.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.BlogCategoryDTO;
import hospital.tourism.Dto.BlogsDTO;
import hospital.tourism.Entity.BlogCategory;
import hospital.tourism.Entity.Blogs;
import hospital.tourism.repo.BlogCategoryRepo;
import hospital.tourism.repo.BlogsRepo;

@Service
public class BlogServiceImpl {

	
	 @Autowired
	    private BlogsRepo blogsRepository;

	    @Autowired
	    private BlogCategoryRepo categoryRepository;

	    // Supabase config values injected from application.properties
	    @Value("${supabase.url}")
	    private String supabaseProjectUrl;

	    @Value("${supabase.api.key}")
	    private String supabaseApiKey;

	    @Value("${supabase.bucket}")
	    private String supabaseBucketName;

	   
	    public BlogsDTO createBlog(BlogsDTO dto, MultipartFile image) {
	        Blogs blog = new Blogs();

	        blog.setAuthorEmail(dto.getAuthorEmail());
	        blog.setAuthorName(dto.getAuthorName());
	        blog.setMetaTitle(dto.getMetaTitle());
	        blog.setMetaDescription(dto.getMetaDescription());
	        blog.setTitle(dto.getTitle());
	        blog.setShortDescription(dto.getShortDescription());
	        blog.setContent(dto.getContent());

	        BlogCategory category = categoryRepository.findById(dto.getCategoryId())
	                .orElseThrow(() -> new RuntimeException("Category not found"));
	        blog.setCategory(category);

	        blog.setCoverImage(""); // Placeholder

	        Blogs savedBlog = blogsRepository.save(blog); // Save first to get ID

	        try {
	            String imageUrl = uploadFileToSupabase(image, "blog_images", savedBlog.getBlogId());
	            savedBlog.setCoverImage(imageUrl);
	            blogsRepository.save(savedBlog);
	        } catch (Exception e) {
	            throw new RuntimeException("Image upload failed: " + e.getMessage());
	        }

	        // Map back to DTO
	        BlogsDTO result = new BlogsDTO();
	        result.setBlogId(savedBlog.getBlogId());
	        result.setAuthorEmail(savedBlog.getAuthorEmail());
	        result.setAuthorName(savedBlog.getAuthorName());
	        result.setMetaTitle(savedBlog.getMetaTitle());
	        result.setMetaDescription(savedBlog.getMetaDescription());
	        result.setTitle(savedBlog.getTitle());
	        result.setShortDescription(savedBlog.getShortDescription());
	        result.setContent(savedBlog.getContent());
	        result.setCoverImage(savedBlog.getCoverImage());
	        result.setCategoryId(savedBlog.getCategory().getBlogCategoryId());
	        result.setCategoryName(savedBlog.getCategory().getBlogCategoryName());

	        return result;
	    }

	    // Supabase Upload Logic (inline in the service)
	    private String uploadFileToSupabase(MultipartFile file, String folder, Long blogId) throws IOException {
	        String fileName = "blog" + blogId + "_" + UUID.randomUUID() + "_" +
	                Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_");

	        String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + folder + "/" + fileName;

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + supabaseApiKey);
	        headers.set("Content-Type", file.getContentType());

	        byte[] fileBytes = StreamUtils.copyToByteArray(file.getInputStream());
	        HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);

	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

	        if (response.getStatusCode().is2xxSuccessful()) {
	            return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + folder + "/" + fileName;
	        } else {
	            throw new RuntimeException("File upload failed: " + response.getStatusCode());
	        }

	    }
	    
		public List<BlogsDTO> getAllBlogs() {
			List<Blogs> blogs = blogsRepository.findAll();
			return blogs.stream().map(blog -> {
				BlogsDTO dto = new BlogsDTO();
				dto.setBlogId(blog.getBlogId());
				dto.setAuthorEmail(blog.getAuthorEmail());
				dto.setAuthorName(blog.getAuthorName());
				dto.setMetaTitle(blog.getMetaTitle());
				dto.setMetaDescription(blog.getMetaDescription());
				dto.setTitle(blog.getTitle());
				dto.setShortDescription(blog.getShortDescription());
				dto.setContent(blog.getContent());
				dto.setCoverImage(blog.getCoverImage());
				if (blog.getCategory() != null) {
					dto.setCategoryId(blog.getCategory().getBlogCategoryId());
					dto.setCategoryName(blog.getCategory().getBlogCategoryName());
				}
				return dto;
			}).collect(Collectors.toList());
		}
}

