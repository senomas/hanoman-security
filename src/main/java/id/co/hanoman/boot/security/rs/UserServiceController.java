package id.co.hanoman.boot.security.rs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.senomas.common.persistence.PageRequestId;
import com.senomas.common.rs.ResourceNotFoundException;

import id.co.hanoman.boot.security.model.Role;
import id.co.hanoman.boot.security.model.User;
import id.co.hanoman.boot.security.model.UserSummary;
import id.co.hanoman.boot.security.repo.RoleRepository;
import id.co.hanoman.boot.security.repo.UserRepository;

@RestController
@RequestMapping("/rs/user")
public class UserServiceController {

	@Autowired
	UserRepository repository;

	@Autowired
	RoleRepository roleRepo;

	@RequestMapping(value = "/id/{id}", method = { RequestMethod.GET })
	@Transactional
	public User get(@PathVariable("id") Long id) {
		User obj = repository.findOne(id);
		if (obj == null)
			throw new ResourceNotFoundException("User '" + id + "' not found.");
		return obj;
	}

	@RequestMapping(method = { RequestMethod.GET })
	@Transactional
	public List<User> list() {
		return repository.findAll();
	}

	@RequestMapping(method = { RequestMethod.POST })
	@Transactional
	public PageRequestId<UserSummary> listSummary(@RequestBody UserPageParam param) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
		return repository.findSummaryFilter(param);
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
		return repository.save(obj);
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
		return obj;
	}

}
