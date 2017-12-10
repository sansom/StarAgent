package com.core.jadlwork.cache.nginx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Nginx缓存信息
 * @类名: NginxCache
 * @作者: lcx
 * @时间: 2017-8-25 下午2:47:05
 */
public class NginxCache {
	
	/*
	 * 待更新的应用id集合
	 */
	public Set<String> changedYyids = new HashSet<String>();
	
	/*
	 * 待更新的集群id集合
	 */
	public Set<String> changedJqids = new HashSet<String>();
	
	/*
	 * 待更新的Nginxid集合
	 */
	public Set<String> changedNginxids = new HashSet<String>();
	
	/*
	 * 存储更新时候的异常信息
	 * 	每一个map：	msg：异常信息内容	time：产生时间
	 */
	public List<Map> updateErrMsg = new ArrayList<Map>();
	
	//单例
	private NginxCache() {}
	private static NginxCache nginxCache = new NginxCache();
	public static NginxCache getInstance() {
		return nginxCache;
	}
}
