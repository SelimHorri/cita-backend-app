package tn.cita.app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.cita.app.model.domain.entity.Credential;

import java.util.Collection;
import java.util.Set;

public record CustomUserDetails(Credential credential) implements UserDetails {
	
	@Override
	public String getUsername() {
		return this.credential.getUsername();
	}
	
	@Override
	public String getPassword() {
		return this.credential.getPassword();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Set.of(new SimpleGrantedAuthority(
				this.credential.getUserRoleBasedAuthority().getRole()));
	}
	
	@Override
	public boolean isEnabled() {
		return this.credential.getIsEnabled();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.credential.getIsAccountNonExpired();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.credential.getIsAccountNonLocked();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credential.getIsCredentialsNonExpired();
	}
	
}



