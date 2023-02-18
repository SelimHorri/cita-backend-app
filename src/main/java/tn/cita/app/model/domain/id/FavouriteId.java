package tn.cita.app.model.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FavouriteId implements Serializable {
	
	private static final long serialVersionUID = -5433078834954903470L;
	private Integer customerId;
	private Integer saloonId;
	
}




