package hospital.tourism.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "location_table")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    private String locationName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    // One location has many physioAndSpa
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhysioAndSpa> physioAndSpaList;


	
	
}
