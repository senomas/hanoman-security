package id.co.hanoman.boot.security;

import id.co.hanoman.boot.security.model.UserSummary;

public interface TokenStore {

	LoginUser get(String token);
	
	LoginUser create(UserSummary user);

}
