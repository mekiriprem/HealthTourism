package hospital.tourism.Crm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.SalesFollowUp;
import hospital.tourism.Entity.SalesTeam;
import hospital.tourism.Service.CrmService;
import hospital.tourism.repo.BookingRepo;

@RestController
@RequestMapping("/api/bookings")
public class crmcontroller {
    
    @Autowired
    private BookingRepo bookingRepository;
    @Autowired
    private CrmService bookingService;

    @GetMapping("/free-offline-unpaid")
    public List<Booking> getFreeOfflineUnpaidBookings() {
        return bookingService.getUnpaidOfflineFreeBookings();
    }

    @PostMapping("/assign")
    public ResponseEntity<List<Booking>> assignMultipleBookingsToSales(
        @RequestParam Long salesId,
        @RequestBody List<Long> bookingIds,
        @RequestParam(required = false) String remark
    ) {
        List<Booking> updatedBookings = bookingService.assignMultipleToSales(bookingIds, salesId, remark);
        return ResponseEntity.ok(updatedBookings);
    }


    @PostMapping("/followup")
    public ResponseEntity<SalesFollowUp> addFollowUp(
        @RequestParam Long bookingId,
        @RequestParam Long salesId,
        @RequestParam String remark,
        @RequestParam String status
    ) {
        SalesFollowUp followUp = bookingService.updateFollowUp(bookingId, salesId, remark, status);
        return ResponseEntity.ok(followUp);
    }
    
    @GetMapping("/followups/sales/{salesId}")
    public List<SalesFollowUp> getFollowUpsBySales(@PathVariable Long salesId) {
        return bookingService.getFollowUpsBySalesId(salesId);
    }

    @GetMapping("/followups/booking/{bookingId}")
    public List<SalesFollowUp> getFollowUpsByBooking(@PathVariable Long bookingId) {
        return bookingService.getFollowUpsByBookingId(bookingId);
    }
    
    @GetMapping("/followups/history/booking/{bookingId}")
    public List<SalesFollowUp> getFollowUpHistoryByBooking(@PathVariable Long bookingId) {
        return bookingService.getFollowUpHistoryByBookingId(bookingId);
    }

    @GetMapping("/bookings/sales/{salesId}")
    public List<Booking> getBookingsAssignedToSales(@PathVariable Long salesId) {
        return bookingService.getBookingsAssignedToSales(salesId);
    }
    @PostMapping("/register")
    public ResponseEntity<SalesTeam> register(@RequestBody SalesTeam salesTeam) {
        return ResponseEntity.ok(bookingService.register(salesTeam));
    }

    @PostMapping("/login")
    public ResponseEntity<SalesTeam> login(@RequestBody SalesTeam loginRequest) {
        SalesTeam sales = bookingService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesTeam> getById(@PathVariable Long id) {
        return bookingService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<SalesTeam>> getAllSalesTeam() {
        return ResponseEntity.ok(bookingService.getAllSales());
    }


}
