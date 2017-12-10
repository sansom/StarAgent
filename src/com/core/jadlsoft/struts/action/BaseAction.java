/**
 * @Description 基础Action
 * @Company 京安丹灵
 * @author zongshuai
 * @date 2013-03-06
 * @version 1.0
 */

package com.core.jadlsoft.struts.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.utils.SystemConstants;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, ApplicationAware{
	public static final String VIEW = "view";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map application;
	/*protected CommonManager commonManager;
	
	public void setCommonManager(CommonManager commonManager) {
		this.commonManager = commonManager;
	}*/

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setApplication(Map application) {
		this.application = application;
	}
	
	/**
	 * 从session中取得当前登录用户信息
	 * @return
	 */
	public UserSessionBean getUserSessionBean() {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return null;
		} else {
			return (UserSessionBean)session.getAttribute(UserUtils.USER_SESSION);
		}
	}
}
