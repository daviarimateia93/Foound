package net.foound.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.foound.exception.HttpException;
import net.foound.model.entity.Client;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.model.entity.Role;
import net.foound.model.entity.Status;
import net.foound.model.entity.User;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController extends BaseController
{
	private static final List<DeferredResult<Establishment>> establishmentUpdateRequests = new ArrayList<>();
	
	public static synchronized final void notifyEstablishmentUpdateRequests(Establishment establishment)
	{
		for(DeferredResult<Establishment> deferredResult : establishmentUpdateRequests)
		{
			deferredResult.setResult(establishment);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		return redirect("/establishment/" + getEstablishmentService().getFromRequest(getRequest()).getId());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public View profile(@PathVariable Long id)
	{
		Establishment establishment = getEstablishmentService().getFromRequest(getRequest());
		
		boolean sameEstablishment = establishment != null ? establishment.getId().equals(id) : false;
		
		if(!sameEstablishment)
		{
			establishment = getEstablishmentService().findOne(id);
		}
		
		if(establishment == null)
		{
			throw new HttpException(HttpStatus.NOT_FOUND);
		}
		
		View view = profile(establishment);
		view.addObject("sameEstablishment", sameEstablishment);
		
		return view;
	}
	
	private View profile(Establishment establishment)
	{
		View view = view("full", "establishment", getI18n().get("TITLE_ESTABLISHMENT"));
		view.addObject("establishment", establishment);
		view.addObject("topPromotions", getTopPromotions(establishment));
		
		return view;
	}
	
	private List<Promotion> getTopPromotions(Establishment establishment)
	{
		List<Promotion> topPromotions = null;
		
		if(establishment.getPromotions() != null)
		{
			topPromotions = new ArrayList<>(establishment.getPromotions());
			
			if(topPromotions.size() > 5)
			{
				for(int i = 4; i < topPromotions.size(); i++)
				{
					topPromotions.remove(i);
				}
			}
		}
		
		return topPromotions;
	}
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public @ResponseBody Establishment me()
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		
		return getEstablishmentService().getFromRequest(getRequest());
	}
	
	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
	public @ResponseBody List<Establishment> searchByName(@PathVariable String name)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		return getEstablishmentService().searchByName(name);
	}
	
	@RequestMapping(value = "/promotion/{promotionId}", method = RequestMethod.GET)
	public @ResponseBody Establishment findByPromotionId(@PathVariable Long promotionId)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		return getEstablishmentService().findByPromotionId(promotionId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Establishment save(Establishment establishment) throws Exception
	{
		Set<Role> roles = new HashSet<>();
		roles.add(getRoleService().findByName(Constants.TEXT_ROLE_ESTABLISHMENT));
		
		Status status = getStatusService().findByName(Constants.TEXT_STATUS_ACTIVE);
		
		User user = establishment.getUser();
		user.setRoles(roles);
		user.setStatus(status);
		
		getUserService().encrypt(user);
		
		establishment = getEstablishmentService().saveAsInsert(establishment);
		
		notifyEstablishmentUpdateRequests(establishment);
		
		return establishment;
	}
	
	@RequestMapping(value = "/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Establishment save(@PathVariable Long id, Establishment establishment, @RequestParam(value = "change-avatar", required = true) final boolean changeAvatar) throws Exception
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByEstablishmentId(id);
		
		establishment.setUser(getUserService().getFromRequest(getRequest()));
		
		establishment = getEstablishmentService().saveAsUpdate(id, establishment, changeAvatar);
		
		notifyEstablishmentUpdateRequests(establishment);
		
		return establishment;
	}
	
	@RequestMapping(value = "/changeAvatar/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Establishment changeAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatarMultipartFile)
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByEstablishmentId(id);
		
		Establishment establishment = getEstablishmentService().changeAvatar(id, avatarMultipartFile, getRequest());
		
		notifyEstablishmentUpdateRequests(establishment);
		
		return establishment;
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Establishment> findAll()
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		return getEstablishmentService().findAll();
	}
	
	@RequestMapping(value = "/addScoreToPromotion", method = RequestMethod.GET)
	public @ResponseBody View addScoreToPromotion()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		Establishment establishment = getEstablishmentService().getFromRequest(getRequest());
		
		List<Promotion> promotions = establishment.getPromotions();
		List<Promotion> removePromotions = new ArrayList<>();
		
		for(Promotion promotion : promotions)
		{
			if(!promotion.isActive())
			{
				removePromotions.add(promotion);
			}
		}
		
		promotions.removeAll(removePromotions);
		
		establishment.setPromotions(promotions);
		
		View view = view("full", "add-score-to-promotion", getI18n().get("TITLE_ADD_SCORE_TO_PROMOTION"));
		view.addObject("client", null);
		view.addObject("establishment", establishment);
		
		return view;
	}
	
	@RequestMapping(value = "/addScoreToPromotion", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Client addScoreToPromotion(@RequestParam String clientUserNickname, @RequestParam Long promotionId, @RequestParam Long score)
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByPromotionId(promotionId);
		
		Client client = getClientService().addScoreToPromotion(clientUserNickname, promotionId, score);
		
		ClientController.notifyClientUpdateRequests(client);
		
		return client;
	}
	
	@RequestMapping(value = "/registerUpdates", method = RequestMethod.GET)
	public @ResponseBody DeferredResult<Establishment> getUpdate()
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		final DeferredResult<Establishment> deferredResult = new DeferredResult<>();
		
		deferredResult.onTimeout(new Runnable()
		{
			@Override
			public void run()
			{
				establishmentUpdateRequests.remove(deferredResult);
			}
		});
		
		deferredResult.onCompletion(new Runnable()
		{
			@Override
			public void run()
			{
				establishmentUpdateRequests.remove(deferredResult);
			}
		});
		
		establishmentUpdateRequests.add(deferredResult);
		
		return deferredResult;
	}
}
