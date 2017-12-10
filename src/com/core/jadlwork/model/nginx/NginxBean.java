package com.core.jadlwork.model.nginx;

import java.util.Date;

/**
 * Nginx服务器操作的bean对象
 * @类名: NginxBean
 * @作者: 李春晓
 * @时间: 2017-4-128 下午1:50:03
 */
public class NginxBean {

	public static final String db_tablename = "t_nginx_jbxx";
	public static final String db_tablepkfields = "id";
	
	/*
	 *    ID       VARCHAR2(17) not null,
		  FWQNAME  VARCHAR2(50),
		  NGINXSRC VARCHAR2(50),
		  FWQIP    VARCHAR2(16),
		  FWQCZXT  CHAR(2),
		  CJSJ     DATE,
		  ZHXGSJ   DATE,
		  CXSJ     DATE,
		  ZT       VARCHAR2(1)
	 */
	private String id;
	private String fwqname;
	private String nginxsrc;
	private String fwqip;
	private String fwqczxt;
	private Date cjsj;
	private Date zhxgsj;
	private Date cxsj;
	private String zt;
    private String gxzt;
    private String gxsbyy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFwqname() {
		return fwqname;
	}
	public void setFwqname(String fwqname) {
		this.fwqname = fwqname;
	}
	public String getNginxsrc() {
		return nginxsrc;
	}
	public void setNginxsrc(String nginxsrc) {
		this.nginxsrc = nginxsrc;
	}
	public String getFwqip() {
		return fwqip;
	}
	public void setFwqip(String fwqip) {
		this.fwqip = fwqip;
	}
	public String getFwqczxt() {
		return fwqczxt;
	}
	public void setFwqczxt(String fwqczxt) {
		this.fwqczxt = fwqczxt;
	}
	public Date getCjsj() {
		return cjsj;
	}
	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
	public Date getZhxgsj() {
		return zhxgsj;
	}
	public void setZhxgsj(Date zhxgsj) {
		this.zhxgsj = zhxgsj;
	}
	public Date getCxsj() {
		return cxsj;
	}
	public void setCxsj(Date cxsj) {
		this.cxsj = cxsj;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getGxzt() {
		return gxzt;
	}
	public void setGxzt(String gxzt) {
		this.gxzt = gxzt;
	}
	public String getGxsbyy() {
		return gxsbyy;
	}
	public void setGxsbyy(String gxsbyy) {
		this.gxsbyy = gxsbyy;
	}
}
