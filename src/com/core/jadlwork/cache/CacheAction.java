package com.core.jadlwork.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.cache.ServerCacheBean;

/**
 * 服务器缓存操作的action，主要处理获取缓存的ajax请求调用
 * @类名: CacheAction
 * @作者: 李春晓
 * @时间: 2017-1-13 上午9:41:40
 */
public class CacheAction extends BaseAction {

	//注入cache的manager
	private  ICacheManager cacheManager;
	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	/**
	 * ajax获取所有的云平台缓存信息
	 * @return: void
	 */
	public void getServerCache(){
		Map serverCache = cacheManager.getServerCache();
		JSONObject jsonObject = new JSONObject();
		Set<Entry> entrySet = serverCache.entrySet();
		for (Entry entry : entrySet) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}
		String serverInfo = jsonObject.toString();
		try {
			ResponseUtils.render(response, serverInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ajax获取所有的托管服务器的缓存信息
	 * @return: void
	 */
	public void getTgServerCache(){
		Map tgServerCache = cacheManager.getTgServerCache();
		JSONObject jsonObject = new JSONObject();
		Set<Entry> entrySet = tgServerCache.entrySet();
		for (Entry entry : entrySet) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}
		String serverInfo = jsonObject.toString();
		try {
			ResponseUtils.render(response, serverInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ajax请求判断是否服务器可用，云平台以及托管平台通用的，传递参数
	 * @return: void
	 */
	public void isServerOnline(){
		String fwqip = request.getParameter("fwqip");
		String serverType = request.getParameter("serverType");
		boolean serverOnline = true;
		if ("server".equals(serverType)) {
			//云平台的
			serverOnline = cacheManager.isServerOnline(fwqip);
		}else if ("tgServer".equals(serverType)) {
			serverOnline = cacheManager.isTgServerOnline(fwqip);
		}
		String result = serverOnline ? "0" : "1";
		try {
			ResponseUtils.render(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
