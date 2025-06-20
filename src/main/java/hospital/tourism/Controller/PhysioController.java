package hospital.tourism.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.PhysioDTO;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Service.PhysioImpl;

@RestController
@RequestMapping("/physio")
public class PhysioController {

	  @Autowired
	    private PhysioImpl physioService;

	    @PostMapping("/save-Physio")
	    public Physio savePhysio(@RequestBody PhysioDTO dto) {
	        return physioService.savePhysio(dto);
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
		public ResponseEntity<PhysioDTO> updatePhysio(@PathVariable Long id, @RequestBody PhysioDTO dto) {
			PhysioDTO updatedDto = physioService.updatePhysio(id, dto);
			return ResponseEntity.ok(updatedDto);
		}
}
