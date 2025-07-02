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

import hospital.tourism.Dto.LabtestsDTO;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Service.LabtestsServices;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/api/labtests")
public class LabtestsController {
	

    @Autowired
    private LabtestsServices labtestsServices;

    // ✅ Add a new lab test
    @PostMapping("/add")
    public ResponseEntity<Labtests> addLabtest(
            @RequestParam("testTitle") String testTitle,
            @RequestParam("testDescription") String testDescription,
            @RequestParam("testPrice") double testPrice,
            @RequestParam("testDepartment") String testDepartment,
            @RequestParam("diognosticsId") Integer diognosticsId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        Labtests labtest = new Labtests();
        labtest.setTestTitle(testTitle);
        labtest.setTestDescription(testDescription);
        labtest.setTestPrice(testPrice);
        labtest.setTestDepartment(testDepartment);
        labtest.setStatus("Active");

        // Image will be handled in service
        Labtests saved = labtestsServices.saveLabtest(labtest, imageFile, diognosticsId);
        return ResponseEntity.ok(saved);
    }


    // 📋 Get all lab tests
    @GetMapping("/all")
    public ResponseEntity<List<LabtestsDTO>> getAllLabtests() {
        System.out.println("=== GET ALL LABTESTS REQUEST ===");
        try {
            List<LabtestsDTO> labtests = labtestsServices.getAllLabtestsDTO();
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
    public ResponseEntity<LabtestsDTO> getLabtestById(@PathVariable Long id) {
        return ResponseEntity.ok(labtestsServices.getLabByIdDTO(id));
    }

 // 🧪 Get lab tests by diagnostics center ID
    @GetMapping("/by-diagnostics/{diognosticsId}")
    public ResponseEntity<List<LabtestsDTO>> getLabtestsByDiagnosticsId(@PathVariable Integer diognosticsId) {
        List<LabtestsDTO> tests = labtestsServices
            .getAllLabtestsDTO()
            .stream()
            .filter(test -> test.getDiagnosticsId() != null && test.getDiagnosticsId().equals(diognosticsId))
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
    public ResponseEntity<Labtests> updateLabtest(
            @PathVariable Long id,
            @RequestParam("testTitle") String testTitle,
            @RequestParam("testDescription") String testDescription,
            @RequestParam("testPrice") double testPrice,
            @RequestParam("testDepartment") String testDepartment,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        System.out.println("=== UPDATE LABTEST REQUEST ===");
        System.out.println("Path variable ID: " + id);
        System.out.println("Title: " + testTitle);
        System.out.println("Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "No Image"));
        System.out.println("==============================");

        try {
            Labtests updatedLabtest = new Labtests();
            updatedLabtest.setTestTitle(testTitle);
            updatedLabtest.setTestDescription(testDescription);
            updatedLabtest.setTestPrice(testPrice);
            updatedLabtest.setTestDepartment(testDepartment);
            updatedLabtest.setStatus(status);

            Labtests saved = labtestsServices.updateLabtest(id, updatedLabtest, imageFile);
            System.out.println("Successfully updated labtest: " + saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.out.println("ERROR in updateLabtest: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
