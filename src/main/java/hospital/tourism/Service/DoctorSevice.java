package hospital.tourism.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Hospital;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.HospitalRepo;

@Service
public class DoctorSevice {
    
    @Autowired
    private DoctorsRepo doctorRepository;

    @Autowired
    private HospitalRepo hospitalRepository;

    public Doctors saveDoctor(Doctors doctor, int hospitalId) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(hospitalId);
        if (optionalHospital.isPresent()) {
            doctor.setHospital(optionalHospital.get());
            return doctorRepository.save(doctor);
        } else {
            throw new IllegalArgumentException("Hospital with ID " + hospitalId + " not found.");
        }
    }

    public List<Doctors> getAllDoctors() {
        return doctorRepository.findAll();
    }
    public Doctors getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));
    }

}
