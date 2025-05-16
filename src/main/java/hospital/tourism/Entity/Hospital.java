package hospital.tourism.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

        @ManyToOne
        @JoinColumn(name = "location_id")
        @JsonBackReference
        private LocationEntity location;

    }


