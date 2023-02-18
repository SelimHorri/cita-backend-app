package tn.cita.app.model.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaloonTagId implements Serializable {
	
	private static final long serialVersionUID = 6463719947333911023L;
	private Integer saloonId;
	private Integer tagId;
	
}




