package hospital.tourism.Controller;

import hospital.tourism.Dto.BasicLocationDTO;
import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Service.LocationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
@RestController
@RequestMapping("/api/locations")
public class LocationController {


    @Autowired
    private LocationService locationService;
    
    @PostMapping
    public LocationEntity createLocation(@RequestBody LocationDTO dto) {
        LocationEntity location = new LocationEntity();
        location.setCountry(dto.getCountry());
        location.setState(dto.getState());
        location.setCity(dto.getCity());
        return locationService.saveLocation(location);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationDetails(@PathVariable Integer id) {
        LocationDTO location = locationService.getLocationWithHospitals(id);
        return ResponseEntity.ok(location);
    }
    @GetMapping("/getall")
    public List<BasicLocationDTO> getAllLocations() {
        return locationService.getAllBasicLocations();
    }
    
    @GetMapping()
    public ResponseEntity<List<BasicLocationDTO>> getMinimalLocations() {
        return ResponseEntity.ok(locationService.getAllBasicLocations());
    }
    
	// update location
	@PutMapping("/{id}")
	public ResponseEntity<LocationDTO> updateLocation(@PathVariable Integer id, @RequestBody LocationDTO dto) {
		LocationDTO updatedLocation = locationService.updateLocation(id, dto);
		return ResponseEntity.ok(updatedLocation);
	}

}

