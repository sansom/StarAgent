package com.core.jadlwork.struts.action.gzzx;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.IGzzxManager;
import com.core.jadlwork.business.gzzx.ITsgzManager;
import com.core.jadlwork.business.gzzx.ITsryManager;
import com.core.jadlwork.cache.gzzx.GzzxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.gzzx.GzzxJbxxBean;

public class GzzxAction extends BaseAction{
	private Logger logger = Logger.getLogger(GzzxAction.class);
	
	private GzzxJbxxBean gzzxJbxxBean;
	public GzzxJbxxBean getGzzxJbxxBean() {
		return gzzxJbxxBean;
	}
	public void setGzzxJbxxBean(GzzxJbxxBean gzzxJbxxBean) {
		this.gzzxJbxxBean = gzzxJbxxBean;
	}

	//================================= 注入 get/set ================================
	private IGzzxManager gzzxManager;
	public void setGzzxManager(IGzzxManager gzzxManager) {
		this.gzzxManager = gzzxManager;
	}
	private ITsryManager tsryManager;
	public void setTsryManager(ITsryManager tsryManager) {
		this.tsryManager = tsryManager;
	}
	private ITsgzManager tsgzManager;
	public void setTsgzManager(ITsgzManager tsgzManager) {
		this.tsgzManager = tsgzManager;
	}
	/**
	 * 手动处理故障，设置为已处理
	 * @return: String
	 */
	public String changeToYcl() {
		int i = gzzxManager.changeToYcl(gzzxJbxxBean.getId());
		return toList(gzzxJbxxBean.getGzlx());
	}
	
	/**
	 * 删除
	 * @return: String
	 */
	public String deleteGzzx() {
		int i = gzzxManager.deleteGzxx(gzzxJbxxBean.getId());
		return toList(gzzxJbxxBean.getGzlx());
	}
	
	/**
	 * 查看捕获信息
	 * @return: String
	 * @throws Exception 
	 */
	public void showBhjl() throws Exception {
		String gzid = gzzxJbxxBean.getId();
		List gzBhxx = gzzxManager.getGzBhXx(gzid);
		if (gzBhxx != null && gzBhxx.size()>0) {
			ResponseUtils.render(response, JsonUtil.list2json(gzBhxx));
			return;
		}
		ResponseUtils.render(response, "");
	}
	
	/**
	 * 更新故障过滤时长
	 * @throws Exception
	 * @return: void
	 */
	public void updateGzFilMinute() throws Exception {
		String minute = request.getParameter("minute");
		if (StringUtils.isEmpty(minute)) {
			ResponseUtils.render(response, "error");
			return;
		}
		try {
			GzzxCache.gzFilterMinute = Integer.parseInt(minute);
			ResponseUtils.render(response, "success");
			return;
		} catch (Exception e) {
			ResponseUtils.render(response, "error");
			return;
		}
	}
	
	/**
	 * 查看推送详情
	 * @return: String
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String showPushInfo() throws ServletException, IOException {
		String gzid = gzzxJbxxBean.getId();
		String[] param = new String[2];
    	param[0] = URLEncoder.encode("&&tsssid~=~"+gzid,"utf-8");
    	param[1] = URLEncoder.encode("tsssid","utf-8");
    	response.sendRedirect("gzpushlist.action?&queryparamter="
				+ param[0] + "&queryparamtername=" + param[1]);
		
//		return "gzpushlist";
    	return null;
	}
	
	private String toList(String gzlx) {
		if (SystemConstants.GZZX_GZLX_YY.equals(gzlx)) {
			//应用
			return "yygzzxlist";
		}
		if (SystemConstants.GZZX_GZLX_YYYX.equals(gzlx)) {
			//应用运行
			return "yyyxgzzxlist";
		}
		//服务器故障信息
		return "fwqgzzxlist";
	}
	
	/**
	 * 设置推送规则
	 * @return: String
	 */
	public String setTsgz() {
		//1、获取所有的人员信息
		List ryList = tsryManager.getAll();
		//2、获取所有的规则
		List gzList = tsgzManager.getAll();
		
		request.setAttribute("ryList", ryList);
		request.setAttribute("gzList", gzList);
		return "settsgz";
	}
	
	/**
	 * ajax，更新推送规则的人员
	 * @return: String
	 * @throws Exception 
	 */
	public void updateTsgzRy() throws Exception {
		String ryids = request.getParameter("ryids");
		String gzid = request.getParameter("gzid");
		
		//先删除
		tsgzManager.deleteRyByGzid(gzid);
		if (StringUtils.isEmpty(ryids)) {
			//为空
			ResponseUtils.render(response, "success");
			return;
		}
		
		String[] ryidsArr = null;
		if (ryids.contains(",")) {
			ryidsArr = ryids.split(",");
		}else {
			ryidsArr = new String[]{ryids};
		}
		tsgzManager.batchSaveRyByGzid(gzid, ryidsArr);
		ResponseUtils.render(response, "success");
	}
	
	/**
	 * ajax，改变规则的启用状态
	 * @return: void
	 * @throws Exception 
	 */
	public void changeGzQyzt() throws Exception {
		String qyzt = request.getParameter("qyzt");
		String gzid = request.getParameter("gzid");
		
		ResultBean resultBean = null;
		if (StringUtils.isEmpty(qyzt)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "启用状态为空");
			return;
		}
		if (StringUtils.isEmpty(gzid)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "规则id为空");
			return;
		}
		
		int i = tsgzManager.changeGzQyzt(gzid, qyzt);
		if (i>0) {
			String res = "启用成功";
			if (SystemConstants.ZT_FALSE.equals(qyzt)) {
				//停用
				res = "停用成功";
			}
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, res);
			return;
		}else {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "操作失败，请稍后再试!");
			return;
		}
	}
}
