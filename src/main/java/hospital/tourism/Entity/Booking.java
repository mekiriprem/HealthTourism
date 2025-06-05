package hospital.tourism.Entity;

import java.time.LocalDateTime;import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity

public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDateTime bookingDate;

    private String bookingStatus; // e.g., "Pending", "Confirmed"

    private String bookingType; // "Cost" or "NoCost"

    private double bookingAmount;

    private String paymentMode; // "Online" or "Offline"

    private String paymentStatus; // "Paid", "Unpaid", "Pending"

    private String discountApplied;

    @ElementCollection
    @Column(name = "slot_info")
    private List<String> slotInfo;

    private String additionalRemarks;

    // 🔗 Relation to User (who booked)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private users user;

    // 🔗 Relation to Service (e.g., Chef, Physio, etc.)
    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = true)
    private Chefs chef;

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

}
