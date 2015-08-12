package id.co.hanoman.boot.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import id.co.hanoman.boot.security.model.RoleSummary;
import id.co.hanoman.boot.security.model.UserSummary;

public class TokenAuthentication implements Authentication {
	private static final long serialVersionUID = 1L;
	private boolean authenticated = false;
	private final String token;
	private UserSummary user;
	private Set<GrantedAuthority> authorities = new HashSet<>();

	public TokenAuthentication(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	public UserSummary getUser() {
		return user;
	}
	
	public void setUser(LoginUser user) {
		if (user != null) {
			this.user = user.getUser();
			authenticated = true;
			authorities.clear();
			for (RoleSummary r : this.user.getRoles()) {
				authorities.add(new LoginAuthority(r));
			}
		} else {
			this.user = null;
			authenticated = false;
			authorities.clear();
		}
	}

	@Override
	public String getName() {
		return user != null ? user.getLogin() : "nobody";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

}
