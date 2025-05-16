package hospital.tourism.repo;

import java.util.List;

import org.apache.tomcat.util.Diagnostics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Labtests;

@Repository
public interface labtestsRepo  extends JpaRepository<Labtests, Long> {
    List<Labtests> findByDiognostics_DiognosticsId(Integer diognosticsId);
}
