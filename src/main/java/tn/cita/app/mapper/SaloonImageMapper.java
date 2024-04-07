package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.SaloonImage;
import tn.cita.app.model.dto.SaloonDto;
import tn.cita.app.model.dto.SaloonImageDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SaloonImageMapper {
	
	public static SaloonImageDto toDto(@NonNull final SaloonImage saloonImage) {
		return SaloonImageDto.builder()
				.id(saloonImage.getId())
				.identifier(saloonImage.getIdentifier())
				.name(saloonImage.getName())
				.type(saloonImage.getType())
				.size(saloonImage.getSize())
				.imageLob(saloonImage.getImageLob())
				.saloonDto(
					SaloonDto.builder()
						.id(saloonImage.getSaloon().getId())
						.identifier(saloonImage.getSaloon().getIdentifier())
						.code(saloonImage.getSaloon().getCode())
						.taxRef(saloonImage.getSaloon().getTaxRef())
						.name(saloonImage.getSaloon().getName())
						.isPrimary(saloonImage.getSaloon().getIsPrimary())
						.openingDate(saloonImage.getSaloon().getOpeningDate())
						.fullAdr(saloonImage.getSaloon().getFullAdr())
						.email(saloonImage.getSaloon().getEmail())
						.build())
				.build();
	}
	
}




