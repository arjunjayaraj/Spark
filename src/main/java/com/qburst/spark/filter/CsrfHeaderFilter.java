package com.qburst.spark.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class CsrfHeaderFilter extends OncePerRequestFilter {

	protected static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
			String token = csrf.getToken();
			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				response.setHeader(RESPONSE_TOKEN_NAME, csrf.getToken());
				cookie = new Cookie("XSRF-TOKEN", token);
				cookie.setPath("/angularLogin/");

				response.addCookie(cookie);
				response.addHeader("X-CSRF-HEADER", token);
			}
		}
		filterChain.doFilter(request, response);
	}

}