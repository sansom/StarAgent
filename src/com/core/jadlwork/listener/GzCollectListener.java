package com.core.jadlwork.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.IGzzxManager;

public class GzCollectListener implements ServletContextListener {

	private WebApplicationContext wac = null;
	private IGzzxManager gzzxManager = null;
	
	private Timer timer;
	private int delay = 60 * 3;  		//定时器延时执行时间(秒)
	private int period = 60 * 3;		//间隔时间
	
	private Logger logger = Logger.getLogger(GzCollectListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		timer = new Timer();
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());	//设置wac对象
		gzzxManager = (IGzzxManager) wac.getBean("gzzxManager");
		/**
		 * 如果系统启用故障收集功能，就执行任务
		 */
		String used = SysConfigUtils.getProperty("gzcollection.isUsed");
		String collUsed = used == null ? "true" : used;
		if ("true".equals(collUsed)) {
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					fwqyyGzCollect();
				}
			}, delay * 1000, period * 1000);
		}
	}
	
	/**
	 * 服务器应用故障信息的收集
	 * @return: void
	 */
	private void fwqyyGzCollect() {
		int[] res = gzzxManager.saveFwqYyGzxx();
		
		int oldGz = res[0];	//重复故障
		int newGz = res[1]; //新故障
		
		String crtl = SystemConstants.LINE_SEPARATER;
		logger.info(crtl+"【故障中心】收集服务器应用故障信息完成！此次任务共收集到"+(oldGz+newGz)
				+"条故障信息，其中，新故障【"+newGz+"】条，重复故障【"+oldGz+"】条");
	}

}
