package tn.cita.app.service.v0;

import tn.cita.app.model.dto.VerificationTokenDto;

public interface VerificationTokenService {
	
	VerificationTokenDto findByToken(final String token);
	boolean deleteByToken(final String token);
	
}






