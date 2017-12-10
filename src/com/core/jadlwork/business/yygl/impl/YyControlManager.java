package com.core.jadlwork.business.yygl.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.SpringBeanFactory;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.yygl.IYyControlManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;
import com.core.jadlwork.utils.FileUtils;
import com.core.jadlwork.utils.SocketUtils;
import com.core.jadlwork.utils.SystemUtils;
import com.core.jadlwork.utils.URLAvailability;

/**
 * 
 * 应用控制器实现类
 * @author wujiaxu
 * @Time 2017-6-12 下午4:43:26
 *
 */
public class YyControlManager extends BaseManager implements IYyControlManager {

	private static Logger log = Logger.getLogger(YyControlManager.class);
	private IYyManager yyManager ; 
	private INginxManager nginxManager;
	
	@Override
	public ResultBean deploy(String yxjlid,String yyid, String fwqip) {
		ResultBean resultBean = null;
		YyBean yyBean = yyManager.getYyBean(yyid);
		YyyxjlBean yyyxjlBean = yyManager.getYyyxjlBean(yxjlid);
		
		/**
		 * 1、检测_war包是否存在
		 */
		String warsrc = "";
		if(yyyxjlBean.getFbzt().equals(SystemConstants.FBZT_SFB)){
			warsrc = yyBean.getWarsrc_sfb();
		}else{
			warsrc = yyBean.getWarsrc();
		}
		if (warsrc == null || warsrc.equals("") || !new File(warsrc).exists()) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "您还未上传war包！");
			return resultBean;
		}
		
		/**
		 * 2、远程部署
		 */
		boolean re = SocketUtils.webapp_deploy(fwqip,warsrc);
		if (!re) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "往服务器【"+fwqip+"】部署war包失败！");
		}
		
		/**
		 * 3、更新部署状态
		 */
		yyyxjlBean.setIsdeploy(SystemConstants.isDeploy_true);
		int i = yyManager.updateYyyxjlByFields(yyyxjlBean, "isdeploy");
		if(i <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新部署状态失败！");
			return resultBean;
		}

		
		resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		return resultBean;
	}

	@Override
	public ResultBean start(String yxjlid,String yyid, String fwqip) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		String result  = "";
		
		YyBean yyBean = yyManager.getYyBean(yyid);
		
		/**
		 * 1、检测_war包是否已部署
		 */
		YyyxjlBean yyyxjlBean = yyManager.getYyyxjlBean(yxjlid);
		if(yyyxjlBean.getIsdeploy() == null || yyyxjlBean.getIsdeploy().equals(SystemConstants.isDeploy_false)){
			resultBean = this.deploy(yxjlid,yyid, fwqip);
			if(!resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				return resultBean;
			}
		}
		
		/**
		 * 2、启动应用
		 */
		
		//2.1、远程启动
		result = SocketUtils.webapp_start(fwqip,yyBean.getWarname());
		if(result != null && result.equals(SystemConstants.WEBAPP_WARNOEXISTS) ){
			
			//2.1.1、验证远程服务器是否存在WAR包【不存在则上传】
			resultBean = this.deploy(yxjlid,yyid, fwqip);
			if(!resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				return resultBean;
			}
			//2.1.2、重新启动
			result = SocketUtils.webapp_start(fwqip,yyBean.getWarname());
		}
		if(result != null && !result.equals(SystemConstants.WEBAPP_ISONLINE) && !result.equals(SystemConstants.WEBAPP_SUCCESS)){
			resultBean = new ResultBean(result, SystemConstants.MAP_WEBAPP_MS.get(result));
			return resultBean;
		}
		
		
		//2.2、等待应用真正启动
		waitForRealStart(fwqip, yyBean, resultBean);
		

		//2.3、更新启动状态
		yyyxjlBean.setYyzt(SystemConstants.YYZT_YQD);
		yyyxjlBean.setYyyxzt(SystemConstants.YYYYZT_ZC);
		int re = yyManager.updateYyyxjlByFields(yyyxjlBean, "yyzt,yyyxzt");
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "保存应用运行记录失败！");
			return resultBean;
		}
		

		/**
		 * 3、更新NIGNX【防止停止后用户访问报404】
		 */
		ResultBean resultBean_ = nginxManager.updateNginxConfByYyid(yyid);
		if(!resultBean_.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			resultBean.setArg1("【<font color=\"#ffb90f\">Nginx更新失败，请手动更新！</font>】");
		}
		
		return resultBean;
	}

	/*
	 * 等待应用真正起来
	 */
	private void waitForRealStart(String fwqip, YyBean yyBean, ResultBean resultBean) {
		IFwqManager fwqManager = (IFwqManager) SpringBeanFactory.getBean("fwqManager");
		List list = fwqManager.getFwqListByFwqIp(fwqip);
		if (list != null || list.size()>0) {
			Map fwqMap = (Map) list.get(0);
			String ip = SystemConstants.PTLX_TG.equals(fwqMap.get("ptlx")) ? (String)fwqMap.get("fwqip_ww") : (String) fwqMap.get("fwqip");
			String dk = (String) fwqMap.get("dk");
			String warname = StringUtils.isEmpty(yyBean.getWarname()) ? "" : yyBean.getWarname().replace(".war", "");
			int i = 0;
			while(i<=20) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//执行检测的功能
				String url = "http://" + ip + ":" + dk + "/" + warname;
				String connect = URLAvailability.isConnect(url);
				if (StringUtils.isEmpty(connect)) {
					//成功
					break;
				}
				if (!connect.startsWith("HTTP")) {
					//5xx开头,war包有问题，返回启动失败
					String result = SystemConstants.WEBAPP_WARERROR;
					resultBean = new ResultBean(result, SystemConstants.MAP_WEBAPP_MS.get(result));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SocketUtils.webapp_stop(fwqip,yyBean.getWarname()); 	//调用远程停止的方法，删除war包和目录
					return;
				}
				i += 3;
			}
		}
	}
	
	@Override
	public ResultBean stop(String yxjlid,String yyid, String fwqip) {
		ResultBean resultBean = null;
		String result  = "";
		YyBean yyBean = yyManager.getYyBean(yyid);
				
		/**
		 * 1、停止应用
		 */
		
		//1.2、变更应用运行状态
		YyyxjlBean yyyxjlBean =  new YyyxjlBean();
		yyyxjlBean.setId(yxjlid);
		yyyxjlBean.setYyyxzt("");
		yyyxjlBean.setYyzt(SystemConstants.YYZT_WQD);
		int re = yyManager.updateYyyxjlByFields(yyyxjlBean, "yyyxzt,yyzt");
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "停止应用时保存应用运行记录失败！");
			return resultBean;
		}
		
		resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		
		/**
		 * 2、更新NIGNX【防止停止后用户访问报404】
		 */
		ResultBean resultBean_ = nginxManager.updateNginxConfByYyid(yyid);
		if(!resultBean_.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			resultBean.setArg1("【<font color=\"#ffb90f\">Nginx更新失败，请手动更新！</font>】");
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//1.1、远程停止
		result = SocketUtils.webapp_stop(fwqip,yyBean.getWarname());
		if(result != null && !result.equals(SystemConstants.WEBAPP_SUCCESS)){
			resultBean = new ResultBean(result, "<font color=\"#FF9900\">远程停止异常！</font>");
			return resultBean;
		}
		
		return resultBean;
	}

	@Override
	public ResultBean remove(String yxjlid,String yyid, String fwqip) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");

		/**
		 * 1.停止应用
		 */
		YyyxjlBean yyyxjlBean = yyManager.getYyyxjlBean(yxjlid);
		if(yyyxjlBean.getYyzt().equals(SystemConstants.YYZT_YQD) 
				&& yyyxjlBean.getYyyxzt() != null
				&& yyyxjlBean.getYyyxzt().equals(SystemConstants.YYYYZT_ZC)){
			resultBean = this.stop(yxjlid, yyid, fwqip);
			if(!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())){
				return resultBean;
			}
		}
		
		/**
		 * 2.处理结果
		 */
		int re = yyManager.deleteYyyxjl(yyyxjlBean);
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "删除应用运行记录失败！");
			return resultBean;
		}

		return resultBean;
	}
	
	
	@Override
	public ResultBean versionToPriavte(String yyid,String isAutoRun) {
		ResultBean resultBean = null;
	
		YyBean yyBean = yyManager.getYyBean(yyid);
		/**
		 * 1、校验试发布版本
		 */
		resultBean = this.validate_sfbbb(yyBean);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/**
		 * 2、隔离服务器
		 */
		//2.1、获取试发布服务器
		resultBean = this.getSfbServer(yyBean);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}

		//2.2、执行隔离【停止应用、应用更新为试发布】
		Map yxjlxx = (Map) resultBean.getArg1();
		String yxjlid = (String) yxjlxx.get("yxjlid");
		String fwqip  = (String) yxjlxx.get("fwqip");
		resultBean = this.serverToPrivate(yxjlid,yyid,fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/*=================================== 是否由后台完成应用回退操作 ===================================*/
		if(isAutoRun == null || !isAutoRun.equals(SystemConstants.isAutoRun_true)){
			String msg = "测试服务器【<font style=\"font-weight: bold;\">"+fwqip+"</font>】已准备好，是否启动试发布版本应用？</br>"+ 
							"<a  href=\"javascript:void(0)\"  style=\"color:#009933;background-color:#DCDCDC;padding:5px;font-weight: bold;\"  onclick=\"control('"+yyid+"','"+fwqip+"','"+SystemConstants.CONTROL_START+"','启动','false')\"> 是,立即发布   </a>"+
							"&nbsp;&nbsp;&nbsp;&nbsp;"+
							"<a  href=\"javascript:void(0)\" style=\"color:#FF9900;background-color:#DCDCDC;padding: 5px;font-weight: bold;\"  onclick=\"finishHandler(this)\"> 不,稍后处理   </a>";
			
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,msg);
			return resultBean;
		}
		/*=================================== 是否由后台完成应用试发布操作 ===================================*/
		
		/**
		 * 3、应用启动【试发布版本】
		 */
	
		//3.1启动应用
		resultBean = this.start(yxjlid, yyid, fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}

		resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"应用试发布成功，试发布服务器为【"+fwqip+"】!");
		return resultBean;
	}
	
	@Override
	public ResultBean versionToBack(String yyid, String isAutoRun) {
		ResultBean resultBean = null;
		
	
		/**
		 * 1、解除隔离
		 */
		
		//1.1、获取已有试发布服务器
		List yyfwqlist = yyManager.getYyInfoListByYyid(yyid);
		Map yxjlxx = this.getServerFromSfb(yyfwqlist);
		if(yxjlxx == null){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"该应用无试发布环境，无需回退！");
			return resultBean;
		}
		 
		//1.2、执行解除【停止应用、应用更新为正式】	
		String yxjlid = (String) yxjlxx.get("yxjlid");
		String fwqip  = (String) yxjlxx.get("fwqip");
		resultBean = this.serverToPublic(yxjlid,yyid,fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
	
		
		/*=================================== 是否由后台完成应用回退操作 ===================================*/
		if(isAutoRun == null || !isAutoRun.equals(SystemConstants.isAutoRun_true)){
			String msg = "服务器【<font style=\"font-weight: bold;\">"+fwqip+"</font>】已完成版本回退，是否启动应用？</br>"+ 
							"<a  href=\"javascript:void(0)\"  style=\"color:#009933;background-color:#DCDCDC;padding:5px;font-weight: bold;\"  onclick=\"control('"+yyid+"','"+fwqip+"','"+SystemConstants.CONTROL_START+"','启动','false')\"> 是,立即发布   </a>"+
							"&nbsp;&nbsp;&nbsp;&nbsp;"+
							"<a  href=\"javascript:void(0)\" style=\"color:#FF9900;background-color:#DCDCDC;padding: 5px;font-weight: bold;\"  onclick=\"finishHandler(this)\"> 不,稍后处理   </a>";
			

			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,msg);
			return resultBean;
		}
		/*=================================== 是否由后台完成应用回退操作 ===================================*/
		
		
		/**
		 * 2、应用启动【正式版本】
		 */
		
				
		//启动应用
		resultBean = this.start(yxjlid, yyid, fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}

		resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"应用回退成功!");
		return resultBean;
	}


	@Override
	public ResultBean versionToPublic(String yyid, String isAutoRun) {
		ResultBean resultBean = null;
		/**
		 * 1、版本升级
		 */
		resultBean = this.yyToPublic(yyid);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/*=================================== 是否由后台完成应用正式发布操作 ===================================*/
//		if(isAutoRun == null || !isAutoRun.equals(SystemConstants.isAutoRun_true)){
//			String msg = "试发布版本已完成正式发布，是否立即更新线上应用？</br>"+ 
//							"<a  href=\"javascript:void(0)\"  style=\"color:#009933;background-color:#DCDCDC;padding:5px;font-weight: bold;\"  onclick=\"control('"+yyid+"','"+SystemConstants.CONTROL_START+"','启动','false')\"> 是,立即更新   </a>"+
//							"&nbsp;&nbsp;&nbsp;&nbsp;"+
//							"<a  href=\"javascript:void(0)\" style=\"color:#FF9900;background-color:#DCDCDC;padding: 5px;font-weight: bold;\"  onclick=\"finishHandler(this)\"> 不,稍后处理   </a>";
//			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,msg);
//			return resultBean;
//		}
		/*=================================== 是否由后台完成应用正式发布操作 ===================================*/
		
		/**
		 * 2、线上版本更新【循环更新】
		 */
		//2.1、获取正常服务器
		List yyfwqlist = yyManager.getYyInfoListByYyid(yyid);
		if(yyfwqlist!= null && yyfwqlist.size() > 0){
			for(int i = 0; i < yyfwqlist.size(); i++){
				Map map = (Map) yyfwqlist.get(i);
				String fwqstatus = (String) map.get("fwqstatus");
				//服务器正常
				if(fwqstatus == null && !fwqstatus.equals(SystemConstants.FWQSTATUS_ZC)){
					yyfwqlist.remove(i);
					i--;
				}
			}
		}
			
	
		//2.2、循环更新【先停止再启动】
		if(yyfwqlist!= null && yyfwqlist.size() > 0){
			resultBean.setMsg("");
			for(int i = 0; i < yyfwqlist.size(); i++){
				Map map = (Map) yyfwqlist.get(i);
				String fwqip = (String) map.get("fwqip");
				String yxjlid = (String) map.get("yxjlid");
				//停止应用
				ResultBean resultBean_ = this.stop(yxjlid, yyid, fwqip);
				if(resultBean_ != null && resultBean_.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
					//启动应用
					resultBean_ = this.start(yxjlid, yyid, fwqip);
				}
				String msg = "<font color=\"black\">在服务器&nbsp;["+fwqip+"]&nbsp;发布正式版本结果：</font>";
				if(resultBean_ != null && resultBean_.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
					msg += "<font color=\"green\">操作成功！</font></br>";
				}else{
					msg += "<font color=\"red\">"+resultBean_.getMsg()+"</font></br>";
				}
				resultBean.setMsg(resultBean.getMsg()+msg);
			}
		}

		return resultBean;
	}
	

	/**
	 * 版本升级
	 * @param yyid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-19 上午11:04:25
	 */
	private ResultBean yyToPublic(String yyid) {
		ResultBean resultBean = null;
		YyBean yyBean = yyManager.getYyBean(yyid);
		/**
		 * 1、校验试发布版本有效性
		 */
		resultBean = this.validate_sfbbb(yyBean);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/**
		 * 2、应用升级
		 */
		
		//2.1、更新应用版本号
		String warsrc = SystemUtils.getAppWarSrc(yyBean.getWarname(), yyBean.getYyversion_sfb())+ File.separator + yyBean.getWarname();
		String warsrc_sfb = yyBean.getWarsrc_sfb();
		yyBean.setWarsrc(warsrc);
		yyBean.setWarsrc_sfb("");
		yyBean.setYyversion(yyBean.getYyversion_sfb());
		yyBean.setYyversion_sfb("");
		
		int re = this.daoUtils.update(yyBean, "warsrc,yyversion,warsrc_sfb,yyversion_sfb");
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"应用版本升级失败！");
			return resultBean;
		}

		//2.2、试发布WAR转移到正式版本文件夹下
		if(re > 0){
			
			File newPath = new File(SystemUtils.getAppWarSrc(yyBean.getWarname(), yyBean.getYyversion()));//正式版本存储路径
			if(!newPath.exists()){
				newPath.mkdirs();
			}
			
			FileUtils.copyFile(warsrc_sfb,  warsrc);
		}
		
	
		
		/**
		 * 3、解除服务器隔离
		 */
		
		//3.1、所有“试发布服务器”转成“正式服务器”、部署状态变更为“未部署”
		String sql = "update t_yygl_yyyxjl  set fbzt = '"+SystemConstants.FBZT_FB+"',isdeploy ='"+SystemConstants.isDeploy_false+"'" +
						" where yyid = '"+yyid+"' ";
		this.daoUtils.execSql(sql);

		return resultBean;

	}

	/**
	 * 隔离服务器
	 * @param yxjlid
	 * @param yyid
	 * @param arg1
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午2:06:49
	 */
	private ResultBean serverToPrivate(String yxjlid, String yyid, String fwqip) {

		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "隔离服务器失败！");
		
		/**
		 * 1、停止应用
		 */
		resultBean = this.stop(yxjlid, yyid, fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/**
		 * 2、变更服务器应用为“试发布”,war包状态变更为“未部署”
		 */
		
		YyyxjlBean yyyxjlBean = new YyyxjlBean();
		
		yyyxjlBean.setId(yxjlid);
		yyyxjlBean.setFbzt(SystemConstants.FBZT_SFB);
		yyyxjlBean.setIsdeploy(SystemConstants.isDeploy_false);
		int re = this.daoUtils.update(yyyxjlBean, "fbzt,isdeploy");
		if(re > 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "隔离服务器成功！");
		}
		
		return resultBean;
		
	}
	

	/**
	 * 解除服务器隔离
	 * @param yxjlid
	 * @param yyid
	 * @param fwqip
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-18 下午5:23:47
	 */
	private ResultBean serverToPublic(String yxjlid, String yyid, String fwqip) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "解除隔离失败！");
		
		/**
		 * 1、停止应用
		 */
		resultBean = this.stop(yxjlid, yyid, fwqip);
		if(resultBean != null && !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			return resultBean;
		}
		
		/**
		 * 2、变更服务器应用为“正式”,war包状态变更为“未部署”
		 */
		
		YyyxjlBean yyyxjlBean = new YyyxjlBean();
		
		yyyxjlBean.setId(yxjlid);
		yyyxjlBean.setFbzt(SystemConstants.FBZT_FB);
		yyyxjlBean.setIsdeploy(SystemConstants.isDeploy_false);
		int re = this.daoUtils.update(yyyxjlBean, "fbzt,isdeploy");
		if(re > 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "解除隔离成功！");
		}
		
		return resultBean;
		
	}


	/**
	 * 获取试发布服务器
	 * @param yyBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 上午10:45:22
	 */
	private ResultBean getSfbServer(YyBean yyBean) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,"无可用服务器！");
		
		String yyid = yyBean.getId();
		List yyfwqlist = yyManager.getYyInfoListByYyid(yyid);
		
		//验证——是否有服务器可用
		boolean re = this.isServerAvailable(yyfwqlist);
		if(!re){
			return resultBean;
		}
		
		/**
		 * 获取一台可用服务器
		 */
		
		//从已存在试发布服务器抽取
		Map yxjlxx = this.getServerFromSfb(yyfwqlist);

		//从空闲服务器抽取
		if(yxjlxx == null){
			yxjlxx = this.getServerFromFree(yyfwqlist);
		}
		//从运行中服务器抽取
		if(yxjlxx == null){
			yxjlxx = this.getServerFromOnline(yyfwqlist);
		}
		
		
		if(yxjlxx != null){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"成功获取到试发布服务器【"+yxjlxx.get("fwqip")+"】！");
			resultBean.setArg1(yxjlxx);
		}

		return resultBean;
	}

	/**
	 * 从当前运行中服务器抽取一台
	 * @param yyfwqlist
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午1:37:54
	 */
	private Map getServerFromOnline(List yyfwqlist) {
		int count = 0;
		for(int i = 0; i < yyfwqlist.size(); i++){
			Map map = (Map) yyfwqlist.get(i);
			String yyzt = (String) map.get("yyzt");
			String fbzt = (String) map.get("fbzt");
			String fwqstatus = (String) map.get("fwqstatus");
	
			//服务器正常 & 正式服务器 & 应用已启动
			if(fwqstatus != null && fwqstatus.equals(SystemConstants.FWQSTATUS_ZC) 
					&& fbzt != null && fbzt.equals(SystemConstants.FBZT_FB)
					&& yyzt != null && yyzt.equals(SystemConstants.YYZT_YQD)){
				count++;
			}
			if(count > 1){
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 从当前应用空闲服务器中抽取一台
	 * @param yyfwqlist
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午1:37:54
	 */
	private Map getServerFromFree(List yyfwqlist) {
		for(int i = 0; i < yyfwqlist.size(); i++){
			Map map = (Map) yyfwqlist.get(i);
			String yyzt = (String) map.get("yyzt");
			String fbzt = (String) map.get("fbzt");
			String fwqstatus = (String) map.get("fwqstatus");
			//服务器正常 & 正式服务器 & 应用未启动
			if(fwqstatus != null && fwqstatus.equals(SystemConstants.FWQSTATUS_ZC) 
					&& fbzt != null && fbzt.equals(SystemConstants.FBZT_FB)
					&& yyzt != null && yyzt.equals(SystemConstants.YYZT_WQD)){
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 从当前应用试发布服务器中抽取一台
	 * @param yyfwqlist
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午1:37:54
	 */
	private Map getServerFromSfb(List yyfwqlist) {
		for(int i = 0; i < yyfwqlist.size(); i++){
			Map map = (Map) yyfwqlist.get(i);
			String fbzt = (String) map.get("fbzt");
			String fwqstatus = (String) map.get("fwqstatus");
			//服务器正常 &  试发布应用
			if(fwqstatus != null && fwqstatus.equals(SystemConstants.FWQSTATUS_ZC) && fbzt != null && fbzt.equals(SystemConstants.FBZT_SFB)){
				return map;
			}
		}
		return null;
	}
	


	/**
	 * 是否有可用服务器
	 * @param yyfwqlist
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午1:10:14
	 */
	private boolean isServerAvailable(List yyfwqlist) {
		if(yyfwqlist != null && yyfwqlist.size() > 0){
			for(int i = 0; i < yyfwqlist.size(); i++){
				Map map = (Map) yyfwqlist.get(i);
				String fwqstatus = (String) map.get("fwqstatus");
				if(fwqstatus != null && fwqstatus.equals(SystemConstants.FWQSTATUS_ZC)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检测试发布版本
	 * 
	 * @author wujiaxu
	 * @param yyBean 
	 * @Time 2017-9-14 下午4:00:55
	 */
	private ResultBean validate_sfbbb(YyBean yyBean) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		//验证——应用是否存在
		if(yyBean == null ){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "应用不存在！");
			return resultBean;
		}
		
		//验证——试发布版本是否存在
		if(yyBean.getWarsrc_sfb() == null || yyBean.getYyversion_sfb() == null){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "无试发布版本！");
			return resultBean;
		}
		
		//验证——试发布WAR文件是否存在
		String warPath = SystemUtils.getAppSfbSrc(yyBean.getWarname()) + File.separator + yyBean.getWarname();
		File warFile = new File(warPath);
		if(!warFile.exists()){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "未找到试发布WAR包！");
			return resultBean;
		}
		
		return resultBean;
	}

	public IYyManager getYyManager() {
		return yyManager;
	}

	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}

	public void setNginxManager(INginxManager nginxManager) {
		this.nginxManager = nginxManager;
	}
	
	
}
