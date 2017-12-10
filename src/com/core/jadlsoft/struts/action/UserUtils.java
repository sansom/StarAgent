/**
 * com.jadlsoft.struts.action UserUtils.java Dec 26, 2007 9:31:28 AM
 */
package com.core.jadlsoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.utils.MBConstant;
import com.core.jadlsoft.utils.XzhqhUtils;

/**
 * @author sky 功能：
 * 
 */
public class UserUtils { 
	public static final String USER_SESSION = "agentUser"; 
	/**
	 * 
	 * getUserSessionBean() 功能：获取session中用户信息
	 * 
	 * @param request
	 * @return UserSessionBean
	 */
	public static UserSessionBean getUserSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		} else {
			return (UserSessionBean) session.getAttribute(USER_SESSION);
		}
	}

	/**
	 * checkGN() 功能：判断用户对某模块是否有修改权限
	 * 
	 * @param userSessionBean
	 *            登录用户基本信息
	 * @param gnCode
	 *            被操作的功能代码
	 * @return boolean true:无修改权限 false:有修改权限
	 */
	public static boolean checkGN(UserSessionBean userSessionBean, String gnCode) {
		List gnCodes = userSessionBean.getGndmList();
		for (int i = 0; i < gnCodes.size(); i++) {
			if (gnCodes.get(i).equals(gnCode)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checkXzqh() 功能：判断用户是否为该行政区划下的用户
	 * 
	 * @param userSessionBean
	 *            登录用户基本信息
	 * @param xzqh
	 *            被操作记录所属行政区划
	 * @param type
	 *            验证类型 1：本行政区划只能操作本行政区划数据，本行政区划下的数据只能由本行政区划下的用户操作
	 *            2：上级可以操作下级数据，下级只能查看上级数据（省、市、县三级）
	 * @return boolean true:无修改权限 false:有修改权限
	 */
	public static boolean checkXzqh(UserSessionBean userSessionBean,
			String xzqh, int type) {
		boolean boolReturn = false;
		String userXzqh = userSessionBean.getXzqh();
		if (type == 1) {
			boolReturn = !userXzqh.equals(xzqh);
		} else if (type == 2) {
			boolReturn = !xzqh.startsWith(getShortXZQH(userXzqh));
		}
		return boolReturn;
	}

	/**
	 * getShortXZQH() 功能：
	 * 
	 * @param xzqh
	 * @return String
	 */
	private static String getShortXZQH(String xzqh) {
		return XzhqhUtils.getXZHQH(xzqh);
	}
	
	public static boolean isAdmin(String yhlx){
		if(MBConstant.ADMIN.equals(yhlx)){
			return true;
		}
		else return  false;
	}
	 
}
