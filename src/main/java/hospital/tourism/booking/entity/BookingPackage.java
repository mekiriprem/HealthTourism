package hospital.tourism.booking.entity;

import java.time.LocalDate;

import hospital.tourism.Entity.users;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Packagesbooking")
public class BookingPackage {

	 @Id @GeneratedValue
	    private Long id;

	    @ManyToOne
	    private users user;

	    @ManyToOne
	    private ServicePackage servicePackage;

	    private LocalDate bookingDate;
	    private String status; // PENDING, CONFIRMED
	    private double totalPrice;
	    
}
