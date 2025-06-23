package hospital.tourism.Entity;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sub-admin_table")  // ✅ Use lowercase for consistency with PostgreSQL
@SQLDelete(sql = "UPDATE sub-admin_table SET status = 'inactive' WHERE admin_id = ?")
public class SubAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    private Integer adminId;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_email", unique = true, nullable = false)
    private String adminEmail;

    @Column(name = "admin_password", nullable = false)
    private String adminPassword;

    @ElementCollection
    @CollectionTable(
        name = "subadmin_permissions",
        joinColumns = @JoinColumn(name = "subadmin_id")
    )
    @Column(name = "permission")
    private List<String> permissions;

    @Column(name = "status", nullable = false)
    private String status = "active";
    
    @Column(name = "role", nullable = false)
    private String role;
}
