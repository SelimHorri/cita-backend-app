package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Saloon;
import tn.cita.app.model.dto.LocationDto;
import tn.cita.app.model.dto.SaloonDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SaloonMapper {
	
	public static SaloonDto toDto(@NonNull final Saloon saloon) {
		return SaloonDto.builder()
				.id(saloon.getId())
				.identifier(saloon.getIdentifier())
				.code(saloon.getCode())
				.taxRef(saloon.getTaxRef())
				.name(saloon.getName())
				.isPrimary(saloon.getIsPrimary())
				.openingDate(saloon.getOpeningDate())
				.fullAdr(saloon.getFullAdr())
				.iframeGoogleMap(saloon.getIframeGoogleMap())
				.email(saloon.getEmail())
				.locationDto(
					LocationDto.builder()
						.id(saloon.getId())
						.identifier(saloon.getLocation().getIdentifier())
						.zipcode(saloon.getLocation().getZipcode())
						.city(saloon.getLocation().getCity())
						.state(saloon.getLocation().getState())
						.build())
				.build();
	}
	
}




