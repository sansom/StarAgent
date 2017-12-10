package com.core.jadlsoft.filter;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.struts.action.UserUtils;

public class UserLoginListener implements ServletContextListener,
		HttpSessionListener {
	
	private Map loginedUsers;  

	public void contextDestroyed(ServletContextEvent arg0) {
		arg0.getServletContext().removeAttribute("loginedusers");
		loginedUsers.clear();
		loginedUsers = null;
	}

	public void contextInitialized(ServletContextEvent arg0) {
		loginedUsers = new Hashtable();
		arg0.getServletContext().setAttribute("loginedusers", loginedUsers);
	}

	public void sessionCreated(HttpSessionEvent arg0) {

	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		Object obj = (UserSessionBean)arg0.getSession().getAttribute(UserUtils.USER_SESSION);
		if(obj!=null && obj instanceof UserSessionBean) {
			UserSessionBean userBean = (UserSessionBean)obj;
			if(loginedUsers.containsKey(userBean.getUserid())) {
				loginedUsers.remove(userBean.getUserid());
			}
		}
	}
}
