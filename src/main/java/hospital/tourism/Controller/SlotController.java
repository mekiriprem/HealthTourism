package hospital.tourism.Controller;

import hospital.tourism.Dto.SlotRequestDTO;
import hospital.tourism.Entity.ServiceSlot;
import hospital.tourism.Service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class SlotController {

    @Autowired
    private SlotService slotService;

    // 🔹 Add slots individually (already done)
    @PostMapping("/slots")
    public String createSlots(@RequestBody SlotRequestDTO request) {
        slotService.addSlots(request.getServiceType(), request.getServiceId(), request.getSlots(), request.getBookedBy());
        return "Slots created successfully";
    }
    

    // ✅ Get slots by service type and ID
    @GetMapping("/slots")
    public List<ServiceSlot> getSlotsByService(
            @RequestParam String serviceType,
            @RequestParam Long serviceId
    ) {
        return slotService.getSlotsByService(serviceType, serviceId);
    }
}

