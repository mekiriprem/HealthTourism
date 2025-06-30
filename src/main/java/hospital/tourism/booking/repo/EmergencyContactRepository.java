package hospital.tourism.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.booking.entity.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {

}
