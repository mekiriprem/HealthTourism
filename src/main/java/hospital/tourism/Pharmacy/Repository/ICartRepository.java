package hospital.tourism.Pharmacy.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.tourism.Pharmacy.Entity.CartItemsEntity;

public interface ICartRepository extends JpaRepository<CartItemsEntity, Integer> {
	 List<CartItemsEntity> findByUserId(Integer userId);
	    Optional<CartItemsEntity> findByUserIdAndMadicineid(Integer userId, Integer madicineid);
	    void deleteByUserIdAndMadicineid(Integer userId, Integer madicineid);
}
