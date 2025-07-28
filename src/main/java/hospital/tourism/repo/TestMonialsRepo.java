package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.Testimonial;

public interface TestMonialsRepo extends JpaRepository<Testimonial, Long> {

}
