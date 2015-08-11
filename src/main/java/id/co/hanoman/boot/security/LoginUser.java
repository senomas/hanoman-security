package id.co.hanoman.boot.security;

import java.io.Serializable;

import id.co.hanoman.boot.security.model.UserSummary;

public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;

	String token;
	long timestamp;
	UserSummary user;
	
	public LoginUser(String token, UserSummary user) {
		this.token = token;
		this.user = user;
		timestamp = System.currentTimeMillis();
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public UserSummary getUser() {
		return user;
	}
	
	public void setUser(UserSummary user) {
		this.user = user;
	}
}
