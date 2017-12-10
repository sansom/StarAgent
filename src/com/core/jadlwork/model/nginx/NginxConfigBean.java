package com.core.jadlwork.model.nginx;

import java.util.Date;

/**
 * Nginx配置操作的bean对象
 * @类名: NginxConfigBean
 * @作者: 李春晓
 * @时间: 2017-2-16 下午3:47:03
 */
public class NginxConfigBean {

	public static final String db_tablename = "t_nginx_config";
	public static final String db_tablepkfields = "id";
	
	private String id;		//序号
	private String fwqname;	//服务器名称
	private String jtdk;	//监听端口
	private String fxdldk;	//反向代理端口，就是socket通信的端口
	private String dlmc;	//代理名称
	private String dlfs;	//代理方式
	private String dllx;	//代理类型
	private String xy;		//http协议
	private String dlfwq;	//代理服务器
	private String dlyy;	//代理应用	选择为动态从应用获取时候保存的内容
	private String sxw;		//访问上下文
	private Date cjsj;		//创建时间
	private Date zhxgsj;	//最后修改时间
	private Date cxsj;		//撤销时间
	private String pzlx;	//配置类型
	private String nginxid;	//所在的Nginx的id
	private String zt;		//状态	0:可用     1:不可用
	
	//=============get/set=====================
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
	public String getJtdk() {
		return jtdk;
	}
	public void setJtdk(String jtdk) {
		this.jtdk = jtdk;
	}
	public String getFxdldk() {
		return fxdldk;
	}
	public void setFxdldk(String fxdldk) {
		this.fxdldk = fxdldk;
	}
	public String getDlmc() {
		return dlmc;
	}
	public void setDlmc(String dlmc) {
		this.dlmc = dlmc;
	}
	public String getDlfs() {
		return dlfs;
	}
	public void setDlfs(String dlfs) {
		this.dlfs = dlfs;
	}
	public String getDllx() {
		return dllx;
	}
	public void setDllx(String dllx) {
		this.dllx = dllx;
	}
	public String getXy() {
		return xy;
	}
	public void setXy(String xy) {
		this.xy = xy;
	}
	public String getDlfwq() {
		return dlfwq;
	}
	public void setDlfwq(String dlfwq) {
		this.dlfwq = dlfwq;
	}
	public String getDlyy() {
		return dlyy;
	}
	public void setDlyy(String dlyy) {
		this.dlyy = dlyy;
	}
	public String getSxw() {
		return sxw;
	}
	public void setSxw(String sxw) {
		this.sxw = sxw;
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
	public String getPzlx() {
		return pzlx;
	}
	public void setPzlx(String pzlx) {
		this.pzlx = pzlx;
	}
	public String getNginxid() {
		return nginxid;
	}
	public void setNginxid(String nginxid) {
		this.nginxid = nginxid;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
