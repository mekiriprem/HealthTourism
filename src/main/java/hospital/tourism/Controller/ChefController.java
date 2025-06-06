package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Service.chefService;

@RestController
@RequestMapping("/api/chefs")
public class ChefController {

    @Autowired
    private chefService chefService;

    // Add new chef
    @PostMapping("/add/{locationId}")
    public ResponseEntity<ChefDTO> addChef(@RequestBody  ChefDTO che ,@PathVariable Integer locationId) {
    	ChefDTO cheff	=chefService.saveChef(che, locationId);      
        return new ResponseEntity<ChefDTO>(cheff,HttpStatus.CREATED);
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

