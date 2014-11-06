package net.foound.model.repository;

import net.foound.model.entity.Speciality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>
{
	@Query("SELECT s FROM Speciality s WHERE s.name = :name")
	public Speciality findByName(@Param("name") String name);
}
