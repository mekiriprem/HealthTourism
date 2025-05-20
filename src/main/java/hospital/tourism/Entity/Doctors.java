package hospital.tourism.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Doctors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private double rating;
    private String description;
    private String department;
    private String profilepic;
    private String Status;

    @ManyToOne(fetch = FetchType.EAGER) // ensure it's fetched
    @JoinColumn(name = "hospital_id", nullable = false)
    @JsonIgnoreProperties("doctors") // prevent circular reference
    private Hospital hospital;

}