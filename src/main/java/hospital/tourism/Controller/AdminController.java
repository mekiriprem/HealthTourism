package hospital.tourism.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Entity.AdminEntity;
import hospital.tourism.Service.AdminServiceimpl;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminServiceimpl adminServiceimpl;

    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody AdminEntity admin) {
        return adminServiceimpl.addAdmin(admin);
    }

    @PostMapping("/adminLogin")
    public Map<String, Object> adminLogin(@RequestBody AdminEntity admin) {
        return adminServiceimpl.adminLogin(admin);
    }



    @GetMapping("/approve")
    @ResponseBody
    public String approveAdmin(@RequestParam String token) {
        return adminServiceimpl.approveAdmin(token);
    }

    @GetMapping("/reject")
    @ResponseBody
    public String rejectAdmin(@RequestParam String token) {
        return adminServiceimpl.rejectAdmin(token);
    }
    // No need to expose approve/reject endpoints anymore
    // We don't need manual approval/rejection via UI or endpoints
}
