package tn.cita.app.service;

import tn.cita.app.dto.VerificationTokenDto;

public interface VerificationTokenService {
	
	VerificationTokenDto findByToken(final String token);
	boolean deleteByToken(final String token);
	
}













