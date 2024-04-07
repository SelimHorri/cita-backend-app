package tn.cita.app.business.rating.customer.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.rating.customer.model.CustomerRatingResponse;
import tn.cita.app.business.rating.customer.service.CustomerRatingService;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/customers/ratings")
@RequiredArgsConstructor
class CustomerRatingResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerRatingService customerRatingService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<CustomerRatingResponse> fetchAllRatings(WebRequest request) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerRatingService.fetchAllRatings(extractUsername));
	}
	
}



