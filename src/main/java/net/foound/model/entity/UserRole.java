package net.foound.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS_ROLES")
public class UserRole extends BaseEntity
{
	private static final long serialVersionUID = -4546258046554474659L;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_USER", nullable = false, updatable = false)
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "ID_ROLE", nullable = false, updatable = false)
	private Role role;
	
}
