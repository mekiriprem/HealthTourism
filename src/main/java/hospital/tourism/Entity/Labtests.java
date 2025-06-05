package hospital.tourism.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Labtests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testTitle;
    private String testDescription;
    private double testPrice;
    private String testDepartment;
    private String testImage;
    private String Status;

    @ManyToOne
    @JoinColumn(name = "diognostics_id", nullable = false)
    @JsonBackReference
    private Diognstics diognostics; // ✅ Corrected type from Hospital → Diognstics
    
    @OneToMany(mappedBy = "labtest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSlot> slots = new ArrayList<>();
}
