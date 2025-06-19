package hospital.tourism.Entity;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "blog_category")
public class BlogCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long blogCategoryId;
	private String blogCategoryName;
	private String blogCategoryDescription;
	private String blogCategoryImageUrl;
	private String blogCategoryCreatedBy;
	@CreationTimestamp
	private String blogCategoryCreatedDate;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Blogs> blogs;


}
