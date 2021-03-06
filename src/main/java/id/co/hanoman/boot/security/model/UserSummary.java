package id.co.hanoman.boot.security.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	Long id;
	
	String login;
	
	String fullName;
	
	List<RoleSummary> roles = new LinkedList<RoleSummary>();
	
	public UserSummary() {
	}
	
	public UserSummary(Long id, String login, String fullName) {
		this.id = id;
		this.login = login;
		this.fullName = fullName;
	}
	
	public UserSummary(User user) {
		id = user.getId();
		login = user.getLogin();
		fullName = user.getFullName();
		for (Role r : user.getRoles()) {
			roles.add(new RoleSummary(r));
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public List<RoleSummary> getRoles() {
		return roles;
	}
	
	public void setRoles(List<RoleSummary> roles) {
		this.roles = roles;
	}
	
	public void addRole(RoleSummary role) {
		roles.add(role);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSummary other = (UserSummary) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserSummary [id=" + id + ", login=" + login + ", fullName=" + fullName + ", roles="
				+ roles + "]";
	}
}
