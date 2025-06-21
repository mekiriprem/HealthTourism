package hospital.tourism.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
	boolean existsByName(String name);
}
