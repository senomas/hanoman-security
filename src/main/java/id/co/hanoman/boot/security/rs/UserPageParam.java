package id.co.hanoman.boot.security.rs;

import com.senomas.common.rs.PageParam;

public class UserPageParam extends PageParam {
	UserFilter filter;
	
	public UserFilter getFilter() {
		return filter;
	}
	
	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}
}
