package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Hospital;
import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.HospitalRepo;
import hospital.tourism.repo.LocationRepo;


@Service
public class HospitalService {
    
    @Autowired
    private HospitalRepo hospitalRepository;
    
    @Autowired
    private DoctorsRepo doctorRepository;
    
    @Autowired
    private LocationRepo locationrepo;

    public Hospital saveHospital(Hospital hospital, Integer locationId) {
        LocationEntity location = locationrepo.findById(locationId)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        
        hospital.setLocation(location);

        // Append location details to the hospital's address
        String locationAddress = String.join(", ",
            location.getCity(),
            location.getState(),
            location.getCountry()
        );

        String fullAddress = hospital.getAddress() + ", " + locationAddress;
        hospital.setAddress(fullAddress);

        return hospitalRepository.save(hospital);
    }
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public List<Doctors> getDoctorsByHospitalId(Integer hospitalId) {
        return doctorRepository.findByHospital_HospitalId(hospitalId);
    }
    

}
