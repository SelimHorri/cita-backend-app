package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.ServiceDetail;
import tn.cita.app.model.dto.CategoryDto;
import tn.cita.app.model.dto.ServiceDetailDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceDetailMapper {
	
	public static ServiceDetailDto toDto(@NonNull final ServiceDetail serviceDetail) {
		return ServiceDetailDto.builder()
				.id(serviceDetail.getId())
				.identifier(serviceDetail.getIdentifier())
				.name(serviceDetail.getName())
				.description(serviceDetail.getDescription())
				.isAvailable(serviceDetail.getIsAvailable())
				.duration(serviceDetail.getDuration())
				.priceUnit(serviceDetail.getPriceUnit())
				.categoryDto(
					CategoryDto.builder()
						.id(serviceDetail.getCategory().getId())
						.identifier(serviceDetail.getCategory().getIdentifier())
						.name(serviceDetail.getCategory().getName())
						.build())
				.build();
	}
	
}




