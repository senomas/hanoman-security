package id.co.hanoman.boot.security.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import id.co.hanoman.boot.security.model.Role;
import id.co.hanoman.boot.security.model.User;

@Component("userDataInitializer")
public class DataInitializer {
	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	@Qualifier("transactionManager")
	protected PlatformTransactionManager txManager;

	@Autowired
	protected UserRepository userRepo;

	@Autowired
	protected RoleRepository roleRepo;
	
	private final static Random rnd = new Random();

	@PostConstruct
	public void populateDummyData() {
		TransactionTemplate tmpl = new TransactionTemplate(txManager);
		tmpl.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("Populate dummy user data...");

					initRoles();
					
					initUsers();

					log.info("Done populate dummy user data.");
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});
	}

	protected void initRoles() {
		Role role = roleRepo.findByCode("admin");
		if (role == null) {
			role = new Role("admin", "Administrator", "Administrator");
			roleRepo.save(role);
		}
		role = roleRepo.findByCode("opr");
		if (role == null) {
			role = new Role("opr", "Operator", "Operator");
			roleRepo.save(role);
		}
		role = roleRepo.findByCode("user");
		if (role == null) {
			role = new Role("user", "User", "User");
			roleRepo.save(role);
		}
	}
	
	protected void initUsers() {
		User user = userRepo.findByLogin("seno");
		if (user == null) {
			user = new User("seno", "dodol123", "Seno");
			user.addRole(roleRepo.findByCode("admin"));
			user.addRole(roleRepo.findByCode("opr"));
			user.addRole(roleRepo.findByCode("user"));
			userRepo.save(user);
		}
		user = userRepo.findByLogin("opr1");
		if (user == null) {
			user = new User("opr1", "dodol123", "Operator 1");
			user.addRole(roleRepo.findByCode("opr"));
			userRepo.save(user);
		}
		user = userRepo.findByLogin("admin1");
		if (user == null) {
			user = new User("admin1", "dodol123", "Admin 1");
			user.addRole(roleRepo.findByCode("admin"));
			user.addRole(roleRepo.findByCode("opr"));
			user.addRole(roleRepo.findByCode("user"));
			userRepo.save(user);
		}
		Map<String, Integer> map = new HashMap<>();
		for (int i=1; i<=400; i++) map.put("user"+i, i);
		for (User u : userRepo.findAll()) {
			map.remove(u.getLogin());
		}
		if (!map.isEmpty()) {
			Role roleUser = roleRepo.findByCode("user");
			Role roleAdmin = roleRepo.findByCode("admin");
			Role roleOpr = roleRepo.findByCode("opr");
			for (int i : map.values()) {
				user = new User("user"+i, "dodol123", "User "+i);
				userRepo.save(user);
				user.addRole(roleUser);
				switch (rnd.nextInt(3)) {
				case 1:
					user.addRole(roleAdmin);
				case 2:
					user.addRole(roleOpr);
				}
			}
		}
	}

}
