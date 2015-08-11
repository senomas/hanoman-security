package id.co.hanoman.boot.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.senomas.common.U;

public class LoginCheckFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(LoginCheckFilter.class);
	
	public LoginCheckFilter() {
    	log.debug("init");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			if (req.getRequestURI().startsWith("/rs/login")) {
				// skip
			} else if (req.getRequestURI().startsWith("/rs/")) {
				TokenAuthentication auth = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
				log.info("AUTH AFTER "+U.dump(auth));
				if (auth.getUser() == null) {
					if (auth.getToken() == null) {
						resp.sendError(HttpServletResponse.SC_FORBIDDEN, "No token");
					} else {
						resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
					}
					return;
				}
			}
		} else {
			throw new RuntimeException("Not supported.");
		}
		chain.doFilter(request, response);
	}

}
