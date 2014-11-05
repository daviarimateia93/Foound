package net.foound.model.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "ESTABLISHMENTS")
public class Establishment extends BaseEntity
{
	private static final long serialVersionUID = -1408913534170643496L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_ESTABLISHMENT", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ID_USER", nullable = false, unique = true, updatable = false)
	private User user;
	
	@Column(name = "NAME", nullable = false, length = 120)
	private String name;
	
	@Column(name = "ADDRESS", nullable = false, length = 120)
	private String address;
	
	@Column(name = "ABOUT", nullable = false, length = 255)
	private String about;
	
	@Column(name = "LATITUDE", nullable = false, precision = 11, scale = 8)
	private BigDecimal latitude;
	
	@Column(name = "LONGITUDE", nullable = false, precision = 11, scale = 8)
	private BigDecimal longitude;
	
	@Column(name = "AVATAR", nullable = true)
	private String avatar;
	
	@OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("creation DESC")
	private List<Promotion> promotions;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAbout()
	{
		return about;
	}
	
	public void setAbout(String about)
	{
		this.about = about;
	}
	
	public BigDecimal getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(BigDecimal latitude)
	{
		this.latitude = latitude;
	}
	
	public BigDecimal getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(BigDecimal longitude)
	{
		this.longitude = longitude;
	}
	
	public String getAvatar()
	{
		return avatar;
	}
	
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	
	public List<Promotion> getPromotions()
	{
		return promotions;
	}
	
	public void setPromotions(List<Promotion> promotions)
	{
		this.promotions = promotions;
	}
}
