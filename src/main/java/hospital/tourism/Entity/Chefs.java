package hospital.tourism.Entity;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE chefs SET status = 'Inactive' WHERE chefid = ?")
public class Chefs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefID;

    private String chefName;
    private String chefDescription;
    private String chefImage;
    private String chefRating;
    private String experience;     
    private String styles;
    private String status;
    private double price;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonBackReference("chefs-location")
    private LocationEntity location;  
   
//    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ServiceSlot> slots = new ArrayList<>();
}
