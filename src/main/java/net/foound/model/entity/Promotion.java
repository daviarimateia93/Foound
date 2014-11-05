package net.foound.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PROMOTIONS")
public class Promotion extends BaseEntity
{
	private static final long serialVersionUID = 5973467257732548997L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PROMOTION", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTABLISHMENT", nullable = false, updatable = false)
	@JsonIgnore
	private Establishment establishment;
	
	@Column(name = "TITLE", nullable = false, length = 40)
	private String title;
	
	@Column(name = "LIMIT_SCORE", nullable = false)
	private Long limitScore;
	
	@Column(name = "DESCRIPTION", nullable = false, length = 255)
	private String description;
	
	@Column(name = "CREATION", nullable = false, updatable = false)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creation;
	
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active;
	
	@Column(name = "AVATAR", nullable = true)
	private String avatar;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Establishment getEstablishment()
	{
		return establishment;
	}
	
	public void setEstablishment(Establishment establishment)
	{
		this.establishment = establishment;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public Long getLimitScore()
	{
		return limitScore;
	}
	
	public void setLimitScore(Long limitScore)
	{
		this.limitScore = limitScore;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Date getCreation()
	{
		return creation;
	}
	
	public void setCreation(Date creation)
	{
		this.creation = creation;
	}
	
	public void setActive(Boolean active)
	{
		this.active = active;
	}
	
	public Boolean getActive()
	{
		return active;
	}
	
	public Boolean isActive()
	{
		return active;
	}
	
	public String getAvatar()
	{
		return avatar;
	}
	
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
}
