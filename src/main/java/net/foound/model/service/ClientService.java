package net.foound.model.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.foound.exception.HttpException;
import net.foound.model.entity.Client;
import net.foound.model.entity.ClientPromotion;
import net.foound.model.entity.Promotion;
import net.foound.model.entity.User;
import net.foound.model.entity.Client.PromotionExtendedInfo;
import net.foound.model.repository.ClientPromotionRepository;
import net.foound.model.repository.ClientRepository;
import net.foound.model.repository.PromotionRepository;
import net.foound.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService
{
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientPromotionRepository clientPromotionRepository;
	
	@Autowired
	private PromotionRepository promotionRepository;
	
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = true)
	public List<Client> findAll()
	{
		return clientRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Client> findInPromotion(Long promotionId)
	{
		return clientRepository.findInPromotion(promotionId);
	}
	
	@Transactional
	// must create a new user
	public Client saveAsInsert(Client client)
	{
		validateAsInsert(client);
		
		return save(client);
	}
	
	@Transactional
	// must not create a new user
	// can update an old user
	public Client saveAsUpdate(Client client)
	{
		validateAsUpdate(client);
		
		return save(client);
	}
	
	@Transactional
	public Client saveAsUpdate(Long id, Client client)
	{
		if(clientRepository.findOne(id) != null)
		{
			client.setId(id);
			
			return saveAsUpdate(client);
		}
		else
		{
			throw new HttpException(Constants.TEXT_CLIENT_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Client save(Client client)
	{
		return clientRepository.save(client);
	}
	
	@Transactional(readOnly = true)
	public Client getFromRequest(HttpServletRequest request)
	{
		User user = userService.getFromRequest(request);
		
		if(user != null)
		{
			return clientRepository.findByUserId(user.getId());
		}
		else
		{
			return null;
		}
	}
	
	public PromotionExtendedInfo getPromotion(Client client, Long promotionId)
	{
		PromotionExtendedInfo promotion = null;
		
		for(PromotionExtendedInfo promotionExtendedInfo : client.getPromotions())
		{
			// Test if is the required promotion
			if(promotionExtendedInfo.getId() == promotionId)
			{
				// Test if there is already a found promotion, otherwise assign
				// the promotion
				if(promotion != null)
				{
					// Test if it was completed, than assign the last promotion
					// to it. As we should know that there is not 2 uncompleted
					// promotions
					if(promotion.isComplete())
					{
						promotion = promotionExtendedInfo;
					}
				}
				else
				{
					promotion = promotionExtendedInfo;
				}
				
				if(!promotion.isComplete())
				{
					break;
				}
			}
		}
		
		return promotion;
	}
	
	@Transactional
	public Client addScoreToPromotion(String clientUserNickname, Long promotionId, Long score)
	{
		User user = userService.findByNickname(clientUserNickname);
		
		if(user == null)
		{
			throw new HttpException(Constants.TEXT_USER_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			Client client = clientRepository.findByUserId(user.getId());
			
			if(client == null)
			{
				throw new HttpException(Constants.TEXT_USER_IS_NOT_A_CLIENT, HttpStatus.NOT_ACCEPTABLE);
			}
			else
			{
				return addScoreToPromotion(client.getId(), promotionId, score);
			}
		}
	}
	
	@Transactional
	public Client addScoreToPromotion(Long clientId, Long promotionId, Long score)
	{
		Client client = clientRepository.findOne(clientId);
		
		if(client == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			PromotionExtendedInfo promotionExtendedInfo = getPromotion(client, promotionId);
			
			if(promotionExtendedInfo == null)
			{
				Promotion promotion = promotionRepository.findOne(promotionId);
				
				promotionExtendedInfo = new PromotionExtendedInfo(promotion, null, null, null, null);
				
				if(promotion == null)
				{
					throw new HttpException(Constants.TEXT_PROMOTION_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
				}
			}
			
			return addScoreToPromotion(client, promotionExtendedInfo, score);
		}
	}
	
	@Transactional
	private Client addScoreToPromotion(Client client, PromotionExtendedInfo promotion, Long score)
	{
		if(promotion.getEstablishment().getUser().getStatus().getName().equals(Constants.TEXT_STATUS_INACTIVE))
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_MUST_BE_ACTIVE, HttpStatus.NOT_ACCEPTABLE);
		}
		else if(client.getUser().getStatus().getName().equals(Constants.TEXT_STATUS_INACTIVE))
		{
			throw new HttpException(Constants.TEXT_CLIENT_MUST_BE_ACTIVE, HttpStatus.NOT_ACCEPTABLE);
		}
		else if(promotion.isActive() == false)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_MUST_BE_ACTIVE, HttpStatus.NOT_ACCEPTABLE);
		}
		else if(score < 0)
		{
			throw new HttpException(Constants.TEXT_SCORE_CAN_NOT_BE_NEGATIVE, HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			ClientPromotion clientPromotion = promotion.getClientPromotionId() != null ? clientPromotionRepository.findOne(promotion.getClientPromotionId()) : null;
			
			if(clientPromotion == null)
			{
				clientPromotion = new ClientPromotion();
				clientPromotion.setClient(client);
				clientPromotion.setPromotion(promotionRepository.findOne(promotion.getId()));
				clientPromotion.setCurrentScore(0L);
				clientPromotion.setComplete(false);
			}
			
			Long totalScore = score;
			
			do
			{
				Long scoreToAdd = clientPromotion.getCurrentScore() + totalScore > clientPromotion.getPromotion().getLimitScore() ? clientPromotion.getPromotion().getLimitScore() - clientPromotion.getCurrentScore() : totalScore;
				
				totalScore -= scoreToAdd;
				
				clientPromotion.setCurrentScore(clientPromotion.getCurrentScore() + scoreToAdd);
				clientPromotion.setComplete(clientPromotion.getCurrentScore() == clientPromotion.getPromotion().getLimitScore());
				clientPromotion.setUpdateTime(new Date());
				
				client.getClientsPromotions().add(clientPromotion);
				
				clientPromotionRepository.save(clientPromotion);
				
				Client lastClientPromotionClient = clientPromotion.getClient();
				Promotion lastClientPromotionPromotion = clientPromotion.getPromotion();
				
				clientPromotion = new ClientPromotion();
				clientPromotion.setClient(lastClientPromotionClient);
				clientPromotion.setPromotion(lastClientPromotionPromotion);
				clientPromotion.setCurrentScore(0L);
				clientPromotion.setComplete(false);
			}
			while(totalScore != 0);
		}
		
		return client;
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Client client)
	{
		validateIgnoringId(client);
		
		if(client.getId() != null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Client clientFoundByEmail = clientRepository.findByEmail(client.getEmail());
		
		if(clientFoundByEmail != null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_EMAIL_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
		
		userService.validateAsInsert(client.getUser());
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdate(Client client)
	{
		validateIgnoringId(client);
		
		if(client.getId() == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(clientRepository.findOne(client.getId()) == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Client clientFoundByEmail = clientRepository.findByEmail(client.getEmail());
		
		if(clientFoundByEmail != null)
		{
			if(!clientFoundByEmail.getId().equals(client.getId()))
			{
				throw new HttpException(Constants.TEXT_CLIENT_EMAIL_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		userService.validateAsUpdate(client.getUser());
		
		Client clientFoundByUserId = clientRepository.findByUserId(client.getUser().getId());
		
		if(clientFoundByUserId != null)
		{
			if(!clientFoundByUserId.getUser().getId().equals(client.getUser().getId()))
			{
				throw new HttpException(Constants.TEXT_CLIENT_USER_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	
	private void validateIgnoringId(Client client)
	{
		if(client == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getUser() == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_USER_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(client.getName()))
		{
			throw new HttpException(Constants.TEXT_CLIENT_NAME_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getName().length() > 120)
		{
			throw new HttpException(Constants.TEXT_CLIENT_NAME_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getBirthday() == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_BIRTHDAY_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getBirthday().after(new Date()))
		{
			throw new HttpException(Constants.TEXT_CLIENT_BIRTHDAY_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(client.getEmail()))
		{
			throw new HttpException(Constants.TEXT_CLIENT_EMAIL_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getEmail().length() > 320)
		{
			throw new HttpException(Constants.TEXT_CLIENT_EMAIL_MUST_NOT_BE_BIGGER_THAN_320_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!Pattern.compile(Constants.TEXT_PATTERN_EMAIL).matcher(client.getEmail()).matches())
		{
			throw new HttpException(Constants.TEXT_CLIENT_EMAIL_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client.getSex() == null)
		{
			throw new HttpException(Constants.TEXT_CLIENT_SEX_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// ISO/IEC 5218
		if(!client.getSex().equals(Constants.CHAR_0) && !client.getSex().equals(Constants.CHAR_1) && !client.getSex().equals(Constants.CHAR_2) && !client.getSex().equals(Constants.CHAR_9))
		{
			throw new HttpException(Constants.TEXT_CLIENT_SEX_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
