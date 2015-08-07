package id.co.hanoman.boot.security.rs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.senomas.common.persistence.PageRequestId;
import com.senomas.common.rs.ResourceNotFoundException;

import id.co.hanoman.boot.security.model.Role;
import id.co.hanoman.boot.security.model.RoleSummary;
import id.co.hanoman.boot.security.model.User;
import id.co.hanoman.boot.security.model.UserRole;
import id.co.hanoman.boot.security.model.UserSummary;
import id.co.hanoman.boot.security.repo.RoleRepository;
import id.co.hanoman.boot.security.repo.UserRepository;

@RestController
@RequestMapping("/rs/user")
public class UserServiceController {
	private static final Logger log = LoggerFactory.getLogger(UserServiceController.class);

	@Autowired
	UserRepository repository;

	@Autowired
	RoleRepository roleRepo;
	
	public UserServiceController() {
		log.debug("init");
	}

	@RequestMapping(value = "/id/{id}", method = { RequestMethod.GET })
	@Transactional
	public User get(@PathVariable("id") Long id) {
		User obj = repository.findOne(id);
		if (obj == null)
			throw new ResourceNotFoundException("User '" + id + "' not found.");
		obj.setPassword(null);
		return obj;
	}

	@RequestMapping(method = { RequestMethod.POST })
	@Transactional
	public PageRequestId<UserSummary> listSummary(@RequestBody UserPageParam param) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
		PageRequestId<UserSummary> result = repository.findSummaryFilter(param);
		Map<Long, UserSummary> map = new HashMap<>();
		for (UserSummary r : result.getContent()) {
			map.put(r.getId(), r);
		}
		for (UserRole ur : repository.getUserRoles(map.keySet())) {
			map.get(ur.getUserId()).addRole(new RoleSummary(ur.getRole().getCode(), ur.getRole().getName()));
		}
		return result;
	}

	@RequestMapping(method = { RequestMethod.PUT })
	@Transactional
	public User save(@RequestBody User obj) {
		if (obj.getLogin().indexOf("xxx") >= 0)
			throw new RuntimeException("Invalid login");
		Set<Role> roles = new HashSet<Role>();
		for (Role r : obj.getRoles()) {
			roles.add(roleRepo.findOne(r.getId()));
		}
		obj.setRoles(roles);
		obj = repository.save(obj);
		obj.setPassword(null);
		return obj;
	}

	@RequestMapping(value = "/id/{id}", method = { RequestMethod.DELETE })
	@Transactional
	public User delete(@PathVariable("id") Long id) {
		User obj = repository.findOne(id);
		if (obj == null)
			throw new ResourceNotFoundException("User '" + id + "' not found.");
		if (obj.getLogin().indexOf("seno") >= 0)
			throw new RuntimeException("You can't delete me!");
		repository.delete(obj);
		obj.setPassword(null);
		return obj;
	}

}
