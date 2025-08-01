package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Service.BookingService;
import hospital.tourism.Service.chefService;

@RestController
@RequestMapping("/api/chefs")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
public class ChefController {

    @Autowired
    private chefService chefService;
    @Autowired
    private BookingService bookingService;

    // Add new chef
    @PostMapping("/add/{locationId}")
    public ResponseEntity<ChefDTO> addChef(
            @PathVariable Integer locationId,
            @RequestParam("chefName") String name,
            @RequestParam("chefDescription") String description,
            @RequestParam("chefRating") String rating,
            @RequestParam("experience") String experience,
            @RequestParam("styles") String styles,
            @RequestParam("status") String status,
            @RequestParam("price") double price,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        ChefDTO dto = new ChefDTO();
        dto.setChefName(name);
        dto.setChefDescription(description);
        dto.setChefRating(rating);
        dto.setExperience(experience);
        dto.setStyles(styles);
        dto.setStatus(status);
        dto.setPrice(price);

        ChefDTO savedChef = chefService.saveChef(dto, locationId, imageFile);
        return new ResponseEntity<>(savedChef, HttpStatus.CREATED);
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
    @PutMapping("/update-chef/{id}")
    public ResponseEntity<ChefDTO> updateChef(
            @PathVariable Long id,
            @RequestParam("chefName") String name,
            @RequestParam("chefDescription") String description,
            @RequestParam("chefRating") String rating,
            @RequestParam("experience") String experience,
            @RequestParam("styles") String styles,
            @RequestParam("status") String status,
            @RequestParam("price") double price,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        ChefDTO dto = new ChefDTO();
        dto.setChefName(name);
        dto.setChefDescription(description);
        dto.setChefRating(rating);
        dto.setExperience(experience);
        dto.setStyles(styles);
        dto.setStatus(status);
        dto.setPrice(price);

        ChefDTO updatedChef = chefService.updateChef(id, dto, imageFile);
        return new ResponseEntity<>(updatedChef, HttpStatus.OK);
    }

    
}

