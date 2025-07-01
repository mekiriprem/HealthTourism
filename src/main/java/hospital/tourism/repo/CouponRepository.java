package hospital.tourism.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Entity.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
	Optional<CouponEntity> findByCodeAndIsActiveTrue(String code);

	CouponEntity findByCode(String couponCode);
}
