package tn.cita.app.business.favourite.customer.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.favourite.customer.model.CustomerFavouriteResponse;
import tn.cita.app.business.favourite.customer.service.CustomerFavouriteService;
import tn.cita.app.model.dto.FavouriteDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/customers/favourites")
@RequiredArgsConstructor
class CustomerFavouriteResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerFavouriteService customerFavouriteService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<CustomerFavouriteResponse> fetchAllFavourites(WebRequest request, @RequestParam Map<String, String> params) {
		return ApiResponse.of(
				this.customerFavouriteService.fetchAllFavourites(
						this.userRequestExtractorUtil.extractUsername(request),
						ClientPageRequest.from(params)));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/{saloonId}")
	ApiResponse<Boolean> deleteFavourite(WebRequest request, @PathVariable String saloonId) {
		return ApiResponse.of(
				this.customerFavouriteService.deleteFavourite(
						this.userRequestExtractorUtil.extractUsername(request),
						Integer.parseInt(saloonId)));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<FavouriteDto> addFavourite(WebRequest webRequest, @RequestParam Integer saloonId) {
		return ApiResponse.of(
				this.customerFavouriteService.addFavourite(
						this.userRequestExtractorUtil.extractUsername(webRequest), 
						saloonId));
	}
	
}




