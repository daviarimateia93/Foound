package net.foound.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.foound.exception.HttpException;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/promotion")
public class PromotionController extends BaseController
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
		
		return view("full", "promotion", getI18n().get("TITLE_PROMOTION"));
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public View index(@PathVariable Long id, @RequestParam(value = "active-promotions", required = false, defaultValue = "false") final boolean activePromotions)
	{	
		Promotion promotion = getPromotionService().findOne(id);
		
		View view = view("full", "promotion", getI18n().get("TITLE_PROMOTION"));
		view.addObject("promotion", promotion);
		view.addObject("activePromotions", activePromotions);
		
		try
		{
			authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
			
			Establishment currentEstablishment = getEstablishmentService().getFromRequest(getRequest());
			
			view.addObject("belongsToEstablishment", currentEstablishment.getId().equals(promotion.getEstablishment().getId()));
		}
		catch(HttpException httpException)
		{
			
		}
		
		return view;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public View list()
	{
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		}
		catch(HttpException httpException)
		{
			return redirect("/authentication");
		}
		
		HttpException exception = null;
		
		try
		{
			authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
			
			return redirect("/promotion/list/establishment/" + getEstablishmentService().getFromRequest(getRequest()).getId());
		}
		catch(HttpException httpException)
		{
			exception = httpException;
		}
		
		try
		{
			authenticate(Constants.TEXT_ROLE_CLIENT);
			
			return list(null);
		}
		catch(HttpException httpException)
		{
			exception = httpException;
		}
		
		throw exception;
	}
	
	@RequestMapping(value = "/list/establishment/{establishmentId}", method = RequestMethod.GET)
	public View list(@PathVariable Long establishmentId)
	{
		View view = view("full", "promotion-list", getI18n().get("TITLE_PROMOTION_LIST"));
		
		Establishment establishment = null;
		boolean sameEstablishment = false;
		
		if(establishmentId != null)
		{
			establishment = getEstablishmentService().getFromRequest(getRequest());
			
			sameEstablishment = establishment != null ? establishment.getId().equals(establishmentId) : false;
			
			if(!sameEstablishment)
			{
				establishment = getEstablishmentService().findOne(establishmentId);
			}
			
			if(establishment == null)
			{
				throw new HttpException(HttpStatus.NOT_FOUND);
			}
			
			setClientsOnPromotion(view, establishment.getPromotions());
			
			view.addObject("establishment", establishment);
			view.addObject("sameEstablishment", sameEstablishment);
		}
		
		return view;
	}
	
	@RequestMapping(value = "/list/active/establishment/{id}", method = RequestMethod.GET)
	public View listActive(@PathVariable Long id)
	{
		authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
		
		Establishment establishment = getEstablishmentService().findOne(id);
		
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
		
		Establishment currentEstablishment = getEstablishmentService().getFromRequest(getRequest());
		
		View view = view("full", "promotion-list", getI18n().get("TITLE_PROMOTION_LIST"));
		view.addObject("selectedEstablishment", establishment);
		view.addObject("activePromotions", true);
		view.addObject("sameEstablishment", currentEstablishment != null ? currentEstablishment.getId().equals(id) : false);
		
		setClientsOnPromotion(view, promotions);
		
		return view;
	}
	
	private void setClientsOnPromotion(View view, List<Promotion> promotions)
	{
		Map<Long, Integer> clientsOnPromotion = new HashMap<>();
		
		for(Promotion promotion : promotions)
		{
			clientsOnPromotion.put(promotion.getId(), getClientService().findInPromotion(promotion.getId()).size());
		}
		
		view.addObject("clientsOnPromotion", clientsOnPromotion);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Promotion save(Promotion promotion)
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		
		Establishment establishment = getEstablishmentService().getFromRequest(getRequest());
		
		promotion.setEstablishment(establishment);
		
		promotion = getPromotionService().saveAsInsert(promotion);
		
		establishment.getPromotions().add(promotion);
		
		EstablishmentController.notifyEstablishmentUpdateRequests(establishment);
		
		return promotion;
	}
	
	@RequestMapping(value = "/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Promotion activate(@PathVariable Long id, @RequestParam boolean active)
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByPromotionId(id);
		
		Promotion promotion = getPromotionService().activate(id, active);
		
		EstablishmentController.notifyEstablishmentUpdateRequests(promotion.getEstablishment());
		
		return promotion;
	}
	
	@RequestMapping(value = "/changeAvatar/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Promotion changeAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatarMultipartFile)
	{
		authenticate(Constants.TEXT_ROLE_ESTABLISHMENT);
		authenticateByPromotionId(id);
		
		Promotion promotion = getPromotionService().changeAvatar(id, avatarMultipartFile, getRequest());
		
		EstablishmentController.notifyEstablishmentUpdateRequests(promotion.getEstablishment());
		
		return promotion;
	}
}
