package net.foound.model.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.foound.util.Constants;
import net.foound.util.ControllerHelper;
import net.foound.util.I18n;
import net.foound.util.I18n.Language;

import org.springframework.stereotype.Service;

@Service
public class I18nService
{
	public static final String COOKIE_NAME_LANGUAGE = "LANGUAGE";
	
	public static Language getLanguage(HttpServletRequest request)
	{
		Cookie i18nCookie = ControllerHelper.getCookie(I18nService.COOKIE_NAME_LANGUAGE, request);
		
		return i18nCookie != null ? I18n.getLanguage(i18nCookie.getValue()) : null;
	}
	
	public static void setLanguage(HttpServletResponse response)
	{
		setLanguage(null, response);
	}
	
	public static void setLanguage(I18n.Language language, HttpServletResponse response)
	{
		Cookie cookie = createCookie(language);
		
		response.addCookie(cookie);
	}
	
	private static Cookie createCookie(I18n.Language language)
	{
		if(language == null)
		{
			language = I18n.getLanguage(Constants.EMPTY);
		}
		
		Cookie cookie = new Cookie(COOKIE_NAME_LANGUAGE, language.getValue());
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		
		return cookie;
	}
}
