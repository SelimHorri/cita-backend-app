package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Favourite;
import tn.cita.app.model.dto.CustomerDto;
import tn.cita.app.model.dto.FavouriteDto;
import tn.cita.app.model.dto.SaloonDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FavouriteMapper {
	
	public static FavouriteDto toDto(@NonNull final Favourite favourite) {
		return FavouriteDto.builder()
				.customerId(favourite.getCustomerId())
				.saloonId(favourite.getSaloonId())
				.favouriteDate(favourite.getFavouriteDate())
				.identifier(favourite.getIdentifier())
				.customerDto(
					CustomerDto.builder()
						.id(favourite.getCustomer().getId())
						.identifier(favourite.getCustomer().getIdentifier())
						.ssn(favourite.getCustomer().getSsn())
						.firstname(favourite.getCustomer().getFirstname())
						.lastname(favourite.getCustomer().getLastname())
						.isMale(favourite.getCustomer().getIsMale())
						.email(favourite.getCustomer().getEmail())
						.phone(favourite.getCustomer().getPhone())
						.birthdate(favourite.getCustomer().getBirthdate())
						.build())
				.saloonDto(
					SaloonDto.builder()
						.id(favourite.getSaloon().getId())
						.identifier(favourite.getSaloon().getIdentifier())
						.code(favourite.getSaloon().getCode())
						.taxRef(favourite.getSaloon().getTaxRef())
						.name(favourite.getSaloon().getName())
						.isPrimary(favourite.getSaloon().getIsPrimary())
						.openingDate(favourite.getSaloon().getOpeningDate())
						.fullAdr(favourite.getSaloon().getFullAdr())
						.email(favourite.getSaloon().getEmail())
						.build())
				.build();
	}
	
}




