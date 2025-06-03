package hospital.tourism.Entity;

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
public class Physio {
	
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Integer physioId;
	private String physioName;
	private String physioDescription;
	private String physioImage;
	private String rating;
	private String address;
	private String price;
	private String Status;

	// Assuming you have a LocationEntity class
	 @ManyToOne
	 @JoinColumn(name = "location_id")
	 private LocationEntity location;

    public static Object stream() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stream'");
    }
	
	 
}
