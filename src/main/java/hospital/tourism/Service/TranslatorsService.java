// package: hospital.tourism.Service

package hospital.tourism.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.TranslatorDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.Translators;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.TranslatorsRepo;

@Service
public class TranslatorsService {

    @Autowired
    private TranslatorsRepo translatorsRepo;

    @Autowired
    private LocationRepo locationRepo;

    // Save translator
    public Translators saveTranslator(Translators translator, Integer locationId) {
        LocationEntity location = locationRepo.findById(locationId)
            .orElseThrow(() -> new RuntimeException("❌ Location not found with id: " + locationId));

        translator.setLocation(location);
        return translatorsRepo.save(translator);
    }


    // Get all translators by locationId
    public List<Translators> getTranslatorsByLocationId(Integer locationId) {
        return translatorsRepo.findByLocation_LocationId(locationId);
    }

    public List<TranslatorDTO> getAllTranslators() {
        List<Translators> translators = translatorsRepo.findAll();

        return translators.stream().map(translator -> {
            TranslatorDTO dto = new TranslatorDTO();
            dto.setTranslatorID(translator.getTranslatorID());
            dto.setTranslatorName(translator.getTranslatorName());
            dto.setTranslatorDescription(translator.getTranslatorDescription());
            dto.setTranslatorImage(translator.getTranslatorImage());
            dto.setTranslatorRating(translator.getTranslatorRating());
            dto.setTranslatorLanguages(translator.getTranslatorLanguages());
            dto.setStatus(translator.getStatus());
            dto.setPrice(translator.getPrice());
			dto.setTranslatorLocIdInteger(translator.getTranslatorLocIdInteger());
			dto.setTranslatorAddress(translator.getTranslatorAddress());

			
            return dto;
        }).toList();
    }

    public TranslatorDTO getbytraslatorId(Long trId) {
    	Optional<Translators> translators= translatorsRepo.findById(trId);
    	TranslatorDTO dto = new TranslatorDTO();
    	dto.setTranslatorID(translators.get().getTranslatorID());
    	dto.setTranslatorName(translators.get().getTranslatorName());
    	dto.setTranslatorDescription(translators.get().getTranslatorDescription());
    	dto.setTranslatorImage(translators.get().getTranslatorImage());
    	dto.setTranslatorRating(translators.get().getTranslatorRating());
    	dto.setTranslatorLanguages(translators.get().getTranslatorLanguages());
    	dto.setStatus(translators.get().getStatus());
    	dto.setPrice(translators.get().getPrice());
    	dto.setTranslatorLocIdInteger(translators.get().getTranslatorLocIdInteger());
    	dto.setTranslatorAddress(translators.get().getTranslatorAddress());
    	return dto;
    }
    
    
    public void softDeleteTranslator(Long id) {
        Translators translator = translatorsRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Translator not found"));

        translator.setStatus("Inactive");
        translatorsRepo.save(translator);
    }
    
    public void activateTranslatorIfInactive(Long id) {
        Translators translator = translatorsRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Translator not found"));

        if ("Inactive".equalsIgnoreCase(translator.getStatus())) {
            translator.setStatus("Active");
            translatorsRepo.save(translator);
        }
    }
    
    public TranslatorDTO updateTraslator(Long traslatorId,TranslatorDTO transeDto) {
		Optional<Translators> translators = translatorsRepo.findById(traslatorId);
		if (translators.isPresent()) {
			Translators translator = translators.get();
			translator.setTranslatorName(transeDto.getTranslatorName());
			translator.setTranslatorDescription(transeDto.getTranslatorDescription());
			translator.setTranslatorImage(transeDto.getTranslatorImage());
			translator.setTranslatorRating(transeDto.getTranslatorRating());
			translator.setTranslatorLanguages(transeDto.getTranslatorLanguages());
			translator.setStatus(transeDto.getStatus());
			translator.setPrice(transeDto.getPrice());
			translator.setTranslatorLocIdInteger(transeDto.getTranslatorLocIdInteger());
			translator.setTranslatorAddress(transeDto.getTranslatorAddress());

			translatorsRepo.save(translator);

			return transeDto;
		}
		return null;
    }


}

