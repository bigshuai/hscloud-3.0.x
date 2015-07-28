package com.hisoft.hscloud.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CheckSessionFilter
 */
public class CheckSessionFilter implements Filter {

	public CheckSessionFilter() {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		validateUrl(httpRequest, httpResponse, "/common/", "currentUser");
		validateUrl(httpRequest, httpResponse, "/system/", "currentUser");

		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

	private void validateUrl(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, String path, String... sessionNames)
			throws IOException {
		if (isCheckingPath(httpRequest, path)
				&& existOnSession(httpRequest, sessionNames))
			httpResponse.sendRedirect(httpRequest.getContextPath());
	}

	private boolean existOnSession(HttpServletRequest httpRequest,
			String... sessionNames) {
		boolean flag=true;
		for(String sessionName:sessionNames)
			flag = flag && (httpRequest.getSession().getAttribute(sessionName) == null);
		return flag;
	}

	private boolean isCheckingPath(HttpServletRequest httpRequest, String path) {
		return httpRequest.getRequestURI().startsWith(
				httpRequest.getContextPath() + path);
	}
}
