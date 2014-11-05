package net.foound.controller;

import net.foound.exception.HttpException;
import net.foound.util.Constants;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
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
		
		return view("full", "dashboard", getI18n().get("TITLE_DASHBOARD"));
	}
}
