package hospital.tourism.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hospital.tourism.Entity.users;


public interface usersrepo extends JpaRepository<users, Long> {
    Optional<users> findByEmail(String email);
    Optional<users> findByVerificationToken(String token);
    Optional<users> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    
    @Query("SELECT u.country, COUNT(u) FROM users u GROUP BY u.country")
    List<Object[]> countUsersByCountry();
  
    Optional<users> findByResetToken(String token);
}