package net.foound.model.repository;

import java.util.List;

import net.foound.model.entity.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>
{
	@Query("SELECT c FROM Client c WHERE c.user.id = :userId")
	public Client findByUserId(@Param("userId") Long userId);
	
	@Query("SELECT c FROM Client c WHERE c.email = :email")
	public Client findByEmail(@Param("email") String email);
	
	@Query(value = "SELECT * FROM CLIENTS WHERE ID_CLIENT IN (SELECT ID_CLIENT FROM CLIENTS_PROMOTIONS WHERE ID_PROMOTION = :promotionId GROUP BY ID_PROMOTION, ID_CLIENT)", nativeQuery = true)
	public List<Client> findInPromotion(@Param("promotionId") Long promotionId);
}
