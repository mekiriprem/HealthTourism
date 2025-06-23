package hospital.tourism.Entity;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "admin_table")  // ✅ Use lowercase for consistency with PostgreSQL
@SQLDelete(sql = "UPDATE admin_table SET status = 'inactive' WHERE admin_id = ?")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    private Integer adminId;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_email", unique = true, nullable = false)
    private String adminEmail;

    @Column(name = "admin_password", nullable = false)
    private String adminPassword;

    @Column(name = "employee_id")
    private String employeeId;

    @ElementCollection
    @Column(name = "permission")
    private List<String> permissions;

    @Column(name = "status", nullable = false)
    private String status = "active";
    
    @Column(name = "role", nullable = false)
    private String role;
}
