package net.foound.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SPECIALITIES")
public class Speciality extends BaseEntity
{
	private static final long serialVersionUID = 5202667200641414427L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_SPECIALITY", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false, length = 40)
	private String name;
	
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
}
