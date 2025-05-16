package hospital.tourism.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
// or "patients"
public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private long mobilenum;
    
    @Column(nullable = false)
    private String country;
    
    @Column(nullable = false)
    private String password;

    // Optional: role field for admin/patient
    @Column(nullable = false)
    private String role = "PATIENT";
    
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(unique = true)
    private String verificationToken;


}