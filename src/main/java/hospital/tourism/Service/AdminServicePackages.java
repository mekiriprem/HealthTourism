package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.ServiceItem;
import hospital.tourism.repo.ServiceItemRepository;
@Service
public class AdminServicePackages {

	@Autowired
	private  ServiceItemRepository serviceItemRepository;

    public ServiceItem createService(ServiceItem item) {
        if (serviceItemRepository.existsByName(item.getName())) {
            throw new RuntimeException("Service already exists");
        }
        return serviceItemRepository.save(item);
    }

    public List<ServiceItem> getAllServices() {
        return serviceItemRepository.findAll();
    }

    public ServiceItem updateService(Long id, ServiceItem updated) {
        ServiceItem existing = serviceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        existing.setName(updated.getName());
        existing.setPricePerDay(updated.getPricePerDay());
        existing.setStatus(updated.getStatus());
        existing.setDescription(updated.getDescription());

        return serviceItemRepository.save(existing);
    }
}
