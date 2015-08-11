package id.co.hanoman.boot.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
	
	public LoginFilter() {
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
			String uri = req.getRequestURI() + (req.getQueryString() != null ? '?' + req.getQueryString() : "");
			String auth = req.getHeader("authorization");
			if (auth != null && auth.startsWith("Bearer ")) {
				String token = auth.substring(7).trim();
				SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(token, uri));
			} else {
				SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(null, uri));
			}
		} else {
			throw new RuntimeException("Not supported.");
		}
		chain.doFilter(request, response);
	}

}
