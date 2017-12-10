package com.core.jadlwork.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlwork.business.gzzx.IGzPushManager;

public class GzPushListener implements ServletContextListener {

	private WebApplicationContext wac = null;
	private IGzPushManager gzPushManager = null;
	
	private Timer timer;
	private int delay = 60;  		//定时器延时执行时间(秒)
	private int period = 10;		//间隔时间
	
	private Logger logger = Logger.getLogger(GzPushListener.class);
	
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
		gzPushManager = (IGzPushManager) wac.getBean("gzPushManager");
		/**
		 * 如果系统启用故障推送功能，就执行任务
		 */
		String used = SysConfigUtils.getProperty("gzpush.isUsed");
		String pushUsed = used == null ? "true" : used;
		if ("true".equals(pushUsed)) {
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					pushErrMsg();
				}
			}, delay * 1000, period * 1000);
		}
	}
	
	/**
	 * 推送故障信息
	 * @return: void
	 */
	private void pushErrMsg() {
//		gzPushManager.pushWeChatMsg();	//暂时没有启用
		gzPushManager.pushEmailMsg();
	}

}
