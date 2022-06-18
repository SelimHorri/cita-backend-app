package tn.cita.app.service.v0;

import org.springframework.data.domain.Page;

import tn.cita.app.dto.EmployeeDto;
import tn.cita.app.repository.EmployeeRepository;

public interface EmployeeService {
	
	EmployeeRepository getEmployeeRepository();
	Page<EmployeeDto> findAll(final int pageOffset);
	EmployeeDto findById(final Integer id);
	EmployeeDto findByUsername(final String username);
	boolean deleteById(final Integer id);
	
}












