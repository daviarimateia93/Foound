package net.foound.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException
{
	private static final long serialVersionUID = -6447762394307272573L;
	
	private String message;
	private HttpStatus httpStatus;
	
	public HttpException(HttpStatus httpStatus)
	{
		this(null, httpStatus);
	}
	
	public HttpException(String message, HttpStatus httpStatus)
	{
		super(message);
		
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}
}
