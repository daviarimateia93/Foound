package net.foound.controller;

import javax.servlet.http.HttpServletResponse;

import net.foound.model.service.I18nService;
import net.foound.util.Constants;
import net.foound.util.I18n;
import net.foound.util.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/i18n")
public class I18nController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View setLanguage(@RequestParam String language, HttpServletResponse response)
	{
		I18nService.setLanguage(I18n.getLanguage(language), response);
		
		return redirect(getRequest().getHeader(Constants.TEXT_HEADER_REFERER));
	}
}
