package hospital.tourism.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Translators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long translatorID;
    private String translatorName;
    private String translatorDescription;
    private String translatorImage;
    private String translatorRating;
    private String translatorLanguages;
    private String Status;
    private Double price;
    private String translatorAddress;
    private Integer translatorLocIdInteger;
    
    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private LocationEntity location;
    
   
    
//    @OneToMany(mappedBy ="translator", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ServiceSlot> slots = new ArrayList<>();

}

