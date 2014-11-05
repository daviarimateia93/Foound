package net.foound.util;

import java.util.ArrayList;
import java.util.List;

public class StringHelper
{
	public static String shuffle(String string)
	{
		if(string.length() > 0)
		{
			List<Character> characters = new ArrayList<>();
			
			for(char character : string.toCharArray())
			{
				characters.add(character);
			}
			
			StringBuilder output = new StringBuilder(string.length());
			
			while(!characters.isEmpty())
			{
				output.append(characters.remove((int) (Math.random() * characters.size())));
			}
			
			return output.toString();
		}
		else
		{
			return string;
		}
	}
	
	public static String hex(byte[] stringBytes)
	{
		StringBuilder output = new StringBuilder();
		
		for(int i = 0; i < stringBytes.length; i++)
		{
			output.append(Integer.toString((stringBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return output.toString();
	}
}
