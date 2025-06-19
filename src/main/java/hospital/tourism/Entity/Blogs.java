package hospital.tourism.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "blogs")
public class Blogs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long blogId;

	
	private String authorEmail; // email of the author

	
	private String authorName; // name of the author


	  private String blogUrl;// e.g., "https://example.com/blogs/primary-care-overview"

	
	private String metaTitle; // for SEO purposes

	@Column(nullable = false, columnDefinition = "TEXT")
	private String metaDescription; // for SEO purposes

	 @Column(nullable = false)
	    private String title;

	   
	  // e.g., "primary-care-overview"

	    @Column(nullable = false, columnDefinition = "TEXT")
	    private String shortDescription; // for blog listing preview

	    @Lob
	    @Column(nullable = false, columnDefinition = "TEXT")
	    private String content; // full blog content (can be long)

	    @Column(nullable = false)
	    private String coverImage; // URL to blog thumbnail/cover image

	    private String author;

	    @ManyToOne
	    @JoinColumn(name = "category_id", nullable = false)
	    private BlogCategory category;

	    @Column(nullable = false)
	    @CreationTimestamp
	    private LocalDateTime createdAt;
	    @UpdateTimestamp
	    @Column(nullable = false)
	    private LocalDateTime updatedAt;
}
