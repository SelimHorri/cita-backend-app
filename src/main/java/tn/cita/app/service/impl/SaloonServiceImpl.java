package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.SaloonNotFoundException;
import tn.cita.app.mapper.SaloonMapper;
import tn.cita.app.model.dto.SaloonDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.SaloonRepository;
import tn.cita.app.service.SaloonService;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class SaloonServiceImpl implements SaloonService {
	
	private final SaloonRepository saloonRepository;
	
	@Override
	public Page<SaloonDto> findAll(final ClientPageRequest clientPageRequest) {
		log.info("Find all paged saloons.. ");
		return this.saloonRepository
				.findAll(PageRequest.of(
						clientPageRequest.getOffset() - 1, clientPageRequest.getSize()))
				.map(SaloonMapper::toDto);
	}
	
	@Override
	public Page<SaloonDto> findAllByLocationState(final String state, final ClientPageRequest clientPageRequest) {
		log.info("Find all saloons by location state.. ");
		return this.saloonRepository
				.findAllByLocationStateIgnoringCase(
						state.strip(),
						PageRequest.of(clientPageRequest.getOffset() - 1, clientPageRequest.getSize()))
				.map(SaloonMapper::toDto);
	}
	
	@Override
	public SaloonDto findById(final Integer id) {
		log.info("Find saloon by id.. ");
		return this.saloonRepository.findById(id)
				.map(SaloonMapper::toDto)
				.orElseThrow(SaloonNotFoundException::new);
	}
	
	@Override
	public SaloonDto findByIdentifier(final String identifier) {
		return this.saloonRepository.findByIdentifier(identifier.strip())
				.map(SaloonMapper::toDto)
				.orElseThrow(SaloonNotFoundException::new);
	}
	
	@Override
	public Page<SaloonDto> findAllByCode(final String code) {
		log.info("Find all saloons by code.. ");
		return new PageImpl<>(this.saloonRepository.
				findAllByCode(code).stream()
					.map(SaloonMapper::toDto)
					.toList());
	}
	
}




