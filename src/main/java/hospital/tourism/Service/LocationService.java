package hospital.tourism.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.LocationEntity;

@Service
public class LocationService {
	
	@Autowired
	private LocationEntity locationEntity;
	
	
}
