package com.core.jadlwork.business.fwqgl.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqglManager;
import com.core.jadlwork.cache.ICacheManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.cache.ServerCacheBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.utils.CmdUtils;
import com.core.jadlwork.utils.SocketUtils;

/**
 * 服务器管理的实现类
 * @ClassName: FwqglManagerImpl
 * @author: 李春晓
 * @date: 2016-12-22 上午10:29:23
 */
public class FwqglManagerImpl extends BaseManager implements IFwqglManager {

	private ICacheManager cacheManager;
	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public int saveFwq(FwqBean fwqBean) {
		//从服务器缓存中通过ip获取缓存中的信息
		fwqBean = cacheManager.getFwqBySaveFwq(fwqBean);
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWQGL_FWQ"));
		fwqBean.setId(id);
		fwqBean.setCjsj(DateUtils.createCurrentDate());
		fwqBean.setZt(SystemConstants.ZT_TRUE);
		
		return this.daoUtils.save(fwqBean);
	}

	@Override
	public FwqBean getFwqBeanById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (FwqBean) this.daoUtils.findObject(FwqBean.class, condition);
	}

	@Override
	public int updateFwqByFields(FwqBean fwqBean, String fields) {
		return this.daoUtils.update(fwqBean,fields);
	}

	@Override
	public int deleteFwq(FwqBean fwqBean) {
		return this.daoUtils.update(fwqBean,"zt");
	}

	@Override
	public List getFwqListByFwqip(String fwqip) {
		Map condition = new HashMap();
		condition.put("fwqip", fwqip);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#fwqgl.getFwqListByFwqip", condition);
	}

	@Override
	public List getAllFwqList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#fwqgl.getAllFwqList", condition);
	}

	@Override
	public ResultBean checkFwqByIp(String fwqip, HttpServletResponse response) {
		// 设置默认成功内容
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		// 1.检测服务器是否已接入
		List fwqlist = this.getFwqListByFwqip(fwqip);
		if(fwqlist != null && fwqlist.size() > 0){
			// 服务器已经存在
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_IPEXIST,"ip已经存在");
			return resultBean;
		}
		
		// 2、检测ip是否ping通
		if (!CmdUtils.isPing(fwqip)) {
			// 说明ping不通
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_PINGERROR,"此服务器ip不能ping通");
			return resultBean;
		}
		
		// 3、检测socket是否正常
		String communicateInfo = SocketUtils.getCommunicateInfo(fwqip);
		// 解析communicateInfo
		if (communicateInfo != null && communicateInfo.startsWith("success")) {
			// 说明socket通信通过
			String serverInfo = communicateInfo.split("&")[1];
			//向缓存中保存信息
			JSONObject jsonObject = JSONObject.fromObject(serverInfo);
			Map appInfo = (Map) jsonObject.get("appInfo");
			ServerCacheBean serverCacheBean = (ServerCacheBean) JSONObject.toBean(jsonObject, ServerCacheBean.class);
			serverCacheBean.setFwqip(fwqip);	//服务器ip
			serverCacheBean.setAppinfo(appInfo);	//设置应用信息
//			cacheManager.addFwqCache(serverCacheBean);
		}else {
			// 说明socket检测未通过
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_SOCKETERROR,"此服务器不能正常socket通信");
			return resultBean;
		}
		
		// ...进行其他的测试，判断失败的进行返回
		
		// 最终返回成功的信息
		return resultBean;
	}

	@Override
	public String getLogsByFwqip(String fwqip) {
		//先判断服务器是否在线
//		if (!cacheManager.isFwqOnline(fwqip)) {
//			return "";
//		}
		return SocketUtils.getFwqLogs(fwqip);
	}

	@Override
	public String showLog(String fwqip, String logName) {
		return null;
	}

	@Override
	public List getFwqList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#fwqgl.getFwqList", condition);
	}

}
