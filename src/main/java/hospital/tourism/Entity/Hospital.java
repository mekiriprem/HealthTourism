package hospital.tourism.Entity;

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
public class Hospital { 
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer hospitalId;
        private String hositalName;
        private String hospitalDescription;
        private String hospitalImage;
        private String rating;
        private String address;
        private String Status;
       
        @ManyToOne
        @JoinColumn(name = "location_id")
        @JsonBackReference
        private LocationEntity location;
        
        @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonManagedReference
        private List<Doctors> doctors;

    }


