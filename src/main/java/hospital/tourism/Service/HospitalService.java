package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Hospital;
import hospital.tourism.repo.HospitalRepo;

@Service
public class HospitalService {
    
    @Autowired
    private HospitalRepo hospitalRepository;

    public Hospital saveHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }
    
    

}
