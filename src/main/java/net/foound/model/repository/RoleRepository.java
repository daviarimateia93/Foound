package net.foound.model.repository;

import net.foound.model.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
	@Query("SELECT r FROM Role r WHERE r.name = :name")
	public Role findByName(@Param("name") String name);
}
