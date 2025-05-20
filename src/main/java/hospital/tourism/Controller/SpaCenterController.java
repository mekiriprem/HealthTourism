package hospital.tourism.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // ✅ Return only essential fields for dropdowns
    @GetMapping("/all")
    public ResponseEntity<List<SpaCenterDTO>> getAllSpaCenters() {
        List<SpaCenter> spaCenters = spaCenterService.getAllSpaCenters();
        List<SpaCenterDTO> dtos = spaCenters.stream().map(spa -> {
            SpaCenterDTO dto = new SpaCenterDTO();
            dto.setSpaId(spa.getSpaId());
            dto.setSpaName(spa.getSpaName());
            dto.setStatus(spa.getStatus()); 
            dto.setRating(spa.getRating());
            dto.setAddress(spa.getAddress());
            
            dto.setSpaDescription(spa.getSpaDescription());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
}

