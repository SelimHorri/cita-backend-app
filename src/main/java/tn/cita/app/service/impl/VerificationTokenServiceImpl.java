package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.VerificationTokenNotFoundException;
import tn.cita.app.mapper.VerificationTokenMapper;
import tn.cita.app.model.dto.VerificationTokenDto;
import tn.cita.app.repository.VerificationTokenRepository;
import tn.cita.app.service.VerificationTokenService;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class VerificationTokenServiceImpl implements VerificationTokenService {
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	@Override
	public VerificationTokenDto findByToken(final String token) {
		log.info("** Find verificationToken by token.. *");
		return this.verificationTokenRepository.findByToken(token)
				.map(VerificationTokenMapper::toDto)
				.orElseThrow(() -> new VerificationTokenNotFoundException(
						"VerificationToken with token: %s is not found".formatted(token)));
	}
	
	@Transactional
	@Override
	public boolean deleteByToken(final String token) {
		log.info("** Delete verificationToken by token..*");
		this.verificationTokenRepository.deleteByToken(token);
		return true;
	}
	
}



