package com.core.jadlwork.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.model.cache.ServerCacheBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.utils.SocketUtils;

public class CacheManager extends BaseManager implements ICacheManager {

	//日志信息
	private static Logger logger = Logger.getLogger(CacheManager.class);


	//注入云平台的manager
	private IFwqManager fwqManager;
//	private IFwqglManager fwqglManager;

	public void setFwqManager(IFwqManager fwqManager) {
		this.fwqManager = fwqManager;
	}

	@Override
	public void initCache() {
		initServerCache();	//初始化云平台缓存操作
		
	}
	
	// ==============================云平台服务器的缓存操作=============================
	
	@Override
	public void updateServerCache() {
		//updateCache(SystemConstants.SERVERCACHE_TYPE_SERVER);
	}
	
	@Override
	public void updateServerCacheByFwqIp(String ip) {
		/*ServerCacheBean serverCacheBean = getUpdateServerCacheBeanByIp(ip, SystemConstants.SERVERCACHE_TYPE_SERVER);
		if (serverCacheBean != null) {
			//更新到服务器缓存中
			String[] exceptFields = {"fwqname","realname","fwqip","dk","szjf"};		//排除字段
			cacheDao.updateByExceptFields(cacheDao.getServerCache(), serverCacheBean, exceptFields);
//			cacheDao.updateExceptFields(serverCacheBean, exceptFields, SystemConstants.SERVERCACHE_TYPE_SERVER);
		}*/
	}
	
	@Override
	public Map getServerCache() {
//		return cacheDao.getServerCache(SystemConstants.SERVERCACHE_TYPE_SERVER);
		return cacheDao.getServerCache();
	}
	
	
	
	
	
	@Override
	public void addServerCache(ServerCacheBean serverCacheBean) {
		cacheDao.add(cacheDao.getServerCache(), serverCacheBean);
//		cacheDao.saveFwq(serverCacheBean, SystemConstants.SERVERCACHE_TYPE_SERVER);
	}
	
	@Override
	public boolean isServerOnline(String fwqip) {
		try {
			
//			ServerCacheBean serverCacheBean = (ServerCacheBean) getServerCache().get(fwqip);
			ServerCacheBean serverCacheBean = (ServerCacheBean) cacheDao.get(cacheDao.getServerCache(), fwqip);
			if (serverCacheBean.getFwqstatus().equals(SystemConstants.ZT_TRUE)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@Override
	public void removeServerCache(String fwqip) {
		cacheDao.remove(cacheDao.getServerCache(), fwqip);
//		cacheDao.delete(fwqip, SystemConstants.SERVERCACHE_TYPE_SERVER);
	}
	
	@Override
	public String getServerStatus(String fwqip) {
		ServerCacheBean cacheBean = (ServerCacheBean) cacheDao.get(cacheDao.getServerCache(), fwqip);
//		ServerCacheBean findOne = cacheDao.findOne(fwqip, SystemConstants.SERVERCACHE_TYPE_SERVER);
		if (cacheBean != null) {
			return cacheBean.getFwqstatus();
		}
		return null;
	}
	
	// ==============================托管服务器的缓存操作=============================
	
	@Override
	public void updateTgServerCache() {
		//updateCache(SystemConstants.SERVERCACHE_TYPE_TGSERVER);
	}
	@Override
	public void updateTgServerCacheByFwqIp(String ip) {
		/*ServerCacheBean serverCacheBean = getUpdateServerCacheBeanByIp(ip, SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		if (serverCacheBean != null) {
			//更新到服务器缓存中
			String[] exceptFields = {"fwqname","fwqip"};		//排除字段
			cacheDao.updateByExceptFields(cacheDao.getTgServerCache(), serverCacheBean, exceptFields);
//			cacheDao.updateExceptFields(serverCacheBean, exceptFields, SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		}*/
	}
	@Override
	public Map getTgServerCache() {
//		return cacheDao.getServerCache(SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		return cacheDao.getTgServerCache();
	}
	
	@Override
	public void addTgServerCache(ServerCacheBean serverCacheBean) {
//		cacheDao.saveFwq(serverCacheBean, SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		cacheDao.add(cacheDao.getTgServerCache(), serverCacheBean);
	}
	
	@Override
	public boolean isTgServerOnline(String tgFwqip) {
		try {
			ServerCacheBean serverCacheBean = (ServerCacheBean) cacheDao.get(cacheDao.getTgServerCache(), tgFwqip);
			if (serverCacheBean.getFwqstatus().equals(SystemConstants.FWQSTATUS_ZC)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@Override
	public void removeTgServerCache(String tgFwqip) {
//		cacheDao.delete(tgFwqip, SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		cacheDao.remove(cacheDao.getTgServerCache(), tgFwqip);
	}
	
	@Override
	public String getTgServerStatus(String tgFwqip) {
		ServerCacheBean cacheBean = (ServerCacheBean) cacheDao.get(cacheDao.getTgServerCache(), tgFwqip);
//		ServerCacheBean findOne = cacheDao.findOne(tgFwqip, SystemConstants.SERVERCACHE_TYPE_TGSERVER);
		if (cacheBean != null) {
			return cacheBean.getFwqstatus();
		}
		return null;
	}
	
	// ==============================第三方服务器的缓存操作=============================
	
	@Override
	public void updateWbServerCache() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateWbServerCacheByFwqIp(String ip) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Map getWbServerCache() {
		return null;
	}
	
	// ==============================公共、私有 操作=============================
	
	/**
	 * 初始化云平台服务器缓存
	 * @return: void
	 */
	private void initServerCache() {
		//获取数据库表中的所有可用的服务器ip
//		List<Map> fwqs = fwqManager.getFwqList();
//		//将从数据库中获取的服务器的信息保存到服务器缓存中
//		for (Map thisMap : fwqs) {
//			ServerCacheBean serverCacheBean = new ServerCacheBean();
//			serverCacheBean.setFwqip((String) thisMap.get("fwqip"));
//			serverCacheBean.setFwqname((String) thisMap.get("fwqname"));
//			serverCacheBean.setRealname((String) thisMap.get("fwqym"));
//			serverCacheBean.setDk((String) thisMap.get("dk"));
//			serverCacheBean.setSzjf((String) thisMap.get("szjf"));
////			cacheDao.saveFwq(serverCacheBean, SystemConstants.SERVERCACHE_TYPE_SERVER);
//			cacheDao.add(cacheDao.getServerCache(), serverCacheBean);
//		}
//		//调用更新服务器缓存的操作
//		updateCache(SystemConstants.SERVERCACHE_TYPE_SERVER);
	}
	
	
	
	/**
	 * 根据服务器ip获取要更新的服务器的缓存对象，通用的方法
	 * @param ip
	 * @return: ServerCacheBean
	 */
	private ServerCacheBean getUpdateServerCacheBeanByIp(String ip, String serverType) {
		ServerCacheBean serverCacheBean = null;
		String serverInfo = SocketUtils.getServerInfo(ip);
		if (serverInfo == null || serverInfo.length()<2) {
			//说明服务器不能通信
			serverCacheBean = new ServerCacheBean();
			serverCacheBean.setFwqip(ip);
			serverCacheBean.setFwqstatus(SystemConstants.ZT_FALSE);
			cacheDao.update(getCacheMapByServerType(serverType), serverCacheBean);
//			cacheDao.update(serverCacheBean, serverType);
			return null;
		}
		//获得要保存的对象
		JSONObject jsonObject = JSONObject.fromObject(serverInfo);
		Map appInfo = (Map) jsonObject.get("appinfo");
		serverCacheBean = (ServerCacheBean) JSONObject.toBean(jsonObject, ServerCacheBean.class);
		serverCacheBean.setFwqip(ip);
		serverCacheBean.setAppinfo(appInfo);
		serverCacheBean.setFwqstatus(SystemConstants.ZT_TRUE);
		return serverCacheBean;
	}
	
	/**
	 * 根据服务器类型获取对应的缓存的集合
	 * @param serverType	服务器的类型
	 * @return: Map	对应的缓存的集合
	 */
	private Map getCacheMapByServerType(String serverType){
//		if (SystemConstants.SERVERCACHE_TYPE_SERVER.equals(serverType)) {
//			//云平台
//			return cacheDao.getServerCache();
//		}else if (SystemConstants.SERVERCACHE_TYPE_TGSERVER.equals(serverType)) {
//			//托管
//			return cacheDao.getTgServerCache();
//		}else if (SystemConstants.SERVERCACHE_TYPE_WBSERVER.equals(serverType)) {
//			//第三方
//			return null;
//		}else {
//			return null;
//		}
		return null;
	}
	
	/**
	 * 根据服务器类型更新缓存
	 * @param serverType 服务器类型
	 * @return: void
	 */
	private void updateCache(String serverType) {
//		// 从缓存中获取所有的服务器的ip信息
//		Map serverCache = cacheDao.getServerCache(serverType);	//缓存中的map集合	
//		Set<String> ips = serverCache.keySet();		//得到ip集合
//		for (String ip : ips) {
//			if (SystemConstants.SERVERCACHE_TYPE_SERVER.equals(serverType)) {
//				//云平台
//				updateServerCacheByFwqIp(ip);
//			}else if (SystemConstants.SERVERCACHE_TYPE_TGSERVER.equals(serverType)) {
//				//托管
//				updateTgServerCacheByFwqIp(ip);
//			}else if (SystemConstants.SERVERCACHE_TYPE_WBSERVER.equals(serverType)) {
//				//第三方
//				updateWbServerCacheByFwqIp(ip);
//			}else {
//				logger.error("传递了未知的缓存类型，请在cacheManager中查看参数的传递，当前获取得到的serverType为:"+serverType);
//			}
//		}
	}

	@Override
	public FwqBean getFwqBySaveFwq(FwqBean fwqBean) {
		if (fwqBean == null || fwqBean.getFwqip() == null || fwqBean.getFwqip().equals("")) {
			logger.info("从缓存中获取服务器对象出错，ip为空");
			throw new RuntimeException("从缓存中获取服务器对象出错，ip为空");
		}
		ServerCacheBean cacheBean = (ServerCacheBean) cacheDao.get(cacheDao.getServerCache(), fwqBean.getFwqip());
//		ServerCacheBean cacheBean = cacheDao.findOne(fwqBean.getFwqip(), SystemConstants.SERVERCACHE_TYPE_SERVER);
		if (cacheBean == null) {
			logger.info("从缓存中获取服务器对象出错，缓存中不存在该服务器");
			throw new RuntimeException("从缓存中获取服务器对象出错，缓存中不存在该服务器");
		}
		fwqBean.setBackupsrc(cacheBean.getBackupsrc());	//设置备份路径
		fwqBean.setFwqczxt(cacheBean.getFwqczxt());	//设置操作系统
		return fwqBean;
	}


}
