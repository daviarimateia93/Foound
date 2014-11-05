package net.foound.model.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import net.foound.util.Constants;

import org.springframework.stereotype.Service;

@Service
public class GoogleService
{
	public static final String GOOGLE_MAPS_API_GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	
	public String searchAddress(String address, String language) throws IOException, URISyntaxException
	{
		String url = GOOGLE_MAPS_API_GEOCODE_URL + URLEncoder.encode(address, Constants.TEXT_CHARSET_UTF_8) + (language != null ? "&language=" + URLEncoder.encode(language, Constants.TEXT_CHARSET_UTF_8) : "");
		
		InputStream inputStream = new URL(url).openStream();
		
		try
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			int read;
			
			while((read = inputStream.read()) != -1)
			{
				byteArrayOutputStream.write(read);
			}
			
			return new String(byteArrayOutputStream.toByteArray(), Constants.TEXT_CHARSET_UTF_8);
		}
		finally
		{
			inputStream.close();
		}
	}
}
