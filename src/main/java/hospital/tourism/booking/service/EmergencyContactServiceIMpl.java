package hospital.tourism.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.booking.entity.EmergencyContact;
import hospital.tourism.booking.repo.EmergencyContactRepository;

@Service
public class EmergencyContactServiceIMpl {

	
	@Autowired
	private EmergencyContactRepository emergencyContactRepository;
	
	
	public EmergencyContact saveEmergencyContact(EmergencyContact emergencyContact) {
		if (emergencyContact.getPhoneNumber() == null || emergencyContact.getPhoneNumber() <= 0 || emergencyContact.getPhoneNumber().toString().length() != 10) {
			throw new IllegalArgumentException("Phone number must be a 10-digit number");
		}
		return emergencyContactRepository.save(emergencyContact);
	}
	
	public List<EmergencyContact> getAllEmergencyContacts() {
		return emergencyContactRepository.findAll();
	}
	
	public EmergencyContact getEmergencyContactById(Integer id) {
		return emergencyContactRepository.findById(id).orElse(null);
	}

	public void deleteEmergencyContact(Integer id) {
		emergencyContactRepository.deleteById(id);
	}

	public EmergencyContact updateEmergencyContact(Integer id, EmergencyContact emergencyContact) {
		if (emergencyContactRepository.existsById(id)) {
			if (emergencyContact.getPhoneNumber() == null || emergencyContact.getPhoneNumber() <= 0
					|| emergencyContact.getPhoneNumber().toString().length() != 10) {
				throw new IllegalArgumentException("Phone number must be a 10-digit number");
			}
			else {
				emergencyContact.setEmergencyContactId(id);
			}
			
			return emergencyContactRepository.save(emergencyContact);
		}
		return null; 
		
		
	}
}
