package id.co.hanoman.boot.security.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import id.co.hanoman.boot.security.model.User;

public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom {

	List<User> findAll();

	User findByLogin(String login);
	
}
