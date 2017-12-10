package com.core.jadlwork.struts.action.yygl;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.yygl.IYyControlManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
/**
 * 应用运行控制器
 * TODO
 * @author wujiaxu
 * @Time 2017-6-9 上午10:32:25
 *
 */
public class YyControlAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(YyControlAction.class);

	private YyBean yyBean ;
	private IYyManager yyManager ; 
	private IYyControlManager yyControlManager; 


	/**
	 * 应用控制主方法
	 */
	public String todo()  {	
		ResultBean resultBean = null;
		
		String yxjlid = request.getParameter("yxjlid");
		String yyid = request.getParameter("yyid");
		String fwqip = request.getParameter("fwqip");
		String dowhat = request.getParameter("dowhat");
		
		//1.校验
		if(yxjlid == null || "".equals(yxjlid) || yyid == null || "".equals(yyid) || fwqip == null || "".equals(fwqip) || dowhat == null || "".equals(dowhat)){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"参数不全！");
			try {
				ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
				return null;
			} catch (Exception e) {
				logger.error("执行应用操作时报错！", e);
			}
		}
		
		//2.执行操作
		if(SystemConstants.CONTROL_DEPLOY.equalsIgnoreCase(dowhat)){
			//部署应用
			resultBean = yyControlManager.deploy(yxjlid,yyid,fwqip);
		}else if(SystemConstants.CONTROL_START.equalsIgnoreCase(dowhat)){
			//启动应用
			resultBean = yyControlManager.start(yxjlid,yyid,fwqip);
		}else if(SystemConstants.CONTROL_STOP.equalsIgnoreCase(dowhat)){
			//停止应用
			resultBean = yyControlManager.stop(yxjlid,yyid,fwqip);
			
		}else if(SystemConstants.CONTROL_REMOVE.equalsIgnoreCase(dowhat)){
			//删除应用
			resultBean = yyControlManager.remove(yxjlid,yyid,fwqip);
			
		}else{
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"无效的操作方法(dowhat:"+dowhat+")！");
		}
		
		try {
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			
		} catch (Exception e) {
			logger.error("执行应用操作时报错！", e);
		}
		return null;
	}

	
	/**
	 * 应用试发布操作
	 * @throws Exception 
	 */
	public String release() throws Exception  {	
		ResultBean resultBean = null;
		
		
		String yyid = request.getParameter("yyid");
		String dowhat = request.getParameter("dowhat");
		String isAutoRun = request.getParameter("isAutoRun");//是否后台自动完成所有操作
		
		/**
		 * 1、校验
		 */
		
		//1.1、校验入参
		if( yyid == null || "".equals(yyid) || dowhat == null || "".equals(dowhat)){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"非法请求！");
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}

		//2.执行操作
		if(SystemConstants.RELEASE_SFB.equalsIgnoreCase(dowhat)){
			//试发布
			resultBean = yyControlManager.versionToPriavte(yyid,isAutoRun);
		}else if(SystemConstants.RELEASE_BACK.equalsIgnoreCase(dowhat)){
			//回退
			resultBean = yyControlManager.versionToBack(yyid,isAutoRun);
		}else if(SystemConstants.RELEASE_ZSFB.equalsIgnoreCase(dowhat)){
			//正式发布
			resultBean = yyControlManager.versionToPublic(yyid,isAutoRun);
		}else{
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"无效的操作方法(dowhat:"+dowhat+")！");
		}
		
		try {
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			
		} catch (Exception e) {
			logger.error("执行应用试发布操作时报错！", e);
		}
		return null;
	}

	public YyBean getYyBean() {
		return yyBean;
	}


	public void setYyBean(YyBean yyBean) {
		this.yyBean = yyBean;
	}


	public IYyManager getYyManager() {
		return yyManager;
	}


	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}


	public IYyControlManager getYyControlManager() {
		return yyControlManager;
	}


	public void setYyControlManager(IYyControlManager yyControlManager) {
		this.yyControlManager = yyControlManager;
	}
	
	
	
	
}
