package id.co.hanoman.boot.security.repo;

import java.util.Collection;
import java.util.List;

import com.senomas.common.persistence.PageRequestId;

import id.co.hanoman.boot.security.model.UserRole;
import id.co.hanoman.boot.security.model.UserSummary;
import id.co.hanoman.boot.security.rs.UserPageParam;

public interface UserRepositoryCustom {

	PageRequestId<UserSummary> findSummaryFilter(UserPageParam param);
	
	List<UserRole> getUserRoles(Collection<Long> userIds);

}
