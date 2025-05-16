package hospital.tourism.Entity;

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

    @ManyToOne
    @JoinColumn(name = "diognostics_id", nullable = false)
    @JsonBackReference
    private Diognstics diognostics; // ✅ Corrected type from Hospital → Diognstics
}
