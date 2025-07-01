package hospital.tourism.booking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.tourism.booking.entity.EmergencyContact;
import hospital.tourism.booking.service.EmergencyContactServiceIMpl;


@RestController
@RequestMapping("/emergency-contact")
public class EmergencyContactController {

	@Autowired
	private EmergencyContactServiceIMpl emergencyContactServiceIMpl;
	
	@PostMapping("/add")
	public ResponseEntity<EmergencyContact> addEmergencyContact(@RequestBody EmergencyContact emergencyContact) {
		EmergencyContact savedContact = emergencyContactServiceIMpl.saveEmergencyContact(emergencyContact);
		return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<EmergencyContact>> getAllEmergencyContacts() {
		List<EmergencyContact> contacts = emergencyContactServiceIMpl.getAllEmergencyContacts();
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<EmergencyContact> getEmergencyContactById(@RequestBody Integer id) {
		EmergencyContact contact = emergencyContactServiceIMpl.getEmergencyContactById(id);
		if (contact != null) {
			return new ResponseEntity<>(contact, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<EmergencyContact> updateEmergencyContact(@PathVariable Integer id,
			@RequestBody EmergencyContact emergencyContact) {
		EmergencyContact updatedContact = emergencyContactServiceIMpl.updateEmergencyContact(id, emergencyContact);
		if (updatedContact != null) {
			return new ResponseEntity<>(updatedContact, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Integer id) {
		emergencyContactServiceIMpl.deleteEmergencyContact(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
