package hospital.tourism.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.BlogCategoryDTO;
import hospital.tourism.Dto.BlogsDTO;
import hospital.tourism.Entity.BlogCategory;
import hospital.tourism.Entity.Blogs;
import hospital.tourism.repo.BlogCategoryRepo;
import hospital.tourism.repo.BlogsRepo;

@Service
public class BlogServiceImpl {

	
	 @Value("${supabase.url}")
	    private String supabaseUrl;

	    @Value("${supabase.api.key}")
	    private String supabaseKey;

	    @Value("${supabase.bucket}")
	    private String supabaseBucket;

	    @Autowired
	    private BlogsRepo blogRepository;

	    @Autowired
	    private BlogCategoryRepo blogCategoryRepository;

	   
	    public BlogsDTO createBlog(BlogsDTO dto, MultipartFile image) {
	        Blogs blog = new Blogs();

	        // Upload image to Supabase
	        String imageUrl = uploadImageToSupabase(image);
	        blog.setCoverImage(imageUrl);

	        blog.setAuthor(dto.getAuthor());
	        blog.setAuthorEmail(dto.getAuthorEmail());
	        blog.setAuthorName(dto.getAuthorName());
	       blog.setBlogUrl(dto.getBlogUrl()); // Assuming blogUrl is set in DTO
	        blog.setMetaTitle(dto.getMetaTitle());
	        blog.setMetaDescription(dto.getMetaDescription());
	        blog.setTitle(dto.getTitle());
	     
	        blog.setShortDescription(dto.getShortDescription());
	        blog.setContent(dto.getContent());

	        BlogCategory category = blogCategoryRepository.findById(dto.getCategory().getBlogCategoryId())
	                .orElseThrow(() -> new RuntimeException("Category not found"));
	        blog.setCategory(category);

	        Blogs saved = blogRepository.save(blog);
	        return mapToDTO(saved);
	    }

	
	    public List<BlogsDTO> getAllBlogs() {
	        return blogRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	    }

	   
	    public BlogsDTO getBlogById(Long id) {
	        Blogs blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
	        return mapToDTO(blog);
	    }

	 
	    public List<BlogCategoryDTO> getBlogsByCategory(String categoryName) {
	        return blogRepository.findByCategory_BlogCategoryName(categoryName)
	                .stream().map(this::mapToDTO).collect(Collectors.toList());
	    }
	    
	    private BlogCategoryDTO mapToDTO(BlogCategory category) {
	        BlogCategoryDTO dto = new BlogCategoryDTO();
	        dto.setBlogCategoryId(category.getBlogCategoryId());
	        dto.setBlogCategoryName(category.getBlogCategoryName());
	        dto.setBlogCategoryDescription(category.getBlogCategoryDescription());
	        dto.setBlogCategoryImageUrl(category.getBlogCategoryImageUrl());
	        dto.setBlogCategoryCreatedBy(category.getBlogCategoryCreatedBy());
	        dto.setBlogCategoryCreatedDate(category.getBlogCategoryCreatedDate());
	        return dto;
	    }


	    private BlogsDTO mapToDTO(Blogs blog) {
	        BlogsDTO dto = new BlogsDTO();

	        dto.setBlogId(blog.getBlogId());
	        dto.setAuthor(blog.getAuthor());
	        dto.setAuthorEmail(blog.getAuthorEmail());
	        dto.setAuthorName(blog.getAuthorName());
	      
	        dto.setMetaTitle(blog.getMetaTitle());
	        dto.setMetaDescription(blog.getMetaDescription());
	        dto.setTitle(blog.getTitle());
	       
	        dto.setShortDescription(blog.getShortDescription());
	        dto.setContent(blog.getContent());
	        dto.setCoverImage(blog.getCoverImage());
	        dto.setCreatedAt(blog.getCreatedAt());
	        dto.setUpdatedAt(blog.getUpdatedAt());

	        BlogCategoryDTO catDTO = new BlogCategoryDTO();
	        catDTO.setBlogCategoryId(blog.getCategory().getBlogCategoryId());
	        catDTO.setBlogCategoryName(blog.getCategory().getBlogCategoryName());
	        catDTO.setBlogCategoryDescription(blog.getCategory().getBlogCategoryDescription());
	        catDTO.setBlogCategoryImageUrl(blog.getCategory().getBlogCategoryImageUrl());

	        dto.setCategory(catDTO);
	        return dto;
	    }

	    private String uploadImageToSupabase(MultipartFile file) {
	        try {
	            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            String uploadUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;

	            OkHttpClient client = new OkHttpClient();
	            RequestBody body = RequestBody.create(file.getBytes(), MediaType.parse(file.getContentType()));

	            Request request = new Request.Builder()
	                    .url(uploadUrl)
	                    .header("apikey", supabaseKey)
	                    .header("Authorization", "Bearer " + supabaseKey)
	                    .put(body)
	                    .build();

	            Response response = client.newCall(request).execute();

	            if (!response.isSuccessful()) {
	                throw new IOException("Failed to upload image to Supabase: " + response);
	            }

	            return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;

	        } catch (Exception e) {
	            throw new RuntimeException("Supabase image upload failed", e);
	        }
	    }
	    
	    //update blog
		public BlogsDTO updateBlog(Long id, BlogsDTO dto, MultipartFile image) {
			Blogs blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));

			// If a new image is provided, upload it
			if (image != null && !image.isEmpty()) {
				String imageUrl = uploadImageToSupabase(image);
				blog.setCoverImage(imageUrl);
			}

			blog.setAuthor(dto.getAuthor());
			blog.setAuthorEmail(dto.getAuthorEmail());
			blog.setAuthorName(dto.getAuthorName());
			
			blog.setMetaTitle(dto.getMetaTitle());
			blog.setMetaDescription(dto.getMetaDescription());
			blog.setTitle(dto.getTitle());
			
			blog.setShortDescription(dto.getShortDescription());
			blog.setContent(dto.getContent());

			BlogCategory category = blogCategoryRepository.findById(dto.getCategory().getBlogCategoryId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			blog.setCategory(category);

			Blogs updated = blogRepository.save(blog);
			return mapToDTO(updated);
		}
		
		// Delete blog
		public void deleteBlog(Long id) {
			Blogs blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
			blogRepository.delete(blog);
		}
}
