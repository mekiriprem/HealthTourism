package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Dto.DiagnosticsDTO;
import hospital.tourism.Entity.Diognstics;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Service.DiagonosticsServices;

@RestController
@RequestMapping("/api/diagnostics")
public class DiognosticsController {

    @Autowired
    private DiagonosticsServices diagonosticsServices;

    // ✅ Add a diagnostics center
    @PostMapping("/add")
    public ResponseEntity<Diognstics> addDiagnostics(@RequestBody Diognstics diognstics) {
        Diognstics saved = diagonosticsServices.saveDiagnostics(diognstics);
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
}

