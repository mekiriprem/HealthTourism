package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.HospitalDTO;
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
    public List<HospitalDTO> getAllHospitalsAsDto() {
        List<Hospital> hospitals = hospitalRepository.findAll();

        return hospitals.stream().map(hospital -> {
            HospitalDTO dto = new HospitalDTO();
            dto.setHospitalId(hospital.getHospitalId());
            dto.setHositalName(hospital.getHositalName());
            dto.setHospitalDescription(hospital.getHospitalDescription());
            dto.setHospitalImage(hospital.getHospitalImage());
            dto.setRating(hospital.getRating());
            dto.setAddress(hospital.getAddress());
            dto.setStatus(hospital.getStatus());


            return dto;
        }).toList();
    }


    public List<Doctors> getDoctorsByHospitalId(Integer hospitalId) {
        return doctorRepository.findByHospital_HospitalId(hospitalId);
    }
    
    public void softDeleteHospital(Integer id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + id));

        hospital.setStatus("Inactive");
        hospitalRepository.save(hospital);
    }

    // Reactivate method
    public void activateHospitalIfInactive(Integer id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + id));

        if ("Inactive".equalsIgnoreCase(hospital.getStatus())) {
            hospital.setStatus("Active");
            hospitalRepository.save(hospital);
        }
    }
    
	public HospitalDTO updateHospital(Integer hospitalId, HospitalDTO dto) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
				.orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + hospitalId));

		hospital.setHositalName(dto.getHositalName());
		hospital.setHospitalDescription(dto.getHospitalDescription());
		hospital.setHospitalImage(dto.getHospitalImage());
		hospital.setRating(dto.getRating());
		hospital.setAddress(dto.getAddress());
		hospital.setStatus(dto.getStatus());

		LocationEntity location = locationrepo.findById(dto.getHospitallocationId())
				.orElseThrow(() -> new RuntimeException("Location not found with ID: " + dto.getHospitallocationId()));
		hospital.setLocation(location);

		Hospital updatedHospital = hospitalRepository.save(hospital);

		HospitalDTO updatedDto = new HospitalDTO();
		updatedDto.setHospitalId(updatedHospital.getHospitalId());
		updatedDto.setHositalName(updatedHospital.getHositalName());
		updatedDto.setHospitalDescription(updatedHospital.getHospitalDescription());
		updatedDto.setHospitalImage(updatedHospital.getHospitalImage());
		updatedDto.setRating(updatedHospital.getRating());
		updatedDto.setAddress(updatedHospital.getAddress());
		updatedDto.setStatus(updatedHospital.getStatus());

		return updatedDto;
	}

}
