package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.BlogCategory;

public interface BlogCategoryRepo extends JpaRepository<BlogCategory, Long> {

}
