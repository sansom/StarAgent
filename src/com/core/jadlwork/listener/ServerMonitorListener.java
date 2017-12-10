package com.core.jadlwork.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.model.cache.ServerCacheBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;
import com.core.jadlwork.utils.CmdUtils;
import com.core.jadlwork.utils.HostUtils;
import com.core.jadlwork.utils.SocketUtils;
import com.core.jadlwork.utils.TimerUtils;
import com.core.jadlwork.utils.URLAvailability;
/**
 * 
 * 服务器监控定时器
 * @author wujiaxu
 * @Time 2017-6-16 上午11:13:08
 *
 */
public class ServerMonitorListener implements ServletContextListener {
	private Logger logger = Logger.getLogger(ServerMonitorListener.class);
	 
	//在监听器中使用Spring容器中的bean所需要的对象
	private WebApplicationContext wac = null;
	private IFwqManager fwqManager = null;
	private SysMonitorTask sysMonitorTask = null;

	private Timer timer;
	private int delay = 60;  		//定时器延时执行时间(秒)
	private int timer_interval = 1;	//循环运行时间(分钟)
	private int timer_loss = 5;		//失联超时时间(分钟)

	public void contextDestroyed(ServletContextEvent sce) {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());	//设置wac对象
		fwqManager = (IFwqManager) wac.getBean("fwqManager");
		timer = new Timer();
		sysMonitorTask = new SysMonitorTask();
		timer_interval = Integer.valueOf(SysConfigUtils.getProperty("timer_interval_short","1"));
		timer.schedule(sysMonitorTask, delay * 1000 ,  timer_interval * 60 *1000);	
	}
	


	/**
	 * 
	 * 启动系统监控任务
	 * @author wujiaxu
	 * @Time 2017-6-22 上午10:36:47
	 *
	 */
	public  class SysMonitorTask extends TimerTask {
		public void run() {
			try {
				/**
				 * 1、获取执行权限
				 */
				boolean b = isCanRun();
				
				/**
				 * 2、有权限则执行任务
				 */
				if(b){
					getServerInfo();
				}
				
			} catch (Exception e) {
				logger.error("系统监控线程出错！", e);
			} 
		}

		
	}
	
	/**
	 * 获取执行权限
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-22 上午11:30:42
	 */
	private boolean isCanRun() {
		boolean isHas = false;
		String host_new = HostUtils.host_Ip + ":" + HostUtils.host_Port;
		/**
		 * 1、检测系统运行模式   【并行模式时，拥有执行权限】
		 */
		String runModel = SysConfigUtils.getProperty("running.model");
		if(runModel != null && SystemConstants.RUNMODEL_BX.equals(runModel)){
			logger.info("【总线监控任务】host["+host_new+"]当前系统为并行运行模式！");
			isHas = true;
			return isHas;
		}
		
		/**
		 * 2、检测本机是否有监控权限
		 */
		
		Map hostMap = fwqManager.getMoniterHost();
		if(hostMap != null && hostMap.size() > 0){
			String hostId = (String) hostMap.get("id");
			String host_old = (String) hostMap.get("systemid");
			String connetTime =(String) hostMap.get("lastcontime");

			if(host_old.equals(host_new) ){
				//2.1 、当监控主机为本机，拥有监控权限
				fwqManager.updateMonitorHost(hostId,host_new);
				isHas =  true;
				logger.info("【总线监控任务】host["+host_old+"]定时执行监控！");
				
			}else if(DateUtils.compareDateStr(DateUtils.getDateAddMin(connetTime, timer_loss), DateUtils.getCurrentData("yyyy-MM-dd HH:mm:ss")) != 1){
				
				//2.2、监控超时，拥有监控权限
				fwqManager.updateMonitorHost(hostId,host_new);
				isHas =  true;
				logger.info("【总线监控任务】host["+host_old+"]监控超时，host["+host_new+"]获取监控权限！");
			}
			
		}else{
			//2.3、无监控主机时，拥有监控权限
			
			fwqManager.addMonitorHost(host_new);
			isHas =  true;
			logger.info("【总线监控任务】无监控主机，host["+host_new+"]获取监控权限！");
		}
	
		/**
		 * 3、重设定时器
		 */
		this.reset_timer(isHas);
		return isHas;
	}
	
	/**
	 * 根据监控权限重设定时器
	 * @param isHas
	 * @author wujiaxu
	 * @Time 2017-6-22 下午4:24:17
	 */
	private void reset_timer(boolean isHas) {
		int shortTime = Integer.valueOf(SysConfigUtils.getProperty("timer_interval_short"));
		int longTime = Integer.valueOf(SysConfigUtils.getProperty("timer_interval_long"));
	
		if(isHas){
			//开启监控定时
			if(shortTime != timer_interval){
				timer_interval = shortTime;
				TimerUtils.re_schedule(sysMonitorTask, timer_interval * 1000 * 60);
				logger.info("【总线监控任务】启动监控任务，监控中心切换到监控模式！");
			}
		}else{
			//关闭监控定时
			if(longTime != timer_interval){
				timer_interval = longTime;
				TimerUtils.re_schedule(sysMonitorTask, timer_interval * 1000 * 60);
				logger.info("【总线监控任务】关闭监控任务，监控中心切换到等候模式！");
			}
		}
	}


	/**
	 * 循环系统服务器，从监控中心获取服务器及应用信息
	 * 
	 * @author wujiaxu
	 * @Time 2017-6-16 上午11:19:03
	 */
	private void getServerInfo(){
		List serverAppList = fwqManager.getServerAppInfo();
		if(serverAppList != null && serverAppList.size() > 0){
			logger.info("更新服务器、应用运行情况开始！");
			
			for(int i = 0; i < serverAppList.size() ;i++){
				Map serverAppMap = (Map) serverAppList.get(i);
				String fwqip = (String) serverAppMap.get("fwqip");
				/**
				 * 1、根据IP地址获取服务器信息
				 */
				String serverInfo = SocketUtils.getServerInfo(fwqip);
				ServerCacheBean serverCacheBean = analyServerInfo(serverInfo);
				
				
				/**
				 * 2、处理服务器信息
				 */
				handlerResult(serverAppMap,serverCacheBean);
				
				
				/**
				 * 3、保存处理结果
				 */	
				fwqManager.updateServerInfo(fwqBean,yyyxjlList);
				
			}
			logger.info("更新服务器、应用运行情况结束！");
		}
	}
	
	
	private FwqBean fwqBean = null;
	private List<YyyxjlBean>  yyyxjlList = null;
	private YyyxjlBean  yyyxjlBean = null;

	
	/**
	 * 处理服务器信息
	 * @param serverAppMap
	 * @param serverCacheBean
	 * @author wujiaxu
	 * @Time 2017-6-20 上午9:19:05
	 */
	private void handlerResult(Map serverAppMap, ServerCacheBean serverCacheBean) {
		String fwqid = (String) serverAppMap.get("id");
		String fwqip = (String) serverAppMap.get("fwqip");
		String dk = (String) serverAppMap.get("dk");
		String appMap = (String) serverAppMap.get("yylist");

		/**
		 * 1、组装服务器信息
		 */
		fwqBean = new FwqBean();
		fwqBean.setId(fwqid);
		fwqBean.setUpdatetime(DateUtils.createCurrentDate());
		boolean b = this.isCommunicate(serverCacheBean);
		
		if(!b){
			//1.1 服务器异常时，本地检测异常情况
			fwqBean.setStatusinfo(this.getErrorInfoByIp(fwqip));
			fwqBean.setFwqstatus(SystemConstants.FWQSTATUS_YC);
		}else{
			//1.2 服务器正常时，填充服务器信息
			fwqBean.setStatusinfo("");
			fwqBean.setFwqstatus(SystemConstants.FWQSTATUS_ZC);
			fwqBean.setCpuused(serverCacheBean.getCpuused());
			fwqBean.setMemeryused(serverCacheBean.getMemeryused());
			fwqBean.setThreadcount(serverCacheBean.getThreadcount()+"");
			fwqBean.setJvmloadedclasscount(serverCacheBean.getJvmloadedclasscount()+"");
			fwqBean.setJvmmemory(serverCacheBean.getJvmmemory());
			fwqBean.setJvmthreadcount(serverCacheBean.getJvmthreadcount()+"");
			fwqBean.setLastcontime(DateUtils.createCurrentDate());
		}
		
		
		/**
		 * 2、组装服务器应用信息
		 */
		yyyxjlList = new ArrayList<YyyxjlBean>();
		if(appMap != null && !"".equals(appMap)){
			String[] appArray = appMap.split(",");
			for(int i = 0; i < appArray.length; i++){
				String[] yyBean = appArray[i].split("@");
				String warname = yyBean[0].replaceAll(".war", "");
				String yyyxjlid = yyBean[1];
				
				yyyxjlBean = new YyyxjlBean();
				yyyxjlBean.setId(yyyxjlid);
				yyyxjlBean.setUpdateTime(DateUtils.createCurrentDate());
				
				if(!b || serverCacheBean.getAppinfo()==null || !serverCacheBean.getAppinfo().keySet().contains(warname)){
					//2.1 服务器异常或服务器无此应用时，本地检测应用运行情况
					yyyxjlBean.setYyyxztinfo(this.getAppStatusInfo(fwqip,dk,warname));
					if(yyyxjlBean.getYyyxztinfo() == null || "".equals(yyyxjlBean.getYyyxztinfo())){
						yyyxjlBean.setYyyxzt(SystemConstants.YYYYZT_ZC);
						yyyxjlBean.setLastcontime(DateUtils.createCurrentDate());
					}else{
						yyyxjlBean.setYyyxzt(SystemConstants.YYYYZT_YC);
					}
				}else{
					//2.2 服务器正常时，填充应用运行信息
					Map appBean = (Map) serverCacheBean.getAppinfo().get(warname);
					String appstatus = (String) appBean.get("appstatus");
					String appconinfo = (String) appBean.get("appconinfo");
					String appversion = (String) appBean.get("appversion");
					if(appstatus != null &&   appstatus.equals(SystemConstants.YYYYZT_ZC)){
						yyyxjlBean.setYyyxzt(appstatus);
						yyyxjlBean.setYyyxztinfo(appconinfo);
						yyyxjlBean.setLastcontime(DateUtils.createCurrentDate());
					}else{
						yyyxjlBean.setYyyxzt(appstatus);
						yyyxjlBean.setYyyxztinfo(appconinfo);
					}
					yyyxjlBean.setVersion(appversion);
				}
				yyyxjlList.add(yyyxjlBean);
			}
		}
	}

	/**
	 * 获取应用运行情况
	 * @param fwqip
	 * @param dk
	 * @param warname
	 * @author wujiaxu
	 * @Time 2017-6-20 上午9:48:37
	 */
	private String getAppStatusInfo(String fwqip, String dk, String warname) {
		return  URLAvailability.isConnect("http://"+fwqip+":"+dk+"/"+warname);
	}


	/**
	 * 获取服务器异常信息
	 * @param fwqip
	 * @author wujiaxu
	 * @Time 2017-6-20 上午9:27:12
	 */
	private String getErrorInfoByIp(String fwqip) {
		//通讯失败   监控功能交于总线执行
		boolean b = CmdUtils.isPing(fwqip, 1);
		if(b){
			return "socket通讯失败！";
		}else{
			return "ping不通！";
		}
	}


	/**
	 * 是否可通信
	 * @param serverInfo
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-20 上午9:17:20
	 */
	private boolean isCommunicate(ServerCacheBean serverCacheBean) {
		if(serverCacheBean == null){
			return false;
		}
		return true;
	}

	/**
	 * 解析服务器信息
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-19 下午4:09:39
	 */
	private ServerCacheBean analyServerInfo(String serverInfo) {
		if(serverInfo == null || serverInfo.equals("")){
			return null;
		}
		
		JSONObject jsonObject = JSONObject.fromObject(serverInfo);
		Map appInfo = (Map) jsonObject.get("appinfo");
		ServerCacheBean serverCacheBean = (ServerCacheBean) JSONObject.toBean(jsonObject, ServerCacheBean.class);
		serverCacheBean.setAppinfo(appInfo);
		return serverCacheBean;
	}
}
