package hospital.tourism.Entity;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

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
@SQLDelete(sql = "UPDATE diognstics SET status = 'Inactive' WHERE diognostics_id = ?")
public class Diognstics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diognosticsId;

    private String diognosticsName;
    private String diognosticsDescription;
    private String diognosticsImage;
    private String diognosticsrating;
    private String diognosticsaddress;

    private String status; // Default status is active
    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private LocationEntity location;

    @OneToMany(mappedBy = "diognostics", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Labtests> labtests; // ✅ Corrected field name
}
