package hospital.tourism.booking.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "service_package")
public class ServicePackage {

	 @Id @GeneratedValue
	    private Long id;
	    private String name;
	    private String description;
	    @Column(name = "total_price")
	    private double totalPrice;
	    private int durationDays;

	    @OneToMany(mappedBy = "servicePackage", cascade = CascadeType.ALL)
	    private List<PackageServiceItem> serviceItems;
}
