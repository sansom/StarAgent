package com.core.jadlwork.model.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 服务缓存信息
 * @类名: ServiceCache
 * @描述: 存储服务相关的信息，方便在调用服务的时候能快速从缓冲中取到
 * @作者: 李春晓
 * @时间: 2017-6-23 上午10:12:36
 */
public class ServiceCache {

	//日志
	private static Logger log = Logger.getLogger(ServiceCache.class);
	
	//单例
	private static ServiceCache instance = new ServiceCache();
	public ServiceCache() {
		instance = this;
	}
	public static ServiceCache getInstance() {
		return instance;
	}
	
	/**
	 * 缓存应用集群信息，结构如下：
	 * 		yyid : {
	 * 			yyid:
	 * 			jqid:
	 * 			jqname:
	 * 			fwym:
	 * 			fwdk:
	 * 		},
	 * 		yyid : {
	 * 			yyid:
	 * 			jqid:
	 * 			jqname:
	 * 			fwym:
	 * 			fwdk:
	 * 		},
	 */
	private static Map yyJqInfo = new HashMap();
	
	/*
	 * 对外提供的获取应用集群信息的方法
	 */
	public static Map getYyJqInfo() {
		return yyJqInfo;
	}
	/*
	 * 交给定时器ServiceInvokeListener设置应用集群的方法
	 */
	public static void setYyJqInfo(Map yyJqInfo) {
		ServiceCache.yyJqInfo = yyJqInfo;
	}

	/**
	 * 缓存应用运行记录信息，结构如下：
	 * 		yyid : [
	 * 			yyid:
	 * 			fwqid:
	 * 			fwqym:
	 * 			yyyxzt:
	 * 			yyzt:
	 * 		],
	 * 		yyid2 : [
	 * 			yyid:
	 * 			fwqid:
	 * 			fwqym:
	 * 			yyyxzt:
	 * 			yyzt:
	 * 		]
	 */
	private static Map yyyxjlInfo = new HashMap();
	
	/*
	 * 对外提供的获取所有的应用运行记录的方法
	 */
	public static Map getYyyxjlInfo() {
		return yyyxjlInfo;
	}
	/*
	 * 交给定时器ServiceInvokeListener设置运行记录的方法
	 */
	public static void setYyyxjlInfo(Map info) {
		yyyxjlInfo = info;
	}
	
}
