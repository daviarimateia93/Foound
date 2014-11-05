package net.foound.controller;

import net.foound.WebMvcConfig;
import net.foound.exception.HttpException;
import net.foound.model.entity.Establishment;
import net.foound.util.View;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class InitialController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		return redirect(WebMvcConfig.getEnvironment().getProperty("initialPage"));
	}
	
	@RequestMapping(value = "/{establishmentUserNickname}", method = RequestMethod.GET)
	public View establishment(@PathVariable String establishmentUserNickname)
	{
		Establishment establishment = getEstablishmentService().findByUserNickname(establishmentUserNickname);
		
		if(establishment == null)
		{
			throw new HttpException(HttpStatus.NOT_FOUND);
		}
		
		return redirect("/establishment/" + establishment.getId());
	}
}
