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

import hospital.tourism.Entity.ServiceItem;
import hospital.tourism.Entity.UserServiceBooking;
import hospital.tourism.Service.AdminServicePackages;
import hospital.tourism.Service.UserPackageBookingService;



@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class PackageBookingController {

	@Autowired
	private  AdminServicePackages adminService;
	@Autowired
    private  UserPackageBookingService userBookingService;

    // Admin endpoints
    @PostMapping("/admin/service")
    public ResponseEntity<ServiceItem> createService(@RequestBody ServiceItem item) {
        return ResponseEntity.ok(adminService.createService(item));
    }

    
    
    @GetMapping("/admin/services/getAll")
    public ResponseEntity<List<ServiceItem>> getAllServices() {
        return ResponseEntity.ok(adminService.getAllServices());
    }

    @PutMapping("/admin/service/{id}")
    public ResponseEntity<ServiceItem> updateService(@PathVariable Long id, @RequestBody ServiceItem item) {
        return ResponseEntity.ok(adminService.updateService(id, item));
    }

    // User endpoints
    @PostMapping("/user/book")
    public ResponseEntity<UserServiceBooking> book(@PathVariable Long serviceId,
                                                   @PathVariable int days,
                                                   @PathVariable Long userId) {
        return ResponseEntity.ok(userBookingService.bookService(serviceId, days, userId));
    }

    @GetMapping("/user/bookings")
    public ResponseEntity<List<UserServiceBooking>> getBookings(@RequestParam Long userId) {
        return ResponseEntity.ok(userBookingService.getUserBookings(userId));
    }
}
