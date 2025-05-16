package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.HospitalDTO;
import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Dto.SpaCenterDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.SpaCenterRepo;

@Service
public class LocationService {
	
	
	
	 @Autowired
	    private LocationRepo locationRepository;
		@Autowired
		private SpaCenterRepo spaCenterRepo;
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
	    public SpaCenterDTO getSpaCenterDTOById(Integer spaCenterId) {
	        SpaCenter spaCenter = spaCenterRepo.findById(spaCenterId)
	            .orElseThrow(() -> new RuntimeException("SpaCenter not found"));

	        SpaCenterDTO dto = new SpaCenterDTO();
	        dto.setSpaName(spaCenter.getSpaName());
	        dto.setSpaDescription(spaCenter.getSpaDescription());
	        dto.setSpaImage(spaCenter.getSpaImage());
	        dto.setRating(spaCenter.getRating());
	        dto.setAddress(spaCenter.getAddress());

	        if (spaCenter.getLocation() != null) {
	            dto.setLocationId(spaCenter.getLocation().getLocationId());
	        }

	        return dto;
	    }
		public List<LocationEntity> getAllLocations() {
			return locationRepository.findAll();
		}

		public LocationEntity getLocationById(Integer id) {
			return locationRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Location not found with id " + id));
		}
}
