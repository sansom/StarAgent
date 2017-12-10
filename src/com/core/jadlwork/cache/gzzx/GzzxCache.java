package com.core.jadlwork.cache.gzzx;

public class GzzxCache {

	//存储故障中心过滤时长
	public static Integer gzFilterMinute = 20;	//单位：分钟
	
	//单例
	private GzzxCache() {}
	private static GzzxCache gzzxCache = new GzzxCache();
	public static GzzxCache getInstance() {
		return gzzxCache;
	}
}
