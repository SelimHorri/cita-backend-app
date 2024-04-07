package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Employee;
import tn.cita.app.model.domain.entity.Saloon;
import tn.cita.app.model.domain.entity.UserImage;
import tn.cita.app.model.dto.CredentialDto;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.SaloonDto;
import tn.cita.app.model.dto.UserImageDto;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeMapper {
	
	public static EmployeeDto toDto(@NonNull final Employee employee) {
		
		final var userImage = Objects
				.requireNonNullElseGet(employee.getUserImage(), UserImage::new);
		final var manager = Objects
				.requireNonNullElseGet(employee.getManager(), Employee::new);
		final var saloon = Objects
				.requireNonNullElseGet(employee.getSaloon(), Saloon::new);
		
		return EmployeeDto.builder()
				.id(employee.getId())
				.identifier(employee.getIdentifier())
				.ssn(employee.getSsn())
				.firstname(employee.getFirstname())
				.lastname(employee.getLastname())
				.isMale(employee.getIsMale())
				.email(employee.getEmail())
				.phone(employee.getPhone())
				.birthdate(employee.getBirthdate())
				.hiredate(employee.getHiredate())
				.userImageDto(
					UserImageDto.builder()
						.id(userImage.getId())
						.identifier(userImage.getIdentifier())
						.imageLob(userImage.getImageLob())
						.build())
				.managerDto(
					EmployeeDto.builder()
						.id(manager.getId())
						.identifier(manager.getIdentifier())
						.firstname(manager.getFirstname())
						.lastname(manager.getLastname())
						.isMale(manager.getIsMale())
						.email(manager.getEmail())
						.phone(manager.getPhone())
						.birthdate(manager.getBirthdate())
						.hiredate(manager.getHiredate())
						.build())
				.credentialDto(
					CredentialDto.builder()
						.id(employee.getCredential().getId())
						.identifier(employee.getCredential().getIdentifier())
						.username(employee.getCredential().getUsername())
						.password(employee.getCredential().getPassword())
						.userRoleBasedAuthority(employee.getCredential().getUserRoleBasedAuthority())
						.isEnabled(employee.getCredential().getIsEnabled())
						.isAccountNonExpired(employee.getCredential().getIsAccountNonExpired())
						.isAccountNonLocked(employee.getCredential().getIsAccountNonLocked())
						.isCredentialsNonExpired(employee.getCredential().getIsCredentialsNonExpired())
						.build())
				.saloonDto(
					SaloonDto.builder()
						.id(saloon.getId())
						.identifier(saloon.getIdentifier())
						.code(saloon.getCode())
						.taxRef(saloon.getTaxRef())
						.name(saloon.getName())
						.isPrimary(saloon.getIsPrimary())
						.openingDate(saloon.getOpeningDate())
						.fullAdr(saloon.getFullAdr())
						.email(saloon.getEmail())
						.build())
				.build();
	}
	
}




