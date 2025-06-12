package hospital.tourism.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDateTime bookingDate;

    private Long slotIdLong;
    
    private String bookingStatus; // e.g., "Pending", "Confirmed"

    private String bookingType; // "Cost" or "NoCost"

    private double bookingAmount;
 
    private String paymentMode; // "Online" or "Offline"

    private String paymentStatus; // "Paid", "Unpaid", "Pending"

    private String discountApplied;

//    @ElementCollection
//    @Column(name = "slot_info")
//    private List<String> slotInfo;
    
    private LocalDateTime bookingStartTime;
    private LocalDateTime bookingEndTime;

    private String additionalRemarks;

   
    @ManyToOne
    @JoinColumn(name = "physio_id", nullable = true)
    private Physio physio;

    @ManyToOne
    @JoinColumn(name = "translator_id", nullable = true)
    private Translators translator;

    @ManyToOne
    @JoinColumn(name = "spa_id", nullable = true)
    private SpaServicese spa;
    

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = true)
    private  Doctors doctors;
    

    @ManyToOne
    @JoinColumn(name = "labtests_id", nullable = true)
    private Labtests labtests;
    
    @ManyToOne
    @JsonIgnoreProperties("bookings") // 👈 Prevents recursion
    private users user;

    @ManyToOne
    @JsonIgnoreProperties("bookings") // If chef has a bookings list
    private Chefs chef;
    
    @Column(name = "package_booking_id")
    private String packageBookingId;


	


    // Apply similar for other services


}
