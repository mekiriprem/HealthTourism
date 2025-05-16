package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Hospital;
import hospital.tourism.repo.HospitalRepo;
import hospital.tourism.repo.DoctorsRepo;


@Service
public class HospitalService {
    
    @Autowired
    private HospitalRepo hospitalRepository;
    
    @Autowired
    private DoctorsRepo doctorRepository;

    public Hospital saveHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public List<Doctors> getDoctorsByHospitalId(Integer hospitalId) {
        return doctorRepository.findByHospital_HospitalId(hospitalId);
    }
    

}
