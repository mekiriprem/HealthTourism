//package hospital.tourism.Entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class PhysioAndSpa {
//
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer serviceId;
//	private String serviceName;
//	private String serviceDescription;
//	private String serviceImage;
//	private Double servicePrice;
//	private String rating;
//	private String reviews;
//
//	 	@ManyToOne
//	    @JoinColumn(name = "location_id") 
//	    private LocationEntity location;
//}
