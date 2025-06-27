package hospital.tourism.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

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
        System.out.println("Getting all ACTIVE hospitals from database...");
        List<Hospital> hospitals = hospitalRepository.findByStatus("Active");
        System.out.println("Found " + hospitals.size() + " active hospitals in database");

        if (hospitals.isEmpty()) {
            System.out.println("No active hospitals found in database!");
            return List.of(); // Return empty list
        }

        return hospitals.stream().map(hospital -> {
            System.out.println("Processing hospital: " + hospital.getHospitalId() + " - " + hospital.getHospitalName());
            HospitalDTO dto = new HospitalDTO();
            dto.setHospitalId(hospital.getHospitalId());
            dto.setHospitalName(hospital.getHospitalName());
            dto.setHospitalDescription(hospital.getHospitalDescription());
            dto.setHospitalImage(hospital.getHospitalImage());
            dto.setRating(hospital.getRating());
            dto.setAddress(hospital.getAddress());
            dto.setStatus(hospital.getStatus());

            // Set location id and name
            if (hospital.getLocation() != null) {
                dto.setHospitallocationId(hospital.getLocation().getLocationId());
                dto.setHospitallocationName(hospital.getLocation().getCity());
            }

            return dto;
        }).toList();
    }

    // Debug method to get raw hospital data
    public List<Hospital> getAllRawHospitals() {
        System.out.println("Getting raw hospitals from database...");
        List<Hospital> hospitals = hospitalRepository.findAll();
        System.out.println("Raw hospitals count: " + hospitals.size());
        for (Hospital h : hospitals) {
            System.out.println("Raw Hospital: ID=" + h.getHospitalId() + ", Name=" + h.getHospitalName() + ", Status=" + h.getStatus());
        }
        return hospitals;
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
		System.out.println("=== SERVICE UPDATE HOSPITAL ===");
		System.out.println("Service received hospitalId: " + hospitalId);
		System.out.println("Service received DTO: " + dto);
		System.out.println("DTO hospitallocationId: " + (dto != null ? dto.getHospitallocationId() : "null"));
		System.out.println("==============================");
		
		if (hospitalId == null) {
			System.out.println("ERROR: Service received null hospitalId!");
			throw new RuntimeException("Hospital ID cannot be null");
		}
		
		Hospital hospital = hospitalRepository.findById(hospitalId)
				.orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + hospitalId));
		hospital.setHospitalName(dto.getHospitalName());
		hospital.setHospitalDescription(dto.getHospitalDescription());
		hospital.setHospitalImage(dto.getHospitalImage());
		hospital.setRating(dto.getRating());
		hospital.setAddress(dto.getAddress());
		hospital.setStatus(dto.getStatus());

		// Handle location update only if locationId is provided
		if (dto.getHospitallocationId() != null) {
			LocationEntity location = locationrepo.findById(dto.getHospitallocationId())
					.orElseThrow(() -> new RuntimeException("Location not found with ID: " + dto.getHospitallocationId()));
			hospital.setLocation(location);
		} else {
			System.out.println("Warning: No location ID provided, keeping existing location");
		}

		Hospital updatedHospital = hospitalRepository.save(hospital);

		HospitalDTO updatedDto = new HospitalDTO();
		updatedDto.setHospitalId(updatedHospital.getHospitalId());
		updatedDto.setHospitalName(updatedHospital.getHospitalName());
		updatedDto.setHospitalDescription(updatedHospital.getHospitalDescription());
		updatedDto.setHospitalImage(updatedHospital.getHospitalImage());
		updatedDto.setRating(updatedHospital.getRating());
		updatedDto.setAddress(updatedHospital.getAddress());
		updatedDto.setStatus(updatedHospital.getStatus());

		return updatedDto;
	}

    	public List<HospitalDTO> getHospitalsByCityAndSpecialization(String city, String specialization) {
    	    List<Hospital> hospitals = hospitalRepository
    	        .findByStatusAndLocation_CityAndSpecializationIgnoreCase("ACTIVE", city, specialization);

    	    return hospitals.stream().map(hospital -> {
    	        HospitalDTO dto = new HospitalDTO();
    	        dto.setHospitalId(hospital.getHospitalId());
    	        dto.setHospitalName(hospital.getHospitalName());
    	        dto.setHospitalDescription(hospital.getHospitalDescription());
    	        dto.setHospitalImage(hospital.getHospitalImage());
    	        dto.setRating(hospital.getRating());
    	        dto.setAddress(hospital.getAddress());
    	        dto.setStatus(hospital.getStatus());

    	        if (hospital.getLocation() != null) {
    	            dto.setHospitallocationId(hospital.getLocation().getLocationId());
    	            dto.setHospitallocationName(hospital.getLocation().getCity());
    	        }

    	        return dto;
    	    }).toList();
    	}

    	public List<HospitalDTO> getHospitalsByCity(String city) {
    	    List<Hospital> hospitals = hospitalRepository.findByStatusAndLocation_City("Active", city);

    	    return hospitals.stream().map(hospital -> {
    	        HospitalDTO dto = new HospitalDTO();
    	        dto.setHospitalId(hospital.getHospitalId());
    	        dto.setHospitalName(hospital.getHospitalName());
    	        dto.setHospitalDescription(hospital.getHospitalDescription());
    	        dto.setHospitalImage(hospital.getHospitalImage());
    	        dto.setRating(hospital.getRating());
    	        dto.setAddress(hospital.getAddress());
    	        dto.setStatus(hospital.getStatus());

    	        if (hospital.getLocation() != null) {
    	            dto.setHospitallocationId(hospital.getLocation().getLocationId());
    	            dto.setHospitallocationName(hospital.getLocation().getCity());
    	        }

    	        return dto;
    	    }).toList();
    	}

    	public List<HospitalDTO> searchHospitalsByCityOrState(String searchTerm) {
    	    List<Hospital> hospitals = hospitalRepository.searchByCityOrState(searchTerm);

    	    return hospitals.stream().map(hospital -> {
    	        HospitalDTO dto = new HospitalDTO();
    	        dto.setHospitalId(hospital.getHospitalId());
    	        dto.setHospitalName(hospital.getHospitalName());
    	        dto.setHospitalDescription(hospital.getHospitalDescription());
    	        dto.setHospitalImage(hospital.getHospitalImage());
    	        dto.setRating(hospital.getRating());
    	        dto.setAddress(hospital.getAddress());
    	        dto.setStatus(hospital.getStatus());

    	        if (hospital.getLocation() != null) {
    	            dto.setHospitallocationId(hospital.getLocation().getLocationId());
    	            dto.setHospitallocationName(hospital.getLocation().getCity());
    	        }

    	        return dto;
    	    }).toList();
    	}


		public List<HospitalDTO> getHospitalsBySpecilization(String specialization) {
			List<Hospital> hospitals = hospitalRepository.findBySpecialization(specialization);

			return hospitals.stream().map(hospital -> {
				HospitalDTO dto = new HospitalDTO();
				dto.setHospitalId(hospital.getHospitalId());
				dto.setHospitalName(hospital.getHospitalName());
				dto.setHospitalDescription(hospital.getHospitalDescription());
				dto.setHospitalImage(hospital.getHospitalImage());
				dto.setRating(hospital.getRating());
				dto.setAddress(hospital.getAddress());
				dto.setStatus(hospital.getStatus());
				dto.setSpecialization(hospital.getSpecialization());

				if (hospital.getLocation() != null) {
					dto.setHospitallocationId(hospital.getLocation().getLocationId());
					dto.setHospitallocationName(hospital.getLocation().getCity());
				}

				return dto;
			}).toList();
		}
		
		//find All hospitals
		public List<HospitalDTO>getAllHospitalss(){
		List<Hospital>listHospitals=	hospitalRepository.findAll();
		HospitalDTO hsDto=new HospitalDTO();
		return listHospitals.stream().map(hospital -> {
            HospitalDTO dto = new HospitalDTO();
            dto.setHospitalId(hospital.getHospitalId());
            dto.setHospitalName(hospital.getHospitalName());
            dto.setHospitalDescription(hospital.getHospitalDescription());
            dto.setHospitalImage(hospital.getHospitalImage());
            dto.setRating(hospital.getRating());
            dto.setAddress(hospital.getAddress());
            dto.setStatus(hospital.getStatus());

            if (hospital.getLocation() != null) {
                dto.setHospitallocationId(hospital.getLocation().getLocationId());
                dto.setHospitallocationName(hospital.getLocation().getCity());
            }

            return dto;
		        }).collect(Collectors.toList());
		}
		

		
		  public List<Hospital> getActiveHospitalsBySpecialization(String specialization) {
		        return hospitalRepository.findBySpecializationIgnoreCaseAndStatus(specialization, "Active");
		    }
		  
		  public List<Hospital> getHospitalsByCitys(String city) {
			    return hospitalRepository.findByLocation_CityIgnoreCaseAndStatus(city, "Active");
			}
			
}
