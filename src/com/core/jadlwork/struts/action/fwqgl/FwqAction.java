package com.core.jadlwork.struts.action.fwqgl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.utils.SocketUtils;
/**
 * 服务器
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 10:09:58 AM
 */
public class FwqAction extends BaseAction{


	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(FwqAction.class);

	private FwqBean fwqBean ;
	
	//注入服务器manager接口
	private IFwqManager fwqManager;

	/**
	 * 进入服务器添加界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String edit()  {

 		if(fwqBean != null && fwqBean.getId() != null && !"".equals(fwqBean.getId())){
			fwqBean = (FwqBean) fwqManager.getFwqBeanById(fwqBean.getId());
			//获取该服务器对应的集群信息，（暂时就一个）
//			List jqFwqJqList = jqManager.getJqListByFwqId(fwqBean.getId());
//			request.setAttribute("jqFwqJqList", jqFwqJqList);
		}

 		//将集群信息放进作用域中
// 		List jqList = jqManager.getJqList();
// 		request.setAttribute("jqList", jqList);
		return "edit";
	}


	/**
	 * 添加服务器
	 * @参数：@return
	 * @返回值：String
	 */
	public String save()  {
		
		int i = fwqManager.saveFwq(fwqBean);
		//加入所属集群
//		String jqid = request.getParameter("ssjq");
//		if (jqid == null || jqid.equals("")) {
//			logger.error("【服务器业务】未选择所属集群！");
//		}else {
//			jqManager.saveJqFwq(jqid, fwqBean.getId());
//		}
		
		return this.toListPage();
		
	}

	/**
	 * 服务器修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String update()  {

		String fields = "fwqname,fwqym,dk,szjf,fwqip,fwqip_ww,dylxr,dylxrdh,delxr,delxrdh";
		int re = fwqManager.updateFwqByFields(fwqBean, fields);
		//加入所属集群
//		String jqid = request.getParameter("ssjq");
//		jqManager.updateJqFwq(jqid, fwqBean.getId());
		
		return this.toListPage();
	}
	
	
	/**
	 * 撤销服务器
	 * @参数：@return
	 * @返回值：String
	 */
	public void remove()  {
		ResultBean resBean = null;
		String fwqid = fwqBean.getId();
		List<ResultBean> resultInfo = fwqManager.deleteFwq(fwqBean);
		try {
			ResponseUtils.render(response, JsonUtil.list2json(resultInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return this.toListPage();
	}

	/**
	 * 停止该服务器上面对应的应用信息
	 * @return: void
	 * @throws Exception 
	 */
	public void stopYyOnFwq() throws Exception {
		String fwqip = request.getParameter("fwqip");
		String warnamesStr = request.getParameter("warnames");
		if (StringUtils.isEmpty(fwqip)) {
			ResponseUtils.render(response, "fwqip为空！");
			return;
		}
		if (StringUtils.isEmpty(warnamesStr)) {
			ResponseUtils.render(response, "没有要停止的应用!");
			return;
		}
		String[] warnamesArr = warnamesStr.split(",");
		for (String warname : warnamesArr) {
			SocketUtils.webapp_stop(fwqip, warname);
		}
		ResponseUtils.render(response, "");
	}
	
	
	/**
	 * ajax_根据ip地址检查服务器
	 * @参数：@return
	 * @返回值：String
	 */
	public String checkfwq(){
		
		String fwqip = request.getParameter("fwqip");
		//根据IP校验服务器状态
		ResultBean resultBean = fwqManager.checkFwqByIp(fwqip, response);
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("根据IP检测服务器失败！",e);
		}
		return null;
	}
	
	
	
	/**
	 * 跳转到list界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-6 下午4:48:40
	 */
	private String toListPage() {
		
		if(fwqBean != null && fwqBean.getPtlx() != null && fwqBean.getPtlx().equals(SystemConstants.PTLX_TG)){
			return "tglist";
		}
		return "list";
	}
	
	/**
	 * 获得tomcat日志列表
	 * @Title: getLogs
	 * @Description: 获得对应服务器中的日志列表
	 * @return: String 
	 */
	public String getTomcatLogs(){
		String fwqip = request.getParameter("fwqip");
		String logsList = SocketUtils.getTomcatLogs(fwqip);
		try {
			ResponseUtils.render(response,logsList);
		} catch (Exception e) {
			logger.error("获取Tomcat日志列表出错！",e);
		}
		return null;
	}
	
	/**
	 * 前往在线查看日志界面
	 * @return: String
	 */
	public String toshowLogOnline() {
		String logname = request.getParameter("logname");
		String fwqip = request.getParameter("fwqip");
		request.setAttribute("logname", logname);
		request.setAttribute("fwqip", fwqip);
		return "logview";
	}
	
	/**
	 * 查看在线日志
	 * @throws Exception
	 * @return: void
	 */
	public void viewLogOnline() throws Exception {
		ResultBean resultBean = null;
		String logname = request.getParameter("logname");
		String pos = request.getParameter("pos");
		String fwqip = request.getParameter("fwqip");
		
		if (StringUtils.isEmpty(fwqip)) {
			//服务器未开启
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "外网IP为空！");
			return;
		}
		
		//判断服务器是否启动
		String info = SocketUtils.getServerInfo(fwqip);
		if (StringUtils.isEmpty(info)) {
			//服务器未开启
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "服务器未开启！");
			return;
		}
		String resStr = SocketUtils.getTomcatLogByPos(fwqip, pos, logname);
		if (StringUtils.isEmpty(resStr)) {
			//获取信息失败，提示异常，稍后再试!
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "获取数据异常，稍后再试！");
			return;
		}else {
			Map map = JsonUtil.parserToMap(resStr);
			List logList = JsonUtil.parserToList((String) map.get("arg1"));
			if (map == null || map.size()==0) {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "解析数据出错！");
				return;
			}
			if (SystemConstants.STATUSCODE_OK.equals(map.get("statusCode"))) {
				//成功
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("statusCode", map.get("statusCode"));
				jsonObject.put("arg1", logList);
				jsonObject.put("arg2", map.get("arg2"));
				ResponseUtils.render(response, jsonObject.toString());
				return;
			}else {
				//出错
				resultBean = new ResultBean((String)map.get("statusCode"), map.get("msg"));
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}
		}
	}


	public void setFwqManager(IFwqManager fwqManager) {
		this.fwqManager = fwqManager;
	}

	public FwqBean getFwqBean() {
		return fwqBean;
	}
	public void setFwqBean(FwqBean fwqBean) {
		this.fwqBean = fwqBean;
	}
}
