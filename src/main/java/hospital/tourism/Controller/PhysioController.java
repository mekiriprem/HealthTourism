package hospital.tourism.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.PhysioDTO;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Service.PhysioImpl;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/physio")
public class PhysioController {

	  @Autowired
	    private PhysioImpl physioService;

	  @PostMapping("/save-Physio")
	  public ResponseEntity<Physio> savePhysio(
	          @RequestParam("physioName") String physioName,
	          @RequestParam("physioDescription") String physioDescription,
	          @RequestParam("rating") String rating,
	          @RequestParam("address") String address,
	          @RequestParam("price") Double price,
	          @RequestParam("locationId") Integer locationId,
	          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
	  ) {
	      PhysioDTO dto = new PhysioDTO();
	      dto.setPhysioName(physioName);
	      dto.setPhysioDescription(physioDescription);
	      dto.setRating(rating);
	      dto.setAddress(address);
	      dto.setPrice(price);
	      dto.setLocationId(locationId);

	      Physio saved = physioService.savePhysio(dto, imageFile);
	      return ResponseEntity.status(HttpStatus.SC_CREATED).body(saved);
	  }

	    @GetMapping
	    public List<Physio> getAllPhysios() {
	        return physioService.getallphysios();
	    }
	    
	    @GetMapping("/getall/pysios")
	    public ResponseEntity<List<PhysioDTO>> getAllPhysio() {
	        List<Physio> physios = physioService.getallphysios();

	        List<PhysioDTO> dtos = physios.stream().map(physio -> {
	            PhysioDTO dto = new PhysioDTO();
	            dto.setPhysioId(physio.getPhysioId());
	            dto.setPhysioName(physio.getPhysioName());
	            dto.setPhysioDescription(physio.getPhysioDescription());
	            dto.setPhysioImage(physio.getPhysioImage());
	            dto.setRating(physio.getRating());
	            dto.setAddress(physio.getAddress());
	            dto.setPrice(physio.getPrice());
	            dto.setStatus(physio.getStatus());
	            if (physio.getLocation() != null) {
	                dto.setLocationId(physio.getLocation().getLocationId());
	            }
	            return dto;
	        }).collect(Collectors.toList());

	        return ResponseEntity.ok(dtos);
	    }

	    @GetMapping("/get/{physioId}")
		public ResponseEntity<PhysioDTO> getPhysioByIdss(@PathVariable Long physioId) {
			PhysioDTO dto = physioService.getPhysioById(physioId);
			return ResponseEntity.ok(dto);
		}
	    @PutMapping("/soft-delete/{id}")
	    public void softDeletePhysio(@PathVariable Long id) {
	        physioService.softDeletePhysio(id);
	    }

	    // Activate physio (if status is Inactive)
	    @PutMapping("/activate/{id}")
	    public void activatePhysio(@PathVariable Long id) {
	        physioService.activatePhysioIfInactive(id);
	    }
	    @PutMapping("/update-physio/{id}")
	    public ResponseEntity<PhysioDTO> updatePhysio(
	            @PathVariable Long id,
	            @RequestParam("physioName") String physioName,
	            @RequestParam("physioDescription") String physioDescription,
	            @RequestParam("rating") String rating,
	            @RequestParam("address") String address,
	            @RequestParam("price") Double price,
	            @RequestParam("locationId") Integer locationId,
	            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
	    ) {
	        PhysioDTO dto = new PhysioDTO();
	        dto.setPhysioName(physioName);
	        dto.setPhysioDescription(physioDescription);
	        dto.setRating(rating);
	        dto.setAddress(address);
	        dto.setPrice(price);
	        dto.setLocationId(locationId);

	        PhysioDTO updatedDto = physioService.updatePhysio(id, dto, imageFile);
	        return ResponseEntity.ok(updatedDto);
	    }

}
