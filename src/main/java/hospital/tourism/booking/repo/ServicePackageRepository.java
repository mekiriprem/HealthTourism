package hospital.tourism.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.ServiceItems;
import hospital.tourism.booking.entity.ServicePackage;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

	ServiceItems save(ServiceItems item);

}
