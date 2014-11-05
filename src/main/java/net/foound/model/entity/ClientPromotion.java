package net.foound.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENTS_PROMOTIONS")
public class ClientPromotion extends BaseEntity
{
	private static final long serialVersionUID = 8272141326971784192L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_CLIENTS_PROMOTIONS", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_CLIENT", nullable = false, updatable = false)
	private Client client;
	
	@ManyToOne
	@JoinColumn(name = "ID_PROMOTION", nullable = false, updatable = false)
	private Promotion promotion;
	
	@Column(name = "CURRENT_SCORE", nullable = false)
	private Long currentScore;
	
	@Column(name = "COMPLETE", nullable = false)
	private Boolean complete;
	
	@Column(name = "UPDATE_TIME", nullable = false)
	private Date updateTime = new Date();
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Client getClient()
	{
		return client;
	}
	
	public void setClient(Client client)
	{
		this.client = client;
	}
	
	public Promotion getPromotion()
	{
		return promotion;
	}
	
	public void setPromotion(Promotion promotion)
	{
		this.promotion = promotion;
	}
	
	public Long getCurrentScore()
	{
		return currentScore;
	}
	
	public void setCurrentScore(Long currentScore)
	{
		this.currentScore = currentScore;
	}
	
	public Boolean isComplete()
	{
		return complete;
	}
	
	public void setComplete(Boolean complete)
	{
		this.complete = complete;
	}
	
	public Date getUpdateTime()
	{
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
}
