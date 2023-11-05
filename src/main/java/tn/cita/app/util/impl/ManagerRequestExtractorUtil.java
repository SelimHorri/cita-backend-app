package tn.cita.app.util.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.exception.wrapper.UsernameNotMatchException;
import tn.cita.app.util.JwtUtils;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class ManagerRequestExtractorUtil implements UserRequestExtractorUtil {
	
	private final JwtUtils jwtUtils;
	
	@Override
	public String extractUsername(final WebRequest request) {
		
		final var usernameAuthHeader = Objects.requireNonNullElse(
				request.getHeader(AppConstants.USERNAME_AUTH_HEADER), "");
		final var authenticatedUsername = this.jwtUtils.extractUsername(Objects.requireNonNullElse(
						request.getHeader(AppConstants.AUTHORIZATION_HEADER), "")
				.substring(7)
				.strip());
		
		if (!usernameAuthHeader.isBlank())
			if (!authenticatedUsername.equalsIgnoreCase(usernameAuthHeader))
				throw new UsernameNotMatchException("Authenticated user is not allowed to access another user resources");
		
		return authenticatedUsername;
	}
	
}



