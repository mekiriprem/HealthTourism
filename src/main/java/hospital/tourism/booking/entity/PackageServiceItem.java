package hospital.tourism.booking.entity;

import hospital.tourism.Entity.ServiceItem;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "package_service_item")
public class PackageServiceItem {

	@Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne
    @JoinColumn(name = "service_item_id")
    private ServiceItems serviceItem;
}
