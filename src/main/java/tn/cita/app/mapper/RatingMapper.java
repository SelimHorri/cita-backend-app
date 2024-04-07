package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Rating;
import tn.cita.app.model.dto.CustomerDto;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.RatingDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RatingMapper {
	
	public static RatingDto toDto(@NonNull final Rating rating) {
		return RatingDto.builder()
				.identifier(rating.getIdentifier())
				.workerId(rating.getWorkerId())
				.customerId(rating.getCustomerId())
				.rateDate(rating.getRateDate())
				.rate(rating.getRate())
				.description(rating.getDescription())
				.workerDto(
					EmployeeDto.builder()
						.id(rating.getWorker().getId())
						.identifier(rating.getWorker().getIdentifier())
						.ssn(rating.getWorker().getSsn())
						.firstname(rating.getWorker().getFirstname())
						.lastname(rating.getWorker().getLastname())
						.isMale(rating.getWorker().getIsMale())
						.email(rating.getWorker().getEmail())
						.phone(rating.getWorker().getPhone())
						.birthdate(rating.getWorker().getBirthdate())
						.build())
				.customerDto(
					CustomerDto.builder()
						.id(rating.getCustomer().getId())
						.identifier(rating.getCustomer().getIdentifier())
						.ssn(rating.getCustomer().getSsn())
						.firstname(rating.getCustomer().getFirstname())
						.lastname(rating.getCustomer().getLastname())
						.isMale(rating.getCustomer().getIsMale())
						.email(rating.getCustomer().getEmail())
						.phone(rating.getCustomer().getPhone())
						.birthdate(rating.getCustomer().getBirthdate())
						.build())
				.build();
	}
	
}




