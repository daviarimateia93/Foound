package net.foound.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ControllerHelper
{
	public static Cookie getCookie(String name, HttpServletRequest request)
	{
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null)
		{
			for(Cookie cookie : request.getCookies())
			{
				if(cookie.getName().equals(name))
				{
					return cookie;
				}
			}
		}
		
		return null;
	}
	
	public static String fileUpload(String relativeFilePath, byte[] bytes, HttpServletRequest request) throws NoSuchAlgorithmException, IOException
	{
		if(relativeFilePath == null)
		{
			return null;
		}
		else if(relativeFilePath.isEmpty())
		{
			return null;
		}
		
		if(relativeFilePath.startsWith("/"))
		{
			relativeFilePath = relativeFilePath.substring(1, relativeFilePath.length());
		}
		
		String dir = "";
		String name = StringHelper.hex(MessageDigest.getInstance("SHA-512").digest(UUID.randomUUID().toString().getBytes()));
		String extension = "";
		
		if(relativeFilePath.contains("/"))
		{
			dir = relativeFilePath.substring(0, relativeFilePath.lastIndexOf("/"));
			
			String nameAndExtension = relativeFilePath.substring(relativeFilePath.lastIndexOf("/"), relativeFilePath.length());
			
			if(relativeFilePath.contains("."))
			{
				extension = nameAndExtension.substring(nameAndExtension.lastIndexOf("."), nameAndExtension.length());
			}
		}
		
		String basePath = request.getServletContext().getRealPath("/");
		
		File fileDir = new File(basePath + dir);
		
		if(!fileDir.exists())
		{
			fileDir.mkdirs();
		}
		
		OutputStream outputStream = new FileOutputStream(new File(basePath + dir + "/" + name + extension));
		outputStream.write(bytes);
		outputStream.flush();
		outputStream.close();
		
		return request.getContextPath() + "/" + dir + "/" + name + extension;
	}
}
