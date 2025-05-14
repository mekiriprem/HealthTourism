package hospital.tourism.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

	public AdminEntity findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);

	Optional<AdminEntity> findByApprovalToken(String token);
}
