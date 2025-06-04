package hospital.tourism.booking.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Entity.Translators;
import hospital.tourism.Entity.users;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@Table(name = "booking_Table")
@Data
public class BookingEntity {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer bookingId;

	    private LocalDateTime bookingStarDateTime;
	    private LocalDateTime bookingEndDateTime;

	    @Column(nullable = false)
	    private String bookingStatus; // e.g., "Pending", "Confirmed"

	    @Column(nullable = false)
	    private String bookingType; // e.g., "Cost" or "NoCost"

	    @ElementCollection
	    @CollectionTable(name = "booking_slots", joinColumns = @JoinColumn(name = "booking_id"))
	    @Column(name = "slot")
	    private List<String> slots;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    private users user;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "chef_id")
	    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    private Chefs chef;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "physio_id")
	    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    private Physio physios;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "translator_id")
	    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    private Translators translator;

	    @CreationTimestamp
	    private LocalDateTime createdAt;

	    @UpdateTimestamp
	    private LocalDateTime updatedAt;
	   
}
