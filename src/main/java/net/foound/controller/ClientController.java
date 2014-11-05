package net.foound.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.foound.exception.HttpException;
import net.foound.model.entity.Client;
import net.foound.model.entity.Role;
import net.foound.model.entity.Status;
import net.foound.model.entity.User;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/client")
public class ClientController extends BaseController
{
	private static final Map<Long, DeferredResult<Client>> clientUpdateRequests = new HashMap<>();
	
	public static synchronized final void notifyClientUpdateRequests(Client client)
	{
		if(clientUpdateRequests.containsKey(client.getId()))
		{
			clientUpdateRequests.get(client.getId()).setResult(client);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		return view("full", "client", getI18n().get("TITLE_CLIENT"));
	}
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public @ResponseBody Client me()
	{
		authenticate(Constants.TEXT_ROLE_CLIENT);
		
		return getClientService().getFromRequest(getRequest());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Client save(Client client)
	{
		Set<Role> roles = new HashSet<>();
		roles.add(getRoleService().findByName(Constants.TEXT_ROLE_CLIENT));
		
		Status status = getStatusService().findByName(Constants.TEXT_STATUS_ACTIVE);
		
		User user = client.getUser();
		user.setRoles(roles);
		user.setStatus(status);
		
		getUserService().encrypt(user);
		
		return getClientService().saveAsInsert(client);
	}
	
	@RequestMapping(value = "/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Client save(@PathVariable Long id, Client client)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT);
		authenticateByClientId(id);
		
		client.setClientPromotion(getClientService().getFromRequest(getRequest()).getClientsPromotions());
		client.setUser(getUserService().getFromRequest(getRequest()));
		
		Client savedClient = getClientService().saveAsUpdate(id, client);
		
		notifyClientUpdateRequests(savedClient);
		
		return client;
	}
	
	@RequestMapping(value = "/registerUpdates", method = RequestMethod.GET)
	public @ResponseBody DeferredResult<Client> getUpdate()
	{
		authenticate(Constants.TEXT_ROLE_CLIENT);
		
		final DeferredResult<Client> deferredResult = new DeferredResult<>();
		
		deferredResult.onCompletion(new Runnable()
		{
			@Override
			public void run()
			{
				clientUpdateRequests.remove(deferredResult);
			}
		});
		
		clientUpdateRequests.put(getClientService().getFromRequest(getRequest()).getId(), deferredResult);
		
		return deferredResult;
	}
}
