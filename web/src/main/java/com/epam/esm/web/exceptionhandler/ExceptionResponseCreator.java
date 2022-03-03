package com.epam.esm.web.exceptionhandler;

import com.epam.esm.web.wrapper.ExceptionWrapper;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public final class ExceptionResponseCreator {

	public static final String NOT_FOUND_EXCEPTION = "not_found_exception";
	public static final String BAD_REQUEST_EXCEPTION = "bad_request";
	public static final String INTERNAL_EXCEPTION = "internal_exception";
	public static final String REPOSITORY_CONFLICT_EXCEPTION = "conflict_exception";
	public static final String FORBIDDEN_EXCEPTION = "forbidden_exception";
	public static final String UNAUTHORIZED_EXCEPTION = "unauthorized_exception";
	public static final String ACCESS_DENIED_EXCEPTION = "access_denied_exception";

	public static final String NOT_VALID_TOKEN_EXCEPTION = "invalid_token_exception";
	public static final String TOKEN_EXPIRED_EXCEPTION = "token_expired_exception";
	public static final String INVALID_SIGNATURE_EXCEPTION = "invalid_token_signature_exception";

	private static MessageSource messageSource;

	@Autowired
	public ExceptionResponseCreator(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public static ResponseEntity<?> conflictResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.CONFLICT.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.CONFLICT.value(),
								messageSource.getMessage(REPOSITORY_CONFLICT_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> internalErrorResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.INTERNAL_SERVER_ERROR.value(),
								messageSource.getMessage(INTERNAL_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> notFoundResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.NOT_FOUND.value(),
								messageSource.getMessage(NOT_FOUND_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> badRequestResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.BAD_REQUEST.value(),
								messageSource.getMessage(BAD_REQUEST_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> forbiddenResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.FORBIDDEN.value(),
								messageSource.getMessage(FORBIDDEN_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> unauthorizedResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.UNAUTHORIZED.value(),
								messageSource.getMessage(UNAUTHORIZED_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> accessDeniedResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.FORBIDDEN.value(),
								messageSource.getMessage(ACCESS_DENIED_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> invalidTokenResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.BAD_REQUEST.value(),
								messageSource.getMessage(NOT_VALID_TOKEN_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> tokenExpiredResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.BAD_REQUEST.value(),
								messageSource.getMessage(TOKEN_EXPIRED_EXCEPTION, null, locale)));
	}

	public static ResponseEntity<?> invalidTokenSignatureResponse(Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(
						new ExceptionWrapper(
								HttpStatus.BAD_REQUEST.value(),
								messageSource.getMessage(INVALID_SIGNATURE_EXCEPTION, null, locale)));
	}
}
