package hospital.tourism.Entity;


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

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

}