package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.SpaServiceDTO;
import hospital.tourism.Entity.SpaCenter;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Entity.Translators;
import hospital.tourism.repo.SpaCenterRepo;
import hospital.tourism.repo.SpaservicesRepo;

@Service
public class SpaServicessImpl {
	@Autowired
	private SpaservicesRepo spaservicesRepo;
	@Autowired
	private SpaCenterRepo spaCenterRepo;
	
	public SpaServicese saveSpaService(SpaServiceDTO dto) {
	    SpaCenter spaCenter = spaCenterRepo.findById(dto.getSpaCenterId())
	        .orElseThrow(() -> new RuntimeException("SpaCenter not found"));

	    SpaServicese spaService = new SpaServicese();
	    spaService.setServiceName(dto.getServiceName());
	    spaService.setServiceDescription(dto.getServiceDescription());
	    spaService.setServiceImage(dto.getServiceImage());
	    spaService.setRating(dto.getRating());
	    spaService.setPrice(dto.getPrice());
	    spaService.setSpaCenter(spaCenter);
	    spaService.setStatus("Active"); // Default status set to Active

	    return spaservicesRepo.save(spaService);
	}

	
	
	public List<SpaServicese> getServicesBySpaCenterId(Long spaId) {
	    return spaservicesRepo.findBySpaCenterSpaId(spaId);
	}

public List<SpaServiceDTO>getAllServicesss(){
	List<SpaServicese>spaServiceses= spaservicesRepo.findAll();
	return spaServiceses.stream().map(spaService -> {
		SpaServiceDTO spaServiceDTO = new SpaServiceDTO();
		spaServiceDTO.setServiceIdLong(spaService.getServiceId());
		spaServiceDTO.setServiceIdLong(spaServiceDTO.getServiceIdLong());
		spaServiceDTO.setServiceName(spaService.getServiceName());
		spaServiceDTO.setServiceDescription(spaService.getServiceDescription());
		spaServiceDTO.setServiceImage(spaService.getServiceImage());
		spaServiceDTO.setRating(spaService.getRating());
		spaServiceDTO.setPrice(spaService.getPrice());
		spaServiceDTO.setSpaCenterId(spaService.getSpaCenter().getSpaId());
		spaServiceDTO.setStatus(spaService.getStatus());
		return spaServiceDTO;
	}).toList();
	
	
}
public SpaServiceDTO getoneRecordById(Long spaId) {
	SpaServicese spaServicese= spaservicesRepo.findById(spaId).orElseThrow(() -> new RuntimeException("Spa Service not found with id " + spaId));
	SpaServiceDTO spaServiceDTO = new SpaServiceDTO();
	spaServiceDTO.setServiceIdLong(spaServicese.getServiceId());
	spaServiceDTO.setServiceName(spaServicese.getServiceName());
	spaServiceDTO.setServiceDescription(spaServicese.getServiceDescription());
	spaServiceDTO.setServiceImage(spaServicese.getServiceImage());
		
	spaServiceDTO.setRating(spaServicese.getRating());
	spaServiceDTO.setPrice(spaServicese.getPrice());
	spaServiceDTO.setSpaCenterId(spaServicese.getSpaCenter().getSpaId());
	return spaServiceDTO;
}

public void softDeleteSpaService(Long id) {
    SpaServicese translator = spaservicesRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Translator not found"));

    translator.setStatus("Inactive");
    spaservicesRepo.save(translator);
}


public void activateIfInactive(Long id) {
    SpaServicese spaService = spaservicesRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Spa Service not found"));

    if ("Inactive".equalsIgnoreCase(spaService.getStatus())) {
        spaService.setStatus("Active");
        spaservicesRepo.save(spaService);
        System.out.println("✅ Spa Service status changed to Active.");
    } else {
        System.out.println("ℹ️ Spa Service is already Active.");
    }
}

public SpaServiceDTO updateSpaService(Long spaId, SpaServiceDTO spaServiceDTO) {
    SpaServicese spaService = spaservicesRepo.findById(spaId)
        .orElseThrow(() -> new RuntimeException("Spa Service not found with id " + spaId));

    spaService.setServiceName(spaServiceDTO.getServiceName());
    spaService.setServiceDescription(spaServiceDTO.getServiceDescription());
    spaService.setServiceImage(spaServiceDTO.getServiceImage());
    spaService.setRating(spaServiceDTO.getRating());
    spaService.setPrice(spaServiceDTO.getPrice());

    SpaCenter spaCenter = spaCenterRepo.findById(spaServiceDTO.getSpaCenterId())
        .orElseThrow(() -> new RuntimeException("Spa Center not found with id " + spaServiceDTO.getSpaCenterId()));
    spaService.setSpaCenter(spaCenter);

    SpaServicese updatedSpaService = spaservicesRepo.save(spaService);
    
    SpaServiceDTO updatedDto = new SpaServiceDTO();
    updatedDto.setServiceIdLong(updatedSpaService.getServiceId());
    updatedDto.setServiceName(updatedSpaService.getServiceName());
    updatedDto.setServiceDescription(updatedSpaService.getServiceDescription());
    updatedDto.setServiceImage(updatedSpaService.getServiceImage());
    updatedDto.setRating(updatedSpaService.getRating());
    updatedDto.setPrice(updatedSpaService.getPrice());
    updatedDto.setSpaCenterId(updatedSpaService.getSpaCenter().getSpaId());

    return updatedDto;
}

}
