package hospital.tourism.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Admin-Table")
public class AdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminId;
	private String adminName;
	private String adminEmail;
	private String adminPassword;
	private String adminRole;
	@Column(name = "approval_token")
	private String approvalToken;

	@Column(name = "is_approved")
	private Boolean isApproved = null; // null = pending, true = approved, false = rejected

	
}
