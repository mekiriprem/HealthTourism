package hospital.tourism.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hospital.tourism.Dto.DoctorsDTO;
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
            // Set default status if not provided
			if (doctor.getStatus() == null || doctor.getStatus().isEmpty()) {
				doctor.setStatus("active");
			}
            return doctorRepository.save(doctor);
        } else {
            throw new IllegalArgumentException("Hospital with ID " + hospitalId + " not found.");
        }
   }

//    public DoctorsDTO saveDoctor(DoctorsDTO doctorDto, int hospitalId) {
//        Optional<Hospital> optionalHospital = hospitalRepository.findById(hospitalId);
//        if (optionalHospital.isPresent()) {
//            Hospital hospital = optionalHospital.get();
//
//            Doctors doctor = new Doctors();
//            doctor.setName(doctorDto.getName());
//            doctor.setEmail(doctorDto.getEmail());
//            doctor.setRating(doctorDto.getRating());
//            doctor.setDescription(doctorDto.getDescription());
//            doctor.setDepartment(doctorDto.getDepartment());
//            doctor.setProfilepic(doctorDto.getProfilepic());
//            doctor.setStatus(
//                doctorDto.getStatus() == null || doctorDto.getStatus().isEmpty() ? "active" : doctorDto.getStatus()
//            );
//            doctor.setHospital(hospital);
//
//            Doctors savedDoctor = doctorRepository.save(doctor);
//
//            // Convert back to DTO
//            DoctorsDTO savedDto = new DoctorsDTO();
//            savedDto.setId(savedDoctor.getId());
//            savedDto.setName(savedDoctor.getName());
//            savedDto.setEmail(savedDoctor.getEmail());
//            savedDto.setRating(savedDoctor.getRating());
//            savedDto.setDescription(savedDoctor.getDescription());
//            savedDto.setDepartment(savedDoctor.getDepartment());
//            savedDto.setProfilepic(savedDoctor.getProfilepic());
//            savedDto.setStatus(savedDoctor.getStatus());
//            savedDto.setHospitalId(hospital.getHospitalId());
//            savedDto.setHospitalName(hospital.getHositalName());
//
//            return savedDto;
//        } else {
//            throw new IllegalArgumentException("Hospital with ID " + hospitalId + " not found.");
//        }
//    }

    public List<Doctors> getAllDoctors() {
        return doctorRepository.findAll();
    }
    public DoctorsDTO getDoctorById(Long id) {
        Doctors doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));
        
        // Manually map to DTO
        DoctorsDTO dto = new DoctorsDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        dto.setRating(doctor.getRating());
        dto.setDescription(doctor.getDescription());
        dto.setDepartment(doctor.getDepartment());
        dto.setProfilepic(doctor.getProfilepic());
        dto.setStatus(doctor.getStatus());
		if (doctor.getHospital() != null) {
			dto.setHospitalId(doctor.getHospital().getHospitalId());
			dto.setHospitalName(doctor.getHospital().getHositalName());
		} else {
			dto.setHospitalId(null);
			dto.setHospitalName(null);
		}

        return dto;
    }

    
     public void softDeleteDoctor(Long id) {
    	 doctorRepository.deleteById(id); // this will run the SQLDelete update
     	}
     public void restoreDoctor(Long id) {
    	    Doctors doctor = doctorRepository.findById(id)
    	        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

    	    doctor.setStatus("active");
    	    doctorRepository.save(doctor);
    	}
     public List<Doctors> getDoctorsByStatus(String status) {
         return doctorRepository.findByStatus(status);
     }

     //update the doctor details
     public DoctorsDTO updateDoctor(Long id, DoctorsDTO doctorDTO) {
    	    // Fetch the existing doctor by ID
    	    Doctors existingDoctor = doctorRepository.findById(id)
    	            .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));

    	    // Update fields from DTO
    	    existingDoctor.setName(doctorDTO.getName());
    	    existingDoctor.setEmail(doctorDTO.getEmail());
    	    existingDoctor.setRating(doctorDTO.getRating());
    	    existingDoctor.setDescription(doctorDTO.getDescription());
    	    existingDoctor.setDepartment(doctorDTO.getDepartment());
    	    existingDoctor.setProfilepic(doctorDTO.getProfilepic());
    	    existingDoctor.setStatus(doctorDTO.getStatus());

    	    // Update hospital if hospitalId is present and different
    	    if (doctorDTO.getHospitalId() != 0 &&
    	        existingDoctor.getHospital(). getHospitalId() != doctorDTO.getHospitalId()) {
    	        
    	        Hospital hospital = hospitalRepository.findById(doctorDTO.getHospitalId())
    	                .orElseThrow(() -> new RuntimeException("Hospital not found with id " + doctorDTO.getHospitalId()));
    	        
    	        existingDoctor.setHospital(hospital);
    	    }

    	    // Save updated doctor
    	    Doctors updated = doctorRepository.save(existingDoctor);

    	    // Map to DTO to return
    	    DoctorsDTO updatedDTO = new DoctorsDTO();
    	    updatedDTO.setId(updated.getId());
    	    updatedDTO.setName(updated.getName());
    	    updatedDTO.setEmail(updated.getEmail());
    	    updatedDTO.setRating(updated.getRating());
    	    updatedDTO.setDescription(updated.getDescription());
    	    updatedDTO.setDepartment(updated.getDepartment());
    	    updatedDTO.setProfilepic(updated.getProfilepic());
    	    updatedDTO.setStatus(updated.getStatus());
    	    updatedDTO.setHospitalId(updated.getHospital() != null ? updated.getHospital().getHospitalId() : null);
    	    updatedDTO.setHospitalName(updated.getHospital() != null ? updated.getHospital().getHositalName() : null);

    	    return updatedDTO;
    	}
     
		public String deleteDoctor(Long id) {
			doctorRepository.deleteById(id);
			return "Doctor with ID " + id + " has been deleted successfully.";
		}
		public DoctorsDTO getDoctorByid(Long docId) {
			Doctors doctors= doctorRepository.findById(docId)
					.orElseThrow(() -> new RuntimeException("Doctor not found with id " + docId));
			DoctorsDTO dto = new DoctorsDTO();
			dto.setId(doctors.getId());
			dto.setName(doctors.getName());
			dto.setEmail(doctors.getEmail());
			dto.setRating(doctors.getRating());
			dto.setDescription(doctors.getDescription());
			dto.setDepartment(doctors.getDepartment());
			dto.setProfilepic(doctors.getProfilepic());
			dto.setStatus(doctors.getStatus());
			if (doctors.getHospital() != null) {
				dto.setHospitalId(doctors.getHospital().getHospitalId());
				dto.setHospitalName(doctors.getHospital().getHositalName());
			} else {
				dto.setHospitalId(null);
				dto.setHospitalName(null);
			}
			return dto;
		}

}
