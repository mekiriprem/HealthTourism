package hospital.tourism.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Wishlist_Service")
public class WishlistService {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private users user;

    @ManyToOne
    private Doctors doctor;

    @ManyToOne
    private SpaServicese spa;

    @ManyToOne
    private Translators translator;

    @ManyToOne
    private Physio physio;

    @ManyToOne
    private Labtests labtests;

    @ManyToOne
    private Chefs chef;

    private String notes; // optional user notes
	
	
}
