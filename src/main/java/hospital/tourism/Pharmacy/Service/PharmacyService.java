package hospital.tourism.Pharmacy.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.Pharmacy.DTO.PharmacyDashboardResponse;
import hospital.tourism.Pharmacy.Entity.PharmacyEntity;
import hospital.tourism.Pharmacy.Repository.ImadicineRepository;

@Service
public class PharmacyService {
@Autowired
	private ImadicineRepository imadicineRepository;

	@Value("${supabase.url}")
	private String supabaseProjectUrl;

	@Value("${supabase.bucket}")
	private String supabaseBucketName;
	
	@Value("${supabase.api.key}")
		private String supabaseApiKey;

		//add madicines
	// ✅ Save medicine with optional image upload
	public PharmacyEntity addMadicine(PharmacyEntity pharmacyEntity, MultipartFile imageFile) {
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String imageUrl = uploadToSupabase(imageFile);
            pharmacyEntity.setMedicineImage(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload medicine image", e);
        }
    }

    return imadicineRepository.save(pharmacyEntity);
}

// ✅ Upload image to Supabase and return public URL
private String uploadToSupabase(MultipartFile file) throws Exception {
    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    String uploadUrl = supabaseProjectUrl + "/storage/v1/object/" + supabaseBucketName + "/" + fileName;

    HttpPost post = new HttpPost(uploadUrl);
    post.setHeader("apikey", supabaseApiKey);
    post.setHeader("Authorization", "Bearer " + supabaseApiKey);
    post.setHeader("Content-Type", file.getContentType());

    try (InputStream inputStream = file.getInputStream();
         CloseableHttpClient client = HttpClients.createDefault()) {

        post.setEntity(new InputStreamEntity(inputStream));
        HttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200 && statusCode != 201) {
            throw new RuntimeException("Supabase upload failed with status code: " + statusCode);
        }
    }

    return supabaseProjectUrl + "/storage/v1/object/public/" + supabaseBucketName + "/" + fileName;
}
		//get All Madicines
		public List<PharmacyEntity> getAllMadicines() {
			return imadicineRepository.findAll();
		}
		
		//Delete Madicine
		public String  deleteMadicine(Integer madicineId) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				imadicineRepository.deleteById(madicineId);
				return "Madicine deleted Sucessfully with Id : " + madicineId;
			}
			else {
				return "Madicine not found with id: " + madicineId;
			}
			
		}
		public PharmacyEntity updateMadicine(Integer madiceid, PharmacyEntity pharmacyEntity, MultipartFile imageFile) {
		    Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madiceid);

		    if (optionalMadicine.isPresent()) {
		        PharmacyEntity existingMadicine = optionalMadicine.get();

		        existingMadicine.setMedicineName(pharmacyEntity.getMedicineName());
		        existingMadicine.setMedicineType(pharmacyEntity.getMedicineType());
		        existingMadicine.setMedicineDescription(pharmacyEntity.getMedicineDescription());
		        existingMadicine.setMedicinePrice(pharmacyEntity.getMedicinePrice());
		        existingMadicine.setMedicineQuantity(pharmacyEntity.getMedicineQuantity());
		        existingMadicine.setMedicineExpiryDate(pharmacyEntity.getMedicineExpiryDate());
		        existingMadicine.setMedicineManufacturer(pharmacyEntity.getMedicineManufacturer());
		        existingMadicine.setMedicineCategory(pharmacyEntity.getMedicineCategory());

		        if (imageFile != null && !imageFile.isEmpty()) {
		            try {
		                String newImageUrl = uploadToSupabase(imageFile);
		                existingMadicine.setMedicineImage(newImageUrl);
		            } catch (Exception e) {
		                throw new RuntimeException("Failed to upload new medicine image", e);
		            }
		        }

		        return imadicineRepository.save(existingMadicine);
		    } else {
		        throw new RuntimeException("Medicine not found with ID: " + madiceid);
		    }
		}

		// Get Madicine by Id
		public PharmacyEntity getMadicineById(Integer madicineId) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				return optionalMadicine.get();
			} else {
				throw new RuntimeException("Madicine not found with id: " + madicineId);
			}
		}
		
		//update the date 
		
		public PharmacyEntity updateMadicineDate(Integer madicineId, LocalDateTime newExpiryDate) {
			Optional<PharmacyEntity> optionalMadicine = imadicineRepository.findById(madicineId);
			if (optionalMadicine.isPresent()) {
				PharmacyEntity existingMadicine = optionalMadicine.get();
				existingMadicine.setMedicineExpiryDate(newExpiryDate);
				return imadicineRepository.save(existingMadicine);
			} else {
				throw new RuntimeException("Madicine not found with id: " + madicineId);
			}
		}
		
		
	    // Method to get medicines with low stock (less than 10)
		public List<PharmacyEntity> getLowStockMedicines() {
		    return imadicineRepository.findByMedicineQuantityLessThan(100);
		}
		
		
	
		public List<PharmacyEntity> getExpiredMedicines() {
		    return imadicineRepository.findByMedicineExpiryDateBefore(LocalDateTime.now());
		}

		// Method to get medicines that are near expiry (within the next 30 days)
		public List<PharmacyEntity> getNearExpiryMedicines() {
		    LocalDateTime now = LocalDateTime.now();
		    LocalDateTime in30Days = now.plusDays(30);
		    return imadicineRepository.findByMedicineExpiryDateBetween(now, in30Days);
		}

		  public PharmacyDashboardResponse getPharmacyDashboard() {
		        PharmacyDashboardResponse response = new PharmacyDashboardResponse();

		        LocalDateTime now = LocalDateTime.now();
		        LocalDateTime in30Days = now.plusDays(30);

		        response.setAllMedicines(imadicineRepository.findAll());
		        response.setLowStockMedicines(imadicineRepository.findByMedicineQuantityLessThan(100));
		        response.setExpiredMedicines(imadicineRepository.findByMedicineExpiryDateBefore(now));
		        response.setNearExpiryMedicines(imadicineRepository.findByMedicineExpiryDateBetween(now, in30Days));

		        return response;
		    }
		  //show top 5 medicines by price
		  
			public List<PharmacyEntity> getTop5MedicinesByPrice() {
            return imadicineRepository.findTop5ByOrderByMedicinePriceDesc();
			}
}
