package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Tag;
import tn.cita.app.model.dto.TagDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagMapper {
	
	public static TagDto toDto(@NonNull Tag tag) {
		return TagDto.builder()
				.id(tag.getId())
				.identifier(tag.getIdentifier())
				.name(tag.getName())
				.description(tag.getDescription())
				.build();
	}
	
}



