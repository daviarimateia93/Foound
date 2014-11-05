package net.foound.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class I18n
{
	public static final String ATTRIBUTE_NAME_LANGUAGE = "LANGUAGE";
	
	public static enum Language
	{
		EN("en"), PT_BR("pt-BR");
		
		private String value;
		
		private Language(String value)
		{
			this.value = value;
		}
		
		public String getValue()
		{
			return value;
		}
	};
	
	private Language language;
	
	public I18n(Language language)
	{
		this.language = language;
	}
	
	public static Language getLanguage(String language)
	{
		switch(language)
		{
			case "en":
			{
				return Language.EN;
			}
			case "pt-BR":
			{
				return Language.PT_BR;
			}
			default:
			{
				return Language.PT_BR;
			}
		}
	}
	
	public Language getLanguage()
	{
		return language;
	}
	
	public String get(String name)
	{
		InputStream inputStream = null;
		
		try
		{
			inputStream = getClass().getResourceAsStream("/i18n_" + language.getValue() + ".properties");
			
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			
			Properties properties = new Properties();
			properties.load(reader);
			
			return properties.getProperty(name);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if(inputStream != null)
				{
					inputStream.close();
				}
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void setLanguageForRequest(Language language, HttpServletRequest request)
	{
		request.setAttribute(ATTRIBUTE_NAME_LANGUAGE, language);
	}
	
	public static Language getLanguageFromRequest(HttpServletRequest request)
	{
		Language language = getLanguage("");
		
		if(request.getAttribute(ATTRIBUTE_NAME_LANGUAGE) != null)
		{
			language = (Language) request.getAttribute(ATTRIBUTE_NAME_LANGUAGE);
		}
		
		return language;
	}
}
