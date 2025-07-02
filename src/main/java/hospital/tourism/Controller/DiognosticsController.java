package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.DiagnosticsDTO;
import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Service.DiagonosticsServices;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/api/diagnostics")
public class DiognosticsController {

    @Autowired
    private DiagonosticsServices diagonosticsServices;

    // ✅ Add a diagnostics center
    @PostMapping("/add")
    public ResponseEntity<Diognstics> addDiagnostics(
            @RequestParam("diognosticsName") String name,
            @RequestParam("diognosticsDescription") String description,
            @RequestParam("diognosticsrating") String rating,
            @RequestParam("diognosticsaddress") String address,
            @RequestParam("locationId") Integer locationId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        Diognstics d = new Diognstics();
        d.setDiognosticsName(name);
        d.setDiognosticsDescription(description);
        d.setDiognosticsrating(rating);
        d.setDiognosticsaddress(address);

        // Set LocationEntity with just the ID
        LocationEntity location = new LocationEntity();
        location.setLocationId(locationId);
        d.setLocation(location);

        Diognstics saved = diagonosticsServices.saveDiagnostics(d, imageFile);
        return ResponseEntity.ok(saved);
    }

    // 📋 Get all diagnostics centers
    @GetMapping
    public ResponseEntity<List<Diognstics>> getAllDiagnostics() {
        return ResponseEntity.ok(diagonosticsServices.getAllDiagnostics());
    }

    
    // 🔍 Get lab tests by diagnostics ID
    @GetMapping("/{id}")
    public ResponseEntity<List<Labtests>> getLabtestsByDiagnosticsId(@PathVariable("id") Integer diagnosticsId) {
        List<Labtests> tests = diagonosticsServices.getLabtestsByDiagnosticsId(diagnosticsId);
        return ResponseEntity.ok(tests);
    }
    // 🔎 Get diagnostics by ID
    @GetMapping("/get/{id}")
	public ResponseEntity<DiagnosticsDTO> getDiagnosticsById(@PathVariable("id") Integer diagId) {
    	DiagnosticsDTO diagnostics = diagonosticsServices.getdiagnosticbyId(diagId);
		return ResponseEntity.ok(diagnostics);
	}
    
    
    @PutMapping("/soft-delete/{id}")
    public void softDelete(@PathVariable Integer id) {
    	diagonosticsServices.softDeleteDiognstics(id);
    }

    @PutMapping("/activate/{id}")
    public void activate(@PathVariable Integer id) {
    	diagonosticsServices.activateDiognstics(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<DiagnosticsDTO> updateDiagnostics(
            @PathVariable Integer id,
            @RequestParam("diognosticsName") String name,
            @RequestParam("diognosticsDescription") String description,
            @RequestParam("diognosticsrating") String rating,
            @RequestParam("diognosticsaddress") String address,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        DiagnosticsDTO dto = new DiagnosticsDTO();
        dto.setDiognosticsName(name);
        dto.setDiognosticsDescription(description);
        dto.setDiognosticsrating(rating);
        dto.setDiognosticsaddress(address);

        DiagnosticsDTO updated = diagonosticsServices.updateDiagnostics(id, dto, imageFile);
        return ResponseEntity.ok(updated);
    }

}

