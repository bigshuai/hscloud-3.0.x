package com.hisoft.hscloud.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisoft.hscloud.common.util.Constants;

public class CheckSessionFilter implements Filter {

	private static final List<String> paths = new ArrayList<String>();

	public CheckSessionFilter() {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		validateUrl(httpRequest, httpResponse, Constants.LOGIN_CURRENTUSER);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.paths.add("/cloud/");
		//this.paths.add("/config/");
		//this.paths.add("/resources/");
		this.paths.add("/shell/");
	}

	private void validateUrl(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, String... sessionNames)
			throws IOException {
		if (isCheckingPath(httpRequest)
				&& !existOnSession(httpRequest, sessionNames))
			httpResponse.sendRedirect(httpRequest.getContextPath()
					+ "/login.html");

	}

	private boolean existOnSession(HttpServletRequest httpRequest,
			String... sessionNames) {
		boolean flag = true;
		for (String sessionName : sessionNames) {
			flag = flag
					&& (httpRequest.getSession().getAttribute(sessionName) == null ? false
							: true);
		}
		return flag;
	}

	private boolean isCheckingPath(HttpServletRequest httpRequest) {
		String requestURI = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();
		boolean flag = false;
		for (String path : paths) {
			flag = flag || requestURI.startsWith(contextPath + path);
		}
		return flag;
	}
}
