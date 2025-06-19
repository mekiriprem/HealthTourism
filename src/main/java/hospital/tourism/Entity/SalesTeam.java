package hospital.tourism.Entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SalesTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;
    
    private String password;
}
