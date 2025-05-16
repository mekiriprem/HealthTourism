package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.HospitalDTO;
import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.repo.LocationRepo;

@Service
public class LocationService {
	
	
	
	 @Autowired
	    private LocationRepo locationRepository;

	    public LocationEntity saveLocation(LocationEntity locationEntity) {
	        return locationRepository.save(locationEntity);
	    }
	    
	    public LocationDTO getLocationWithHospitals(Integer id) {
	        LocationEntity location = locationRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Location not found"));

	        LocationDTO dto = new LocationDTO();
	        dto.setLocationId(location.getLocationId());
	        dto.setCity(location.getCity());
	        dto.setState(location.getState());
	        dto.setCountry(location.getCountry());

	        List<HospitalDTO> hospitalDTOs = location.getHospitalList().stream().map(h -> {
	            HospitalDTO hdto = new HospitalDTO();
	            hdto.setHospitalId(h.getHospitalId());
	            hdto.setHositalName(h.getHositalName());
	            hdto.setHospitalDescription(h.getHospitalDescription());
	            hdto.setHospitalImage(h.getHospitalImage());
	            hdto.setRating(h.getRating());
	            hdto.setAddress(h.getAddress());
	            return hdto;
	        }).toList();

	        dto.setHospitals(hospitalDTOs);

	        return dto;
	    }

	    
	  
	
	
}
