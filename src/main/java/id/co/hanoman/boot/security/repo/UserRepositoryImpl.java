package id.co.hanoman.boot.security.repo;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;

import com.senomas.common.persistence.AbstractCustomRepository;
import com.senomas.common.persistence.Filter2;
import com.senomas.common.persistence.PageRequestId;

import id.co.hanoman.boot.security.model.Role;
import id.co.hanoman.boot.security.model.User;
import id.co.hanoman.boot.security.model.UserRole;
import id.co.hanoman.boot.security.model.UserSummary;
import id.co.hanoman.boot.security.rs.UserFilter;
import id.co.hanoman.boot.security.rs.UserPageParam;

public class UserRepositoryImpl extends AbstractCustomRepository implements UserRepositoryCustom {
	
	@Autowired
	EntityManager entityManager;

	@Override
	public PageRequestId<UserSummary> findSummaryFilter(final UserPageParam param) {
		return findWithSpecification(param.getRequestId(), entityManager, param.getRequest(), new Filter2<UserSummary, User>() {
			@Override
			public Predicate toPredicate(Root<User> user, CriteriaQuery<?> query, CriteriaBuilder builder) {
				UserFilter filter = param.getFilter();
				Predicate p = null;
				if (filter != null) {
					if (filter.getName() != null && filter.getName().length() > 0) {
						Predicate xp = builder.like(user.get("name").as(String.class), filter.getName());
						p = p != null ? builder.and(p, xp) : xp;
					}
					// TODO roles
				}
				return p;
			}
			
			@Override
			public Selection<? extends UserSummary> getSelection(Root<User> user, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.construct(UserSummary.class, user.get("id"), user.get("login"), user.get("fullName"));
			}

		}, UserSummary.class, User.class);
	}
	
	@Override
	public List<UserRole> getUserRoles(Collection<Long> userIds) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRole> cq = builder.createQuery(UserRole.class);
		Root<User> user = cq.from(User.class);
		Join<Role, User> roles = user.join("roles");
		cq.where(user.get("id").in(userIds));
		cq.select(builder.construct(UserRole.class, user.get("id"), roles.get("id"), roles.get("code"), roles.get("name"), roles.get("description")));
		return entityManager.createQuery(cq).getResultList();
	}

}
