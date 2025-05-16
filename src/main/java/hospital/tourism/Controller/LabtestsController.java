package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Entity.Labtests;
import hospital.tourism.Service.LabtestsServices;

@RestController
@RequestMapping("/api/labtests")
public class LabtestsController {

    @Autowired
    private LabtestsServices labtestsServices;

    // ✅ Add a new lab test
    @PostMapping("/add")
    public ResponseEntity<Labtests> addLabtest(@RequestBody Map<String, Object> labtestMap) {
        Labtests labtest = new Labtests();
        labtest.setTestTitle((String) labtestMap.get("testTitle"));
        labtest.setTestDescription((String) labtestMap.get("testDescription"));
        labtest.setTestPrice(Double.parseDouble(labtestMap.get("testPrice").toString()));
        labtest.setTestDepartment((String) labtestMap.get("testDepartment"));
        labtest.setTestImage((String) labtestMap.get("testImage"));

        Integer diognosticsId = Integer.parseInt(labtestMap.get("diognosticsId").toString());

        Labtests saved = labtestsServices.saveLabtest(labtest, diognosticsId);
        return ResponseEntity.ok(saved);
    }

    // 📋 Get all lab tests
    @GetMapping
    public ResponseEntity<List<Labtests>> getAllLabtests() {
        return ResponseEntity.ok(labtestsServices.getAllLabtests());
    }

    // 🔍 Get lab test by ID
    @GetMapping("/{id}")
    public ResponseEntity<Labtests> getLabtestById(@PathVariable Long id) {
        return ResponseEntity.ok(labtestsServices.getLabById(id));
    }

    // 🧪 Get lab tests by diagnostics center ID
    @GetMapping("/by-diagnostics/{diognosticsId}")
    public ResponseEntity<List<Labtests>> getLabtestsByDiagnosticsId(@PathVariable Integer diognosticsId) {
        List<Labtests> tests = labtestsServices
            .getAllLabtests()
            .stream()
            .filter(test -> test.getDiognostics().getDiognosticsId().equals(diognosticsId))
            .toList();
        return ResponseEntity.ok(tests);
    }
}
