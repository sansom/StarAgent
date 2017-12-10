package com.core.jadlwork.model.cache;

import java.util.Map;

/**
 * 缓存服务器的bean对象
 * @类名: ServerCacheBean
 * @作者: 李春晓
 * @时间: 2017-1-11 下午2:58:17
 */
public class ServerCacheBean{

	private String fwqip;		//服务器ip
	private String fwqczxt;		//服务器操作系统
	private String fwqstatus;	//服务器状态
	private String fwqname;		//服务器名称
	private String backupsrc;	//文件备份路径
	private String realname;	//服务器访问域名
	private String cpuused;		//CPU使用率
	private String memeryused;	//内存使用率
	private int threadcount;	//线程数量
	private String jvmmemory;	//JVM可用堆内存
	private int jvmthreadcount;	//JVM运行线程数
	private int jvmloadedclasscount;	//JVM加载的类的个数
	private String dk;		//服务器端口
	private String szjf;		//服务器所在机房
	private Map appinfo;		//应用信息
	
	public String getDk() {
		return dk;
	}
	public void setDk(String dk) {
		this.dk = dk;
	}
	public String getSzjf() {
		return szjf;
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
	public String getFwqstatus() {
		return fwqstatus;
	}
	public void setFwqstatus(String fwqstatus) {
		this.fwqstatus = fwqstatus;
	}
	public String getFwqname() {
		return fwqname;
	}
	public void setFwqname(String fwqname) {
		this.fwqname = fwqname;
	}
	public String getBackupsrc() {
		return backupsrc;
	}
	public void setBackupsrc(String backupsrc) {
		this.backupsrc = backupsrc;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCpuused() {
		return cpuused;
	}
	public void setCpuused(String cpuused) {
		this.cpuused = cpuused;
	}
	public String getMemeryused() {
		return memeryused;
	}
	public void setMemeryused(String memeryused) {
		this.memeryused = memeryused;
	}
	public int getThreadcount() {
		return threadcount;
	}
	public void setThreadcount(int threadcount) {
		this.threadcount = threadcount;
	}
	public String getJvmmemory() {
		return jvmmemory;
	}
	public void setJvmmemory(String jvmmemory) {
		this.jvmmemory = jvmmemory;
	}
	public int getJvmthreadcount() {
		return jvmthreadcount;
	}
	public void setJvmthreadcount(int jvmthreadcount) {
		this.jvmthreadcount = jvmthreadcount;
	}
	public int getJvmloadedclasscount() {
		return jvmloadedclasscount;
	}
	public void setJvmloadedclasscount(int jvmloadedclasscount) {
		this.jvmloadedclasscount = jvmloadedclasscount;
	}
	public Map getAppinfo() {
		return appinfo;
	}
	public void setAppinfo(Map appinfo) {
		this.appinfo = appinfo;
	}
	public void setSzjf(String szjf) {
		this.szjf = szjf;
	}
	@Override
	public String toString() {
		return "ServerCacheBean [fwqip=" + fwqip + ", fwqczxt=" + fwqczxt
				+ ", fwqStatus=" + fwqstatus + ", fwqname=" + fwqname
				+ ", backupSrc=" + backupsrc + ", realname=" + realname
				+ ", cpuUsed=" + cpuused + ", memeryUsed=" + memeryused
				+ ", threadCount=" + threadcount + ", jvmMemory=" + jvmmemory
				+ ", jvmThreadCount=" + jvmthreadcount
				+ ", jvmLoadedClassCount=" + jvmloadedclasscount + ", appInfo="
				+ appinfo + "]";
	}
}