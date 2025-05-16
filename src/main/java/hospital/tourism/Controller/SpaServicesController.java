package hospital.tourism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.SpaCenterDTO;
import hospital.tourism.Dto.SpaServiceDTO;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Service.SpaServicessImpl;

@RestController
@RequestMapping("/spaServices")
public class SpaServicesController {

	@Autowired
	private SpaServicessImpl spaServicessImpl;
	@PostMapping("/save")
	public ResponseEntity<SpaServiceDTO> saveSpaService(@RequestBody SpaServiceDTO spaServiceDTO) {
        // Call service to save entity
        SpaServicese savedEntity = spaServicessImpl.saveSpaService(spaServiceDTO);

        // Convert saved entity back to DTO
           SpaServiceDTO responseDTO = convertToDTO(savedEntity);

        // Return ResponseEntity with saved DTO and HTTP status 201 CREATED
        return ResponseEntity.status(201).body(responseDTO);
    }

    // Converts SpaService entity to SpaServiceDTO
    private SpaServiceDTO convertToDTO(SpaServicese spaService) {
        SpaServiceDTO dto = new SpaServiceDTO();
        dto.setServiceName(spaService.getServiceName());
        dto.setServiceDescription(spaService.getServiceDescription());
        dto.setServiceImage(spaService.getServiceImage());
        dto.setRating(spaService.getRating());
        dto.setPrice(spaService.getPrice());   	
        return dto;
    }
}
