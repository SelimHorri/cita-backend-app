package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.UserImage;
import tn.cita.app.model.dto.UserImageDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserImageMapper {
	
	public static UserImageDto toDto(@NonNull final UserImage userImage) {
		return UserImageDto.builder()
				.id(userImage.getId())
				.identifier(userImage.getIdentifier())
				.name(userImage.getName())
				.type(userImage.getType())
				.size(userImage.getSize())
				.imageLob(userImage.getImageLob())
				.build();
	}
	
}



