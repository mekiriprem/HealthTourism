package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("=== GET ALL LABTESTS REQUEST ===");
        try {
            List<Labtests> labtests = labtestsServices.getAllLabtests();
            System.out.println("Found " + labtests.size() + " lab tests");
            return ResponseEntity.ok(labtests);
        } catch (Exception e) {
            System.out.println("ERROR in getAllLabtests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
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
    @PutMapping("/soft-delete/{id}")
    public void softDeleteLabtest(@PathVariable Long id) {
    	labtestsServices.softDeleteLabtest(id);
    }

    // Reactivate endpoint
    @PutMapping("/activate/{id}")
    public void activateLabtest(@PathVariable Long id) {
    	labtestsServices.activateLabtestIfInactive(id);
    }
    // Update lab test
    @PutMapping("/update/{id}")
	public ResponseEntity<Labtests> updateLabtest(@PathVariable Long id, @RequestBody Labtests updatedLabtest) {
		System.out.println("=== UPDATE LABTEST REQUEST ===");
		System.out.println("Path variable ID: " + id);
		System.out.println("Request body: " + updatedLabtest);
		System.out.println("==============================");
		
		try {
			Labtests labtest = labtestsServices.updateLabtest(id, updatedLabtest);
			System.out.println("Successfully updated labtest: " + labtest.getId());
			return ResponseEntity.ok(labtest);
		} catch (Exception e) {
			System.out.println("ERROR in updateLabtest: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
}
