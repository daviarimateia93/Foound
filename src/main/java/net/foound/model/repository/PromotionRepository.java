package net.foound.model.repository;

import net.foound.model.entity.Promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionRepository extends JpaRepository<Promotion, Long>
{
	@Query("SELECT p FROM Promotion p WHERE p.establishment.id = :establishmentId AND p.title = :title")
	public Promotion findByEstablishmentIdAndTitle(@Param("establishmentId") Long establishmentId, @Param("title") String title);
}
