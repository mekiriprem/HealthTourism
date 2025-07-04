package hospital.tourism.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "coupon_services")
public class CouponServiceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceType; // e.g., "SPA", "DOCTOR", "CHEF"

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponEntity coupon;
}