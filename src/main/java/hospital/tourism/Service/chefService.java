package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.LocationRepo;

@Service
public class chefService {

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private chefsRepo chefsRepo;

    // Save a chef with location mapping
    public Chefs saveChef(Chefs chef, Integer locationId) {
        LocationEntity location = locationRepo.findById(locationId)
                .orElseThrow(() -> new RuntimeException("❌ Location not found with ID: " + locationId));

        chef.setLocation(location);
        return chefsRepo.save(chef);
    }

    // Get all chefs by location ID
    public List<Chefs> getChefsByLocationId(Integer locationId) {
        return chefsRepo.findByLocation_LocationId(locationId);
    }

    // Get all chefs
    public List<Chefs> getAllChefs() {
        return chefsRepo.findAll();
    }
}
