package hospital.tourism.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.PackageServiceItem;

public interface PackageServiceItemRepository extends JpaRepository<PackageServiceItem, Long>  {

}
