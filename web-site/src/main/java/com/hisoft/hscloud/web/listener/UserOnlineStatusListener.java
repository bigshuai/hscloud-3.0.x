package com.hisoft.hscloud.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

public class UserOnlineStatusListener implements HttpSessionListener {
	@Autowired
    private Facade facade;
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		ServletContext sc=event.getSession().getServletContext();
		WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		facade=(Facade)wc.getBean("facade");
		HttpSession session=event.getSession();
		User user=(User)session.getAttribute(Constants.LOGIN_CURRENTUSER);
		if(user!=null){
			facade.modifyUserOnlineStatus(user.getId(),(short)0);
		}
	}
}
