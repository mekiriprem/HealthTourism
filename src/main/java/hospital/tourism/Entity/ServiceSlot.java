package hospital.tourism.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServiceSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slotTime; // e.g., "10:00 AM - 11:00 AM"

    private String bookingStatus; // "BOOKED", "AVAILABLE"

    private Long bookedBy; // user ID who booked it

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Chefs chef;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctors doctor;

    @ManyToOne
    @JoinColumn(name = "spa_id")
    private SpaServicese spa;

    @ManyToOne
    @JoinColumn(name = "physio_id")
    private Physio physio;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translators translator;

    @ManyToOne
    @JoinColumn(name = "labtest_id")
    private Labtests labtest;
}
