package hospital.tourism.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.BlogCategoryDTO;
import hospital.tourism.Entity.BlogCategory;
import hospital.tourism.repo.BlogCategoryRepo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class BlogCategoryService {

	@Autowired
    private BlogCategoryRepo blogCategoryRepo;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

  
    public BlogCategoryDTO createCategoryWithImage(BlogCategoryDTO dto, MultipartFile image) {
        String imageUrl = uploadImageToSupabase(image);

        BlogCategory category = new BlogCategory();
        category.setBlogCategoryName(dto.getBlogCategoryName());
        category.setBlogCategoryDescription(dto.getBlogCategoryDescription());
        category.setBlogCategoryCreatedBy(dto.getBlogCategoryCreatedBy());
        category.setBlogCategoryCreatedDate(dto.getBlogCategoryCreatedDate());
        category.setBlogCategoryImageUrl(imageUrl);

        BlogCategory saved = blogCategoryRepo.save(category);
        return mapToDTO(saved);
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

   
    public List<BlogCategoryDTO> getAllCategories() {
        return blogCategoryRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

  
    public BlogCategoryDTO getCategoryById(Long id) {
        BlogCategory category = blogCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToDTO(category);
    }

 
    public void deleteCategory(Long id) {
        blogCategoryRepo.deleteById(id);
    }

    private BlogCategoryDTO mapToDTO(BlogCategory category) {
        BlogCategoryDTO dto = new BlogCategoryDTO();
        dto.setBlogCategoryId(category.getBlogCategoryId());
        dto.setBlogCategoryName(category.getBlogCategoryName());
        dto.setBlogCategoryDescription(category.getBlogCategoryDescription());
        dto.setBlogCategoryCreatedBy(category.getBlogCategoryCreatedBy());
        dto.setBlogCategoryCreatedDate(category.getBlogCategoryCreatedDate());
        dto.setBlogCategoryImageUrl(category.getBlogCategoryImageUrl());
        return dto;
    }
    //update category
	public BlogCategoryDTO updateCategory(Long id, BlogCategoryDTO dto, MultipartFile image) {
		BlogCategory category = blogCategoryRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		if (image != null && !image.isEmpty()) {
			String imageUrl = uploadImageToSupabase(image);
			category.setBlogCategoryImageUrl(imageUrl);
		}

		category.setBlogCategoryName(dto.getBlogCategoryName());
		category.setBlogCategoryDescription(dto.getBlogCategoryDescription());
		category.setBlogCategoryCreatedBy(dto.getBlogCategoryCreatedBy());
		category.setBlogCategoryCreatedDate(dto.getBlogCategoryCreatedDate());

		BlogCategory updated = blogCategoryRepo.save(category);
		return mapToDTO(updated);
	}
	//delete category by id
	public void deleteCategoryById(Long id) {
		BlogCategory category = blogCategoryRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
		blogCategoryRepo.delete(category);
	}
}
