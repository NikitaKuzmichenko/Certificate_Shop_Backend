package com.epam.esm.web.exceptionhandler;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.*;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Locale;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired private MessageSource messageSource;

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> illegalArgumentExceptionHandler(Exception exception, Locale locale) {
		return badRequestResponse(locale);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ResponseEntity<?> integrityConstraintExceptionHandler(Exception exception, Locale locale) {
		return conflictResponse(locale);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<?> handlerNotFoundExceptionHandler(Exception exception, Locale locale) {
		return notFoundResponse(locale);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> notValidEntityExceptionHandler(Exception exception, Locale locale) {
		return badRequestResponse(locale);
	}

	@ExceptionHandler(JWTDecodeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> notValidTokenExceptionHandler(Exception exception, Locale locale) {
		return invalidTokenResponse(locale);
	}

	@ExceptionHandler(TokenExpiredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> tokenExpiredExceptionHandler(Exception exception, Locale locale) {
		return tokenExpiredResponse(locale);
	}

	@ExceptionHandler(SignatureVerificationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> invalidTokenSignatureExceptionHandler(
			Exception exception, Locale locale) {
		return invalidTokenSignatureResponse(locale);
	}
}
