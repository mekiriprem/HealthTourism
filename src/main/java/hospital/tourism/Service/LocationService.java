package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Dto.DiagnosticsDTO;
import hospital.tourism.Dto.HospitalDTO;
import hospital.tourism.Dto.LocationDTO;
import hospital.tourism.Dto.TranslatorDTO;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.repo.LocationRepo;

@Service
public class LocationService {
	
	
	
	 @Autowired
	    private LocationRepo locationRepository;

	    public LocationEntity saveLocation(LocationEntity locationEntity) {
	        return locationRepository.save(locationEntity);
	    }
	    public LocationDTO getLocationWithHospitals(Integer id) {
	        LocationEntity location = locationRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Location not found"));

	        LocationDTO dto = new LocationDTO();
	        dto.setLocationId(location.getLocationId());
	        dto.setCity(location.getCity());
	        dto.setState(location.getState());
	        dto.setCountry(location.getCountry());

	        // 🏥 Hospitals
	        List<HospitalDTO> hospitalDTOs = location.getHospitalList().stream().map(hospital -> {
	            HospitalDTO hDto = new HospitalDTO();
	            hDto.setHospitalId(hospital.getHospitalId());
	            hDto.setHositalName(hospital.getHositalName());
	            hDto.setHospitalDescription(hospital.getHospitalDescription());
	            hDto.setHospitalImage(hospital.getHospitalImage());
	            hDto.setRating(hospital.getRating());
	            hDto.setAddress(hospital.getAddress());
	            return hDto;
	        }).toList();
	        dto.setHospitals(hospitalDTOs);

	        // 🌍 Translators
	        List<TranslatorDTO> translatorDTOs = location.getTranslatorsList().stream().map(translator -> {
	            TranslatorDTO tDto = new TranslatorDTO();
	            tDto.setTranslatorID(translator.getTranslatorID());
	            tDto.setTranslatorName(translator.getTranslatorName());
	            tDto.setTranslatorDescription(translator.getTranslatorDescription());
	            tDto.setTranslatorImage(translator.getTranslatorImage());
	            tDto.setTranslatorRating(translator.getTranslatorRating());
	            tDto.setTranslatorLanguages(translator.getTranslatorLanguages());
	            return tDto;
	        }).toList();
	        dto.setTranslators(translatorDTOs);

	        // 🧪 Diagnostics
	        List<DiagnosticsDTO> diagnosticsDTOs = location.getDiognsticsList().stream().map(diag -> {
	            DiagnosticsDTO dDto = new DiagnosticsDTO();
	            dDto.setDiognosticsId(diag.getDiognosticsId());
	            dDto.setDiognosticsName(diag.getDiognosticsName());
	            dDto.setDiognosticsDescription(diag.getDiognosticsDescription());
	            dDto.setDiognosticsImage(diag.getDiognosticsImage());
	            dDto.setDiognosticsrating(diag.getDiognosticsrating());
	            dDto.setDiognosticsaddress(diag.getDiognosticsaddress());
	            return dDto;
	        }).toList();
	        dto.setDiagnostics(diagnosticsDTOs);
	        
	        List<ChefDTO> chefDTOs = location.getChefsList().stream().map(chef -> {
	            ChefDTO cDto = new ChefDTO();
	            cDto.setChefID(chef.getChefID());
	            cDto.setChefName(chef.getChefName());
	            cDto.setChefDescription(chef.getChefDescription());
	            cDto.setChefImage(chef.getChefImage());
	            cDto.setChefRating(chef.getChefRating());
	            cDto.setExperience(chef.getExperience());
	            cDto.setStyles(chef.getStyles());
	            return cDto;
	        }).toList();
	        dto.setChefs(chefDTOs);

	        return dto;
	    }
	    
	  
	
	
}
