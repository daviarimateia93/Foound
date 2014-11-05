package net.foound.controller;

import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		return view("home", getI18n().get("TITLE_HOME"));
	}
}
