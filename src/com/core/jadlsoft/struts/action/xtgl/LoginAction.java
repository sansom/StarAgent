package com.core.jadlsoft.struts.action.xtgl;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.core.jadlsoft.business.xtgl.IGnlbManager;
import com.core.jadlsoft.business.xtgl.IRoleManager;
import com.core.jadlsoft.business.xtgl.IUserManager;
import com.core.jadlsoft.model.xtgl.Gnlb;
import com.core.jadlsoft.model.xtgl.UserBean;
import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.struts.action.UserUtils;
import com.core.jadlsoft.utils.MBConstant;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.XzhqhUtils;
import com.opensymphony.xwork2.Action;
/**
 * 
 * @功能 登录Action
 * @作者 吴家旭 Feb 27, 2013 4:12:21 PM
 * @version 1.0
 */
public class LoginAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserBean userBean;
	private IUserManager userManager;
	private UserSessionBean userSessionBean;
	private IRoleManager roleManager;
	private IGnlbManager gnlbManager;
	

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public void setGnlbManager(IGnlbManager gnlbManager) {
		this.gnlbManager = gnlbManager;
	}
	
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	
	public String execute() {
		
		String username = userBean.getUserid();
		String pwd = userBean.getPassword();
		userBean = userManager.get(username, pwd);
		if (userBean == null) {
			//登录失败
			request.setAttribute("errMsg", "用户名或者密码错误，请重新登录");
			return Action.INPUT;
		}
		HttpSession session = request.getSession(true);
		userSessionBean = tranUserToSession(userBean);
		//将菜单保存到session中
		List gnlbList = gnlbManager.getGnlbListByRoleId(userSessionBean.getRole().getRoleid());
		session.setAttribute("gnlbList", gnlbList);
		session.setAttribute(UserUtils.USER_SESSION, userSessionBean);
		return "login";
		/*
		String username = userBean.getUserid();
		String pwd = userBean.getPassword();
		
		
		UserBean userBean = null;
		try {
			userBean = userManager.get(username, pwd);
		} catch (Exception e) {
			LOG.error("登录出错！", e);
			throw new RuntimeException("登录出错！");
		}
		if (userBean != null) {

			HttpSession session = request.getSession(true);
			userSessionBean = tranUserToSession(userBean);
			userSessionBean.setLoginip(request.getRemoteAddr()); // ip地址

			List treelist = null;
			treelist = userManager.getTreeModuleInfo(String.valueOf(userSessionBean.getUserid()));
			if (treelist != null && treelist.size() > 0) {
				Module module = (Module) treelist.get(0);
				Module module_1 = (Module) treelist.get(1);
				if (module.getCode().endsWith("0000")) {
					if (!"".equals(module.getLink())
							&& module.getLink() != null) {
						userSessionBean.setFirstPageUrl(request
								.getContextPath()
								+ module.getLink());
					} else {
						userSessionBean.setFirstPageUrl(request
								.getContextPath()
								+ module_1.getLink());
					}
				}
			}
			
			
			session.setAttribute("treelist", treelist);
			session.setAttribute("userSessionBean", userSessionBean);
			
			//初始化集团、企业信息
			String rt = checkQydm(userBean);
			if(!"".equals(rt)){
				return rt;
			}
			
			return "login";
	
		}
		return INPUT;*/
	}
	private String getXzqhmc(String xzqh) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @功能 用户退出
	 * @return
	 * @作者 吴家旭 Feb 27, 2013 4:08:52 PM
	 */
	public String loginout() {
		request.getSession(true).invalidate();
		return "failure";
	}
	
	/**
	 * 
	 * @功能 登录用户名和密码验证
	 * @return
	 * @throws Exception
	 * @作者 吴家旭 Feb 27, 2013 4:07:50 PM
	 */
	public String checkUserAndPwd() throws Exception {
		  
		String userid = java.net.URLDecoder.decode(request.getParameter("userid") , "UTF-8");
		String pwd = request.getParameter("password");
		String res = "success";
		//List list = userManager.getUserByuserid(userid);
		/*if (list == null || list.size() == 0) {
			res = "用户名错误！";
		} else {
			Map<String, String> map = (Map<String, String>) list.get(0);
			String pwd_ = map.get("password");
			if (!pwd.equals(pwd_)) {
				res = "密码错误！";
			}

		}*/
	
		ResponseUtils.render(response, res);
		return null;
		
	}
	private UserSessionBean tranUserToSession(UserBean userBean) {
		
		UserSessionBean userSessionBean = new UserSessionBean();
		userSessionBean.setId(userBean.getId());
		userSessionBean.setUserName(userBean.getUsername());
		userSessionBean.setUserid(userBean.getUserid());
		userSessionBean.setQydm(userBean.getQydm());
		userSessionBean.setXzqh(XzhqhUtils.getXZHQH(userBean.getXzqh()));
		userSessionBean.setZXzqh(userBean.getXzqh());
		userSessionBean.setZt(userBean.getZt());
		//设置用户的角色
		userSessionBean.setRole(roleManager.getRoleByUserId(userBean.getId()));
//		userSessionBean.setGndmList(gncodes);
		return userSessionBean;
	}
	
}
