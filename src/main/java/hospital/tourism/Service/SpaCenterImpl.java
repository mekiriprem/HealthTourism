package hospital.tourism.Service;

import java.util.List;

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
	    spaCenter.setStatus("Active"); // Default status set to Active

	    return spaCenterRepo.save(spaCenter);
	}
	
	
    
	public List<SpaCenterDTO> getAllSpaCenters() {
	    List<SpaCenter> centers = spaCenterRepo.findAll();

	    return centers.stream().map(spa -> {
	        SpaCenterDTO dto = new SpaCenterDTO();
	        dto.setSpaId(spa.getSpaId());
	        dto.setSpaName(spa.getSpaName());
	        dto.setSpaDescription(spa.getSpaDescription());
	        dto.setSpaImage(spa.getSpaImage());
	        dto.setRating(spa.getRating());
	        dto.setAddress(spa.getAddress());
	        dto.setStatus(spa.getStatus());

	        if (spa.getLocation() != null) {
	            dto.setLocationId(spa.getLocation().getLocationId());
	            dto.setCity(spa.getLocation().getCity());
	            dto.setState(spa.getLocation().getState());
	            dto.setCountry(spa.getLocation().getCountry());
	        }

	        return dto;
	    }).toList();
	}
	public SpaCenterDTO getSpaCenterById(Integer id) {
	    SpaCenter spa = spaCenterRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Spa Center not found with id " + id));

	    SpaCenterDTO dto = new SpaCenterDTO();
	    dto.setSpaId(spa.getSpaId());
	    dto.setSpaName(spa.getSpaName());
	    dto.setSpaDescription(spa.getSpaDescription());
	    dto.setSpaImage(spa.getSpaImage());
	    dto.setRating(spa.getRating());
	    dto.setAddress(spa.getAddress());
	    dto.setStatus(spa.getStatus());

	    if (spa.getLocation() != null) {
	        dto.setLocationId(spa.getLocation().getLocationId());
	        dto.setCity(spa.getLocation().getCity());
	        dto.setState(spa.getLocation().getState());
	        dto.setCountry(spa.getLocation().getCountry());
	    }

	    return dto;
	}

    
	public SpaCenterDTO updateSpaCenter(Integer id, SpaCenterDTO dto) {
		SpaCenter spaCenter = spaCenterRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Spa Center not found with id " + id));

		spaCenter.setSpaName(dto.getSpaName());
		spaCenter.setSpaDescription(dto.getSpaDescription());
		spaCenter.setSpaImage(dto.getSpaImage());
		spaCenter.setRating(dto.getRating());
		spaCenter.setAddress(dto.getAddress());
        spaCenter.setStatus(dto.getStatus());
                spaCenter.setLocation(locationRepo.findById(dto.getLocationId())
                		.orElseThrow(() -> new RuntimeException("Location not found with id " + dto.getLocationId())));
                        SpaCenter updatedSpaCenter = spaCenterRepo.save(spaCenter);
                                SpaCenterDTO updatedDto = new SpaCenterDTO();
                                updatedDto.setSpaId(updatedSpaCenter.getSpaId());
                                updatedDto.setSpaName(updatedSpaCenter.getSpaName());
                                updatedDto.setSpaDescription(updatedSpaCenter.getSpaDescription());
                                updatedDto.setSpaImage(updatedSpaCenter.getSpaImage());
                                updatedDto.setRating(updatedSpaCenter.getRating());
                                updatedDto.setAddress(updatedSpaCenter.getAddress());
                                updatedDto.setLocationId(updatedSpaCenter.getLocation().getLocationId());
                                updatedDto.setStatus(updatedSpaCenter.getStatus());
                                        return updatedDto;
	
    }
    
	// Soft delete
	public void softDeleteSpaCenter(Integer id) {
		SpaCenter spaCenter = spaCenterRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Spa Center not found with ID: " + id));
		spaCenter.setStatus("Inactive");
		spaCenterRepo.save(spaCenter);
	}
	
	// Reactivate
	public void activateSpaCenter(Integer id) {
		SpaCenter spaCenter = spaCenterRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Spa Center not found with ID: " + id));
		if ("Inactive".equalsIgnoreCase(spaCenter.getStatus())) {
			spaCenter.setStatus("Active");
			spaCenterRepo.save(spaCenter);
		}
	}
}
