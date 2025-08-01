package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Service.UserStatsService;

@RestController
@RequestMapping("/api/user-stats")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4545", "http://localhost:3000","https://medi-tailor.com"},allowCredentials = "true")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;

    /**
     * Get user count by country for globe visualization
     * @return List of country statistics with user counts
     */
    @GetMapping("/countries")
    public ResponseEntity<List<Map<String, Object>>> getUserCountByCountry() {
        try {
            List<Map<String, Object>> countryStats = userStatsService.getUserCountByCountry();
            return ResponseEntity.ok(countryStats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get total user count
     * @return Total number of registered users
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getTotalUserCount() {
        try {
            Long totalUsers = userStatsService.getTotalUserCount();
            return ResponseEntity.ok(Map.of("totalUsers", totalUsers));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
