package net.foound.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import net.foound.exception.HttpException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/google")
public class GoogleController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String find(@RequestParam String address, @RequestParam(required = false) final String language)
	{
		try
		{
			return getGoogleService().searchAddress(address, language);
		}
		catch(IOException | URISyntaxException exception)
		{
			throw new HttpException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
