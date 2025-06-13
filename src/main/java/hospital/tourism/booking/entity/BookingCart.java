package hospital.tourism.booking.entity;

import java.time.LocalDateTime;

import hospital.tourism.Entity.users;
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
@Table(name = "booking_cart")
public class BookingCart {

	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private users user;

	    @Column(nullable = false)
	    private String serviceType; // e.g., "doctor", "spa", etc.

	    @Column(nullable = false)
	    private Long serviceId;

	    @Column(nullable = false)
	    private String serviceName;

	    private double amount;

	    private LocalDateTime selectedStartTime;
	    private LocalDateTime selectedEndTime;

	    private boolean isConfirmed = false;
}
