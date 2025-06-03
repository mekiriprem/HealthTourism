package hospital.tourism.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hospital.tourism.Entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

	public AdminEntity findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);

	public Optional<AdminEntity> findByAdminEmail(String adminEmail);

	public  List<AdminEntity> findByStatus(String status);
	
	@Query("SELECT a FROM AdminEntity a") // fetch all without filtering
    List<AdminEntity> findAllAdmins();
}
