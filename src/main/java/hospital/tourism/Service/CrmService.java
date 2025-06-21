package hospital.tourism.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.SalesFollowUp;
import hospital.tourism.Entity.SalesTeam;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.SalesFollowUpRepository;
import hospital.tourism.repo.SalesTeamRepository;

@Service
public class CrmService {
    
    @Autowired
    private BookingRepo bookingRepository;

    @Autowired
    private SalesTeamRepository salesTeamRepository;

    @Autowired
    private SalesFollowUpRepository salesFollowUpRepository;

    public List<Booking> getUnpaidOfflineFreeBookings() {
        return bookingRepository.findByBookingAmountAndBookingStatusAndPaymentModeAndPaymentStatus(
            0, "Booked", "offline", "Unpaid"
        );
    }

    public Booking assignToSales(Long bookingId, Long salesId, String remark) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        SalesTeam sales = salesTeamRepository.findById(salesId)
            .orElseThrow(() -> new RuntimeException("Sales Team not found"));

        booking.setSalesTeam(sales);
        bookingRepository.save(booking);

        SalesFollowUp followUp = new SalesFollowUp();
        followUp.setBooking(booking);
        followUp.setSalesTeam(sales);
        followUp.setRemarks(remark);
        followUp.setStatus("Assigned");

        salesFollowUpRepository.save(followUp);

        return booking;
    }

    public SalesFollowUp updateFollowUp(Long bookingId, Long salesId, String remark, String status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        SalesTeam sales = salesTeamRepository.findById(salesId).orElseThrow();

        SalesFollowUp followUp = new SalesFollowUp();
        followUp.setBooking(booking);
        followUp.setSalesTeam(sales);
        followUp.setRemarks(remark);
        followUp.setStatus(status);

        return salesFollowUpRepository.save(followUp);
    }
    public List<SalesFollowUp> getFollowUpsBySalesId(Long salesId) {
        return salesFollowUpRepository.findBySalesTeam_Id(salesId);

    }
        public List<SalesFollowUp> getFollowUpsByBookingId(Long bookingId) {

        return salesFollowUpRepository.findByBooking_BookingId(bookingId);
    }
    
    public List<SalesFollowUp> getFollowUpHistoryByBookingId(Long bookingId) {
        return salesFollowUpRepository.findByBooking_BookingIdOrderByFollowUpDateDesc(bookingId);
    }

    public List<Booking> getBookingsAssignedToSales(Long salesId) {
        return bookingRepository.findBySalesTeam_Id(salesId);
    }
    
    public SalesTeam register(SalesTeam salesTeam) {
        // You can add password hashing here
        return salesTeamRepository.save(salesTeam);
    }

    public SalesTeam login(String email, String password) {
        Optional<SalesTeam> optionalSales = salesTeamRepository.findByEmail(email);
        if (optionalSales.isPresent()) {
            SalesTeam sales = optionalSales.get();
            if (sales.getPassword().equals(password)) {
                return sales;
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<SalesTeam> getById(Long id) {
        return salesTeamRepository.findById(id);
    }
    
    public List<SalesTeam> getAllSales() {
        return salesTeamRepository.findAll();
    }
    public List<Booking> assignMultipleToSales(List<Long> bookingIds, Long salesId, String remark) {
        SalesTeam sales = salesTeamRepository.findById(salesId)
            .orElseThrow(() -> new RuntimeException("Sales Team not found"));

        List<Booking> updatedBookings = new ArrayList<>();

        for (Long bookingId : bookingIds) {
            Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

            booking.setSalesTeam(sales);
            bookingRepository.save(booking);

            SalesFollowUp followUp = new SalesFollowUp();
            followUp.setBooking(booking);
            followUp.setSalesTeam(sales);
            followUp.setRemarks(remark != null ? remark : "Assigned");
            followUp.setStatus("Assigned");

            salesFollowUpRepository.save(followUp);

            updatedBookings.add(booking);
        }

        return updatedBookings;
    }

}

