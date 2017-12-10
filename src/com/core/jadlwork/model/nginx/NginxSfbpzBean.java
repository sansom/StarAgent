package com.core.jadlwork.model.nginx;

import java.util.Date;

/**
 * nginx试发布配置bean
 * @类名: NginxSfbpzBean
 * @作者: lcx
 * @时间: 2017-9-13 下午3:17:33
 */
public class NginxSfbpzBean {

	public static final String db_tablename = "t_nginx_sfbpz";
	public static final String db_tablepkfields = "id";
	
	private String id;
	private String nginxid;
	private String regex;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNginxid() {
		return nginxid;
	}
	public void setNginxid(String nginxid) {
		this.nginxid = nginxid;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
}
