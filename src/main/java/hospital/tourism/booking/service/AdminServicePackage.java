package hospital.tourism.booking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    
//	    public ServicePackage createPackage(String name, String description, int duration, List<Long> serviceItemIds) {
//	        // Step 1: Initialize the service package without totalPrice and serviceItems for now
//	        ServicePackage servicePackage = new ServicePackage();
//	        servicePackage.setName(name);
//	        servicePackage.setDescription(description);
//	        servicePackage.setDurationDays(duration);
//	        
//	         // Initialize to zero, will be updated later
//
//	        // Step 2: Fetch all service items and calculate the total price
//	        List<PackageServiceItem> packageServiceItems = new ArrayList<>();
//	        double totalPrice = 0.0;
//
//	        for (Long itemId : serviceItemIds) {
//	            ServiceItems serviceItem = serviceItemRepo.findById(itemId)
//	                    .orElseThrow(() -> new RuntimeException("Service item not found with ID: " + itemId));
//
//	            // Create link entity
//	            PackageServiceItem psi = new PackageServiceItem();
//	            psi.setServiceItem(serviceItem);
//	            psi.setServicePackage(servicePackage); // Will be saved later
//
//	            packageServiceItems.add(psi);
//	            totalPrice += serviceItem.getPrice();
//	        }
//
//	        // Step 3: Validate total price
//	        if (totalPrice <= 0) {
//	            throw new RuntimeException("Cannot create a package with zero or negative total price.");
//	        }
//
//	        // Step 4: Set total price and persist package (without serviceItems for now)
//	        servicePackage.setTotalPrice(totalPrice);
//	        ServicePackage savedPackage = servicePackageRepo.save(servicePackage);
//
//	        // Step 5: Update each link with saved package reference and save all
//	        for (PackageServiceItem psi : packageServiceItems) {
//	            psi.setServicePackage(savedPackage);
//	        }
//
//	        psiRepo.saveAll(packageServiceItems);
//
//	        // Step 6: (Optional) Set back the list to return the full object with items
//	        savedPackage.setServiceItems(packageServiceItems);
//
//	        return savedPackage;
//	    }


	    public ServicePackageDTO createPackage(String name, String description, int duration, List<Long> serviceItemIds) {
	        // Step 1: Build base package entity
	        ServicePackage servicePackage = new ServicePackage();
	        servicePackage.setName(name);
	        servicePackage.setDescription(description);
	        servicePackage.setDurationDays(duration);

	        // Step 2: Calculate total price and prepare link entities
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

	        // Step 3: Save base package
	        servicePackage.setTotalPrice(totalPrice);
	        ServicePackage savedPackage = servicePackageRepo.save(servicePackage);

	        // Step 4: Save linked items
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

	        // Convert each PackageServiceItem to DTO
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

		public List<ServicePackageDTO> getAllServicePackages() {
			List<ServicePackage> packages = servicePackageRepo.findAll();
			List<ServicePackageDTO> dtos = new ArrayList<>();

			for (ServicePackage pkg : packages) {
				ServicePackageDTO dto = new ServicePackageDTO();
				dto.setId(pkg.getId());
				dto.setName(pkg.getName());
				dto.setDescription(pkg.getDescription());
				dto.setTotalPrice(pkg.getTotalPrice());
				dto.setDurationDays(pkg.getDurationDays());

				// Convert PackageServiceItems to DTOs
				List<PackageServiceItemDTO> psiDTOs = pkg.getServiceItems().stream().map(psi -> {
					PackageServiceItemDTO itemDTO = new PackageServiceItemDTO();
					itemDTO.setId(psi.getId());
					itemDTO.setServiceItemId(psi.getServiceItem().getId());
					itemDTO.setServicePackageId(pkg.getId());
					return itemDTO;
				}).toList();

				dto.setServiceItems(psiDTOs);
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

		public ServicePackageDTO updateServicePackage(Long id, PackageRequestDTO dto2) {
			ServicePackage existingPackage = servicePackageRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Service package not found with ID: " + id));

			existingPackage.setName(dto2.getName());
			existingPackage.setDescription(dto2.getDescription());
			existingPackage.setDurationDays(dto2.getDurationDays());

			// Recalculate total price
			double totalPrice = 0.0;
			for (PackageServiceItem psi : existingPackage.getServiceItems()) {
				totalPrice += psi.getServiceItem().getPrice();
			}
			existingPackage.setTotalPrice(totalPrice);

			ServicePackage savedPackage = servicePackageRepo.save(existingPackage);

			ServicePackageDTO dto = new ServicePackageDTO();
			dto.setId(savedPackage.getId());
			dto.setName(savedPackage.getName());
			dto.setDescription(savedPackage.getDescription());
			dto.setTotalPrice(savedPackage.getTotalPrice());
			dto.setDurationDays(savedPackage.getDurationDays());

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
