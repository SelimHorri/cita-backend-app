package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.OrderedDetail;
import tn.cita.app.model.dto.OrderedDetailDto;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.ServiceDetailDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderedDetailMapper {
	
	public static OrderedDetailDto toDto(@NonNull final OrderedDetail orderedDetail) {
		return OrderedDetailDto.builder()
				.reservationId(orderedDetail.getReservationId())
				.serviceDetailId(orderedDetail.getServiceDetailId())
				.orderedDate(orderedDetail.getOrderedDate())
				.identifier(orderedDetail.getIdentifier())
				.reservationDto(
					ReservationDto.builder()
						.id(orderedDetail.getReservation().getId())
						.identifier(orderedDetail.getReservation().getIdentifier())
						.code(orderedDetail.getReservation().getCode())
						.description(orderedDetail.getReservation().getDescription())
						.startDate(orderedDetail.getReservation().getStartDate())
						.cancelDate(orderedDetail.getReservation().getCancelDate())
						.completeDate(orderedDetail.getReservation().getCompleteDate())
						.status(orderedDetail.getReservation().getStatus())
						.build())
				.serviceDetailDto(
					ServiceDetailDto.builder()
						.id(orderedDetail.getServiceDetail().getId())
						.identifier(orderedDetail.getServiceDetail().getIdentifier())
						.name(orderedDetail.getServiceDetail().getName())
						.description(orderedDetail.getServiceDetail().getDescription())
						.isAvailable(orderedDetail.getServiceDetail().getIsAvailable())
						.duration(orderedDetail.getServiceDetail().getDuration())
						.priceUnit(orderedDetail.getServiceDetail().getPriceUnit())
						.build())
				.build();
	}
	
}




