package com.core.jadlwork.cache;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.cache.ServerCacheBean;

import edu.emory.mathcs.backport.java.util.Arrays;

public class CacheDao implements ICacheDao {

	//日志
	private static Logger logger = Logger.getLogger(CacheDao.class);
	
	//总的缓存
	private ServerCache serverCache = ServerCache.getInstance();
	
	
	@Override
	public void add(Map cacheMap, Object cacheBean) {
		try {
			String fwqip = getFwqipByBean(cacheBean);
			cacheMap.put(fwqip, cacheBean);
		} catch (Exception e) {
			logger.error("添加缓存服务器出错");
			throw new RuntimeException("添加缓存服务器出错");
		}
	}

	@Override
	public void remove(Map cacheMap, String ip) {
		cacheMap.remove(ip);
	}

	@Override
	public void update(Map cacheMap, Object cacheBean) {
		try {
			cacheMap.put(getFwqipByBean(cacheBean), cacheBean);
		} catch (Exception e) {
			logger.error("更新缓存服务器出错");
			throw new RuntimeException("更新缓存服务器出错");
		}
	}

	@Override
	public void update(Map cacheMap, Map condition, String fwqip) {
		try {
			Object object = cacheMap.get(fwqip);
			Set<String> keySet = condition.keySet();
			for (String key : keySet) {
				if ("fwqip".equals(key)) {
					continue;
				}
				//反射执行set方法
				Field field = object.getClass().getDeclaredField(key);
				field.setAccessible(true);
				field.set(object, condition.get(key));
			}
		} catch (Exception e) {
			logger.error("按条件更新缓存服务器出错");
			throw new RuntimeException("按条件更新缓存服务器出错");
		}
	}

	@Override
	public void updateByFields(Map cacheMap, Object cacheBean, String[] fields) {
		try {
			//得到当前ip在缓存中的对象
			Object bean = cacheMap.get(getFwqipByBean(cacheBean));
			//得到所有的字段
			Field[] declaredFields = bean.getClass().getDeclaredFields();	
			for (Field field : declaredFields) {
				String fieldName = field.getName();
				//如果不在字段中跳过
				if (!Arrays.asList(fields).contains(fieldName)) {
					continue;
				}
				//获取属性的描述对象，该对象可以获取该属性的读写方法、也就是get/set方法
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cacheBean.getClass());
				//使用field的set方法来执行属性的set方法，使用pd获取属性的get方法
				field.setAccessible(true);
				//获取传递过来的值，通过pd调用其get方法
				Object value = pd.getReadMethod().invoke(cacheBean, new Object[]{});
				field.set(bean, value);
			}
		} catch (Exception e) {
			logger.error("按字段更新缓存服务器出错");
			throw new RuntimeException("按字段更新缓存服务器出错");
		}
	}

	@Override
	public void updateByExceptFields(Map cacheMap, Object cacheBean,
			String[] exceptFields) {
		try {
			//得到当前ip在缓存中的对象
			Object bean = cacheMap.get(getFwqipByBean(cacheBean));
			//得到所有的字段
			Field[] declaredFields = bean.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				String fieldName = field.getName();
				//包含要排除的字段就跳过
				if (Arrays.asList(exceptFields).contains(fieldName)) {
					continue;
				}
				//获取属性的描述对象，该对象可以获取该属性的读写方法也就是get/set方法
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cacheBean.getClass());
				//使用field的set方法来执行属性的set方法，使用pd获取属性的get方法
				field.setAccessible(true);
				//获取要设置的值，就是传递过来的对象中的，使用pd调用其get方法获取
				Object value = pd.getReadMethod().invoke(cacheBean, new Object[]{});
				field.set(bean, value);
			}
		} catch (Exception e) {
			logger.error("按排除字段更新缓存服务器出错");
			throw new RuntimeException("按排除字段更新缓存服务器出错");
		}
	}

	@Override
	public Object get(Map cacheMap, String fwqip) {
		try {
			return cacheMap.get(fwqip);
		} catch (Exception e) {
			logger.error("获取单个缓存服务器出错");
			throw new RuntimeException("获取单个缓存服务器出错");
		}
	}

	@Override
	public Map getServerCache() {
		return serverCache.getServerCache();
	}

	@Override
	public Map getTgServerCache() {
		return serverCache.getTgServerCache();
	}
	
	
	//=================================================================================================
	
	@Override
	public void saveFwq(ServerCacheBean serverCacheBean, String serverType) {
		if (serverCacheBean == null || StringUtils.isEmpty(serverCacheBean.getFwqip())) {
			logger.error("要保存的缓存不存在或者其ip为空，保存失败");
			throw new RuntimeException("要保存的缓存不存在或者其ip为空，保存失败");
		}
		getCacheByServerType(serverType).put(serverCacheBean.getFwqip(), serverCacheBean);
	}
	
	@Override
	public Map getServerCache(String serverType) {
		return getCacheByServerType(serverType);
	}
	
	@Override
	public void update(ServerCacheBean serverCacheBean, String serverType) {
		if (serverCacheBean != null && serverCacheBean.getFwqip() != null && !"".equals(serverCacheBean.getFwqip())) {
			getCacheByServerType(serverType).put(serverCacheBean.getFwqip(), serverCacheBean);
		}else {
			logger.info("更新缓存服务器出错");
			throw new RuntimeException("更新缓存服务器出错");
		}
	}

	@Override
	public void update(ServerCacheBean serverCacheBean, String[] fields,
			String serverType) {
		try {
			if (serverCacheBean != null && serverCacheBean.getFwqip() != null && !"".equals(serverCacheBean.getFwqip())) {
				//获取缓存中的对应的服务器对象，是个静态的，所以直接设置值之后就已经保存了
				ServerCacheBean bean = (ServerCacheBean) getCacheByServerType(serverType).get(serverCacheBean.getFwqip());
				if (bean == null) {
					return;
				}
				//得到所有的字段
				Field[] declaredFields = bean.getClass().getDeclaredFields();	
				for (Field field : declaredFields) {
					String fieldName = field.getName();
					//如果不在字段中跳过
					if (!Arrays.asList(fields).contains(fieldName)) {
						continue;
					}
					//获取属性的描述对象，该对象可以获取该属性的读写方法、也就是get/set方法
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), serverCacheBean.getClass());
					//使用field的set方法来执行属性的set方法，使用pd获取属性的get方法
					field.setAccessible(true);
					//获取传递过来的值，通过pd调用其get方法
					Object value = pd.getReadMethod().invoke(serverCacheBean, new Object[]{});
					field.set(bean, value);
				}
			}
		} catch (Exception e) {
			logger.info("更新缓存服务器出错");
			throw new RuntimeException("更新缓存服务器出错");
		}
	}

	@Override
	public void updateExceptFields(ServerCacheBean serverCacheBean,
			String[] exceptFields, String serverType) {
		try {
			if (serverCacheBean != null && serverCacheBean.getFwqip() != null && !"".equals(serverCacheBean.getFwqip())) {
				//获取缓存中的对应的服务器对象，是个静态的，所以直接设置值之后就已经保存了
				ServerCacheBean bean = (ServerCacheBean) getCacheByServerType(serverType).get(serverCacheBean.getFwqip());
				if (bean == null) {
					return;
				}
				//获取所有属性字段
				Field[] declaredFields = bean.getClass().getDeclaredFields();
				for (Field field : declaredFields) {
					String fieldName = field.getName();
					//包含要排除的字段就跳过
					if (Arrays.asList(exceptFields).contains(fieldName)) {
						continue;
					}
					//获取属性的描述对象，该对象可以获取该属性的读写方法也就是get/set方法
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), serverCacheBean.getClass());
					//使用field的set方法来执行属性的set方法，使用pd获取属性的get方法
					field.setAccessible(true);
					//获取要设置的值，就是传递过来的对象中的，使用pd调用其get方法获取
					Object value = pd.getReadMethod().invoke(serverCacheBean, new Object[]{});
					field.set(bean, value);
				}
			}
		} catch (Exception e) {
			logger.info("更新缓存服务器出错");
			throw new RuntimeException("更新缓存服务器出错");
		}
	}
	
	//根据条件更新单个服务器
	@Override
	public void updateOne(Map condition, String serverType) {
		//得到服务器的ip
		Object object = condition.get("fwqip");
		if (object == null) {
			logger.info("更新缓存服务器出错，条件中没有fwqip信息");
			throw new RuntimeException("更新缓存服务器出错，条件中没有fwqip信息");
		}
		String fwqip = (String) object;
		updateOne(fwqip, condition, serverType);
	}

	//根据条件更新单个服务器
	@Override
	public void updateOne(String fwqip, Map condition, String serverType) {
		Object obj = getCacheByServerType(serverType).get(fwqip);
		if (obj == null) {
			logger.info("更新缓存服务器出错，缓存中没有该服务器记录");
			throw new RuntimeException("更新缓存服务器出错，缓存中没有该服务器记录");
		}
		ServerCacheBean cacheBean = (ServerCacheBean) obj;
		
		Set<String> keySet = condition.keySet();
		try {
			for (String key : keySet) {
				if ("fwqip".equals(key)) {
					continue;
				}
				//反射执行set方法
				Field field = cacheBean.getClass().getDeclaredField(key);
				field.setAccessible(true);
				field.set(cacheBean, condition.get(key));
			}
			getCacheByServerType(serverType).put(fwqip, cacheBean);
		} catch (Exception e) {
			logger.info("更新缓存服务器出错");
			throw new RuntimeException("更新缓存服务器出错");
		}
	}
	
	@Override
	public ServerCacheBean findOne(String fwqip, String serverType) {
		return (ServerCacheBean) getCacheByServerType(serverType).get(fwqip);
	}
	
	/**
	 * 根据服务器的类型获取对应的缓存的map集合
	 * @param serverType 服务器类型  在常量中存储
	 * @return: Map	返回对应的缓存map集合
	 */
	private Map getCacheByServerType(String serverType) {
//		if (SystemConstants.SERVERCACHE_TYPE_SERVER.equals(serverType)) {
//			//云平台
//			return serverCache.getServerCache();
//		}else if (SystemConstants.SERVERCACHE_TYPE_TGSERVER.equals(serverType)) {
//			//托管
//			return serverCache.getTgServerCache();
//		}else if (SystemConstants.SERVERCACHE_TYPE_WBSERVER.equals(serverType)) {
//			//第三方
//			return serverCache.getWbServerCache();
//		}else {
//			logger.error("传递了未知的缓存类型，请在cacheManager中查看参数的传递，当前获取得到的serverType为:"+serverType);
//		}
		return null;
	}
	
	@Override
	public void delete(String ip, String serverType) {
		Object remove = getCacheByServerType(serverType).remove(ip);
		if (remove == null) {
			logger.warn("删除服务器没有成功，可能ip为空或者缓存中没有该服务器");
		}
	}
	
	/**
	 * 通过对象按照反射的方式获取fwqip
	 * @param cacheBean	包含fwqip属性的对象
	 * @return: String	返回fwqip
	 */
	private String getFwqipByBean(Object cacheBean){
		try {
			//获取属性的描述对象，该对象可以获取该属性的读写方法也就是get/set方法
			PropertyDescriptor pd = new PropertyDescriptor("fwqip", cacheBean.getClass());
			//获取要设置的值，就是传递过来的对象中的，使用pd调用其get方法获取
			Object fwqip = pd.getReadMethod().invoke(cacheBean, new Object[]{});
			return (String) fwqip;
		} catch (Exception e) {
			return null;
		}
	}
}
