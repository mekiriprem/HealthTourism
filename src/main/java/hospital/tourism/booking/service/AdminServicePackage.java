package hospital.tourism.booking.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.booking.DTO.PackageRequestDTO;
import hospital.tourism.booking.DTO.PackageServiceItemDTO;
import hospital.tourism.booking.DTO.ServiceItemsDTO;
import hospital.tourism.booking.DTO.ServicePackageDTO;
import hospital.tourism.booking.entity.PackageServiceItem;
import hospital.tourism.booking.entity.ServiceItems;
import hospital.tourism.booking.entity.ServicePackage;
import hospital.tourism.booking.repo.PackServiceItemRepository;
import hospital.tourism.booking.repo.PackageServiceItemRepository;
import hospital.tourism.booking.repo.ServicePackageRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AdminServicePackage {

	
	 @Autowired
	    private PackServiceItemRepository serviceItemRepo;

	    @Autowired
	    private ServicePackageRepository servicePackageRepo;

	    @Autowired
	    private PackageServiceItemRepository psiRepo;
	    
	    
	    @Value("${supabase.url}")
	    private String supabaseUrl;

	    @Value("${supabase.api.key}")
	    private String supabaseKey;

	    @Value("${supabase.bucket}")
	    private String supabaseBucket;


	    public String uploadFileToSupabase(MultipartFile file, String folderName) {
	        try {
	            // Generate unique and URL-safe file name
	            String rawFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	            String encodedFileName = URLEncoder.encode(rawFileName, StandardCharsets.UTF_8); // ✅ Handles spaces, ( ), etc.
	            byte[] fileBytes = file.getBytes();

	            // Construct the upload URL
	            String uploadUrl = String.format(
	                "%s/storage/v1/object/%s/%s/%s",
	                supabaseUrl, supabaseBucket, folderName, encodedFileName
	            );

	            HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(uploadUrl))
	                .header("apikey", supabaseKey)
	                .header("Authorization", "Bearer " + supabaseKey)
	                .header("Content-Type", file.getContentType())
	                .PUT(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
	                .build();

	            HttpClient client = HttpClient.newHttpClient();
	            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	            if (response.statusCode() == 200 || response.statusCode() == 201) {
	                // Public access URL
	                return String.format(
	                    "%s/storage/v1/object/public/%s/%s/%s",
	                    supabaseUrl, supabaseBucket, folderName, encodedFileName
	                );
	            } else {
	                throw new RuntimeException("Failed to upload file: " + response.body());
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Supabase file upload error: " + e.getMessage(), e);
	        }
	    }
	
	    
	    public ServiceItemsDTO addServiceItem(ServiceItems item) {
	        ServiceItems savedItem = serviceItemRepo.save(item);

	        // Convert entity to DTO
	        ServiceItemsDTO dto = new ServiceItemsDTO();
	        dto.setId(savedItem.getId());
	        dto.setName(savedItem.getName());
	        dto.setDescription(savedItem.getDescription());
	        dto.setType(savedItem.getType());
	        dto.setPrice(savedItem.getPrice());

	        return dto;
	    }

    



//	    public ServicePackageDTO createPackage(String name, String description, int duration, List<Long> serviceItemIds) {
//	        // Step 1: Build base package entity
//	        ServicePackage servicePackage = new ServicePackage();
//	        servicePackage.setName(name);
//	        servicePackage.setDescription(description);
//	        servicePackage.setDurationDays(duration);
//
//	        // Step 2: Calculate total price and prepare link entities
//	        List<PackageServiceItem> packageServiceItems = new ArrayList<>();
//	        double totalPrice = 0.0;
//
//	        for (Long itemId : serviceItemIds) {
//	            ServiceItems serviceItem = serviceItemRepo.findById(itemId)
//	                    .orElseThrow(() -> new RuntimeException("Service item not found with ID: " + itemId));
//
//	            PackageServiceItem psi = new PackageServiceItem();
//	            psi.setServiceItem(serviceItem);
//	            psi.setServicePackage(servicePackage);
//	            packageServiceItems.add(psi);
//	            totalPrice += serviceItem.getPrice();
//	        }
//
//	        if (totalPrice <= 0) {
//	            throw new RuntimeException("Cannot create a package with zero or negative total price.");
//	        }
//
//	        // Step 3: Save base package
//	        servicePackage.setTotalPrice(totalPrice);
//	        ServicePackage savedPackage = servicePackageRepo.save(servicePackage);
//
//	        // Step 4: Save linked items
//	        for (PackageServiceItem psi : packageServiceItems) {
//	            psi.setServicePackage(savedPackage);
//	        }
//
//	        psiRepo.saveAll(packageServiceItems);
//	        savedPackage.setServiceItems(packageServiceItems); // optional
//
//	        // Step 5: Convert to DTO
//	        ServicePackageDTO dto = new ServicePackageDTO();
//	        dto.setId(savedPackage.getId());
//	        dto.setName(savedPackage.getName());
//	        dto.setDescription(savedPackage.getDescription());
//	        dto.setTotalPrice(savedPackage.getTotalPrice());
//	        dto.setDurationDays(savedPackage.getDurationDays());
//
//	        // Convert each PackageServiceItem to DTO
//	        List<PackageServiceItemDTO> psiDTOList = packageServiceItems.stream().map(psi -> {
//	            PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
//	            itemDTO.setId(psi.getId());
//	            itemDTO.setServiceItemId(psi.getServiceItem().getId());
//	            itemDTO.setServicePackageId(savedPackage.getId());
//	            return itemDTO;
//	        }).toList();
//
//	        dto.setServiceItems(psiDTOList);
//	        return dto;
//	    }
//	    
		public List<ServiceItemsDTO> getAllServiceItems() {
			List<ServiceItems> items = serviceItemRepo.findAll();
			List<ServiceItemsDTO> dtos = new ArrayList<>();

			for (ServiceItems item : items) {
				ServiceItemsDTO dto = new ServiceItemsDTO();
				dto.setId(item.getId());
				dto.setName(item.getName());
				dto.setDescription(item.getDescription());
				dto.setType(item.getType());
				dto.setPrice(item.getPrice());
				dtos.add(dto);
			}

			return dtos;
		}

		
		public ServiceItemsDTO getServiceItemById(Long id) {
			ServiceItems item = serviceItemRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Service item not found with ID: " + id));

			ServiceItemsDTO dto = new ServiceItemsDTO();
			dto.setId(item.getId());
			dto.setName(item.getName());
			dto.setDescription(item.getDescription());
			dto.setType(item.getType());
			dto.setPrice(item.getPrice());

			return dto;
		}

		public ServicePackageDTO getServicePackageById(Long id) {
			ServicePackage servicePackage = servicePackageRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Service package not found with ID: " + id));

			ServicePackageDTO dto = new ServicePackageDTO();
			dto.setId(servicePackage.getId());
			dto.setName(servicePackage.getName());
			dto.setDescription(servicePackage.getDescription());
			dto.setTotalPrice(servicePackage.getTotalPrice());
			dto.setDurationDays(servicePackage.getDurationDays());

			// Convert PackageServiceItems to DTOs
			List<PackageServiceItemDTO> psiDTOs = servicePackage.getServiceItems().stream().map(psi -> {
				PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
				itemDTO.setId(psi.getId());
				itemDTO.setServiceItemId(psi.getServiceItem().getId());
				itemDTO.setServicePackageId(servicePackage.getId());
				return itemDTO;
			}).toList();

			dto.setServiceItems(psiDTOs);
			return dto;
		}

		public void deleteServiceItem(Long id) {
			if (!serviceItemRepo.existsById(id)) {
				throw new RuntimeException("Service item not found with ID: " + id);
			}
			serviceItemRepo.deleteById(id);
		}

		public void deleteServicePackage(Long id) {
			if (!servicePackageRepo.existsById(id)) {
				throw new RuntimeException("Service package not found with ID: " + id);
			}
			servicePackageRepo.deleteById(id);
		}

		public ServiceItemsDTO updateServiceItem(Long id, ServiceItems updatedItem) {
			ServiceItems existingItem = serviceItemRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Service item not found with ID: " + id));

			existingItem.setName(updatedItem.getName());
			existingItem.setDescription(updatedItem.getDescription());
			existingItem.setType(updatedItem.getType());
			existingItem.setPrice(updatedItem.getPrice());

			ServiceItems savedItem = serviceItemRepo.save(existingItem);

			ServiceItemsDTO dto = new ServiceItemsDTO();
			dto.setId(savedItem.getId());
			dto.setName(savedItem.getName());
			dto.setDescription(savedItem.getDescription());
			dto.setType(savedItem.getType());
			dto.setPrice(savedItem.getPrice());

			return dto;
		}

		// Update service package
		public ServicePackageDTO updateServicePackage(Long id, PackageRequestDTO dto) {
            ServicePackage existingPackage = servicePackageRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Service package not found with ID: " + id));

            existingPackage.setName(dto.getName());
            existingPackage.setDescription(dto.getDescription());
            existingPackage.setDurationDays(dto.getDurationDays());

            // Clear existing items
            List<PackageServiceItem> existingItems = existingPackage.getServiceItems();
            if (existingItems != null) {
                psiRepo.deleteAll(existingItems);
            }

            // Add new items
            List<PackageServiceItem> packageServiceItems = new ArrayList<>();
            double totalPrice = 0.0;

            for (Long itemId : dto.getServiceItemIds()) {
                ServiceItems serviceItem = serviceItemRepo.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Service item not found with ID: " + itemId));

                PackageServiceItem psi = new PackageServiceItem();
                psi.setServiceItem(serviceItem);
                psi.setServicePackage(existingPackage);
                packageServiceItems.add(psi);
                totalPrice += serviceItem.getPrice();
            }

            if (totalPrice <= 0) {
                throw new RuntimeException("Cannot update package with zero or negative total price.");
            }

            existingPackage.setTotalPrice(totalPrice);
            ServicePackage savedPackage = servicePackageRepo.save(existingPackage);

            // Save new package service items
            for (PackageServiceItem psi : packageServiceItems) {
                psi.setServicePackage(savedPackage);
            }

            psiRepo.saveAll(packageServiceItems);
            savedPackage.setServiceItems(packageServiceItems); // optional

            ServicePackageDTO responseDto = new ServicePackageDTO();
            responseDto.setId(savedPackage.getId());
            responseDto.setName(savedPackage.getName());
            responseDto.setDescription(savedPackage.getDescription());
            responseDto.setTotalPrice(savedPackage.getTotalPrice());
            responseDto.setDurationDays(savedPackage.getDurationDays());

            List<PackageServiceItemDTO> psiDTOList = packageServiceItems.stream().map(psi -> {
                PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
                itemDTO.setId(psi.getId());
                itemDTO.setServiceItemId(psi.getServiceItem().getId());
                itemDTO.setServicePackageId(savedPackage.getId());
                return itemDTO;
                }).toList();
            responseDto.setServiceItems(psiDTOList);
            return responseDto;
                    }
		
		
		//get All Packages
		public List<ServicePackageDTO> getAllServicePackages() {
			List<ServicePackage> packages = servicePackageRepo.findAll();
			List<ServicePackageDTO> dtos = new ArrayList<>();

			for (ServicePackage sp : packages) {
				ServicePackageDTO dto = new ServicePackageDTO();
				dto.setId(sp.getId());
				dto.setName(sp.getName());
				dto.setDescription(sp.getDescription());
				dto.setTotalPrice(sp.getTotalPrice());
				dto.setDurationDays(sp.getDurationDays());

				// Convert PackageServiceItems to DTOs
				List<PackageServiceItemDTO> psiDTOs = sp.getServiceItems().stream().map(psi -> {
					PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
					itemDTO.setId(psi.getId());
					itemDTO.setServiceItemId(psi.getServiceItem().getId());
					itemDTO.setServicePackageId(sp.getId());
					return itemDTO;
				}).toList();

				dto.setServiceItems(psiDTOs);
				dtos.add(dto);
			}

			return dtos;
		}

		
		
		public ServicePackageDTO createPackage(
		        String name,
		        String description,
		        int duration,
		        List<Long> serviceItemIds,
		        MultipartFile imageFile // 👈 Add this for image
		) {
		    // Step 1: Build base package entity
		    ServicePackage servicePackage = new ServicePackage();
		    servicePackage.setName(name);
		    servicePackage.setDescription(description);
		    servicePackage.setDurationDays(duration);

		    // Step 2: Upload image to Supabase
		    if (imageFile != null && !imageFile.isEmpty()) {
		        String imageUrl = uploadFileToSupabase(imageFile, "service_packages");
		        servicePackage.setImageUrl(imageUrl);
		    }

		    servicePackage.setFeatured("yes"); // 👈 Set featured to "yes"

		    // Step 3: Calculate total price and link items
		    List<PackageServiceItem> packageServiceItems = new ArrayList<>();
		    double totalPrice = 0.0;

		    for (Long itemId : serviceItemIds) {
		        ServiceItems serviceItem = serviceItemRepo.findById(itemId)
		                .orElseThrow(() -> new RuntimeException("Service item not found with ID: " + itemId));

		        PackageServiceItem psi = new PackageServiceItem();
		        psi.setServiceItem(serviceItem);
		        psi.setServicePackage(servicePackage);
		        packageServiceItems.add(psi);
		        totalPrice += serviceItem.getPrice();
		    }

		    if (totalPrice <= 0) {
		        throw new RuntimeException("Cannot create a package with zero or negative total price.");
		    }

		    servicePackage.setTotalPrice(totalPrice);
		    ServicePackage savedPackage = servicePackageRepo.save(servicePackage);

		    // Step 4: Save package service items
		    for (PackageServiceItem psi : packageServiceItems) {
		        psi.setServicePackage(savedPackage);
		    }

		    psiRepo.saveAll(packageServiceItems);
		    savedPackage.setServiceItems(packageServiceItems); // optional

		    // Step 5: Convert to DTO
		    ServicePackageDTO dto = new ServicePackageDTO();
		    dto.setId(savedPackage.getId());
		    dto.setName(savedPackage.getName());
		    dto.setDescription(savedPackage.getDescription());
		    dto.setTotalPrice(savedPackage.getTotalPrice());
		    dto.setDurationDays(savedPackage.getDurationDays());
		    dto.setImageUrl(savedPackage.getImageUrl());
		    dto.setFeatured(savedPackage.getFeatured());

		    List<PackageServiceItemDTO> psiDTOList = packageServiceItems.stream().map(psi -> {
		        PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
		        itemDTO.setId(psi.getId());
		        itemDTO.setServiceItemId(psi.getServiceItem().getId());
		        itemDTO.setServicePackageId(savedPackage.getId());
		        return itemDTO;
		    }).toList();

		    dto.setServiceItems(psiDTOList);
		    return dto;
		}
		
		//update featured status
		public ServicePackageDTO updateFeaturedStatus(Long id) {
			ServicePackage existingPackage = servicePackageRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Service package not found with ID: " + id));

			existingPackage.setFeatured("No");
			ServicePackage savedPackage = servicePackageRepo.save(existingPackage);
			ServicePackageDTO dto = new ServicePackageDTO();
			dto.setId(savedPackage.getId());
			dto.setName(savedPackage.getName());
			dto.setDescription(savedPackage.getDescription());
			dto.setTotalPrice(savedPackage.getTotalPrice());
			dto.setDurationDays(savedPackage.getDurationDays());
			dto.setImageUrl(savedPackage.getImageUrl());
			dto.setFeatured(savedPackage.getFeatured());
			// Convert PackageServiceItems to DTOs
			List<PackageServiceItemDTO> psiDTOs = savedPackage.getServiceItems().stream().map(psi -> {
				PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
				itemDTO.setId(psi.getId());
				itemDTO.setServiceItemId(psi.getServiceItem().getId());
				itemDTO.setServicePackageId(savedPackage.getId());
				return itemDTO;
			}).toList();
			dto.setServiceItems(psiDTOs);
			return dto;
		}


	    
}
