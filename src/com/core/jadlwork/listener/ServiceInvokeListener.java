package com.core.jadlwork.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.cache.ServiceCache;

public class ServiceInvokeListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(ServiceInvokeListener.class);
	
	private WebApplicationContext wac = null;
	private IYyManager yyManager = null;
	private IJqManager jqManager = null;
	
	private Timer timer;
	private int delay = 5;  		//定时器延时执行时间(秒)
	private int period = 60;		//间隔时间
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());	//设置wac对象
		yyManager = (IYyManager)wac.getBean("yyManager");
		jqManager = (IJqManager)wac.getBean("jqManager");
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
//				updateYyyxjlInfo();
				updateYyJqInfo();
			}
		}, delay * 1000, period * 1000);
	}

	private void updateYyJqInfo() {
		/*
		 * 1、调用jqManager获取应用集群信息
		 * 2、组装成缓存中要存储的格式类型
		 * 3、调用缓存单例完成缓存的更新
		 */
		Map yyJqInfoMap = new HashMap();
		//1、调用jqManager获取应用集群信息
		List jqYyInfoList = jqManager.getJqYyInfoList();
		if (jqYyInfoList != null && jqYyInfoList.size()>0) {
			//2、组装成缓存中要存储的格式类型
			/*
			 *  yyid:[
			 *  	{
			 *			yyid:
			 *			fwqid:
			 *			fwqname:
			 *			yyyxzt:
			 *			jqid:
			 *			jqname:
			 *			fwym:
			 *			fwdk:
			 *  	}
			 *  ]
			 */
			for (Map jqYyMap : (List<Map>)jqYyInfoList) {
				if (yyJqInfoMap.containsKey(jqYyMap.get("yyid"))) {
					//说明该应用id已经存在
					List yyJqList = (List) yyJqInfoMap.get(jqYyMap.get("yyid"));
					yyJqList.add(jqYyMap);
				}else {
					//说明还没有该yyid的信息
					List yyJqList = new ArrayList();
					yyJqList.add(jqYyMap);
					yyJqInfoMap.put(jqYyMap.get("yyid"), yyJqList);
				}
			}
			//3、调用缓存单例完成缓存的更新
			ServiceCache.setYyJqInfo(yyJqInfoMap);
			log.info("【服务缓存】已更新应用集群信息");
		}
	}
	
	/**
	 * 定时更新应用运行记录的缓存信息
	 * @return: void
	 */
	private void updateYyyxjlInfo(){
		/*
		 * 1、调用yyManager获取应用运行记录中的信息（将服务器的相关信息也拿到）
		 * 2、组装成缓存中要存储的格式类型
		 * 3、调用缓存单例完成缓存的更新
		 */
		Map yyyxjlInfo = new HashMap();
		//1、获取所有运行信息
		List yxjlInfo = yyManager.getYyyxjlInfo();
		if (yxjlInfo != null && yxjlInfo.size()>0) {
			//2、组装成存储的格式
			/*
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
			for (Map yxjlMap : (List<Map>)yxjlInfo) {
				if (yyyxjlInfo.containsKey(yxjlMap.get("yyid"))) {
					//说明该应用id已经存在
					List yxjlList = (List) yyyxjlInfo.get(yxjlMap.get("yyid"));
					yxjlList.add(yxjlMap);
				}else {
					//说明还没有该yyid的信息
					List yxjlList = new ArrayList();
					yxjlList.add(yxjlMap);
					yyyxjlInfo.put(yxjlMap.get("yyid"), yxjlList);
				}
			}
		}
		//3、存储到单例中
		ServiceCache.setYyyxjlInfo(yyyxjlInfo);
		log.info("【服务缓存】已更新应用运行记录数据");
	}
}
