package hospital.tourism.Entity;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "physio")
@SQLDelete(sql = "UPDATE physio SET status = 'inactive' WHERE physio_id = ?")

public class Physio {
	
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long physioId;
	private String physioName;
	private String physioDescription;
	private String physioImage;
	private String rating;
	private String address;
	private Double price;
	private String Status;

	// Assuming you have a LocationEntity class
	 @ManyToOne
	 @JoinColumn(name = "location_id")
	 private LocationEntity location;
//	 
//	    @OneToMany(mappedBy = "physio", cascade = CascadeType.ALL, orphanRemoval = true)
//	    private List<ServiceSlot> slots = new ArrayList<>();

  
	 
}
