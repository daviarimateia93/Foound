package net.foound.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Role extends BaseEntity
{
	private static final long serialVersionUID = 5051848181596785779L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_ROLE", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false, unique = true, length = 40)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 255)
	private String description;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
