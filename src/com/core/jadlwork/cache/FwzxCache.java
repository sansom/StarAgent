package com.core.jadlwork.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.MapUtils;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.utils.ServiceInvokeUtils;
/**
 * 服务总线缓存管理
 * TODO
 * @作者：吴家旭
 * @时间：Sep 17, 2015 10:44:13 AM
 */
public class FwzxCache {
	private static Logger log = Logger.getLogger(FwzxCache.class);
	/**
	 * 服务器运行情况缓存{serverip:serverStatus,serverip:serverStatus}  
	 * 用途：用于接入系统的服务器列表状态
	 * serverip:服务器IP
	 * serverStatus:服务器状态  0：正常 1：异常
	 */
	private static Map serverCache = new HashMap();
	
	
	/**
	 * 服务器域名缓存{serverip:serverym,serverip:serverym}  
	 * 用途：对外的域名请求
	 * serverip:服务器IP
	 * serverym:服务器域名
	 */
	private static Map serverYm = new HashMap();
	
	/**
	 * 服务器应用部署均衡缓存 {serverip:appCount,serverip:appCount}
	 * 用途：记录服务器上部署的应用数量，用于应用部署时决定应部署到哪台服务器上
	 * serverip:服务器ip
	 * appCount:应用数量
	 */
	private static Map nomalServerCache = new HashMap();

	/**
	 * 应用运行服务器缓存{appid:[ip,ip,ip],appid:[ip,ip,ip]}
	 * 用途：记录应用部署在哪些服务器上
	 * appid：应用ID
	 * ip：服务器IP
	 */
	private static Map appCache = new HashMap();
	
	/**
	 * 远程服务器线程数缓存{serverip:ThreadActiveCount,serverip:ThreadActiveCount}
	 * 用途：记录服务器底层运行的线程数，用于云平台调用应用的负载均衡计算，云平台优先调用线程数少的服务器
	 * serverip:服务器IP
	 * ThreadActiveCount:当前运行的线程数
	 */
	private static Map ThreadActiveCountCache = new HashMap();
	
	/**
	 * 远程服务器cpu使用率缓存{serverip:CpuUsed,serverip:CpuUsed}
	 * 用途：记录服务器当前的cpu使用率
	 * serverip:服务器IP
	 * CpuUsed:cpu使用率
	 */
	private static Map CpuUsedCache = new HashMap();
	
	/**
	 * 服务访问权限缓存--允许列表{fwid:iplist}  
	 * 用途：云平台请求url的时候判断IP是否有权限
	 * fwid:服务ID
	 * iplist:ip列表
	 */ 
	private static Map service_allow_Cache = new HashMap();
	
	/**
	 * 服务访问权限缓存--不允许列表{fwid:iplist}  
	 * 用途：云平台请求url的时候判断IP是否有权限
	 * fwid:服务ID
	 * iplist:ip列表
	 */ 
	private static Map service_unallow_Cache = new HashMap();
	
	private static FwzxCache instance = new FwzxCache();
	
	public FwzxCache() {
		instance = this;
	}

	
	public static Map getThreadActiveCountCache() {
		return ThreadActiveCountCache;
	}


	public static Map getCpuUsedCache() {
		return CpuUsedCache;
	}


	public static void setCpuUsedCache(Map cpuUsedCache) {
		CpuUsedCache = cpuUsedCache;
	}


	public static FwzxCache getInstance() {
		return instance;
	}

	public static Map getServerCache() {
		return serverCache;
	}

	public static Map getNomalServerCache() {
		return nomalServerCache;
	}

	/**
	 * 服务器缓存删除服务器
	 * @参数：@param fwqip 服务器IP地址
	 * @返回值：void
	 */
	public static  void removeServerCache(String fwqip) {
		serverCache.remove(fwqip);
	}

	/**
	 * 服务器缓存加入服务器信息
	 * @参数：@param fwqip 服务器ip地址
	 * @参数：@param fwqzt 服务器状态
	 * @返回值：void
	 */
	public static void addServerCache(String fwqip,String fwqzt) {
		serverCache.put(fwqip, fwqzt);
	}
	
	/**
	 * 删除IP与域名对应关系
	 * @参数：@param fwqip 服务器IP地址
	 * @返回值：void
	 */
	public static  void removeServerym(String fwqip) {
		serverYm.remove(fwqip);
	}

	/**
	 * 增加IP与域名对应关系
	 * @参数：@param fwqip 服务器ip
	 * @参数：@param fwqym 服务器域名
	 * @返回值：void
	 */
	public static void addServerYm(String fwqip,String fwqym) {
		if(fwqym == null || "".equals(fwqym)){
			serverYm.put(fwqip, fwqip);
		}else{
			serverYm.put(fwqip, fwqym);
		}
	}
	
	
	/**
	 * 应用部署均衡缓存添加应用次数
	 * @参数：@param ip 服务器ip地址
	 * @返回值：void
	 */
	public static void addNomalServerCache(String ip) {
		if(nomalServerCache.containsKey(ip)){
			int i = Integer.valueOf(String.valueOf(nomalServerCache.get(ip)));
			nomalServerCache.put(ip, i+1);
		}else{
			nomalServerCache.put(ip, 1);
		}
		
	}
	
	/**
	 * 应用部署均衡缓存减少应用次数
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void minusNomalServerCache(String ip) {
		if(nomalServerCache.containsKey(ip)){
			int i = Integer.valueOf(String.valueOf(nomalServerCache.get(ip)));
			if(i > 0){
				nomalServerCache.put(ip, i-1);
			}
		}
	}
	
	/**
	 *  应用部署均衡缓存队列添加ip
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void addFwqyyCache(String ip) {
		if(!nomalServerCache.keySet().contains(ip)){
			nomalServerCache.put(ip, 0);
		}		
	}
	
	/**
	 * 应用部署均衡缓存队列删除ip
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void removeFwqyyCache(String ip) {
		nomalServerCache.remove(ip);
	}


	/**
	 * 添加应用运行情况缓存
	 * @参数：@param yyid
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void addAppCache(String yyid, String ip) {
		if(appCache.containsKey(yyid)) {
			if(!((List) appCache.get(yyid)).contains(ip)){
				((List) appCache.get(yyid)).add(ip);
			}
		}else{
			List serverList = new ArrayList();
			serverList.add(ip);
			appCache.put(yyid, serverList);
		}
	}
	
	/**
	 * 删除应用运行缓存
	 * @参数：@param yyid
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void removeAppCache(String yyid) {
		if(appCache.containsKey(yyid)){
			appCache.remove(yyid);
		}
	}
	
	
	/**
	 * 根据YYID和IP删除应用运行缓存
	 * @参数：@param yyid
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void removeAppCacheByIP(String yyid,String ip) {
		if(appCache.containsKey(yyid)){
			((List) appCache.get(yyid)).remove(ip);
		}
	}

	
	
	public static Map getAppCache() {
		return appCache;
	}

	/**
	 * 更新cpu使用率
	 * @param cpuUsed2 
	 * @参数：@param ip
	 * @参数：@param cpuUsed
	 * @返回值：void
	 */
	public static void addCpuRatioCache(String ip, String activeThreahCount, String cpuUsed) {
		//System.out.println("当前线程缓存："+ThreadActiveCountCache);
		//if(!ThreadActiveCountCache.containsKey(ip)){
			ThreadActiveCountCache.put(ip,Integer.valueOf(activeThreahCount));
		//}
		//System.out.println("更新后线程缓存："+ThreadActiveCountCache);
		//if(!CpuUsedCache.containsKey(ip)){
			CpuUsedCache.put(ip,cpuUsed);
		//}
		
	}

	/**
	 * 清除cpu使用率
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void removeCpuRatioCache(String ip) {
		ThreadActiveCountCache.remove(ip);
		CpuUsedCache.remove(ip);
	}
	
	
	/**
	 * 动态负载均衡，根据cpu使用率获取IP地址
	 * @参数：@param url
	 * @参数：@return
	 * @返回值：String
	 */
	public String getRealIpFromServiceBus(String yyid) {
		/*
		 * 2017/06/30 李春晓修改  从应用集群缓存中去取数据
		 */
		return ServiceInvokeUtils.getYmFromYyJqInfo(yyid);
		/*String ip = "";
		if(yyid == null || yyid.equals("")){
			return "";
		}
		//1.获取应用部署的服务器IP列表
		List ipList = (List) appCache.get(yyid);
		if(ipList == null || ipList.size() <= 0){
			return "";
		}
		
		//2.获取线程数最小的服务器
		if(ThreadActiveCountCache != null && ThreadActiveCountCache.size() > 0){
			Map map = MapUtils.sortByValue(ThreadActiveCountCache);
			for (Object key : map.keySet()) {
				if(ipList.contains(key)){
					System.out.println(key+":线程数量"+map.get(key)+"个");
				}
			}
			for (Object key : map.keySet()) {
				 if(key != null && !key.equals("") && ipList.contains(key) ){
					 ip = String.valueOf(key);
					 ThreadActiveCountCache.put(key, Integer.valueOf(String.valueOf(ThreadActiveCountCache.get(key)))+1);
					 break;
				 }
			}
		}
		//3.获取IP对应的域名
		
		return String.valueOf(serverYm.get(ip));*/
	}


	/**
	 * 服务器添加时更新的缓存
	 * @param cpuUsed 
	 * @参数：@param ip
	 * @参数：@param result
	 * @返回值：void
	 */
	public static void serverAdd(String ip, String activeThreahCount, String cpuUsed) {
		
		addFwqyyCache(ip);
		addCpuRatioCache(ip,activeThreahCount,cpuUsed);
	}

	/**
	 * 服务器删除时更新缓存
	 * @参数：@param ip
	 * @返回值：void
	 */
	public static void serverRemove(String ip) {
		removeFwqyyCache(ip);
		removeCpuRatioCache(ip);
	}

	
	/**
	 * 服务授权--允许访问缓存添加
	 * @参数：@param 
	 * @返回值：void
	 */
	public static  void addServiceAllowCache(String fwid,List iplist) {
		service_allow_Cache.put(fwid, iplist);
	}

	/**
	 * 服务授权--不允许访问缓存添加
	 * @参数：@param 
	 * @返回值：void
	 */
	public static  void addServiceUnallowCache(String fwid,List iplist) {
		service_unallow_Cache.put(fwid, iplist);
	}

	public static void setServerCache(Map serverCache) {
		FwzxCache.serverCache = serverCache;
	}


	public static void setNomalServerCache(Map nomalServerCache) {
		FwzxCache.nomalServerCache = nomalServerCache;
	}


	public static void setAppCache(Map appCache) {
		FwzxCache.appCache = appCache;
	}


	public static void setThreadActiveCountCache(Map threadActiveCountCache) {
		ThreadActiveCountCache = threadActiveCountCache;
	}


	public static Map getService_allow_Cache() {
		return service_allow_Cache;
	}


	public static void setService_allow_Cache(Map service_allow_Cache) {
		FwzxCache.service_allow_Cache = service_allow_Cache;
	}


	public static Map getService_unallow_Cache() {
		return service_unallow_Cache;
	}


	public static void setService_unallow_Cache(Map service_unallow_Cache) {
		FwzxCache.service_unallow_Cache = service_unallow_Cache;
	}

	
}