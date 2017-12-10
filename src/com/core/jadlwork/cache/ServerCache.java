package com.core.jadlwork.cache;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * 服务器缓存
 * @author wujiaxu
 * @Time 2017-1-10 下午01:48:48
 *
 */
public class ServerCache {
	
	/**
	 * 服务器缓存
	 * 用途：缓存所有接入的服务器信息
	 * 结构：
	 * 	{
	 *		serverip(服务器IP)	:	{
	 *			fwqStatus	:	状态0:正常 1:异常,
	 *			fwqczxt		:	服务器操作系统,
	 *			fwqname		:	服务器名称,
	 *			backupSrc	:	文件备份路径,
	 *			realname	:	服务器访问域名,
	 *			cpuUsed		:	CPU使用率,
	 *			memeryUsed	:	内存使用率,
	 *			threadCount	:	线程数量,
	 *			jvmMemory	:	JVM可用堆内存,
	 *		jvmThreadCount	:	JVM运行线程数,
	 *	jvmLoadedClassCount	:	JVM加载的类的个数,
	 *				dk		:	服务器端口,
	 *				szjf	:	服务器所在机房,
	 *			appInfo		:	{
	 *				appname		:	{
	 *					appid		:	应用ID,	//暂无
	 *					appname		:	应用名称
	 *					appStatus	:	应用状态	//暂无
	 *				},
	 *				appname...
	 *			}
	 *		},
	 *		serverip...
	 *	}
	 */
	
	/*
	 * 云平台服务器缓存
	 */
	private static Map serverCache = new HashMap();
	
	/*
	 * 托管服务器缓存
	 */
	private static Map tgServerCache = new HashMap();
	
	/*
	 * 第三方服务器缓存
	 */
	private static Map wbServerCache = new HashMap();
	
	//云平台服务器缓存get/set
	public static Map getServerCache() {
		return serverCache;
	}
	public static void setServerCache(Map serverCache) {
		ServerCache.serverCache = serverCache;
	}
	
	//托管服务器缓存get/set
	public static Map getTgServerCache() {
		return tgServerCache;
	}
	public static void setTgServerCache(Map tgServerCache) {
		ServerCache.tgServerCache = tgServerCache;
	}

	//外部服务器缓存的get/set
	public static Map getWbServerCache() {
		return wbServerCache;
	}
	public static void setWbServerCache(Map wbServerCache) {
		ServerCache.wbServerCache = wbServerCache;
	}
	
	//单例设计
	private static ServerCache instance = new ServerCache();
	public static ServerCache getInstance() {
		return instance;
	}

}