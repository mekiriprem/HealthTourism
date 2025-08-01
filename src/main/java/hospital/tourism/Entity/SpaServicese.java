
package hospital.tourism.Entity;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "spa_services")
@SQLDelete(sql = "UPDATE spa_services SET status = 'inactive' WHERE id = ?")
public class SpaServicese {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long serviceId;
	private String serviceName;
	private String serviceDescription;
	private String serviceImage;
	private String rating;
	private Double price;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "spa_id")
	private SpaCenter spaCenter;

	private String status;
	
//    @OneToMany(mappedBy = "spa", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ServiceSlot> slots = new ArrayList<>();


}

