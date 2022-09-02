package tn.cita.app.resource.v0.business.customer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.dto.request.ClientPageRequest;
import tn.cita.app.dto.response.CustomerFavouriteResponse;
import tn.cita.app.dto.response.api.ApiResponse;
import tn.cita.app.service.v0.business.customer.CustomerFavouriteService;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping(AppConstants.API_CONTEXT_V0 + "/customers/favourites")
@Slf4j
@RequiredArgsConstructor
public class CustomerFavouriteResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerFavouriteService customerFavouriteService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<CustomerFavouriteResponse>> fetchAllFavourites(final WebRequest request, 
			@RequestParam final Map<String, String> params) {
		log.info("** Fetch all customer favourites.. *\n");
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, 
				this.customerFavouriteService.fetchAllFavourites(this.userRequestExtractorUtil.extractUsername(request), 
						new ClientPageRequest(params))));
	}
	
	@DeleteMapping("/{saloonId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteFavourite(final WebRequest request, 
			@PathVariable final String saloonId) {
		log.info("** Delete customer favourite.. *\n");
		final Boolean isDeleted = this.customerFavouriteService.deleteFavourite(this.userRequestExtractorUtil.extractUsername(request), 
				Integer.parseInt(saloonId));
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, isDeleted));
	}
	
	
	
}















