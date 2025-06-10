package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Dto.SlotRequestDTO;
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
    
    //update chef slots by ID
    @PutMapping("/update/{chefId}")
	public ResponseEntity<List<SlotRequestDTO>> updateChefSlots(@PathVariable Long chefId, @RequestBody List<SlotRequestDTO> slotRequests) {
		List<SlotRequestDTO> updatedChef = chefService.updateChefSlots(chefId, slotRequests);
		if (updatedChef != null) {
			return new ResponseEntity<>(updatedChef, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

