package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.TagNotFoundException;
import tn.cita.app.mapper.TagMapper;
import tn.cita.app.model.dto.TagDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.TagRepository;
import tn.cita.app.service.TagService;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class TagServiceImpl implements TagService {
	
	private final TagRepository tagRepository;
	
	@Override
	public Page<TagDto> findAll(final ClientPageRequest clientPageRequest) {
		log.info("Find all paged tags.. ");
		return this.tagRepository
				.findAll(PageRequest.of(
						clientPageRequest.getOffset() - 1, clientPageRequest.getSize()))
				.map(TagMapper::toDto);
	}
	
	@Override
	public TagDto findById(final Integer id) {
		log.info("Find tag by id.. ");
		return this.tagRepository.findById(id)
				.map(TagMapper::toDto)
				.orElseThrow(TagNotFoundException::new);
	}
	
	@Override
	public TagDto findByIdentifier(final String identifier) {
		return this.tagRepository.findByIdentifier(identifier.strip())
				.map(TagMapper::toDto)
				.orElseThrow(TagNotFoundException::new);
	}
	
}



