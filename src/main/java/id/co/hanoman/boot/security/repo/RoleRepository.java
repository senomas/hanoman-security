package id.co.hanoman.boot.security.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import id.co.hanoman.boot.security.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	List<Role> findAll();

	Role findByCode(String code);
	
}
