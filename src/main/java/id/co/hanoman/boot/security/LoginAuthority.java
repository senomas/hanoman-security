package id.co.hanoman.boot.security;

import org.springframework.security.core.GrantedAuthority;

import id.co.hanoman.boot.security.model.RoleSummary;

public class LoginAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	private final RoleSummary role;

	public LoginAuthority(RoleSummary role) {
		this.role = role;
	}
	
	@Override
	public String getAuthority() {
		return role.getCode();
	}
	
	public RoleSummary getRole() {
		return role;
	}

}
