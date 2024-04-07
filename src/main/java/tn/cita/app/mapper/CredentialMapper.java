package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Credential;
import tn.cita.app.model.dto.CredentialDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CredentialMapper {
	
	public static CredentialDto toDto(@NonNull final Credential credential) {
		return CredentialDto.builder()
				.id(credential.getId())
				.identifier(credential.getIdentifier())
				.username(credential.getUsername())
				.password(credential.getPassword())
				.userRoleBasedAuthority(credential.getUserRoleBasedAuthority())
				.isEnabled(credential.getIsEnabled())
				.isAccountNonExpired(credential.getIsAccountNonExpired())
				.isAccountNonLocked(credential.getIsAccountNonLocked())
				.isCredentialsNonExpired(credential.getIsCredentialsNonExpired())
				.build();
	}
	
}




