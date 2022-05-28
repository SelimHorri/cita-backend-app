package tn.cita.app.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.cita.app.constant.AppConstant;
import tn.cita.app.dto.SaloonDto;
import tn.cita.app.dto.request.ClientPageRequest;
import tn.cita.app.exception.wrapper.SaloonNotFoundException;
import tn.cita.app.mapper.SaloonMapper;
import tn.cita.app.repository.SaloonRepository;
import tn.cita.app.service.SaloonService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaloonServiceImpl implements SaloonService {
	
	private final SaloonRepository saloonRepository;
	
	@Override
	public Page<SaloonDto> findAll(final ClientPageRequest clientPageRequest) {
		return this.saloonRepository.findAll(PageRequest.of(clientPageRequest.getOffset() - 1, clientPageRequest.getSize()))
				.map(SaloonMapper::map);
	}
	
	@Override
	public Page<SaloonDto> findAllByLocationState(final String state, final int offset) {
		return this.saloonRepository.findAllByLocationStateIgnoringCase(state.strip(), 
					PageRequest.of(offset - 1, AppConstant.PAGE_SIZE))
				.map(SaloonMapper::map);
	}
	
	@Override
	public SaloonDto findById(final Integer id) {
		return this.saloonRepository.findById(id)
				.map(SaloonMapper::map)
				.orElseThrow(() -> new SaloonNotFoundException(String
						.format("Saloon with id: %d not found", id)));
	}
	
	@Override
	public Page<SaloonDto> findAllByCode(final String code) {
		return this.saloonRepository.findAllByCode(code, PageRequest.of(1 - 1, AppConstant.PAGE_SIZE))
				.map(SaloonMapper::map);
	}
	
	
	
}














