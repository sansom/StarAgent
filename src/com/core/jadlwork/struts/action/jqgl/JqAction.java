package com.core.jadlwork.struts.action.jqgl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.jq.JqJbxxBean;
/**
 * 集群
 * @作者：李春晓
 * @时间：2017/06/27 10:36
 */
public class JqAction extends BaseAction{


	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(JqAction.class);

	/**
	 * 进入集群添加界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String edit()  {
		String jqid = request.getParameter("jqid");
		if (jqid != null && !jqid.equals("")) {
			JqJbxxBean jqJbxxBean = jqManager.getJqJbxxBean(jqid);
			request.setAttribute("jqJbxxBean", jqJbxxBean);
			//根据集群获取对应的服务器信息
			List fwqList = jqManager.getFwqListByJqid(jqid);
			request.setAttribute("fwqList", fwqList);
		}
		//获取所有的可用服务器，可以在这里进行添加
		if (jqid == null) {
			jqid = "";
		}
		List kyfwqList = jqManager.getKyFwqList(jqid);
		request.setAttribute("kyfwqList", kyfwqList);
		return "edit";
	}

	/**
	 * 添加集群
	 * @参数：@return
	 * @返回值：String
	 */
	/*public String save()  {
		String[] fwqids = request.getParameterValues("fwqids");
		int i = jqManager.saveJq(jqJbxxBean, fwqids);
		return "list";
	}*/
	
	/**
	 * ajax 保存集群
	 * @return: void
	 */
	public void save() {
		ResultBean resultBean = null;
		String[] fwqids = request.getParameterValues("fwqids");
		try {
			resultBean = jqManager.saveJq(jqJbxxBean, fwqids);
		} catch (RuntimeException e) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, e.getMessage());
			try {
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 集群修改
	 * @参数：@return
	 * @返回值：String
	 */
	/*public String update()  {
		String[] fwqids = request.getParameterValues("fwqids");
		int i = jqManager.updateJq(jqJbxxBean, fwqids);
		return "list";
	}*/

	/**
	 * ajax 修改集群
	 * @return: void
	 */
	public void update() {
		String[] fwqids = request.getParameterValues("fwqids");
		List<ResultBean> res = jqManager.updateJq(jqJbxxBean, fwqids);
		try {
			ResponseUtils.render(response, JsonUtil.list2json(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 撤销集群
	 * @参数：@return
	 * @返回值：String
	 */
	public void remove()  {
		String jqid = request.getParameter("jqid");
		List<ResultBean> list = jqManager.deleteJq(jqid);
		try {
			ResponseUtils.render(response, JsonUtil.list2json(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异步更新Nginx配置信息 
	 * @return: void
	 * @throws Exception 
	 */
	public void asyncUpdateNginxConf() throws Exception {
		ResultBean resultBean = null;
		String nids = request.getParameter("nids");
		if (StringUtils.isEmpty(nids)) {
			//没有要修改的Nginx配置
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "没有要修改配置的Nginx信息，已跳过！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		}else {
			String[] nidArr = nids.split(",");
			for (String nid : nidArr) {
				resultBean = nginxManager.updateNginxConfByNginxid(nid);
				if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
					//失败
					//放到后台队列中去处理
					NginxCache.getInstance().changedNginxids.add(nid);
					//保存Nginx的更新状态数据
					nginxManager.updateNginxGxzt(nid, SystemConstants.ZT_FALSE, (String) resultBean.getMsg());
					ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
					return;
				}
			}
		}
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "更新成功！");
	}

	//=================注入 set/get==========================
	private JqJbxxBean jqJbxxBean;
	public JqJbxxBean getJqJbxxBean() {
		return jqJbxxBean;
	}
	public void setJqJbxxBean(JqJbxxBean jqJbxxBean) {
		this.jqJbxxBean = jqJbxxBean;
	}
	//注入集群管理的manager
	private IJqManager jqManager;
	public void setJqManager(IJqManager jqManager) {
		this.jqManager = jqManager;
	}
	//注入Nginxmanager
	private INginxManager nginxManager;
	public void setNginxManager(INginxManager nginxManager) {
		this.nginxManager = nginxManager;
	}
	
	
}
