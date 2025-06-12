package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.DiagnosticsDTO;
import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.repo.DiagnosticsRepo;


@Service
public class DiagonosticsServices {

    @Autowired
    private DiagnosticsRepo diagnosticsRepo;

    @Autowired
    private hospital.tourism.repo.labtestsRepo labtestsRepo;

    // Save a diagnostics center
    public Diognstics saveDiagnostics(Diognstics diognstics) {
        return diagnosticsRepo.save(diognstics);
    }

    // Get all diagnostics centers
    public List<Diognstics> getAllDiagnostics() {
        return diagnosticsRepo.findAll();
    }

    // Get lab tests by diagnostics ID
    public List<Labtests> getLabtestsByDiagnosticsId(Integer diognosticsId) {
        return labtestsRepo.findByDiognostics_DiognosticsId(diognosticsId);
    }
    
    //get diagnostics by ID
    public DiagnosticsDTO getdiagnosticbyId(Integer diagId) {
		Diognstics diognstics = diagnosticsRepo.findById(diagId)
				.orElseThrow(() -> new RuntimeException("Diagnostics not found"));

		DiagnosticsDTO dto = new DiagnosticsDTO();
		dto.setDiognosticsId(diognstics.getDiognosticsId());
		dto.setDiognosticsName(diognstics.getDiognosticsName());
		dto.setDiognosticsDescription(diognstics.getDiognosticsDescription());
		dto.setDiognosticsImage(diognstics.getDiognosticsImage());
		dto.setDiognosticsrating(diognstics.getDiognosticsrating());
		dto.setDiognosticsaddress(diognstics.getDiognosticsaddress());

		return dto;
    }
}

