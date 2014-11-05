package net.foound.controller;

import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/support")
public class SupportController extends BaseController
{
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public View sendMail(@RequestParam String to, @RequestParam String subject, @RequestParam String body)
	{
		getSupportService().sendMail(to, subject, body);
		
		return View.redirect("/support");
	}
}
