package hospital.tourism.booking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hospital.tourism.booking.DTO.BookingPackageDTO;
import hospital.tourism.booking.DTO.PackageRequestDTO;
import hospital.tourism.booking.DTO.ServiceItemsDTO;
import hospital.tourism.booking.DTO.ServicePackageDTO;
import hospital.tourism.booking.entity.ServiceItems;
import hospital.tourism.booking.service.AdminServicePackage;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com/"})

@RestController
@RequestMapping("/admin/packege")
public class PackageAdminController {

	
	@Autowired
    private AdminServicePackage adminService;

    @PostMapping("/service")
    public ResponseEntity<ServiceItemsDTO> addServiceItem(@RequestBody ServiceItems item) {
        return ResponseEntity.ok(adminService.addServiceItem(item));
    }

    @PostMapping(value = "/package", consumes = "multipart/form-data")
    public ResponseEntity<ServicePackageDTO> createServicePackage(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int duration,
            @RequestParam("serviceItemIds") List<Long> serviceItemIds,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        ServicePackageDTO createdPackage = adminService.createPackage(name, description, duration, serviceItemIds, imageFile);
        return ResponseEntity.ok(createdPackage);
    }
    
    //get all packages
    @GetMapping("/All/packages")
	public ResponseEntity<List<ServicePackageDTO>> getAllPackages() {
		return ResponseEntity.ok(adminService.getAllServicePackages());
		
	}
    //get all service items
    @GetMapping("/All/service/items")
        public ResponseEntity<List<ServiceItemsDTO>> getAllServiceItems() {
    	        return ResponseEntity.ok(adminService.getAllServiceItems());
    }
    
    //get all service items by id
    @GetMapping("/service/items/{id}")
	public ResponseEntity<ServiceItemsDTO> getServiceItemById(@PathVariable Long id) {
		return ResponseEntity.ok(adminService.getServiceItemById(id));
	}
    //get all packages by id
    @GetMapping("/packages/{id}")
    	    public ResponseEntity<ServicePackageDTO> getPackageById(@PathVariable Long id) {
    	        return ResponseEntity.ok(adminService.getServicePackageById(id));
    
    }
    //update service item
    @PutMapping("/service/items/{id}")
	public ResponseEntity<ServiceItemsDTO> updateServiceItem(@PathVariable Long id, @RequestBody ServiceItems item) {
		return ResponseEntity.ok(adminService.updateServiceItem(id, item));
	}
    
    //update package
    @PutMapping("/packages/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable Long id, @RequestBody PackageRequestDTO dto) {
        try {
            ServicePackageDTO updatedPackage = adminService.updateServicePackage(id, dto);
            return ResponseEntity.ok(updatedPackage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Failed to update package: " + e.getMessage()));
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    //delete service item
    @GetMapping("/service/items/delete/{id}")
	public ResponseEntity<String> deleteServiceItem(@PathVariable Long id) {
		adminService.deleteServiceItem(id);
		return ResponseEntity.ok("Service item deleted successfully");
	}
    //delete package
    @DeleteMapping("/packages/delete/{id}")
        public ResponseEntity<String> deletePackage(@PathVariable Long id) {
    		        adminService.deleteServicePackage(id);
    		                return ResponseEntity.ok("Service package deleted successfully");
    }
    
    //update featured status
    @PutMapping("/packages/featured/{id}")
    public ResponseEntity<ServicePackageDTO> updateFeaturedStatus(
            @PathVariable Long id,
            @RequestParam String featured) {

        ServicePackageDTO updatedDto = adminService.updateFeaturedStatus(id, featured);
        return ResponseEntity.ok(updatedDto);
    }
    
  //get all Booking
    @GetMapping("/bookings")
	public List<BookingPackageDTO> getAllBookings() {
		return adminService.getAllBookings();
	}

	

	
    
}
