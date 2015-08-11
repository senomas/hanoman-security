package id.co.hanoman.boot.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.senomas.common.U;

public class TokenAuthenticationProvider implements AuthenticationProvider {
	private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	private final TokenStore tokenStore;

	public TokenAuthenticationProvider(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
		log.debug("init");
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		TokenAuthentication tokenAuth = (TokenAuthentication) authentication;
		LoginUser user = tokenStore.get(tokenAuth.getToken());
		log.info("AUTH "+U.dump(tokenAuth)+"\nUSER "+U.dump(user));
		tokenAuth.setUser(user);
		return tokenAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return TokenAuthentication.class.isAssignableFrom(authentication);
	}

}
