package hospital.tourism.Controller;

import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Service.LocationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public LocationEntity createLocation(@RequestBody LocationEntity locationEntity) {
        return locationService.saveLocation(locationEntity);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationDetails(@PathVariable Integer id) {
        LocationDTO location = locationService.getLocationWithHospitals(id);
        return ResponseEntity.ok(location);
    }
    @GetMapping
    public List<LocationEntity> getAllLocations() {
        return locationService.getAllLocations();
    }


}

