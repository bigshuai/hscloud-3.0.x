package com.hisoft.hscloud.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;



public class UserListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {

		
		HttpSession session = event.getSession();
		ServletContext sc=(ServletContext)session.getServletContext();
		String vmId = (String)sc.getAttribute(session.getId());
		HttpSession appSession = (HttpSession)sc.getAttribute(vmId);
		if(null != appSession && session.getId().equals(appSession.getId())){
			sc.removeAttribute(vmId);
		}
		sc.removeAttribute(session.getId());
		
	}
	
}
