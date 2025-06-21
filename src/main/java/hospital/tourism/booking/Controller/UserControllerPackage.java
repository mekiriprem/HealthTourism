package hospital.tourism.booking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.booking.DTO.BookingPackageDTO;
import hospital.tourism.booking.DTO.ServicePackageDTO;
import hospital.tourism.booking.service.UserPackageService;


@RestController
@RequestMapping("/user/package")
public class UserControllerPackage {

	
	@Autowired
    private UserPackageService userService;

    @PostMapping("/book/{userId}/{packageId}")
    public ResponseEntity<BookingPackageDTO> book(@PathVariable Long userId, @PathVariable Long packageId) {
        return ResponseEntity.ok(userService.bookPackage(userId, packageId));
    }

    @GetMapping("/packages")
    public ResponseEntity<List<ServicePackageDTO>> listPackages() {
        return ResponseEntity.ok(userService.listPackages());
    }

  
}
