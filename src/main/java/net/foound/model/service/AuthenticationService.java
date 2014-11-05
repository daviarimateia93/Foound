package net.foound.model.service;

import javax.servlet.http.HttpServletRequest;

import net.foound.exception.HttpException;
import net.foound.model.entity.Client;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.model.entity.User;
import net.foound.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService
{
	public static final String SESSION_ATTRIBUTE_USER = "USER";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private EstablishmentService establishmentService;
	
	@Autowired
	private PromotionService promotionService;
	
	public boolean isLoggedIn(HttpServletRequest request)
	{
		return (request.getSession().getAttribute(SESSION_ATTRIBUTE_USER) != null);
	}
	
	public void login(HttpServletRequest request, String userNickname, String userPassword)
	{
		User user = test(userNickname, userPassword);
		
		setupLoginSession(request, user);
	}
	
	public void loginEncrypted(HttpServletRequest request, String userNickname, String userEncryptedPassword)
	{
		User user = testEncrypted(userNickname, userEncryptedPassword);
		
		setupLoginSession(request, user);
	}
	
	private void setupLoginSession(HttpServletRequest request, User user)
	{
		request.getSession().setAttribute(SESSION_ATTRIBUTE_USER, user);
	}
	
	public void logout(HttpServletRequest request)
	{
		request.getSession().invalidate();
	}
	
	public void authenticateForSession(HttpServletRequest request, String... roleNames)
	{
		if(isLoggedIn(request))
		{
			User user = (User) request.getSession().getAttribute(SESSION_ATTRIBUTE_USER);
			
			authenticate(user.getNickname(), user.getPassword(), roleNames);
		}
		else
		{
			throw new HttpException(Constants.TEXT_USER_IS_NOT_LOGGED_IN, HttpStatus.FORBIDDEN);
		}
	}
	
	public void authenticateForRequest(HttpServletRequest request, String... roleNames)
	{
		// User-Credentials must follow this format:
		// nickname@sha512(password)
		String userCredentials = request.getHeader(Constants.TEXT_HEADER_USER_CREDENTIALS);
		
		if(StringUtils.isNotEmpty(userCredentials))
		{
			if(userCredentials.contains("@"))
			{
				String[] credentials = userCredentials.split("\\@");
				
				authenticate(credentials[0], credentials[1], roleNames);
			}
			else
			{
				throw new HttpException(Constants.TEXT_INVALID_USER_CREDENTIALS_FORMAT, HttpStatus.FORBIDDEN);
			}
		}
		else
		{
			throw new HttpException(Constants.TEXT_USER_CREDENTIALS_NOT_FOUND, HttpStatus.FORBIDDEN);
		}
	}
	
	public void authenticate(HttpServletRequest request, String... roleNames)
	{
		if(isLoggedIn(request))
		{
			authenticateForSession(request, roleNames);
		}
		else
		{
			authenticateForRequest(request, roleNames);
		}
	}
	
	public void authenticate(HttpServletRequest request, User user)
	{
		User userFromRequest = userService.getFromRequest(request);
		
		if(!userFromRequest.getId().equals(user.getId()))
		{
			throw new HttpException(Constants.TEXT_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
		}
	}
	
	public void authenticate(HttpServletRequest request, Client client)
	{
		Client clientFromRequest = clientService.getFromRequest(request);
		
		if(!clientFromRequest.getId().equals(client.getId()))
		{
			throw new HttpException(Constants.TEXT_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
		}
	}
	
	public void authenticate(HttpServletRequest request, Establishment establishment)
	{
		Establishment establishmentFromRequest = establishmentService.getFromRequest(request);
		
		if(!establishmentFromRequest.getId().equals(establishment.getId()))
		{
			throw new HttpException(Constants.TEXT_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
		}
	}
	
	public void authenticate(HttpServletRequest request, Promotion promotion)
	{
		Promotion fullPromotion = promotionService.findOne(promotion.getId());
		
		if(fullPromotion != null)
		{
			authenticate(request, fullPromotion.getEstablishment());
		}
		else
		{
			throw new HttpException(Constants.TEXT_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
		}
	}
	
	public void authenticate(String userNickname, String userEncryptedPassword, String... roleNames)
	{
		User user = testEncrypted(userNickname, userEncryptedPassword);
		
		if(!userService.isInRoles(user, roleNames))
		{
			throw new HttpException(Constants.TEXT_UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
		}
		
		// At this point we are authenticated :-)
	}
	
	public User test(String userNickname, String userPassword)
	{
		return testEncrypted(userNickname, userService.encryptPassword(userPassword));
	}
	
	public User testEncrypted(String userNickname, String userEncryptedPassword)
	{
		User user = userService.findByNickname(userNickname);
		
		if(user == null)
		{
			throw new HttpException(Constants.TEXT_USER_NOT_FOUND, HttpStatus.FORBIDDEN);
		}
		else
		{
			if(!user.getPassword().equals(userEncryptedPassword))
			{
				throw new HttpException(Constants.TEXT_INVALID_USER_PASSWORD, HttpStatus.FORBIDDEN);
			}
			
			// User matchs :-)
			
			return user;
		}
	}
}
