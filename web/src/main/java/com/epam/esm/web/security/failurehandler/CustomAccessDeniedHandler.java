package com.epam.esm.web.security.failurehandler;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.accessDeniedResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		ResponseEntity responseEntity = accessDeniedResponse(request.getLocale());
		response.setStatus(responseEntity.getStatusCodeValue());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().print(mapper.writeValueAsString(responseEntity.getBody()));
		response.flushBuffer();
	}
}
