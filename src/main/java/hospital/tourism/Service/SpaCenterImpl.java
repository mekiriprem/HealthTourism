package hospital.tourism.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.SpaCenterDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.SpaCenterRepo;

@Service
public class SpaCenterImpl {

	@Autowired
	private SpaCenterRepo spaCenterRepo;
	@Autowired
	private LocationRepo locationRepo;
	
	public SpaCenter saveSpaCenter(SpaCenterDTO dto) {
	    LocationEntity location = locationRepo.findById(dto.getLocationId())
	        .orElseThrow(() -> new RuntimeException("Location not found"));

	    SpaCenter spaCenter = new SpaCenter();
	    spaCenter.setSpaName(dto.getSpaName());
	    spaCenter.setSpaDescription(dto.getSpaDescription());
	    spaCenter.setSpaImage(dto.getSpaImage());
	    spaCenter.setRating(dto.getRating());
	    spaCenter.setAddress(dto.getAddress());
	    spaCenter.setLocation(location);

	    return spaCenterRepo.save(spaCenter);
	}
	
	public SpaCenter getSpaCenterById(Integer id) {
		return spaCenterRepo.findById(id).orElseThrow(() -> new RuntimeException("Spa Center not found with id " + id));
	}
}
