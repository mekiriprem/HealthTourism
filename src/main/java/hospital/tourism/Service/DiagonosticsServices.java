package hospital.tourism.Service;

import java.util.List;

import javax.tools.Diagnostic;

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
    
    
    // Soft delete
    public void softDeleteDiognstics(Integer id) {
        Diognstics entity = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diognstics not found with ID: " + id));
        entity.setStatus("Inactive");
        diagnosticsRepo.save(entity);
    }

    // Reactivate
    public void activateDiognstics(Integer id) {
        Diognstics entity = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diognstics not found with ID: " + id));
        if ("Inactive".equalsIgnoreCase(entity.getStatus())) {
            entity.setStatus("Active");
            diagnosticsRepo.save(entity);
        }
    }
    // Update diagnostics
    
    public DiagnosticsDTO updateDiagnostics(Integer id, DiagnosticsDTO dto) {
        // Step 1: Get existing entity from DB
        Diognstics existing = diagnosticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnostics not found with ID: " + id));

        // Step 2: Update entity fields
        existing.setDiognosticsName(dto.getDiognosticsName());
        existing.setDiognosticsDescription(dto.getDiognosticsDescription());
        existing.setDiognosticsImage(dto.getDiognosticsImage());
        existing.setDiognosticsrating(dto.getDiognosticsrating());
        existing.setDiognosticsaddress(dto.getDiognosticsaddress());

        // Step 3: Save updated entity
        Diognstics updated = diagnosticsRepo.save(existing);

        // Step 4: Convert updated entity back to DTO
        DiagnosticsDTO updatedDto = new DiagnosticsDTO();
        updatedDto.setDiognosticsId(updated.getDiognosticsId());
        updatedDto.setDiognosticsName(updated.getDiognosticsName());
        updatedDto.setDiognosticsDescription(updated.getDiognosticsDescription());
        updatedDto.setDiognosticsImage(updated.getDiognosticsImage());
        updatedDto.setDiognosticsrating(updated.getDiognosticsrating());
        updatedDto.setDiognosticsaddress(updated.getDiognosticsaddress());

        return updatedDto;
    }

}

