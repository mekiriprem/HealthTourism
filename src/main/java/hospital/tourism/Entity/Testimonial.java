package hospital.tourism.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "testimonials")
public class Testimonial {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String title;
	    private String description;

	    private boolean hasVideo;
	    private boolean hasNewspaperClip;

	    private String videoFileName;       // Stored file name
	    private String newspaperFileName;   // Stored file name

	    private LocalDate uploadedAt;
}
