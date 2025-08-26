package x.bv.demo.std.ware.core.web.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestProcessFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		// TODO: 做规则拦截, 指定规则的 请求排除或包含
		filterChain.doFilter(new CachedHttpServletRequestWrapper(request), response);
	}
}
