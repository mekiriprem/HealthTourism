package hospital.tourism.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chefs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chefID;

    private String chefName;
    private String chefDescription;
    private String chefImage;
    private String chefRating;
    private String experience;     
    private String styles;
    private String Status;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonBackReference("chefs-location")
    private LocationEntity location;
}
