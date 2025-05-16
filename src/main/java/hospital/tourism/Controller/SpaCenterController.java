package hospital.tourism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.SpaCenterDTO;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.Service.SpaCenterImpl;

@RestController
@RequestMapping("/spaCenter")
public class SpaCenterController {
	@Autowired
	private SpaCenterImpl spaCenterService;
	
	@PostMapping("/upload")
	public ResponseEntity<SpaCenter> saveSpaCenter(@RequestBody SpaCenterDTO spaCenter) {
		SpaCenter savedSpaCenter = spaCenterService.saveSpaCenter(spaCenter);
		 return ResponseEntity.ok(savedSpaCenter);
	}

}
