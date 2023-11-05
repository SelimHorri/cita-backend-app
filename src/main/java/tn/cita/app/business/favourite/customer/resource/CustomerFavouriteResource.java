package tn.cita.app.business.favourite.customer.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequiredArgsConstructor
public class CustomerFavouriteResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerFavouriteService customerFavouriteService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<CustomerFavouriteResponse>> fetchAllFavourites(
					final WebRequest request, @RequestParam final Map<String, String> params) {
		log.info("** Fetch all customer favourites.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.customerFavouriteService.fetchAllFavourites(
						this.userRequestExtractorUtil.extractUsername(request),
						ClientPageRequest.from(params))));
	}
	
	@DeleteMapping("/{saloonId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteFavourite(final WebRequest request, @PathVariable final String saloonId) {
		log.info("** Delete customer favourite.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.customerFavouriteService.deleteFavourite(
						this.userRequestExtractorUtil.extractUsername(request),
						Integer.parseInt(saloonId))));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<FavouriteDto>> addFavourite(final WebRequest webRequest, @RequestParam final Integer saloonId) {
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.customerFavouriteService.addFavourite(
						this.userRequestExtractorUtil.extractUsername(webRequest), 
						saloonId)));
	}
	
}




