package hospital.tourism.repo;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import hospital.tourism.Entity.SalesTeam;

public interface SalesTeamRepository extends JpaRepository<SalesTeam, Long> {
    Optional<SalesTeam> findByEmail(String email);
}

