package com.core.jadlwork.cache;

import java.util.Map;

import com.core.jadlwork.model.cache.ServerCacheBean;
import com.core.jadlwork.model.fwqgl.FwqBean;

/**
 * 缓存操作的manager接口
 * @描述: 在manager中将不同的缓存分开，也就是提供不同的方法，
 * 			云平台的服务器的缓存、托管服务器的缓存以及之后的一些需要的其他的缓存
 * @作者: 李春晓
 * @时间: 2017-2-10 下午2:44:35
 */
public interface ICacheManager {

	/**
	 * 初始化缓存信息
	 * @功能: 依次将所有的缓存信息初始化
	 * @return: void
	 */
	void initCache();
	
	// ==============================云平台服务器的缓存操作=============================
	
	/**
	 * 更新云平台的缓存
	 * @return: void
	 */
	void updateServerCache();
	
	/**
	 * 更新云平台中指定的服务器的缓存信息
	 * @param ip 服务器ip
	 * @return: void
	 */
	void updateServerCacheByFwqIp(String ip);
	
	/**
	 * 获取云平台所有的缓存信息
	 * @return: Map
	 */
	Map getServerCache();
	

	
	
	/**
	 * 添加云平台的一个服务器
	 * @param serverCacheBean
	 * @return: void
	 */
	void addServerCache(ServerCacheBean serverCacheBean);
	
	/**
	 * 判断服务器是否在线
	 * @param fwqip
	 * @return: boolean
	 */
	boolean isServerOnline(String fwqip);
	
	/**
	 * 根据ip移除云平台中的服务器缓存信息
	 * @param fwqip
	 * @return: void
	 */
	void removeServerCache(String fwqip);
	
	/**
	 * 获取服务器的运行状态
	 * @param string
	 * @return: String
	 */
	String getServerStatus(String fwqip);
	
	// ==============================托管服务器的缓存操作=============================
	
	/**
	 * 更新托管服务器的缓存
	 * @return: void
	 */
	void updateTgServerCache();
	
	/**
	 * 更新托管服务器中指定的服务器的缓存信息
	 * @param ip 服务器ip
	 * @return: void
	 */
	void updateTgServerCacheByFwqIp(String ip);
	
	/**
	 * 获取所有的托管服务器缓存信息
	 * @return: Map
	 */
	Map getTgServerCache();
	
	
	
	/**
	 * 添加托管的一个服务器
	 * @param serverCacheBean
	 * @return: void
	 */
	void addTgServerCache(ServerCacheBean serverCacheBean);
	
	/**
	 * 判断服务器是否在线
	 * @param fwqip
	 * @return: boolean
	 */
	boolean isTgServerOnline(String tgFwqip);
	
	/**
	 * 根据ip移除托管服务器中的服务器缓存信息
	 * @param fwqip
	 * @return: void
	 */
	void removeTgServerCache(String tgFwqip);
	
	/**
	 * 获取托管服务器中服务器的运行状态
	 * @param string
	 * @return: String
	 */
	String getTgServerStatus(String tgFwqip);
	
	// ==============================第三方服务器的缓存操作=============================
	
	/**
	 * 更新外部服务器的缓存
	 * @return: void
	 */
	void updateWbServerCache();
	
	/**
	 * 更新第三方服务器中指定的服务器的缓存信息
	 * @param ip 服务器ip
	 * @return: void
	 */
	void updateWbServerCacheByFwqIp(String ip);
	
	/**
	 * 获取所有的第三方服务器缓存信息
	 * @return: Map
	 */
	Map getWbServerCache();

	/**
	 * 向数据库中插入服务器记录之前，从缓存中获取信息
	 * @param fwqBean
	 * @return: FwqBean
	 */
	FwqBean getFwqBySaveFwq(FwqBean fwqBean);
	
}
