package net.foound.controller;

import net.foound.exception.HttpException;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/join")
public class JoinController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index(@RequestParam(value = "form-success", required = false) final boolean formSuccess)
	{
		if(formSuccess)
		{
			return redirect("/authentication");
		}
		else
		{
			try
			{
				authenticate(Constants.TEXT_ROLE_CLIENT, Constants.TEXT_ROLE_ESTABLISHMENT);
			}
			catch(HttpException httpException)
			{
				return view("full", "join", getI18n().get("TITLE_JOIN"));
			}
			
			return redirect("/");
		}
	}
}
