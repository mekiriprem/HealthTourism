package hospital.tourism.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.BlogCategory;
import hospital.tourism.Entity.Blogs;

public interface BlogsRepo extends JpaRepository<Blogs, Long> {

	Optional<BlogCategory> findByCategory_BlogCategoryName(String categoryName);

}
