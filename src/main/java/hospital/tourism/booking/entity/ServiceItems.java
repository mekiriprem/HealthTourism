package hospital.tourism.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table (name = "service_item")
@Data
public class ServiceItems {

	@Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String type; // SPA, DOCTOR, HOTEL, etc.
    private double price;
}
