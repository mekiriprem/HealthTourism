package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.Service.chefService;

@RestController
@RequestMapping("/api/chefs")
public class ChefController {

    @Autowired
    private chefService chefService;

    // Add new chef
    @PostMapping("/add")
    public Chefs addChef(@RequestBody Map<String, Object> chefMap) {
        Chefs chef = new Chefs();
        chef.setChefName((String) chefMap.get("chefName"));
        chef.setChefDescription((String) chefMap.get("chefDescription"));
        chef.setChefImage((String) chefMap.get("chefImage"));
        chef.setChefRating((String) chefMap.get("chefRating"));
        chef.setExperience((String) chefMap.get("experience"));
        chef.setStyles((String) chefMap.get("styles"));

        Integer locationId = Integer.parseInt(chefMap.get("locationId").toString());
        return chefService.saveChef(chef, locationId);
    }

    // Get all chefs
    @GetMapping
    public List<Chefs> getAllChefs() {
        return chefService.getAllChefs();
    }

    // Get chefs by location ID
    @GetMapping("/location/{locationId}")
    public List<Chefs> getChefsByLocation(@PathVariable Integer locationId) {
        return chefService.getChefsByLocationId(locationId);
    }
}

