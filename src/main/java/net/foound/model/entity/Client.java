package net.foound.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "CLIENTS")
public class Client extends BaseEntity
{
	private static final long serialVersionUID = -8209255439996844446L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_CLIENT", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ID_USER", nullable = false, unique = true, updatable = false)
	private User user;
	
	@Column(name = "NAME", nullable = false, length = 120)
	private String name;
	
	@Column(name = "BIRTHDAY", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private Date birthday;
	
	@Column(name = "EMAIL", nullable = false, length = 255, unique = true)
	private String email;
	
	@Column(name = "SEX", nullable = false, length = 1)
	private Character sex;
	
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<ClientPromotion> clientsPromotions;
	
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
	
	public Date getBirthday()
	{
		return birthday;
	}
	
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public Character getSex()
	{
		return sex;
	}
	
	public void setSex(Character sex)
	{
		this.sex = sex;
	}
	
	public Set<ClientPromotion> getClientsPromotions()
	{
		return clientsPromotions;
	}
	
	public void setClientPromotion(Set<ClientPromotion> clientsPromotions)
	{
		this.clientsPromotions = clientsPromotions;
	}
	
	public List<PromotionExtendedInfo> getPromotions()
	{
		List<PromotionExtendedInfo> promotionsScores = new ArrayList<>();
		
		if(clientsPromotions != null)
		{
			for(ClientPromotion clientPromotion : clientsPromotions)
			{
				promotionsScores.add(new PromotionExtendedInfo(clientPromotion.getPromotion(), clientPromotion.getId(), clientPromotion.getCurrentScore(), clientPromotion.isComplete(), clientPromotion.getUpdateTime()));
			}
		}
		
		Collections.sort(promotionsScores, new Comparator<PromotionExtendedInfo>()
		{
			@Override
			public int compare(PromotionExtendedInfo o1, PromotionExtendedInfo o2)
			{
				int updateTimeComparison = o2.getUpdateTime().compareTo(o1.getUpdateTime());
				
				int dateComparison = o2.getCreation().compareTo(o1.getCreation());
				
				int nameComparison = o1.getTitle().compareTo(o2.getTitle());
				
				int scoreComparison = o1.getCurrentScore().compareTo(o2.getCurrentScore());
				
				return updateTimeComparison != 0 ? updateTimeComparison : nameComparison != 0 ? nameComparison : scoreComparison != 0 ? scoreComparison : dateComparison;
			}
		});
		
		return promotionsScores;
	}
	
	public static class PromotionExtendedInfo extends Promotion
	{
		private static final long serialVersionUID = 1L;
		
		private Establishment establishment;
		private Long clientPromotionId;
		private Long currentScore;
		private Boolean complete;
		private Date updateTime;
		
		public PromotionExtendedInfo(Promotion promotion, Long clientPromotionId, Long currentScore, Boolean complete, Date updateTime)
		{
			this.establishment = promotion.getEstablishment();
			this.clientPromotionId = clientPromotionId;
			this.currentScore = currentScore;
			this.complete = complete;
			this.updateTime = updateTime;
			
			setId(promotion.getId());
			setEstablishment(promotion.getEstablishment());
			setTitle(promotion.getTitle());
			setLimitScore(promotion.getLimitScore());
			setDescription(promotion.getDescription());
			setCreation(promotion.getCreation());
			setActive(promotion.isActive());
		}
		
		public Establishment getEstablishment()
		{
			return establishment;
		}
		
		public Long getClientPromotionId()
		{
			return clientPromotionId;
		}
		
		public Long getCurrentScore()
		{
			return currentScore;
		}
		
		@JsonProperty("complete")
		public boolean isComplete()
		{
			return complete;
		}
		
		public Date getUpdateTime()
		{
			return updateTime;
		}
	}
}
