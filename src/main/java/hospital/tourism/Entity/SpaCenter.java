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
@Table(name = "spa_center")
public class SpaCenter {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Integer spaId;
	private String spaName;
	private String spaDescription;
	private String spaImage;
	private String rating;
	private String address;
	 private String Status;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private LocationEntity location;
}
