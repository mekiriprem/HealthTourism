package hospital.tourism.Controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Dto.HospitalDTO;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Hospital;
import hospital.tourism.Service.HospitalService;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})
@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Value("${supabase.url}")
    private String supabaseProjectUrl;

    @Value("${supabase.bucket}")
    private String supabaseBucketName;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @Autowired
    private HospitalService hospitalService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addHospital(
            @RequestParam("hospitalName") String name,
            @RequestParam("hospitalDescription") String description,
            @RequestParam("hospitalRating") String ratingStr,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("hospitalAdress") String address,
            @RequestPart("image") MultipartFile imageFile,
            @RequestParam("specialization") String specialization
    ) {
        try {
            // Validations
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Hospital name is required");
            }
            if (locationId == null) {
                return ResponseEntity.badRequest().body("Location ID is required");
            }
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is required");
            }

            // Parse rating
            Double rating;
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 5) {
                    return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid rating format");
            }            // Build Hospital entity
            Hospital hospital = new Hospital();
            hospital.setHospitalName(name);  // Fixed: Capital H in Hospital
            hospital.setHospitalDescription(description);
            hospital.setRating(ratingStr);
            hospital.setAddress(address);
            hospital.setStatus("Active");
            hospital.setSpecialization(specialization); // Assuming specialization is passed as a parameter);

            // Upload image to Supabase
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/hospital-images/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
            ResponseEntity<String> uploadResponse = new RestTemplate().exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image to Supabase: " + uploadResponse.getStatusCode());
            }

            String publicImageUrl = supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/hospital-images/" + fileName;
            hospital.setHospitalImage(publicImageUrl);

            Hospital savedHospital = hospitalService.saveHospital(hospital, locationId);
            return ResponseEntity.ok(savedHospital);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving hospital: " + e.getMessage());
        }
    }
    @GetMapping("/getall/hospitals/active")
    public List<HospitalDTO> getAllHospitals() {
        System.out.println("Controller: getAllHospitals called");
        List<HospitalDTO> result = hospitalService.getAllHospitalsAsDto();
        System.out.println("Controller: Returning " + result.size() + " hospitals");
        return result;
    }

    @GetMapping("/getall/hospitals")
	public ResponseEntity<List<HospitalDTO>> getAllRawHospitals() {
			List<HospitalDTO> hospitals = hospitalService.getAllHospitalss();
			return ResponseEntity.ok(hospitals);
		
	}
    // Debug endpoint to check raw data
    @GetMapping("/debug/raw")
    public ResponseEntity<?> debugRawHospitals() {
        try {
            List<Hospital> rawHospitals = hospitalService.getAllRawHospitals();
            return ResponseEntity.ok(rawHospitals);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    
    @GetMapping("/{hospitalId}")
    public List<Doctors> getDoctorsByHospital(@PathVariable Integer hospitalId) {
        return hospitalService.getDoctorsByHospitalId(hospitalId);
    }
    
    
    @PutMapping("/soft-delete/{id}")
    public void softDeleteHospital(@PathVariable Integer id) {
        hospitalService.softDeleteHospital(id);
    }

    @PutMapping("/activate/{id}")
    public void activateHospital(@PathVariable Integer id) {
        hospitalService.activateHospitalIfInactive(id);
    }    @PutMapping("/update-hospital/{id}")
	public ResponseEntity<HospitalDTO> updateHospital(@PathVariable Integer id, @RequestBody HospitalDTO hospitalDto) {
		System.out.println("=== UPDATE HOSPITAL REQUEST ===");
		System.out.println("Path variable ID: " + id);
		System.out.println("ID type: " + (id != null ? id.getClass().getSimpleName() : "null"));
		System.out.println("Request body: " + hospitalDto);
		System.out.println("Request body hospitalId: " + (hospitalDto != null ? hospitalDto.getHospitalId() : "null"));
		System.out.println("===============================");
				if (id == null) {
			System.out.println("ERROR: Path variable ID is null!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try {
			HospitalDTO updatedHospital = hospitalService.updateHospital(id, hospitalDto);
			if (updatedHospital != null) {
				System.out.println("Successfully updated hospital: " + updatedHospital.getHospitalId());
				return new ResponseEntity<>(updatedHospital, HttpStatus.OK);
			} else {
				System.out.println("Update returned null - hospital not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println("ERROR in updateHospital: " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @GetMapping("/by-city-specialization")
    public ResponseEntity<List<HospitalDTO>> getByCityAndSpecialization(
        @RequestParam String city,
        @RequestParam String specialization) {
        return ResponseEntity.ok(hospitalService.getHospitalsByCityAndSpecialization(city, specialization));
    }
    
    @GetMapping("/by-city/{city}")
    public ResponseEntity<List<Hospital>> getByCity(@PathVariable String city) {
        return ResponseEntity.ok(hospitalService.getHospitalsByCitys(city));
    }
    @GetMapping("/search-by-location")
    public ResponseEntity<List<HospitalDTO>> searchByCityOrState(@RequestParam String location) {
        List<HospitalDTO> result = hospitalService.searchHospitalsByCityOrState(location);
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/by-specialization/{specialization}")
//    public ResponseEntity<List<HospitalDTO>> getHospitalsBySpecialization(@PathVariable String specialization) {
//        List<HospitalDTO> hospitals = hospitalService.getHospitalsBySpecilization(specialization);
//        return ResponseEntity.ok(hospitals);
//    }

    
    
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Hospital>> getHospitalsBySpecialization(@PathVariable String specialization) {
        List<Hospital> hospitals = hospitalService.getActiveHospitalsBySpecialization(specialization);
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }
    
  

}