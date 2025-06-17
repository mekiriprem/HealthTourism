package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Service.BookingService;
import hospital.tourism.Service.chefService;

@RestController
@RequestMapping("/api/chefs")
public class ChefController {

    @Autowired
    private chefService chefService;
    @Autowired
    private BookingService bookingService;

    // Add new chef
    @PostMapping("/add/{locationId}")
    public ResponseEntity<ChefDTO> addChef(@RequestBody  ChefDTO che ,@PathVariable Integer locationId) {
    	ChefDTO cheff	=chefService.saveChef(che, locationId);      
        return new ResponseEntity<ChefDTO>(cheff,HttpStatus.CREATED);
    }

    // Get all chefs
    @GetMapping
    public List<ChefDTO> getAllChefs() {
        return chefService.getAllChefs();
    }

    // Get chefs by location ID
    @GetMapping("/location/{locationId}")
    public List<Chefs> getChefsByLocation(@PathVariable Integer locationId) {
        return chefService.getChefsByLocationId(locationId);
    }
    
    // Get chef by ID
    @GetMapping("chef-By/Id/{chefId}")
	public ResponseEntity<ChefDTO> getChefById(@PathVariable Long chefId) {
		ChefDTO chef = chefService.getChefById(chefId);
		if (chef != null) {
			return new ResponseEntity<>(chef, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
    
    @PutMapping("/soft-delete/{id}")
    public void softDelete(@PathVariable Long id) {
    	chefService.softDeleteChef(id);
    }

    @PutMapping("/activate/{id}")
    public void activate(@PathVariable Long id) {
    	chefService.activateChef(id);
    }
    
    
}

