package com.core.jadlwork.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;

public class NginxUpdateListener implements ServletContextListener {

	private WebApplicationContext wac = null;
	private INginxManager nginxManager = null;
	private IJqManager jqManager = null;
	
	private Timer timer;
	private int delay = 30;  		//定时器延时执行时间(秒)
	private int period = 10;		//间隔时间
	
	private Logger logger = Logger.getLogger(NginxUpdateListener.class);
	
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
		nginxManager = (INginxManager) wac.getBean("nginxManager");
		jqManager = (IJqManager) wac.getBean("jqManager");
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//先将所有更新失败的Nginx放到Nginx的id集合中
				List nginxList = nginxManager.getGxsbList();
				if (nginxList == null || nginxList.size()==0) {
					return;
				}
				for (Map nginxMap : (List<Map>)nginxList) {
					String nid = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
					NginxCache.getInstance().changedNginxids.add(nid);
				}
			}
		}, 10 * 1000);
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				updateNginxConf();
			}
		}, delay * 1000, period * 1000);
	}
	
	/**
	 * 更新Nginx配置
	 * @return: void
	 */
	private void updateNginxConf() {
		
//		CopyOnWriteArrayList<String> currList = (CopyOnWriteArrayList<String>) NginxCache.getInstance().changedYyids; 
		
		//使用iterator避免并发修改异常
		Iterator<String> yyidIt = NginxCache.getInstance().changedYyids.iterator();
		Iterator<String> jqidIt = NginxCache.getInstance().changedJqids.iterator();
		Iterator<String> nginxidIt = NginxCache.getInstance().changedNginxids.iterator();
		
		//更新应用的
		String yyid;
		while (yyidIt.hasNext()) {
			yyid = yyidIt.next();
			ResultBean resultBean = nginxManager.updateNginxConfByYyid(yyid);
			List nginxList = nginxManager.getNginxListByYyid(yyid);
			if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
				//失败
				if (nginxList != null && nginxList.size()>0) {
					for (Map nginxMap : (List<Map>)nginxList) {
						String thisId = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
						nginxManager.updateNginxGxzt(thisId, SystemConstants.ZT_FALSE, (String) resultBean.getMsg());
					}
				}
			}else {
				if (nginxList != null && nginxList.size()>0) {
					for (Map nginxMap : (List<Map>)nginxList) {
						String thisId = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
						nginxManager.updateNginxGxzt(thisId, SystemConstants.ZT_TRUE, "");
					}
				}
				yyidIt.remove();
			}
		}
		//更新集群的
		String jqid;
		while (jqidIt.hasNext()) {
			jqid = jqidIt.next();
			ResultBean resultBean = nginxManager.updateNginxConfByJqid(jqid);
			List nginxList = nginxManager.getNginxListByJqid(jqid);
			if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
				//失败
				for (Map nginxMap : (List<Map>)nginxList) {
					String thisId = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
					nginxManager.updateNginxGxzt(thisId, SystemConstants.ZT_FALSE, (String) resultBean.getMsg());
				}
			}else {
				for (Map nginxMap : (List<Map>)nginxList) {
					String thisId = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
					nginxManager.updateNginxGxzt(thisId, SystemConstants.ZT_TRUE, "");
				}
				jqidIt.remove();
			}
		}
		
		//更新Nginx的
		String nid;
		while (nginxidIt.hasNext()) {
			nid = nginxidIt.next();
			ResultBean resultBean = nginxManager.updateNginxConfByNginxid(nid);
			if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
				//失败，更新nginx的状态信息
				nginxManager.updateNginxGxzt(nid, SystemConstants.ZT_FALSE, (String) resultBean.getMsg());
			}else {
				//更新成功，取消该id信息，更新nginx的状态信息
				nginxManager.updateNginxGxzt(nid, SystemConstants.ZT_TRUE, "");
				nginxidIt.remove();
			}
		}
	}

}
