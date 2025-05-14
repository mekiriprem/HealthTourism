package hospital.tourism.Entity;

import jakarta.persistence.*;

@Entity
// or "patients"
public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private long mobilenum;
    
    @Column(nullable = false, unique = true)
    private String country;
    
    @Column(nullable = false)
    private String password;

    // Optional: role field for admin/patient
    @Column(nullable = false)
    private String role = "PATIENT";

}
