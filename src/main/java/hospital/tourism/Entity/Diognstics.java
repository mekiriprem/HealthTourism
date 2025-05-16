package hospital.tourism.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Diognstics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diognosticsId;

    private String diognosticsName;
    private String diognosticsDescription;
    private String diognosticsImage;
    private String diognosticsrating;
    private String diognosticsaddress;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private LocationEntity location;

    @OneToMany(mappedBy = "diognostics", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Labtests> labtests; // ✅ Corrected field name
}
