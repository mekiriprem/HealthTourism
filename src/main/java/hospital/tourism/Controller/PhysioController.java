package hospital.tourism.Controller;

import javax.print.PrintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
