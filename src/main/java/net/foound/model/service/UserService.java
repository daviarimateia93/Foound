package net.foound.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.foound.exception.HttpException;
import net.foound.model.entity.Role;
import net.foound.model.entity.Status;
import net.foound.model.entity.User;
import net.foound.model.repository.UserRepository;
import net.foound.util.Constants;
import net.foound.util.StringHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private RoleService roleService;
	
	@Transactional(readOnly = true)
	public List<User> findAll()
	{
		return userRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public User findByNickname(String nickname)
	{
		return userRepository.findByNickname(nickname);
	}
	
	@Transactional
	// must not create a new role or status
	public User saveAsInsert(User user, boolean encrypt)
	{
		validateAsInsert(user);
		
		return save(user, encrypt);
	}
	
	@Transactional
	// must not create a new role or status
	public User saveAsUpdate(User user, boolean encrypt)
	{
		validateAsUpdate(user);
		
		return save(user, encrypt);
	}
	
	@Transactional
	public User saveAsUpdate(Long id, User user, boolean encrypt)
	{
		if(userRepository.findOne(id) != null)
		{
			user.setId(id);
			
			return saveAsUpdate(user, encrypt);
		}
		else
		{
			throw new HttpException(Constants.TEXT_USER_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private User save(User user, boolean encrypt)
	{
		return userRepository.save(encrypt ? encrypt(user) : user);
	}
	
	public User setStatus(User user)
	{
		Status status = statusService.findByName(user.getStatus().getName());
		
		user.setStatus(status);
		
		return user;
	}
	
	public boolean hasRole(User user, String roleName)
	{
		for(Role role : user.getRoles())
		{
			if(role.getName().equals(roleName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasRoles(User user, String... roleNames)
	{
		for(String roleName : roleNames)
		{
			if(!hasRole(user, roleName))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isInRoles(User user, String... roleNames)
	{
		for(String roleName : roleNames)
		{
			for(Role role : user.getRoles())
			{
				if(role.getName().equals(roleName))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Transactional(readOnly = true)
	public User getFromRequest(HttpServletRequest request)
	{
		if(authenticationService.isLoggedIn(request))
		{
			return (User) request.getSession().getAttribute(AuthenticationService.SESSION_ATTRIBUTE_USER);
		}
		else
		{
			String userCredentials = request.getHeader(Constants.TEXT_HEADER_USER_CREDENTIALS);
			
			if(StringUtils.isNotEmpty(userCredentials))
			{
				if(userCredentials.contains("@"))
				{
					String[] credentials = userCredentials.split("\\@");
					
					return userRepository.findByNickname(credentials[0]);
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	}
	
	public String encryptPassword(String decryptedPassword)
	{
		MessageDigest sha512 = null;
		
		try
		{
			sha512 = MessageDigest.getInstance(Constants.TEXT_ALGORITHM_SHA_512);
		}
		catch(NoSuchAlgorithmException exception)
		{
			throw new HttpException("NoSuchAlgorithmException for MessageDigest.getInstance(\"SHA-512\"))", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try
		{
			return StringHelper.hex(sha512.digest(decryptedPassword.getBytes()));
		}
		catch(Exception exception)
		{
			return null;
		}
	}
	
	public User encrypt(User decryptedUser)
	{
		decryptedUser.setPassword(encryptPassword(decryptedUser.getPassword()));
		
		return decryptedUser;
	}
	
	@Transactional
	public User changePassword(HttpServletRequest request, String oldPassword, String newPassword)
	{
		User user = getFromRequest(request);
		
		if(user.getPassword().equals(encryptPassword(oldPassword)))
		{
			user.setPassword(newPassword);
			
			return saveAsUpdate(user, true);
		}
		else
		{
			throw new HttpException(Constants.TEXT_USER_INCORRECT_PASSWORD, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(User user)
	{
		validateIgnoringId(user);
		
		if(user.getId() != null)
		{
			throw new HttpException(Constants.TEXT_USER_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(userRepository.findByNickname(user.getNickname()) != null)
		{
			throw new HttpException(Constants.TEXT_USER_NICKNAME_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdate(User user)
	{
		validateIgnoringId(user);
		
		if(user.getId() == null)
		{
			throw new HttpException(Constants.TEXT_USER_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(userRepository.findOne(user.getId()) == null)
		{
			throw new HttpException(Constants.TEXT_USER_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		
		User foundUserByNickname = userRepository.findByNickname(user.getNickname());
		
		if(foundUserByNickname != null)
		{
			if(!foundUserByNickname.getId().equals(user.getId()))
			{
				throw new HttpException(Constants.TEXT_USER_NICKNAME_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	
	@Transactional(readOnly = true)
	private void validateIgnoringId(User user)
	{
		if(user == null)
		{
			throw new HttpException(Constants.TEXT_USER_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(user.getStatus() == null)
		{
			throw new HttpException(Constants.TEXT_USER_STATUS_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(user.getStatus().getId() == null)
		{
			throw new HttpException(Constants.TEXT_USER_STATUS_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Status status = statusService.findOne(user.getStatus().getId());
		
		if(status == null)
		{
			throw new HttpException(Constants.TEXT_USER_STATUS_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		
		user.setStatus(status);
		
		if(StringUtils.isEmpty(user.getNickname()))
		{
			throw new HttpException(Constants.TEXT_USER_NICKNAME_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(user.getNickname().length() > 40)
		{
			throw new HttpException(Constants.TEXT_USER_NICKNAME_MUST_NOT_BE_BIGGER_THAN_40_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(user.getPassword()))
		{
			throw new HttpException(Constants.TEXT_USER_PASSWORD_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(user.getPassword().length() > 128)
		{
			throw new HttpException(Constants.TEXT_USER_PASSWORD_MUST_NOT_BE_BIGGER_THAN_128_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(user.getRoles() != null)
		{
			Set<Role> roles = new HashSet<Role>();
			
			for(Role role : user.getRoles())
			{
				Role foundRole = roleService.findOne(role.getId());
				
				if(foundRole == null)
				{
					throw new HttpException(Constants.TEXT_USER_ROLE_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
				}
				
				roles.add(foundRole);
			}
			
			user.setRoles(roles);
		}
	}
}
