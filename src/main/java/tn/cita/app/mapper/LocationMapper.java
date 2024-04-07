package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Location;
import tn.cita.app.model.dto.LocationDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocationMapper {
	
	public static LocationDto toDto(@NonNull final Location location) {
		return LocationDto.builder()
				.id(location.getId())
				.identifier(location.getIdentifier())
				.zipcode(location.getZipcode())
				.city(location.getCity())
				.state(location.getState())
				.build();
	}
	
}




