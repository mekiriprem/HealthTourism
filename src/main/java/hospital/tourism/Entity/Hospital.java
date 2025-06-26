package hospital.tourism.Entity;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity

@Data

@SQLDelete(sql = "UPDATE hospital SET status = 'Inactive' WHERE hospital_id = ?")
@Table(name = "hospitals")
public class Hospital { 

// Temporarily comment out soft delete to debug
// @SQLDelete(sql = "UPDATE hospital SET status = 'Inactive' WHERE hospital_id = ?")

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "hospital_id")
        private Integer hospitalId;
        
        @Column(name = "hospital_name")
        private String hospitalName;
        
        @Column(name = "hospital_description")
        private String hospitalDescription;
        
        @Column(name = "hospital_image")
        private String hospitalImage;
        
        @Column(name = "rating")
        private String rating;
        
        @Column(name = "address")
        private String address;
        
        @Column(name = "specialization")
        private String specialization;
        
        @Column(name = "status")
        private String status;
        
        @Column(name = "hospitallocation_id")
        private Integer hospitallocationId;
       
        @ManyToOne
        @JoinColumn(name = "location_id")
        @JsonBackReference
        private LocationEntity location;
        
        @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonManagedReference("hospital-doctors")
        private List<Doctors> doctors;
        
        // Constructor
        public Hospital() {}
        
        // Getters and Setters
        public Integer getHospitalId() {
            return hospitalId;
        }
        
        public void setHospitalId(Integer hospitalId) {
            this.hospitalId = hospitalId;
        }
        
        public String getHospitalName() {
            return hospitalName;
        }
        
        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }
        
        public String getHospitalDescription() {
            return hospitalDescription;
        }
        
        public void setHospitalDescription(String hospitalDescription) {
            this.hospitalDescription = hospitalDescription;
        }
        
        public String getHospitalImage() {
            return hospitalImage;
        }
        
        public void setHospitalImage(String hospitalImage) {
            this.hospitalImage = hospitalImage;
        }
        
        public String getRating() {
            return rating;
        }
        
        public void setRating(String rating) {
            this.rating = rating;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public Integer getHospitallocationId() {
            return hospitallocationId;
        }
        
        public void setHospitallocationId(Integer hospitallocationId) {
            this.hospitallocationId = hospitallocationId;
        }
    }


