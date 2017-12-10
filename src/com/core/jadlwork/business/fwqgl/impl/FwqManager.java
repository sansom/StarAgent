package com.core.jadlwork.business.fwqgl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.cache.ServerCacheBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.model.fwqgl.MonitorhostBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;
import com.core.jadlwork.utils.CmdUtils;
import com.core.jadlwork.utils.SocketUtils;

/**
 * 云平台服务器管理的实现类
 * @类名: FwqManager
 * @作者: 李春晓
 * @时间: 2017-2-15 上午11:18:04
 */
public class FwqManager extends BaseManager implements IFwqManager {
	private static Logger logger = Logger.getLogger(FwqManager.class);

	private IYyManager yyManager;
	private IJqManager jqManager;
	private INginxManager nginxManager;
	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}
	public void setJqManager(IJqManager jqManager) {
		this.jqManager = jqManager;
	}
	public void setNginxManager(INginxManager nginxManager) {
		this.nginxManager = nginxManager;
	}
	@Override
	public int saveFwq(FwqBean fwqBean) {

		String id = String.valueOf(this.daoUtils.getNextval("Q_FWQGL_FWQ"));
		fwqBean.setId(id);
		fwqBean.setCjsj(DateUtils.createCurrentDate());
		fwqBean.setZt(SystemConstants.ZT_TRUE);
		
		return this.daoUtils.save(fwqBean);
	}

	@Override
	public List<ResultBean> deleteFwq(FwqBean fwqBean) {
		List<ResultBean> res = new ArrayList<ResultBean>();
		ResultBean resultOKBean = new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "操作结果");	//最终操作成功对象
		ResultBean resultERRORBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "操作结果");	//最终操作失败对象
		fwqBean.setCxsj(DateUtils.createCurrentDate());
		fwqBean.setZt(SystemConstants.ZT_FALSE);
		int i = this.daoUtils.update(fwqBean,"cxsj,zt");
		if (i<=0) {
			ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "删除失败！");
			resultBean.setArg1("删除服务器信息");
			res.add(resultBean);
			res.add(resultERRORBean);
			return res;
		}else {
			ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "删除成功！");
			resultBean.setArg1("删除服务器信息");
			res.add(resultBean);
		}
		
		//在删除运行记录之前，先获取当前所在的集群信息（应用应该是已启动的），用来更新Nginx配置文件使用
		List jqList = jqManager.getYqdJqListByFwqId(fwqBean.getId());
		//在删除运行记录之前，先获取应用信息（用于停止、删除使用）
		List yyInfoList = yyManager.getYyInfoListByFwqid(fwqBean.getId());
		//删除服务器集群信息
		try {
			jqManager.deleteJqFwqByFwqId(fwqBean.getId());
		} catch (Exception e) {
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "删除失败！", "删除服务器集群信息"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(resultERRORBean);
			return res;
		}
//		res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "删除成功！", "删除服务器集群信息"));
		//将服务器对应的运行记录表删除
		try {
			deleteYyyxjlByFwqid(fwqBean.getId());
		} catch (Exception e) {
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "删除失败！", "删除对应的应用运行记录信息"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(resultERRORBean);
			return res;
		}
//		res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "删除成功！", "删除对应的应用运行记录信息"));
		//完成后更新Nginx配置
		if (jqList == null || jqList.size()==0) {
			res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "无须修改，已跳过！", "更新Nginx配置"));
			res.add(resultOKBean);
			return res;
		}else {
			//更新Nginx
			for (Map jqMap : (List<Map>)jqList) {
				ResultBean bean = nginxManager.updateNginxConfByJqid((String) jqMap.get("id"));
				if (!bean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)) {
					//更新失败
					res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, bean.getMsg(), "更新Nginx配置"));
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
					res.add(resultERRORBean);
					return res;
				}else {
					//更新成功
					res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "更新成功！", "更新Nginx配置"));
				}
			}
			
			String warnames = "";
			Set<String> warnameSet = new HashSet<String>();
			if (yyInfoList != null && yyInfoList.size()>0) {
				for (Map yyMap : (List<Map>)yyInfoList) {
					warnameSet.add((String) yyMap.get("warname"));
				}
			}
			if (warnameSet.size()>0) {
				for (String name : warnameSet) {
					warnames += name;
				}
			}
			res.add(new ResultBean(SystemConstants.STATUSCODE_OK, warnames));
			res.add(resultOKBean);
			return res;
		}
	}

	@Override
	public int updateFwqByFields(FwqBean fwqBean, String fields) {
		return this.daoUtils.update(fwqBean,fields);
	}

	@Override
	public FwqBean getFwqBeanById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (FwqBean) this.daoUtils.findObject(FwqBean.class, condition);
	}

	@Override
	public List getFwqListByFwqIp(String fwqip) {
		Map condition = new HashMap();
		condition.put("fwqip", fwqip);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#fwqgl.getFwqListByFwqip", condition);
	}

	
	@Override
	public List getKyFwqByYyid(String yyid, String ptlx) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("ptlx", ptlx);
		return daoUtils.find("#fwqgl.getKyFwqByYyid", condition);
	}


	@Override
	public ResultBean checkFwqByIp(String fwqip, HttpServletResponse response) {
		// 设置默认成功内容
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		// 1.检测服务器是否已接入
		List fwqlist = this.getFwqListByFwqIp(fwqip);
		if(fwqlist != null && fwqlist.size() > 0){
			// 服务器已经存在
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_IPEXIST,"ip已经存在");
			return resultBean;
		}
		
		// 2、检测ip是否ping通
		if (!CmdUtils.isPing(fwqip)) {
			// 说明ping不通
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_PINGERROR,
					"此服务器ip不能ping通"+SystemConstants.LINE_SEPARATER
					+"    1）请检查ip地址是否正确 " 
					+ SystemConstants.LINE_SEPARATER
					+ "    2）请检查服务器是否处于开机状态");
			return resultBean;
		}
		
		// 3、检测socket是否正常
		String communicateInfo = SocketUtils.getServerInfo(fwqip);
		// 解析communicateInfo
		if (communicateInfo != null && !communicateInfo.equals("")) {
			// 说明socket通信通过
//			String serverInfo = communicateInfo.split("&")[1];
			String serverInfo = communicateInfo;
			JSONObject jsonObject = JSONObject.fromObject(serverInfo);
			Map appInfo = (Map) jsonObject.get("appinfo");
			ServerCacheBean serverCacheBean = (ServerCacheBean) JSONObject.toBean(jsonObject, ServerCacheBean.class);
			serverCacheBean.setFwqip(fwqip);	//服务器ip
			serverCacheBean.setAppinfo(appInfo);	//设置应用信息
			serverCacheBean.setFwqstatus(SystemConstants.FWQSTATUS_ZC);	//设置服务器状态正常
			//组装服务器bean
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,serverCacheBean);
		}else {
			// 说明socket检测未通过
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_SOCKETERROR,
					"此服务器不能正常socket通信"
							+SystemConstants.LINE_SEPARATER
							+"    1）请检查tomcat是否已启动 " 
							+ SystemConstants.LINE_SEPARATER
							+ "    2）请检查fwzxutils工具包是否已安装  ");
		}
		
		// ...进行其他的测试，判断失败的进行返回
		
		// 最终返回成功的信息
		return resultBean;
	}

	
	@Override
	public List getServerAppInfo() {
		return daoUtils.find("#fwqgl.gerServerAppInfo", new HashMap());
	}


	@Override
	public void updateServerInfo(FwqBean fwqBean, 
			List<YyyxjlBean> yyyxjlList) {
		//1、更新服务器信息
		int r = this.daoUtils.update(fwqBean);
		if(r > 0){
			//2、更新应用运行情况
			if(yyyxjlList != null && yyyxjlList.size() > 0){
				for(int i = 0; i < yyyxjlList.size(); i++){
					 this.daoUtils.update(yyyxjlList.get(i));
				}
			}
		}
	}
	

	@Override
	public Map getMoniterHost() {
		List list = daoUtils.find("#fwqgl.getMoniterHost", new HashMap());
		if(list != null && list.size() > 0){
			return (Map)list.get(0);
		}
		return null;
	}
	

	@Override
	public List getFwqList(String ptlx) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("ptlx", ptlx);
		List srcData = daoUtils.find("#fwqgl.getFwqListWithYy", condition);
		return formatFwqListWithYy(srcData);
//		return daoUtils.find(FwqBean.class, condition);
	}
	
	/*
	 * 将查询出来的服务器应用list转化为服务器中包含应用的map的格式
	 */
	private List formatFwqListWithYy(List srcData) {
		if (srcData == null || srcData.size()==0) {
			return srcData;
		}
		Map tempMap = new HashMap();
		
		for (Map fwqMap : (List<Map>)srcData) {
			if (tempMap.containsKey(fwqMap.get("id"))) {
				//将该应用的信息放进去
				Map oneMap = (Map) tempMap.get(fwqMap.get("id"));
				List yyList = (List) oneMap.get("yylist");
				Map yyMap = new HashMap();
				yyMap.put("yyname", fwqMap.get("yyname"));
				yyMap.put("warname", fwqMap.get("warname"));
				yyMap.put("warsrc", fwqMap.get("warsrc"));
				yyMap.put("yylogsrc", fwqMap.get("yylogsrc"));
				yyMap.put("yyversion", fwqMap.get("yyversion"));
				yyMap.put("yyzt", fwqMap.get("yyzt"));
				yyMap.put("yyyxzt", fwqMap.get("yyyxzt"));
				yyMap.put("id", fwqMap.get("yyid"));
				yyList.add(yyMap);
				oneMap.put("yylist", yyList);
			}else {
				//还没有当前服务器信息
				List yyList = new ArrayList();
				Map yyMap = new HashMap();
				yyMap.put("yyname", fwqMap.get("yyname"));
				yyMap.put("warname", fwqMap.get("warname"));
				yyMap.put("warsrc", fwqMap.get("warsrc"));
				yyMap.put("yylogsrc", fwqMap.get("yylogsrc"));
				yyMap.put("yyversion", fwqMap.get("yyversion"));
				yyMap.put("id", fwqMap.get("yyid"));
				yyList.add(yyMap);
				fwqMap.put("yylist", yyList);
				tempMap.put(fwqMap.get("id"), fwqMap);
			}
		}
		
		if (tempMap.size()==0) {
			return null;
		}
		
		List resList = new ArrayList();
		Set keySet = tempMap.keySet();
		for (Object key : keySet) {
			resList.add(tempMap.get(key));
		}
		return resList;
	}
	
	@Override
	public int updateMonitorHost(String hostId, String newHost) {
		MonitorhostBean bean = new MonitorhostBean();
		bean.setId(hostId);
		bean.setLastcontime(DateUtils.createCurrentDate());
		bean.setSystemid(newHost);
		return this.daoUtils.update(bean, "lastcontime,systemid");
	}

	@Override
	public int addMonitorHost(String newHost) {
		MonitorhostBean bean = new MonitorhostBean();
		bean.setId(String.valueOf(this.getMaxId(MonitorhostBean.db_tablename, MonitorhostBean.db_tablepkfields)));
		bean.setLastcontime(DateUtils.createCurrentDate());
		bean.setSystemid(newHost);
		return this.daoUtils.save(bean);
	}

	@Override
	public void deleteYyyxjlByFwqid(String fwqid) {
		Map condition = new HashMap();
		condition.put("fwqid", fwqid);
		daoUtils.execSql("#fwqgl.deleteYyyxjlByFwqid", condition);
	}
	

}
