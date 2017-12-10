package com.core.jadlwork.cache.wechat;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信缓存信息
 * @类名: WeChatCache
 * @描述: 存储所有微信服务相关的缓存信息
 * @作者: lcx
 * @时间: 2017-8-2 上午11:01:38
 */
public class WeChatCache {
	
	//存储每个带参二维码的扫描信息（ticket与openID的映射关系），保证在添加用户的时候，之存储第一个扫描二维码的用户信息
	/*
	 * ticket : {
	 * 			openid : 
	 * 			scantime :
	 * 		  }
	 */
	public static Map ticketOpenidMapping = new HashMap();
	
	//单例
	private WeChatCache() {}
	private static WeChatCache weChatCache = new WeChatCache();
	public static WeChatCache getInstance() {
		return weChatCache;
	}
	
}
