package hospital.tourism.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Dto.SlotRequestDTO;
import hospital.tourism.Entity.ServiceSlot;
import hospital.tourism.Service.SlotService;

@RestController
@RequestMapping("/api/services")
public class SlotController {

    @Autowired
    private SlotService slotService;

    // 🔹 Add slots individually (already done)
//    @PostMapping("/slots")
//    public String createSlots(@RequestBody SlotRequestDTO request) {
//        slotService.addSlots(request.getServiceType(), request.getServiceId(), request.getSlots(), request.getBookedBy());
//        return "Slots created successfully";
//    }
    
    @PostMapping("/slots/add")
    public ResponseEntity<List<SlotRequestDTO>> addServiceSlots(
    		@RequestBody SlotRequestDTO request
    ) {
        List<SlotRequestDTO> slots = slotService.addSlots(request.getServiceType(), request.getServiceId(), request.getSlots(), request.getBookedByUserId());
        return ResponseEntity.ok(slots);
    }

    

    // ✅ Get slots by service type and ID
    @GetMapping("/slots")
    public List<ServiceSlot> getSlotsByService(
            @RequestParam String serviceType,
            @RequestParam Long serviceId
    ) {
        return slotService.getSlotsByService(serviceType, serviceId);
    }
    
    @GetMapping("/All-Slots")
    public ResponseEntity<List<SlotRequestDTO>> getAllSlotesssss(){
    	List<SlotRequestDTO>  msf=slotService.getAllSlotses();
    	return new ResponseEntity<List<SlotRequestDTO>>(msf,HttpStatus.OK);
    }
}

