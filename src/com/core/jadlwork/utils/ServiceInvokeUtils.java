package com.core.jadlwork.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.cache.ServiceCache;

/**
 * 服务调用工具类
 * @类名: ServiceInvokeUtils
 * @作者: 李春晓
 * @时间: 2017-6-23 上午11:05:11
 */
public class ServiceInvokeUtils {

	private static Logger log = Logger.getLogger(ServiceInvokeUtils.class);
	
	
	/**
	 * 通过应用id从应用集群缓存中取出调用的地址
	 * @param yyid 应用id
	 * @return: String
	 */
	public static String getYmFromYyJqInfo(String yyid) {
		String url = "";
		List yyJqList = ServiceCache.getYyJqInfo().get(yyid) == null ? null : (List)ServiceCache.getYyJqInfo().get(yyid);
		if (yyJqList != null && yyJqList.size()>0) {
			Map yyJqMap = (Map) yyJqList.get(0);
			String fwym = yyJqMap.get("fwym") == null ? "" : (String)yyJqMap.get("fwym"); 
			String fwdk = yyJqMap.get("fwdk") == null ? "" : (String)yyJqMap.get("fwdk"); 
			if ("80".equals(fwdk)) {
				//80端口就不用加上端口号
				url = fwym; 
			}else {
				url = fwym + ":" + fwdk;
			}
		}
		return url;
	}
	
	/**
	 * 通过应用id从应用运行记录的缓存中取出应用所在的可用的服务器的域名信息
	 * @param yyid	应用id
	 * @return: String
	 */
	public static String getYmFromYyyxjlInfo(String yyid) {
		String ip = "";
		/*
		 * 1、拿到缓存中的该应用的所有的信息(是一个list)
		 * 2、遍历list，取出可用的（如果都不可用，返回第一个，如果有任何一个可用就返回）
		 */
		List yxjlList = ServiceCache.getYyyxjlInfo().get(yyid) == null ? null : (List)ServiceCache.getYyyxjlInfo().get(yyid);
		if (yxjlList == null || yxjlList.size()==0) {
			//说明缓存中没有该应用的信息
			log.error("【服务调用】没有在缓存信息中查询到该应用的信息，请稍后再试！");
			return ip;
		}
		boolean flag = true;
		for (Map yxjlMap : (List<Map>)yxjlList) {
			if (yxjlMap.get("yyzt") != null && yxjlMap.get("yyzt").equals(SystemConstants.YYZT_YQD) 
					&& yxjlMap.get("yyyxzt") != null && yxjlMap.get("yyyxzt").equals(SystemConstants.YYYYZT_ZC)) {
				//说明当前这个就符合条件
				ip = (String) yxjlMap.get("fwqym");
				flag = false;
				break;
			}
		}
		if (flag) {
			//说明应用在各服务器中都不正常，返回第一个即可
			ip = (String)(((Map)yxjlList.get(0)).get("fwqym"));
		}
		return ip;
	}
}
