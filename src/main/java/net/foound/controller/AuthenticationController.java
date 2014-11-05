package net.foound.controller;

import java.util.HashMap;
import java.util.Map;

import net.foound.WebMvcConfig;
import net.foound.exception.HttpException;
import net.foound.model.entity.User;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController extends BaseController
{
	private static final Map<String, DeferredResult<User>> authenticationLogoutRequests = new HashMap<>();
	
	public static synchronized final void notifyAuthenticationLogoutRequests(String sessionId, User user)
	{
		if(authenticationLogoutRequests.containsKey(sessionId))
		{
			authenticationLogoutRequests.get(sessionId).setResult(user);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return view("full", "authentication", getI18n().get("TITLE_AUTHENTICATION"));
		}
		
		return redirect(WebMvcConfig.getEnvironment().getProperty("posAuthenticationPage"));
	}
	
	@RequestMapping(value = "/isAuthenticated", method = RequestMethod.GET)
	public @ResponseBody boolean isAuthenticated()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
			
			return true;
		}
		catch(HttpException httpException)
		{
			return false;
		}
	}
	
	@RequestMapping(value = "/registerLogout", method = RequestMethod.GET)
	public @ResponseBody DeferredResult<User> getLogout()
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		final DeferredResult<User> deferredResult = new DeferredResult<>();
		
		deferredResult.onTimeout(new Runnable()
		{
			@Override
			public void run()
			{
				authenticationLogoutRequests.remove(deferredResult);
			}
		});
		
		deferredResult.onCompletion(new Runnable()
		{
			@Override
			public void run()
			{
				authenticationLogoutRequests.remove(deferredResult);
			}
		});
		
		authenticationLogoutRequests.put(getRequest().getSession(false).getId(), deferredResult);
		
		return deferredResult;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public View login(User user)
	{
		getAuthenticationService().login(getRequest(), user.getNickname(), user.getPassword());
		
		return redirect(WebMvcConfig.getEnvironment().getProperty("posAuthenticationPage"));
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public View logout()
	{
		if(getAuthenticationService().isLoggedIn(getRequest()))
		{
			User user = getUserService().getFromRequest(getRequest());
			
			String sessionId = getRequest().getSession(false).getId();
			
			getAuthenticationService().logout(getRequest());
			
			notifyAuthenticationLogoutRequests(sessionId, user);
		}
		
		return redirect("/");
	}
}
