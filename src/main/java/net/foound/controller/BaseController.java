package net.foound.controller;

import javax.servlet.http.HttpServletRequest;

import net.foound.model.entity.Client;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.model.entity.User;
import net.foound.model.service.AuthenticationService;
import net.foound.model.service.ClientService;
import net.foound.model.service.EstablishmentService;
import net.foound.model.service.GoogleService;
import net.foound.model.service.PromotionService;
import net.foound.model.service.RoleService;
import net.foound.model.service.StatusService;
import net.foound.model.service.SupportService;
import net.foound.model.service.UserService;
import net.foound.util.I18n;
import net.foound.util.View;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController
{
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private EstablishmentService establishmentService;
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private GoogleService googleService;
	
	@Autowired
	private SupportService supportService;
	
	protected HttpServletRequest getRequest()
	{
		return request;
	}
	
	protected AuthenticationService getAuthenticationService()
	{
		return authenticationService;
	}
	
	protected ClientService getClientService()
	{
		return clientService;
	}
	
	protected EstablishmentService getEstablishmentService()
	{
		return establishmentService;
	}
	
	protected PromotionService getPromotionService()
	{
		return promotionService;
	}
	
	protected UserService getUserService()
	{
		return userService;
	}
	
	protected StatusService getStatusService()
	{
		return statusService;
	}
	
	protected RoleService getRoleService()
	{
		return roleService;
	}
	
	protected GoogleService getGoogleService()
	{
		return googleService;
	}
	
	protected SupportService getSupportService()
	{
		return supportService;
	}
	
	protected void authenticate(String... roleNames)
	{
		authenticationService.authenticate(request, roleNames);
	}
	
	protected void authenticateByUserId(Long id)
	{
		User user = new User();
		user.setId(id);
		
		authenticationService.authenticate(request, user);
	}
	
	protected void authenticateByClientId(Long id)
	{
		Client client = new Client();
		client.setId(id);
		
		authenticationService.authenticate(request, client);
	}
	
	protected void authenticateByEstablishmentId(Long establishmentId)
	{
		Establishment establishment = new Establishment();
		establishment.setId(establishmentId);
		
		authenticationService.authenticate(request, establishment);
	}
	
	protected void authenticateByPromotionId(Long promotionId)
	{
		Promotion promotion = new Promotion();
		promotion.setId(promotionId);
		
		authenticationService.authenticate(request, promotion);
	}
	
	protected View redirect(String path)
	{
		return View.redirect(path);
	}
	
	protected View view(String viewName)
	{
		return view(viewName, null);
	}
	
	protected View view(String viewName, String title)
	{
		return view(null, viewName, title);
	}
	
	protected View view(String layoutName, String partialViewName, String title)
	{
		Client client = getClientService().getFromRequest(getRequest());
		Establishment establishment = getEstablishmentService().getFromRequest(getRequest());
		
		View view = new View(layoutName, partialViewName, title);
		view.addObject("client", client);
		view.addObject("establishment", establishment);
		
		return view;
	}
	
	protected I18n getI18n()
	{
		return new I18n(I18n.getLanguageFromRequest(request));
	}
}
