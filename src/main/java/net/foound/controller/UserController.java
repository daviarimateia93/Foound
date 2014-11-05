package net.foound.controller;

import net.foound.exception.HttpException;
import net.foound.model.entity.User;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		return view("full", "user", getI18n().get("TITLE_USER"));
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public View changePassword()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		return view("user-change-password");
	}
	
	@RequestMapping(value = "/changePassword", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody User save(@RequestParam String newPassword, @RequestParam String oldPassword)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		return getUserService().changePassword(getRequest(), oldPassword, newPassword);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody User save(User user)
	{
		getUserService().setStatus(user);
		
		user = getUserService().saveAsInsert(user, true);
		
		return user;
	}
	
	@RequestMapping(value = "/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody User save(@PathVariable Long id, User user)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByUserId(id);
		
		boolean isUpdatingPassword = (user.getPassword() != null);
		
		getUserService().setStatus(user);
		
		user.setRoles(getUserService().getFromRequest(getRequest()).getRoles());
		
		if(!isUpdatingPassword)
		{
			user.setPassword(getUserService().getFromRequest(getRequest()).getPassword());
			
			getUserService().saveAsUpdate(id, user, false);
			
			getAuthenticationService().loginEncrypted(getRequest(), user.getNickname(), user.getPassword());
		}
		else
		{
			getUserService().saveAsUpdate(id, user, true);
			
			getAuthenticationService().login(getRequest(), user.getNickname(), user.getPassword());
		}
		
		EstablishmentController.notifyEstablishmentUpdateRequests(getEstablishmentService().getFromRequest(getRequest()));
		
		return user;
	}
}
