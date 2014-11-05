package net.foound.model.repository;

import net.foound.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	@Query("SELECT u FROM User u WHERE u.nickname = :nickname")
	public User findByNickname(@Param("nickname") String nickname);
}
