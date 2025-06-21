package hospital.tourism.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import hospital.tourism.booking.entity.ServiceItems;

public interface PackServiceItemRepository extends JpaRepository<ServiceItems, Long>  {

}
