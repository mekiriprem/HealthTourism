package hospital.tourism.Entity;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@SQLDelete(sql = "UPDATE doctors SET status = 'inactive' WHERE id = ?")
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
    private double price;
    
    private String status= "active"; // Default status is active;

    @ManyToOne(fetch = FetchType.EAGER) // ensure it's fetched
    @JoinColumn(name = "hospital_id", nullable = false)
    @JsonBackReference("hospital-doctors")// prevent circular reference
    private Hospital hospital;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSlot> slots = new ArrayList<>();
    
    

}