package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.exception.wrapper.EmployeeNotFoundException;
import tn.cita.app.mapper.EmployeeMapper;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.service.EmployeeService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	
	@Override
	public Page<EmployeeDto> findAll(final int pageOffset) {
		log.info("** Find all employees offset paged..*");
		return this.employeeRepository
				.findAll(PageRequest.of(pageOffset - 1, AppConstants.PAGE_SIZE))
				.map(EmployeeMapper::toDto);
	}
	
	@Override
	public EmployeeDto findById(final Integer id) {
		log.info("** Find employee by id..*");
		return this.employeeRepository.findById(id)
				.map(EmployeeMapper::toDto)
				.orElseThrow(EmployeeNotFoundException::new);
	}
	
	@Override
	public EmployeeDto findByIdentifier(final String identifier) {
		log.info("** Find employee by identifier.. *");
		return this.employeeRepository.findByIdentifier(identifier.strip())
				.map(EmployeeMapper::toDto)
				.orElseThrow(EmployeeNotFoundException::new);
	}
	
	@Override
	public EmployeeDto findByCredentialUsername(final String username) {
		log.info("** Find employee by credential username.. *");
		return this.employeeRepository.findByCredentialUsernameIgnoringCase(username)
				.map(EmployeeMapper::toDto)
				.orElseThrow(() -> new EmployeeNotFoundException(String
						.format("Employee with username: %s not found", username)));
	}
	
	@Override
	public List<EmployeeDto> findAllBySsn(final String ssn) {
		log.info("** Find employee(s) by ssn.. *");
		return this.employeeRepository
				.findAllBySsn(ssn.strip()).stream()
					.map(EmployeeMapper::toDto)
					.toList();
	}
	
	@Transactional
	@Override
	public boolean deleteById(final Integer id) {
		log.info("** Delete employee by id..*");
		this.employeeRepository.deleteById(id);
		return !this.employeeRepository.existsById(id);
	}
	
	@Override
	public List<EmployeeDto> findAllByManagerId(final Integer managerId) {
		log.info("** Find all employees by managerId.. *");
		return this.employeeRepository.findAllByManagerId(managerId).stream()
				.map(EmployeeMapper::toDto)
				.toList();
	}
	
}




