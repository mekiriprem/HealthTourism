package hospital.tourism.Entity;

import org.hibernate.annotations.SQLDelete;

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
@SQLDelete(sql = "UPDATE labtests SET status = 'Inactive' WHERE id = ?") 
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
    
//    @OneToMany(mappedBy = "labtest", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ServiceSlot> slots = new ArrayList<>();

	
}
