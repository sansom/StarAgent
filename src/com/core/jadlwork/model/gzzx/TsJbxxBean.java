package com.core.jadlwork.model.gzzx;

import java.util.Date;

public class TsJbxxBean {

	public static final String db_tablename = "t_ts_jbxx";
	public static final String db_tablepkfields = "id";
	
	/**
	 * ID       VARCHAR2(17) not null,
	  TSLX     VARCHAR2(2),
	  MSG      VARCHAR2(255),
	  TOUSER   VARCHAR2(100),
	  TSSSID   VARCHAR2(17),
	  INTIME   DATE,
	  SENDTIME DATE,
	  TSZT     VARCHAR2(2),
	  CFCS     VARCHAR2(1),
	  ZT       VARCHAR2(1)
	 */
	
	private String id;
	private String tslx;
	private String msg;
	private String touser;
	private String tsssid;
	private Date intime;
	private Date sendtime;
	private String tszt;
	private String cfcs;
	private String zt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTslx() {
		return tslx;
	}
	public void setTslx(String tslx) {
		this.tslx = tslx;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTsssid() {
		return tsssid;
	}
	public void setTsssid(String tsssid) {
		this.tsssid = tsssid;
	}
	public Date getIntime() {
		return intime;
	}
	public void setIntime(Date intime) {
		this.intime = intime;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public String getTszt() {
		return tszt;
	}
	public void setTszt(String tszt) {
		this.tszt = tszt;
	}
	public String getCfcs() {
		return cfcs;
	}
	public void setCfcs(String cfcs) {
		this.cfcs = cfcs;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
}
