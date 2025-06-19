package hospital.tourism.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SalesFollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remarks;

    private String status; // e.g., "Called", "Scheduled", "Converted", etc.

    private LocalDateTime followUpDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "sales_id")
    private SalesTeam salesTeam;
}
