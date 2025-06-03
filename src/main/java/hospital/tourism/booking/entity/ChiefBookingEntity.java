package hospital.tourism.booking.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.users;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chief_booking")
@Data
public class ChiefBookingEntity {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer bookingId;

	    private LocalDateTime bookingDate;

	    private String bookingStatus; // e.g., "Pending", "Confirmed"

	    private String bookingType; // "Cost" or "NoCost"

	    private double bookingAmount;

	    private Integer userId; // if you don't have a User entity yet

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "chef_id", nullable = false)
	    @JsonBackReference
	    private Chefs chef; // Don't use DTO here. Use the actual entity.
	    
	   
}
