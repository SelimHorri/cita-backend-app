package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.FavouriteNotFoundException;
import tn.cita.app.mapper.FavouriteMapper;
import tn.cita.app.model.domain.id.FavouriteId;
import tn.cita.app.model.dto.FavouriteDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.FavouriteRepository;
import tn.cita.app.service.FavouriteService;
import tn.cita.app.util.ClientPageRequestUtils;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class FavouriteServiceImpl implements FavouriteService {
	
	private final FavouriteRepository favouriteRepository;
	
	@Override
	public FavouriteDto findById(final FavouriteId favouriteId) {
		return this.favouriteRepository.findById(favouriteId)
				.map(FavouriteMapper::toDto)
				.orElseThrow(FavouriteNotFoundException::new);
	}
	
	@Override
	public Page<FavouriteDto> findAllByCustomerId(final Integer customerId, final ClientPageRequest clientPageRequest) {
		log.info("** Find all favourites by customerId.. *");
		return this.favouriteRepository.findAllByCustomerId(customerId, 
					ClientPageRequestUtils.from(clientPageRequest))
				.map(FavouriteMapper::toDto);
	}
	
	@Transactional
	@Override
	public Boolean deleteById(final FavouriteId favouriteId) {
		log.info("** Delete favourite by id.. *");
		this.favouriteRepository.deleteById(favouriteId);
		return !this.favouriteRepository.existsById(favouriteId);
	}
	
}



