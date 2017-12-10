package com.core.jadlsoft.model.xtgl;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class UserSessionBean implements Serializable {

	/**
	 * 过滤字段
	 */
	private String xzqh;
	/**
	 * 行政区划名称
	 */
	private String xzqh_cn;
	/**
	 * 短的行政区划（长度：2位 或者 4位 或者 6位），用于控制用户的访问权限
	 */
	private String Shortxzqh;

	/**
	 * 用户的功能代码
	 */
	@SuppressWarnings("unchecked")
	private List gndmList;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 企业代码
	 */
	private String qydm;
	
	/**
	 * 状态
	 */
	private String zt;
	
	/**
	 * 登陆IP
	 */
	private String loginip;
	
	/**
	 * 用户id
	 */
	private String userid;
	
	/**
	 * 角色
	 */
	private Role role;
	
	/**
	 * 用户主键
	 */
	private String id;
	
	/**
	 * 行政区划
	 */
	private String zXzqh;
	
	
	private String firstPageUrl;

	public String getQydm() {
		return qydm;
	}

	public void setQydm(String qydm) {
		this.qydm = qydm;
	}

	public String getFirstPageUrl() {
		return firstPageUrl;
	}

	public void setFirstPageUrl(String firstPageUrl) {
		this.firstPageUrl = firstPageUrl;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getShortxzqh() {
		return Shortxzqh;
	}

	public void setShortxzqh(String shortxzqh) {
		Shortxzqh = shortxzqh;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getXzqh_cn() {
		return xzqh_cn;
	}

	public void setXzqh_cn(String xzqh_cn) {
		this.xzqh_cn = xzqh_cn;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}



	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	

	public List getGndmList() {
		return gndmList;
	}

	public void setGndmList(List gndmList) {
		this.gndmList = gndmList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZXzqh() {
		return zXzqh;
	}

	public void setZXzqh(String xzqh) {
		zXzqh = xzqh;
	}
	
}
