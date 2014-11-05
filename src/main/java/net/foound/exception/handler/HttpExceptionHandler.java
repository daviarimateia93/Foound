package net.foound.exception.handler;

import net.foound.exception.HttpException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HttpExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = { HttpException.class })
	protected ResponseEntity<Object> handle(HttpException httpException, WebRequest webRequest)
	{
		return handleExceptionInternal(httpException, httpException.getMessage(), new HttpHeaders(), httpException.getHttpStatus(), webRequest);
	}
}
