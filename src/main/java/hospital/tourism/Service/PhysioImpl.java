package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.PhysioDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.Physio;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.PhysioRepo;

@Service
public class PhysioImpl {

	@Autowired
	private PhysioRepo physioRepo;
	@Autowired
	private LocationRepo locationRepo;
	
	
	public Physio savePhysio(PhysioDTO dto) {
	    LocationEntity location = locationRepo.findById(dto.getLocationId())
	        .orElseThrow(() -> new RuntimeException("Location not found"));

	    Physio physio = new Physio();
	    physio.setPhysioName(dto.getPhysioName());
	    physio.setPhysioDescription(dto.getPhysioDescription());
	    physio.setPhysioImage(dto.getPhysioImage());
	    physio.setRating(dto.getRating());
	    physio.setAddress(dto.getAddress());
	    physio.setPrice(dto.getPrice());
	    physio.setLocation(location);

	    return physioRepo.save(physio);
	}
	

    // Get all chefs 
    public List<Physio> getallphysios() {
        return  physioRepo.findAll();
    }


    public PhysioDTO getPhysioById(Long physioId) {
        if (physioId == null) {
            throw new IllegalArgumentException("Physio ID must not be null");
        }

        Physio physio = physioRepo.findById(physioId)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + physioId));

        PhysioDTO dto = new PhysioDTO();
        dto.setPhysioId(physio.getPhysioId());
        dto.setPhysioName(physio.getPhysioName());
        dto.setPhysioDescription(physio.getPhysioDescription());
        dto.setPhysioImage(physio.getPhysioImage());
        dto.setRating(physio.getRating());
        dto.setAddress(physio.getAddress());
        dto.setPrice(physio.getPrice());
        dto.setStatus(physio.getStatus());

        if (physio.getLocation() != null) {
            dto.setLocationId(physio.getLocation().getLocationId());
        }

        return dto;
    }

    // Soft delete by setting status to "Inactive"
    public void softDeletePhysio(Long id) {
        Physio physio = physioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + id));

        physio.setStatus("Inactive");
        physioRepo.save(physio);
    }

    // Activate if currently inactive
    public void activatePhysioIfInactive(Long id) {
        Physio physio = physioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physio not found with ID: " + id));

        if ("Inactive".equalsIgnoreCase(physio.getStatus())) {
            physio.setStatus("Active");
            physioRepo.save(physio);
        }
    }
    
	public PhysioDTO updatePhysio(Long physioId, PhysioDTO dto) {
		Physio physio = physioRepo.findById(physioId)
				.orElseThrow(() -> new RuntimeException("Physio not found with ID: " + physioId));

		physio.setPhysioName(dto.getPhysioName());
		physio.setPhysioDescription(dto.getPhysioDescription());
		physio.setPhysioImage(dto.getPhysioImage());
		physio.setRating(dto.getRating());
		physio.setAddress(dto.getAddress());
		physio.setPrice(dto.getPrice());

		LocationEntity location = locationRepo.findById(dto.getLocationId())
				.orElseThrow(() -> new RuntimeException("Location not found with ID: " + dto.getLocationId()));
		physio.setLocation(location);

		Physio updatedPhysio = physioRepo.save(physio);

		PhysioDTO updatedDto = new PhysioDTO();
		updatedDto.setPhysioId(updatedPhysio.getPhysioId());
		updatedDto.setPhysioName(updatedPhysio.getPhysioName());
		updatedDto.setPhysioDescription(updatedPhysio.getPhysioDescription());
		updatedDto.setPhysioImage(updatedPhysio.getPhysioImage());
		updatedDto.setRating(updatedPhysio.getRating());
		updatedDto.setAddress(updatedPhysio.getAddress());
		updatedDto.setPrice(updatedPhysio.getPrice());
		updatedDto.setStatus(updatedPhysio.getStatus());
		updatedDto.setLocationId(updatedPhysio.getLocation().getLocationId());

		return updatedDto;
	}
	
}
