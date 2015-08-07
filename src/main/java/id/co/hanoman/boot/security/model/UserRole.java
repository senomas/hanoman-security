package id.co.hanoman.boot.security.model;

import java.io.Serializable;

public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	Long userId;
	
	Role role;
	
	public UserRole() {
	}
	
	public UserRole(Long userId, Role role) {
		this.userId = userId;
		this.role = role;
	}
	
	public UserRole(Long userId, Long roleId, String code, String name, String description) {
		this.userId = userId;
		this.role = new Role(code, name, description);
		this.role.setId(roleId);
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}
