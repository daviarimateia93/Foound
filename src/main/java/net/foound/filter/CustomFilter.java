package net.foound.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.foound.WebMvcConfig;
import net.foound.model.service.I18nService;
import net.foound.util.I18n;

import org.springframework.web.filter.OncePerRequestFilter;

public class CustomFilter extends OncePerRequestFilter
{
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		String url = request.getRequestURL().toString();
		
		if(request.getQueryString() != null)
		{
			url += "?" + request.getQueryString();
		}
		
		response.addHeader("X-App-Name", WebMvcConfig.APP_NAME);
		response.addHeader("X-App-Version", WebMvcConfig.APP_VERSION);
		response.addHeader("X-Url", url);
		
		I18n.Language language = I18nService.getLanguage(request);
		
		if(language == null)
		{
			I18nService.setLanguage(response);
		}
		
		I18n.setLanguageForRequest(language, request);
		
		filterChain.doFilter(request, response);
	}
}
