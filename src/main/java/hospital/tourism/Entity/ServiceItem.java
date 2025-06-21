package hospital.tourism.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "services")
public class ServiceItem {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // e.g., "Chef", "Translator", etc.

    @Column(nullable = false)
    private double pricePerDay;

    @Column(nullable = false)
    private String status;  // e.g., "ACTIVE", "INACTIVE"

    @Column(length = 500)
    private String description;
}
