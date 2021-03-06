package net.foound.model.repository;

import net.foound.model.entity.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>
{
	@Query("SELECT s FROM Status s WHERE s.name = :name")
	public Status findByName(@Param("name") String name);
}
