package com.core.jadlwork.business.jqgl.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.CreateID;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.jq.JqFwqBean;
import com.core.jadlwork.model.jq.JqJbxxBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;

/**
 * 集群管理的manager实现类
 * @类名: JqManager
 * @作者: 李春晓
 * @时间: 2017-6-27 上午10:40:29
 */
public class JqManager extends BaseManager implements IJqManager {
	
	private Logger logger = Logger.getLogger(JqManager.class);
	
	@Override
	public JqJbxxBean getJqJbxxBean(String jqid) {
		Map condition = new HashMap();
		condition.put("id", jqid);
		Object object = daoUtils.findObjectCompatibleNull(JqJbxxBean.class, condition);
		return object == null ? null : (JqJbxxBean) object;
	}
	
	@Override
	public ResultBean saveJq(JqJbxxBean jqJbxxBean, String[] fwqids) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "");
		String id = "";
		synchronized (jqJbxxBean) {
			id = String.valueOf(this.daoUtils.getNextval("Q_JQ_JBXX"));
		}
		jqJbxxBean.setId(id);
		jqJbxxBean.setCjsj(DateUtils.createCurrentDate());
		jqJbxxBean.setZt(SystemConstants.ZT_TRUE);
		int i = daoUtils.save(jqJbxxBean);
		if (i<=0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "向数据库保存出错！");
			return resultBean;
		}
		//更新集群服务器
		//先删除
		deleteJqFwqByJqId(id);
		
		if (fwqids != null && fwqids.length>0) {
			for (String fwqid : fwqids) {
				saveJqFwq(id, fwqid);
			}
		}
		
		//更新Nginx配置
		/*resultBean = updateNginxAfterEditJq(id);
		if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
			throw new RuntimeException((String) resultBean.getMsg());
		}*/
		return resultBean;
	}
	
	@Override
	public List<ResultBean> updateJq(JqJbxxBean jqJbxxBean, String[] fwqids) {
		List<ResultBean> res = new ArrayList<ResultBean>();	//返回结果
		ResultBean resultOKBean = new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "操作结果：");	//最终操作成功对象
		ResultBean resultERRORBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "操作结果：");	//最终操作失败对象
		ResultBean resultBean = null;
		jqJbxxBean.setZhxgsj(DateUtils.createCurrentDate());
		int i = daoUtils.update(jqJbxxBean);
		if (i<=0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新失败！", "更新集群基本信息：");
			res.add(resultBean);
			res.add(resultERRORBean);
			return res;
		}
		//1、首先获取之前的所有的服务器
		List fwqBefore = getFwqListByJqid(jqJbxxBean.getId());
		if ((fwqids == null || fwqids.length==0) && (fwqBefore == null || fwqBefore.size()==0)) {
			//两个都为空，不用管
		}
		List addFwqList = new ArrayList();	//新增服务器列表
		List removeFwqList = new ArrayList();	//减少的服务器列表
		//2、遍历获取新增的和减少的服务器
		if (fwqBefore == null || fwqBefore.size()==0) {
			//都是新增的
			addFwqList = Arrays.asList(fwqids);
		}else {
			if (fwqids == null || fwqids.length==0) {
				//都是要删除的
				for (Map fwqMap : (List<Map>)fwqBefore) {
					removeFwqList.add(fwqMap.get("id"));
				}
			}else {
				//两边都不为空，循环处理
				for (String fwqid : fwqids) {
					boolean flag = true;
					for (Map thisFwq : (List<Map>)fwqBefore) {
						if (fwqid.equals(thisFwq.get("id"))) {
							//原来的就有该服务器，不用处理
							flag = false;
							continue;
						}
					}
					if (flag) {
						//该服务器之前没有，属于新加的
						addFwqList.add(fwqid);
					}
				}
				
				for (Map thisFwq : (List<Map>)fwqBefore) {
					boolean flag = true;
					for (String fwqid : fwqids) {
						if (thisFwq.get("id").equals(fwqid)) {
							//原来的现在还有，不用处理
							flag = false;
							continue;
						}
					}
					if (flag) {
						//该服务器之前有，现在没有了，属于要减少的
						removeFwqList.add(thisFwq.get("id"));
					}
				}
			}
		}
		//3、更新集群服务器信息
		//更新集群服务器
		//先删除
		try {
			deleteJqFwqByJqId(jqJbxxBean.getId());
			if (fwqids != null && fwqids.length>0) {
				for (String fwqid : fwqids) {
					saveJqFwq(jqJbxxBean.getId(), fwqid);
				}
			}
		} catch (Exception e) {
			//更新集群服务器信息失败
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新失败！", "更新集群服务器信息："));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(resultERRORBean);
			return res;
		}
		res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "更新成功！", "更新集群服务器信息："));
		
		//4、更新应用运行记录的集群、服务器、应用信息
		//更新集群、服务器、应用表
		try {
			if (addFwqList.size()>0) {
				for (String fwqid : (List<String>)addFwqList) {
					//保存
					saveYyyxjlByJq(jqJbxxBean.getId(), fwqid);
				}
			}
			if (removeFwqList.size()>0) {
				for (String fwqid : (List<String>)removeFwqList) {
					//移除
					removeYyyxjlByJq(jqJbxxBean.getId(), fwqid);
				}
			}
		} catch (Exception e) {
			//更新集群服务器信息失败
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新失败！", "更新应用运行记录信息："));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(resultERRORBean);
			return res;
		}
//		res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "更新成功！", "更新应用运行记录信息："));
		
		//更新Nginx配置
		resultBean = nginxManager.updateNginxConfByJqid(jqJbxxBean.getId());
//		resultBean = updateNginxAfterEditJq(jqJbxxBean.getId());
		if (!SystemConstants.STATUSCODE_OK.equals(resultBean.getStatusCode())) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, resultBean.getMsg(), "更新Nginx配置："));
			res.add(resultERRORBean);
			return res;
		}
		res.add(new ResultBean(SystemConstants.STATUSCODE_OK, resultBean.getMsg(), "更新nginx配置信息："));
		res.add(resultOKBean);
		
		//添加询问框
		//判断是否有未启动的应用信息
		ResultBean bean = getWqdYyAfterEditJq(jqJbxxBean.getId());
		if (bean != null) {
			bean.setArg2(SystemConstants.ZT_TRUE);	//询问的状态标识
			res.add(bean);
		}
		return res;
	}
	
	/*
	 * 根据集群保存运行记录
	 */
	private void saveYyyxjlByJq(String jqid, String fwqid) {
		//1、查询该集群下的所有应用id信息
		List yyList = getYyListByJqidyyjl(jqid);
		//2、批量将应用信息插入到运行记录表中
		if (yyList != null && yyList.size()>0) {
			//批量添加
			String yyfwq_insert_sql = "insert into t_yygl_yyyxjl"
					+ "(id, yyid, fwqid, yyzt, jqid) " + "values (?, ?, ?, ?, ?) ";
			String[] yyfwq_insert_fields = { "id", "yyid", "fwqid", "yyzt", "jqid" };
			
			List data = new ArrayList();
			Map dataMap = null;
			String maxId = "";
			for (Map yyMap : (List<Map>)yyList) {
				dataMap = new HashMap();
				dataMap.put("id", maxId = (maxId == "" ? this.getMaxId(
								YyyxjlBean.db_tablename, "to_number("
										+ YyyxjlBean.db_tablepkfields + ")")
								+ "" : CreateID.getNextID(maxId)));
				dataMap.put("yyid", yyMap.get("id"));
				dataMap.put("fwqid", fwqid);
				dataMap.put("yyzt", SystemConstants.YYZT_WQD);
				dataMap.put("jqid", jqid);
				
				data.add(dataMap);
			}
			
			if (data.size()>0) {
				daoUtils.executeBatchUpdate(yyfwq_insert_sql,
						yyfwq_insert_fields, data);
			}
		}
	}
	
	/*
	 * 获取集群id下的所有应用信息
	 */
	private List getYyListByJqidyyjl(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#jqgl.getYyListByJqidyyjl", condition);
	}

	/*
	 * 根据集群移除运行记录
	 */
	private void removeYyyxjlByJq(String jqid, String fwqid) {
		//将运行记录表中相应的jqid和fwqid删除即可
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("fwqid", fwqid);
		daoUtils.execSql("#jqgl.removeYyyxjlByJq", condition);
	}

	/*
	 * 获取未启动的应用
	 */
	private ResultBean getWqdYyAfterEditJq(String id) {
		ResultBean resultBean = null;
		//获取该集群下是否有没有启动的应用
		List yyinfo = getWqdYyByJqid(id);
		StringBuffer sb = new StringBuffer("");
		if (yyinfo != null && yyinfo.size()>0) {
			for (Map yyMap : (List<Map>)yyinfo) {
				sb.append(yyMap.get("yyname"));
				sb.append(",");
			}
		}
		String wqdyyStr = "";
		if (sb.length()>0) {
			wqdyyStr = sb.substring(0, sb.length()-1);
		}
		
		if (wqdyyStr.length()>0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "");
			//有未启动应用
			resultBean.setMsg("集群中应用【"+wqdyyStr+"】未启动，是否前往应用列表界面？");
			resultBean.setArg3("../yygl/yylist.action");
		}
		return resultBean;
	}

	/**
	 * 根据集群id获取未启动的应用信息
	 * @return: List
	 */
	private List getWqdYyByJqid(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("qdzt", SystemConstants.ZT_FALSE);	//启动状态，未启动的
		return daoUtils.find("#jqgl.getWqdYyByJqid", condition);
	}
	
	@Override
	public List<ResultBean> deleteJq(String jqid) {
		List<ResultBean> res = new ArrayList<ResultBean>();
		ResultBean resultOKBean = new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "操作结果：");	//最终操作成功对象
		ResultBean resultERRORBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "操作结果：");	//最终操作失败对象
		JqJbxxBean jqJbxxBean = this.getJqJbxxBean(jqid);
		if (jqJbxxBean == null) {
			logger.error("【集群业务处理】删除集群失败，没有该id对应的集群信息！");
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "没有该id对应的集群信息！", "删除集群基本信息："));
			res.add(resultERRORBean);
			return res;
		}
		jqJbxxBean.setCxsj(DateUtils.createCurrentDate());
		jqJbxxBean.setZt(SystemConstants.ZT_FALSE);
		int i = daoUtils.update(jqJbxxBean, "zt");
		List nginxList = nginxManager.getNginxListByJqid(jqid);
		if (i>0) {
			//删除集群服务器中的该集群对应的信息
			try {
				deleteJqFwqByJqId(jqid);
			} catch (Exception e) {
				res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "删除集群服务器信息："));
				res.add(resultERRORBean);
				return res;
			}
			res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "删除集群-服务器信息："));
			
			//将Nginx集群表中的该集群下的数据删除
			try {
				nginxManager.deleteNginxJqByJqid(jqid);
			} catch (Exception e) {
				res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "删除Nginx-集群信息："));
				res.add(resultERRORBean);
				return res;
			}
			res.add(new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "删除Nginx-集群信息："));
		}else {
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "删除失败！", "删除集群基本信息："));
			res.add(resultERRORBean);
			return res;
		}
		res.add(resultOKBean);
		//将要更新的Nginx的id信息返回
		if (nginxList == null || nginxList.size() == 0) {
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, ""));
		}else {
			StringBuffer sb = new StringBuffer();
			String nids = "";
			for (Map nginxMap : (List<Map>)nginxList) {
				if (!nginxMap.get("id").equals("")) {
					sb.append(nginxMap.get("id")).append(",");
				}
			}
			if (sb.length()>0) {
				nids = sb.substring(0, sb.length()-1);
			}
			if (StringUtils.isEmpty(nids)) {
				res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, ""));
			}else {
				res.add(new ResultBean(SystemConstants.STATUSCODE_OK, nids));
			}
		}
		return res;
	}
	
	@Override
	public List getJqList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find(JqJbxxBean.class, condition);
	}
	
	@Override
	public List getYqdJqListByFwqId(String fwqid) {
		Map condition = new HashMap();
		condition.put("fwqid", fwqid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("yyzt", SystemConstants.YYZT_YQD);
		return daoUtils.find("#jqgl.getJqListByFwqId", condition);
	}
	
	@Override
	public JqFwqBean getJqFwqBeanByJqidAndFwqId(String jqid, String fwqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("fwqid", fwqid);
		Object object = daoUtils.findObjectCompatibleNull(JqFwqBean.class, condition);
		return object == null ? null : (JqFwqBean) object;
	}
	
	@Override
	public void saveJqFwq(String jqid, String fwqid) {
		String id = "";
		synchronized (id) {
			id = String.valueOf(this.daoUtils.getNextval("Q_JQ_FWQ"));
		}
		JqFwqBean jqFwqBean = new JqFwqBean();
		jqFwqBean.setId(id);
		jqFwqBean.setFwqid(fwqid);
		jqFwqBean.setJqid(jqid);
		daoUtils.save(jqFwqBean);
	}
	
	@Override
	public void updateJqFwq(String jqid, String fwqid) {
		//先删除，再添加
		this.deleteJqFwqByFwqId(fwqid);
		if (jqid != null && !jqid.equals("")) {
			this.saveJqFwq(jqid, fwqid);
		}
	}
	
	@Override
	public void deleteJqFwqByFwqId(String fwqid) {
		Map condition = new HashMap();
		condition.put("fwqid", fwqid);
		daoUtils.execSql("#jqgl.deleteJqFwqByFwqId", condition);
	}
	
	@Override
	public void deleteJqFwqByJqId(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		daoUtils.execSql("#jqgl.deleteJqFwqByJqId", condition);
	}
	
	@Override
	public List getjqListByNginxId(String nid) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("nid", nid);
		return daoUtils.find("#jqgl.getjqListByNginxId", condition);
	}
	
	@Override
	public void updateNginxJqxx(String[] jqxxs, String nid) {
		//先删除
		deleteNginxJqByNid(nid);
		//再添加,批量添加
		if (jqxxs == null || jqxxs.length == 0) {
			return;
		}
		
		String[] nginxJqField = { "id", "nginxid", "jqid"}; // 待插入值的字段
		List nginxJqList = new ArrayList();	//待插入的数据，里面放的是map
		Map data = null;
		for (int i = 0; i < jqxxs.length; i++) {
			data = new HashMap();
			data.put("id", String.valueOf(this.daoUtils.getNextval("Q_NGINX_JQ")));
			data.put("nginxid", nid);
			data.put("jqid", jqxxs[i]);
			nginxJqList.add(data);
		}
		daoUtils.executeBatchUpdate("#jqgl.insert_t_nginx_jq", nginxJqField, nginxJqList);
		
	}
	
	@Override
	public void deleteNginxJqByNid(String nid) {
		Map condition = new HashMap();
		condition.put("nid", nid);
		daoUtils.execSql("#jqgl.deleteNginxJqByNid", condition);
	}
	
	@Override
	public List getJqYyInfoList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#jqgl.getJqYyInfoList",condition);
	}
	
	@Override
	public List getKyFwqList(String jqid) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("jqid", jqid);
		return daoUtils.find("#jqgl.getKyFwqList", condition);
	}
	
	@Override
	public List getFwqListByJqid(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#jqgl.getFwqListByJqid", condition);
	}
	
	@Override
	public List getJqListByYyId(String yyid) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#jqgl.getJqListByYyid", condition);
	}
	
	//============================注入  get/set =================================
	private INginxManager nginxManager;
	public void setNginxManager(INginxManager nginxManager) {
		this.nginxManager = nginxManager;
	}
	
}
