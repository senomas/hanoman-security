package id.co.hanoman.boot.security.rs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.senomas.common.U;
import com.senomas.common.rs.UnauthorizedException;

import id.co.hanoman.boot.security.LoginRequest;
import id.co.hanoman.boot.security.LoginUser;
import id.co.hanoman.boot.security.TokenStore;
import id.co.hanoman.boot.security.model.User;
import id.co.hanoman.boot.security.model.UserSummary;
import id.co.hanoman.boot.security.repo.RoleRepository;
import id.co.hanoman.boot.security.repo.UserRepository;

@RestController
@RequestMapping("/rs/login")
public class LoginServiceController {
	private static final Logger log = LoggerFactory.getLogger(LoginServiceController.class);

	@Autowired
	UserRepository repository;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	TokenStore tokenStore;

	public LoginServiceController() {
		log.debug("init");
	}

	@RequestMapping(value = "/{token}", method = { RequestMethod.GET })
	@Transactional
	public LoginUser getToken(@PathVariable("token") String token) {
		LoginUser user = tokenStore.get(token);
		if (user == null)
			throw new UnauthorizedException("Invalid token");
		return user;
	}

	@RequestMapping(method = { RequestMethod.GET })
	@Transactional
	public LoginRequest login(HttpServletRequest req) {
		try {
			LoginRequest request = new LoginRequest();
			request.setTimestamp(new Date());
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String sx = U.randomHex(8);
			request.setSalt(sx + U
					.toHex(md.digest(U.getBytes(sx + req.getRemoteAddr() + "|" + request.getTimestamp().getTime()))));
			tokenStore.putSalt(request.getSalt());
			return request;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@RequestMapping(method = { RequestMethod.POST })
	@Transactional
	public LoginUser login(HttpServletRequest req, @RequestBody LoginRequest request) {
		if (Math.abs(request.getTimestamp().getTime() - System.currentTimeMillis()) > 300000)
			throw new UnauthorizedException("Invalid timestamp");
		String sx = request.getSalt().substring(0, 8);
		try {
			log.info("login request " + U.dump(request));
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String salt = sx
					+ U.toHex(md.digest(U.getBytes(sx + req.getRemoteAddr() + "|" + request.getTimestamp().getTime())));
			if (!salt.equals(request.getSalt()))
				throw new UnauthorizedException("Invalid salt");
			if (!tokenStore.hasSalt(salt))
				throw new UnauthorizedException("Invalid salt");
			User user = repository.findByLogin(request.getLogin());
			if (user == null)
				throw new UnauthorizedException("Invalid login");
			if (user.getLastLogin() != null && !user.getLastLogin().before(request.getTimestamp()))
				throw new UnauthorizedException("Invalid login timestamp");
			if (user.getLastSalt() != null && user.getLastSalt().equals(request.getSalt()))
				throw new UnauthorizedException("Invalid login salt");
			log.info("USER TOKEN " + user.getPassword());
			String token = U.toHex(md.digest(
					U.getBytes(user.getPassword() + "|" + request.getSalt() + "|" + request.getTimestamp().getTime())));
			log.info("LOGIN CHECK " + token.equals(request.getToken()) + "   " + token + "  " + request.getToken());
			if (!token.equals(request.getToken()))
				throw new UnauthorizedException("Invalid user");
			user.setLastLogin(request.getTimestamp());
			user.setLastSalt(request.getSalt());
			repository.save(user);
			return tokenStore.create(new UserSummary(user));
		} catch (RuntimeException e) {
			log.warn("Login error " + e.getMessage(), e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/hash", method = { RequestMethod.POST })
	@Transactional
	public LoginRequest loginTest(@RequestBody LoginRequest req) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String token = U.toHex(md.digest(U.getBytes(req.getLogin() + "|" + req.getToken())));
			req.setToken(
					U.toHex(md.digest(U.getBytes(token + "|" + req.getSalt() + "|" + req.getTimestamp().getTime()))));
			return req;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
