package tn.cita.app.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import tn.cita.app.dto.OrderedDetailDto;
import tn.cita.app.dto.ServiceDetailDto;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Builder
public final class ServiceDetailsReservationContainerResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("orderedDetails")
	private final List<OrderedDetailDto> orderedDetailDtos;
	
	@JsonProperty("serviceDetails")
	private final List<ServiceDetailDto> serviceDetailDtos;
	
	// @JsonProperty("category")
	// private final CategoryDto categoryDto;
	
}














