package com.core.jadlwork.cache;

import java.util.Map;

import com.core.jadlwork.model.cache.ServerCacheBean;

/**
 * 操作服务器缓存的Dao接口
 * @作者: 李春晓
 * @时间: 2017-2-10 下午2:55:41
 */
public interface ICacheDao {
	
	/**
	 * 添加一个服务器缓存对象
	 * @param cacheMap	要添加到的服务器缓存map集合
	 * @param cacheBean	要添加的对象，目前，该对象必须包含fwqip属性，因为添加到缓存中key就是fwqip
	 * @return: void
	 */
	void add(Map cacheMap, Object cacheBean);
	
	/**
	 * 根据ip删除缓存服务器
	 * @param cacheMap	要删除的缓存的map集合
	 * @param ip	要删除的服务器的ip
	 */
	void remove(Map cacheMap, String ip);
	
	/**
	 * 更新指定的服务器的信息
	 * @param cacheMap	要更新的缓存的map集合
	 * @param cacheBean	要更新的缓存对象，目前，该对象必须包含fwqip属性，因为添加到缓存中key就是fwqip
	 */
	void update(Map cacheMap, Object cacheBean);
	
	/**
	 * 按条件更新服务器的缓存信息
	 * @param cacheMap		要更新的缓存的map集合
	 * @param condition		条件
	 * @param fwqip 		服务器的ip
	 */
	void update(Map cacheMap, Map condition, String fwqip);
	
	/**
	 * 更新一个服务器的某些字段
	 * @param cacheMap	要更新的缓存的map集合
	 * @param cacheBean	要更新的对象
	 * @param fields	要更新的字段数组
	 */
	void updateByFields(Map cacheMap, Object cacheBean, String[] fields);
	
	/**
	 * 更新一个服务器排除字段后的字段
	 * @param cacheMap		要更新的缓存的map集合
	 * @param cacheBean		要更新的缓存对象
	 * @param exceptFields	要排除的字段
	 */
	void updateByExceptFields(Map cacheMap, Object cacheBean, String[] exceptFields);
	
	/**
	 * 查询指定类型的单个服务器
	 * @param cacheMap 要查询的服务器缓存的集合
	 * @param ip	要查询的服务器的ip
	 * @return: Object	查询到的服务器对象
	 */
	Object get(Map cacheMap, String ip);
	
	/**
	 * 获取云平台的服务器的缓存的集合
	 * @return: Map	云平台的服务器缓存集合
	 */
	Map getServerCache();
	
	/**
	 * 获取托管服务器的缓存的集合
	 * @return: Map	托管服务器的缓存的集合
	 */
	Map getTgServerCache();
	
	
	//================================================================================
	
	

	/**
	 * 增加一个服务器信息
	 * @param serverCacheBean	服务器缓存对象
	 * @param serverType 	操作的服务器的类型
	 * @return: void
	 */
	void saveFwq(ServerCacheBean serverCacheBean, String serverType);

	/**
	 * 获取指定类型的缓存信息
	 * @param serverType 服务器类型
	 * @return: Map
	 */
	Map getServerCache(String serverType);
	
	/**
	 * 更新单个服务器缓存信息
	 * @param serverCacheBean
	 * @param serverType 服务器类型
	 * @return: void
	 */
	void update(ServerCacheBean serverCacheBean, String serverType);
	
	/**
	 * 根据字段更新
	 * @param serverCacheBean
	 * @param fields 要更新的字段数组
	 * @param serverType 服务器类型
	 * @return: void 
	 */
	void update(ServerCacheBean serverCacheBean, String[] fields, String serverType);
	
	/**
	 * 根据排除字段更新
	 * @param serverCacheBean
	 * @param exceptFields 要排除的字段数组
	 * @param serverType 服务器类型
	 * @return: void
	 */
	void updateExceptFields(ServerCacheBean serverCacheBean, String[] exceptFields, String serverType);

	/**
	 * 根据条件更新一个缓存服务器的信息
	 * @功能: 条件中要包含fwqip字段
	 * @param condition 条件
	 * @param serverType 服务器类型
	 * @return: void
	 */
	void updateOne(Map condition, String serverType);

	/**
	 * 根据服务器ip和条件更新一个缓存服务器的信息
	 * @param fwqip		服务器ip
	 * @param condition		条件
	 * @param serverType	服务器类型
	 * @return: void
	 */
	void updateOne(String fwqip, Map condition, String serverType);

	/**
	 * 通过服务器ip获取缓存对象
	 * @param fwqip
	 * @param serverType
	 * @return: ServerCacheBean
	 */
	ServerCacheBean findOne(String fwqip, String serverType);
	
	/**
	 * 通过ip删除服务器缓存中的服务器
	 * @param ip
	 * @param serverType
	 * @return: void
	 */
	void delete(String ip, String serverType);
	
}
