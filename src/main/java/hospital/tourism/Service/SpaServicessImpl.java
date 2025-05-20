package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.SpaServiceDTO;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.repo.SpaCenterRepo;
import hospital.tourism.repo.SpaservicesRepo;

@Service
public class SpaServicessImpl {
	@Autowired
	private SpaservicesRepo spaservicesRepo;
	@Autowired
	private SpaCenterRepo spaCenterRepo;
	
	public SpaServicese saveSpaService(SpaServiceDTO dto) {
	    SpaCenter spaCenter = spaCenterRepo.findById(dto.getSpaCenterId())
	        .orElseThrow(() -> new RuntimeException("SpaCenter not found"));

	    SpaServicese spaService = new SpaServicese();
	    spaService.setServiceName(dto.getServiceName());
	    spaService.setServiceDescription(dto.getServiceDescription());
	    spaService.setServiceImage(dto.getServiceImage());
	    spaService.setRating(dto.getRating());
	    spaService.setPrice(dto.getPrice());
	    spaService.setSpaCenter(spaCenter);

	    return spaservicesRepo.save(spaService);
	}

	public SpaServicese getSpaServiceById(Integer id) {
		return spaservicesRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Spa Service not found with id " + id));
	}
	
	public List<SpaServicese> getServicesBySpaCenterId(Integer spaId) {
	    return spaservicesRepo.findBySpaCenterSpaId(spaId);
	}


}
