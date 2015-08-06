package id.co.hanoman.boot.security.repo;

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
		Role role = roleRepo.findByName("admin");
		if (role == null) {
			role = new Role("admin", "Administrator");
			roleRepo.save(role);
		}
		role = roleRepo.findByName("opr");
		if (role == null) {
			role = new Role("opr", "Operator");
			roleRepo.save(role);
		}
		role = roleRepo.findByName("user");
		if (role == null) {
			role = new Role("user", "User");
			roleRepo.save(role);
		}
	}
	
	protected void initUsers() {
		User user = userRepo.findByLogin("seno");
		if (user == null) {
			user = new User("seno", "dodol123", "Seno");
			user.addRole(roleRepo.findByName("admin"));
			userRepo.save(user);
		}
		user = userRepo.findByLogin("opr1");
		if (user == null) {
			user = new User("opr1", "dodol123", "Operator 1");
			user.addRole(roleRepo.findByName("opr"));
			userRepo.save(user);
		}
		user = userRepo.findByLogin("user1");
		if (user == null) {
			user = new User("user1", "dodol123", "User 1");
			user.addRole(roleRepo.findByName("user"));
			userRepo.save(user);
		}
	}

}
