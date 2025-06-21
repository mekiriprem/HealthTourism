package hospital.tourism.booking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.booking.DTO.PackageRequestDTO;
import hospital.tourism.booking.DTO.ServiceItemsDTO;
import hospital.tourism.booking.DTO.ServicePackageDTO;
import hospital.tourism.booking.entity.ServiceItems;
import hospital.tourism.booking.service.AdminServicePackage;




@RestController
@RequestMapping("/admin/packege")
public class PackageAdminController {

	
	@Autowired
    private AdminServicePackage adminService;

    @PostMapping("/service")
    public ResponseEntity<ServiceItemsDTO> addServiceItem(@RequestBody ServiceItems item) {
        return ResponseEntity.ok(adminService.addServiceItem(item));
    }

    @PostMapping("/package")
    public ResponseEntity<ServicePackageDTO> createPackage(@RequestBody PackageRequestDTO dto) {
        return ResponseEntity.ok(adminService.createPackage(
                dto.getName(),
                dto.getDescription(),
                dto.getDurationDays(),
                dto.getServiceItemIds()
        ));
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
        public ResponseEntity<ServicePackageDTO> updatePackage(@PathVariable Long id, @RequestBody PackageRequestDTO dto) {
    	adminService.updateServicePackage(id, dto);
		return ResponseEntity.ok(adminService.getServicePackageById(id));
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
    
}
