package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.repo.DiagnosticsRepo;


@Service
public class LabtestsServices {

    @Autowired
    private hospital.tourism.repo.labtestsRepo labtestsRepo;

    @Autowired
    private DiagnosticsRepo diagnosticsRepo;

    // Save a lab test and assign it to a diagnostics center
    public Labtests saveLabtest(Labtests labtests, Integer diognosticsId) {
        Diognstics diognstics = diagnosticsRepo.findById(diognosticsId)
                .orElseThrow(() -> new RuntimeException("Diagnostics center not found with ID: " + diognosticsId));
        labtests.setDiognostics(diognstics); // ✅ set correct relation
        return labtestsRepo.save(labtests);
    }

    // Get all lab tests
    public List<Labtests> getAllLabtests() {
        return labtestsRepo.findAll();
    }

    // Get lab test by ID
    public Labtests getLabById(Long id) {
        return labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));
    }
    
    public void softDeleteLabtest(Long id) {
        Labtests labtest = labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));

        labtest.setStatus("Inactive");
        labtestsRepo.save(labtest);
    }

    // Activate lab test if inactive
    public void activateLabtestIfInactive(Long id) {
        Labtests labtest = labtestsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with ID: " + id));

        if ("Inactive".equalsIgnoreCase(labtest.getStatus())) {
            labtest.setStatus("Active");
            labtestsRepo.save(labtest);
        }
    }
}
