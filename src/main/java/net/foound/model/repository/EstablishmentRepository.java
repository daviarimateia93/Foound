package net.foound.model.repository;

import java.math.BigDecimal;
import java.util.List;

import net.foound.model.entity.Establishment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long>
{
	@Query("SELECT e FROM Establishment e WHERE e.user.nickname = :userNickname")
	public Establishment findByUserNickname(@Param("userNickname") String userNickname);
	
	@Query("SELECT e FROM Promotion p INNER JOIN p.establishment e WHERE p.id = :promotionId")
	public Establishment findByPromotionId(@Param("promotionId") Long promotionId);
	
	@Query("SELECT e FROM Establishment e WHERE e.user.id = :userId")
	public Establishment findByUserId(@Param("userId") Long userId);
	
	@Query("SELECT e FROM Establishment e WHERE LOWER(e.name) LIKE LOWER(:namePattern)")
	public List<Establishment> searchByName(@Param("namePattern") String namePattern);
	
	@Query("SELECT e FROM Establishment e WHERE e.name = :name AND e.address = :address AND e.latitude = :latitude AND e.longitude = :longitude")
	public Establishment findByNameAddressLatitudeAndLongitude(@Param("name") String name, @Param("address") String address, @Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
}
