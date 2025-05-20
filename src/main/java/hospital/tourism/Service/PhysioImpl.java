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


}
