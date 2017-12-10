package com.core.jadlwork.business.fwqgl.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.nginx.NginxBean;
import com.core.jadlwork.model.nginx.NginxConfigBean;
import com.core.jadlwork.model.nginx.NginxSfbpzBean;
import com.core.jadlwork.utils.BeanUtils;
import com.core.jadlwork.utils.GenerateNginxConfUtils;
import com.core.jadlwork.utils.NginxUtils;
import com.core.jadlwork.utils.SocketUtils;

/**
 * Nginx服务器操作的manager实现类
 * @类名: NginxManager
 * @作者: 李春晓
 * @时间: 2017-2-17 上午9:58:19
 */
public class NginxManager extends BaseManager implements INginxManager {

	private static Logger log = Logger.getLogger(NginxManager.class);
	
	@Override
	public int saveNginx(NginxBean nginxBean) {
		nginxBean.setCjsj(DateUtils.createCurrentDate());
		nginxBean.setZt(SystemConstants.ZT_TRUE);
		int i = daoUtils.save(nginxBean);
		return i;
	}

	@Override
	public int deleteNginx(String nid) {
		//获取服务器
		NginxBean nginxBean = getNginxBean(nid);
		if (nginxBean == null) {
			return 0;
		}
		//设置状态
		nginxBean.setZt(SystemConstants.ZT_FALSE);
		//设置移除时间
		nginxBean.setCxsj(DateUtils.createCurrentDate());
		int update = daoUtils.update(nginxBean, "zt,cxsj");
		if (update > 0) {
			//移除Nginx服务器后，将该Nginx对应的Nginx-集群表中的信息删除
			deleteNginxJqByNginxId(nid);
			//删除对应的试发布配置信息
			deleteSfbpzxxByNid(nid);
		}
		return update;
	}

	@Override
	public int updateNginx(NginxBean nginxBean) {
		//设置最后修改时间
		nginxBean.setZhxgsj(DateUtils.createCurrentDate());
		
		int i =daoUtils.update(nginxBean, "fwqname,nginxsrc,zhxgsj");
		return i;
		
	}

	@Override
	public String getNextval() {
		return String.valueOf(this.daoUtils.getNextval("Q_NGINX_JBXX"));
	}
	
	@Override
	public int updateNginxByFields(NginxBean nginxBean, String fields) {
		return daoUtils.update(nginxBean, fields);
	}

	@Override
	public List getNginxList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find(NginxBean.class, condition);
	}

	@Override
	public NginxBean getNginxBean(String id) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("id", id);
		return (NginxBean) daoUtils.findObjectCompatibleNull(NginxBean.class, condition);
	}
	
	@Override
	public NginxBean getNginxBeanByIp(String ip) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwqip", ip);
		return (NginxBean) daoUtils.findObjectCompatibleNull(NginxBean.class, condition);
	}
	
	@Override
	public Map getJqFwqYyByNginxId(String nid) {
		Map condition = new HashMap();
		condition.put("nid", nid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("yyzt", SystemConstants.YYZT_YQD);
		List srcData = daoUtils.find("#jqgl.getJqFwqYyByNginxId", condition);
		return formatNginxConfInfo(srcData);
	}
	
	@Override
	public Map getJqFwqYyByNginxIdForShow(String nid) {
		Map condition = new HashMap();
		condition.put("nid", nid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("yyzt", SystemConstants.YYZT_YQD);
		List srcData = daoUtils.find("#jqgl.getJqFwqYyByNginxId", condition);
		return formatNginxConfInfoForShow(srcData);
	}
	
	/*
	 * 格式化（生成配置文件使用）
	 */
	private Map formatNginxConfInfo(List srcData) {
		Map resultMap = new HashMap();
		if (srcData == null || srcData.size()==0) {
			return resultMap;
		}
		for (Map infoMap : (List<Map>)srcData) {
			String jqid = infoMap.get("jqid") == null ? "" : (String)infoMap.get("jqid");
			
			String fwqip = infoMap.get("fwqip") == null ? "" : (String)infoMap.get("fwqip");
			/*
			 * 如果是托管服务器，就需要使用外网
			 */
			if (SystemConstants.PTLX_TG.equals(infoMap.get("ptlx"))) {
				fwqip = infoMap.get("fwqip_ww") == null ? "" : (String)infoMap.get("fwqip_ww");
			}
			String fwqid = infoMap.get("id") == null ? "" : (String)infoMap.get("id");
			String yyid = infoMap.get("yyid") == null ? "" : (String)infoMap.get("yyid");
			
			Map jqMap = new HashMap();
			if (resultMap.containsKey(jqid)) {
				jqMap = (Map) resultMap.get(jqid);
			}else {
				//设置集群信息
				jqMap.put("fwym", infoMap.get("fwym"));
				jqMap.put("fwdk", infoMap.get("fwdk"));
				jqMap.put("jqname", infoMap.get("jqname"));
			}
			
			/*
			 * 应用信息里面分为应用基本信息和两个map，一个key为fbfwqinfo的，一个key为sfbfwqinfo的
			 */
			Map yyinfoMap = (Map) jqMap.get("yyinfo");	//运行的应用信息
			if (yyinfoMap == null) {
				yyinfoMap = new HashMap();
			}
			
			//应用信息
			Map yyMap = (Map) yyinfoMap.get(yyid);
			
			if (yyMap == null || yyMap.size()==0) {
				yyMap = new HashMap();
				//设置应用信息
				yyMap.put("yyname", infoMap.get("yyname"));
				yyMap.put("warname", infoMap.get("warname"));
			}
			
			//服务器信息
			Map fbfwqMap = (Map)yyMap.get("fbfwqinfo");
			Map sfbfwqMap = (Map)yyMap.get("sfbfwqinfo");
			if (fbfwqMap == null) {
				fbfwqMap = new HashMap();
			}
			if (sfbfwqMap == null) {
				sfbfwqMap = new HashMap();
			}
			
			Map fwqMap = new HashMap();
			fwqMap.put("fwqip", fwqip);
			fwqMap.put("dk", infoMap.get("dk"));
			fwqMap.put("fwqname", infoMap.get("fwqname"));
			fwqMap.put("fwqstatus", infoMap.get("fwqstatus"));
			Object fbzt = infoMap.get("fbzt");
			if (SystemConstants.FBZT_SFB.equals(fbzt)) {
				//试发布
				sfbfwqMap.put(fwqid, fwqMap);
			}else {
				//发布状态
				fbfwqMap.put(fwqid, fwqMap);
			}
			
			//一级一级加入
			yyMap.put("fbfwqinfo", fbfwqMap);
			yyMap.put("sfbfwqinfo", sfbfwqMap);
			yyinfoMap.put(yyid, yyMap);
			jqMap.put("yyinfo", yyinfoMap);
			resultMap.put(jqid, jqMap);
		}
		return resultMap;
	}
	
	/*
	 * 格式化Nginx配置文件信息,格式化为：
	 * 		{
	 * 			jqid:{
	 * 				jqname:
	 *				fwym:
	 *				fwdk:
	 *				yyinfo:{
	 *					yyid:{
	 *						yyname:
	 *						warname:
	 *						fwqinfo:{
	 *							fwqid:{
	 *								fwqip:
	 *								dk:
	 *							},
	 *							fwqid:{
	 *								fwqip:
	 *								dk:
	 *							}
	 *							
	 *						}
	 *					},
	 *					yyid:{
	 *						warname:
	 *						fwqInfo:{
	 *							fwqip:
	 *							dk:
	 *						}
	 *					}
	 *				}
	 * 		 	}
	 * 		}
	 */
	/*
	 * 格式化（页面展示使用）
	 */
	private Map formatNginxConfInfoForShow(List srcData) {
		Map resultMap = new HashMap();
		if (srcData == null || srcData.size()==0) {
			return resultMap;
		}
		for (Map infoMap : (List<Map>)srcData) {
			String jqid = infoMap.get("jqid") == null ? "" : (String)infoMap.get("jqid");
			
			String fwqip = infoMap.get("fwqip") == null ? "" : (String)infoMap.get("fwqip");
			 // 如果是托管服务器，就需要使用外网
			if (SystemConstants.PTLX_TG.equals(infoMap.get("ptlx"))) {
				fwqip = infoMap.get("fwqip_ww") == null ? "" : (String)infoMap.get("fwqip_ww");
			}
			String fwqid = infoMap.get("id") == null ? "" : (String)infoMap.get("id");
			String yyid = infoMap.get("yyid") == null ? "" : (String)infoMap.get("yyid");
			if (resultMap.containsKey(jqid)) {
				//已经有该集群对象
				Map jqMap = (Map) resultMap.get(jqid);
				//判断该应用是否已经存在
				Map yyinfoMap = (Map) jqMap.get("yyinfo");
				if (yyinfoMap.containsKey(yyid)) {
					//该应用已经存在，直接将该服务器的信息放进去
					Map yyMap = (Map) yyinfoMap.get(yyid);
					Map fwqinfoMap = (Map) yyMap.get("fwqinfo");
					Map fwqMap = new HashMap();
					fwqMap.put("fwqip", fwqip);
					fwqMap.put("dk", infoMap.get("dk"));
					fwqMap.put("fwqname", infoMap.get("fwqname"));
					fwqMap.put("fwqstatus", infoMap.get("fwqstatus"));
					fwqMap.put("fbzt", infoMap.get("fbzt"));
					fwqinfoMap.put(fwqid, fwqMap);
				}else {
					//该应用还不存在，所以，创建下面的一套新的，将内容放进去即可
					Map yyMap = new HashMap();
					Map fwqinfoMap = new HashMap();
					Map fwqMap = new HashMap();
					fwqMap.put("fwqip", fwqip);
					fwqMap.put("dk", infoMap.get("dk"));
					fwqMap.put("fwqname", infoMap.get("fwqname"));
					fwqMap.put("fwqstatus", infoMap.get("fwqstatus"));
					fwqMap.put("fbzt", infoMap.get("fbzt"));
					fwqinfoMap.put(fwqid, fwqMap);
					yyMap.put("fwqinfo", fwqinfoMap);
					yyMap.put("yyname", infoMap.get("yyname"));
					yyMap.put("warname", infoMap.get("warname"));
					yyinfoMap.put(yyid, yyMap);
				}
			}else {
				//还没有该集群对象，创建下面的一系列新的，将内容放进去
				Map jqMap = new HashMap();
				Map yyinfoMap = new HashMap();
				Map yyMap = new HashMap();
				Map fwqinfoMap = new HashMap();
				Map fwqMap = new HashMap();
				
				//服务器信息
				fwqMap.put("fwqip", fwqip);
				fwqMap.put("dk", infoMap.get("dk"));
				fwqMap.put("fwqname", infoMap.get("fwqname"));
				fwqMap.put("fwqstatus", infoMap.get("fwqstatus"));
				fwqMap.put("fbzt", infoMap.get("fbzt"));
				fwqinfoMap.put(fwqid, fwqMap);
				yyMap.put("fwqinfo", fwqinfoMap);
				//应用信息
				yyMap.put("warname", infoMap.get("warname"));
				yyMap.put("yyname", infoMap.get("yyname"));
				yyinfoMap.put(yyid, yyMap);
				//集群信息
				jqMap.put("fwym", infoMap.get("fwym"));
				jqMap.put("fwdk", infoMap.get("fwdk"));
				jqMap.put("jqname", infoMap.get("jqname"));
				jqMap.put("yyinfo", yyinfoMap);
				//加入map中
				resultMap.put(infoMap.get("jqid"), jqMap);
			}
		}
		return resultMap;
	}
	
	@Override
	public int deleteNginxJqByNginxId(String nginxid) {
		Map condition = new HashMap();
		condition.put("nginxid", nginxid);
		return daoUtils.execSql("#nginx.deleteNginxJqByNginxId", condition);
	}
	
	@Override
	public int deleteNginxJqByJqid(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		return daoUtils.execSql("#nginx.deleteNginxJqByJqId", condition);
	}
	
	@Override
	public List getNginxListByJqid(String jqid) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("jqid", jqid);
		return daoUtils.find("#nginx.getNginxListByJqid", condition);
	}
	
	private List getNginxListByFwqid(String fwqid) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwqid", fwqid);
		return daoUtils.find("#nginx.getNginxListByFwqid", condition);
	}
	
	@Override
	public List getNginxListByYyid(String yyid) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("yyid", yyid);
		return daoUtils.find("#nginx.getNginxListByYyid", condition);
	}
	
	@Override
	public ResultBean updateNginxConfByFwqid(String fwqid) {
		ResultBean resultBean = null;
		if (!SystemConstants.isNginxUsed) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "系统没有启用Nginx！");
			return resultBean;
		}
		List nginxList = getNginxListByFwqid(fwqid);
		if (nginxList == null || nginxList.size()==0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "该服务器没有对应的Nginx信息！");
			return resultBean;
		}
		String code = SystemConstants.STATUSCODE_OK;
		StringBuffer sb = new StringBuffer();
		for (Map nginxMap : (List<Map>)nginxList) {
			String id = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
			ResultBean beanOne = updateNginxConfByNginxid(id);
			if (SystemConstants.STATUSCODE_FALSE.equals(beanOne.getStatusCode())) {
				//有一个为失败，就返回失败！
				code = SystemConstants.STATUSCODE_FALSE;
				resultBean.setStatusCode(code);
				resultBean.setMsg(beanOne.getMsg());
				return resultBean;
			}
		}
		resultBean = new ResultBean(code, "");
		return resultBean;
	}
	
	@Override
	public ResultBean updateNginxConfByJqid(String jqid) {
		ResultBean resultBean = null;
		if (!SystemConstants.isNginxUsed) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "系统没有启用Nginx！");
			return resultBean;
		}
		List nginxList = getNginxListByJqid(jqid);
		if (nginxList == null || nginxList.size()==0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "该集群没有对应的Nginx信息！");
			return resultBean;
		}
		String code = SystemConstants.STATUSCODE_OK;
		StringBuffer sb = new StringBuffer();
		for (Map nginxMap : (List<Map>)nginxList) {
			String id = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
			ResultBean beanOne = updateNginxConfByNginxid(id);
			if (SystemConstants.STATUSCODE_FALSE.equals(beanOne.getStatusCode())) {
				//有一个为失败，就返回失败！
				code = SystemConstants.STATUSCODE_FALSE;
				resultBean = new ResultBean();
				resultBean.setStatusCode(code);
				resultBean.setMsg(beanOne.getMsg());
				return resultBean;
			}
		}
		resultBean = new ResultBean(code, "更新成功");
		return resultBean;
	}
	
	@Override
	public ResultBean updateNginxConfByYyid(String yyid) {
		ResultBean resultBean = null;
		if (!SystemConstants.isNginxUsed) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "系统没有启用Nginx！");
			return resultBean;
		}
		List nginxList = getNginxListByYyid(yyid);
		if (nginxList == null || nginxList.size()==0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "该应用没有对应的Nginx信息！");
			return resultBean;
		}
		String code = SystemConstants.STATUSCODE_OK;
		StringBuffer sb = new StringBuffer();
		for (Map nginxMap : (List<Map>)nginxList) {
			String id = nginxMap.get("id") == null ? "" : (String) nginxMap.get("id");
			ResultBean beanOne = updateNginxConfByNginxid(id);
			if (SystemConstants.STATUSCODE_FALSE.equals(beanOne.getStatusCode())) {
				//有一个为失败，就返回失败！
//				code = SystemConstants.STATUSCODE_FALSE;
//				sb.append(beanOne.getMsg());
//				sb.append(SystemConstants.LINE_SEPARATER);
				//有一个错误的，就直接返回，就返回该项的错误信息
				code = SystemConstants.STATUSCODE_FALSE;
				resultBean = new ResultBean();
				resultBean.setStatusCode(code);
				resultBean.setMsg(beanOne.getMsg());
				return resultBean;
			}else {
				//成功
			}
		}
		resultBean = new ResultBean(code, "更新成功");
		return resultBean;
	}
	
	/*
	 * 根据Nginxid更新配置
	 * 1、生成本地文件
	 * 2、替换线上文件并reload Nginx
	 */
	@Override
	public ResultBean updateNginxConfByNginxid(String nginxid) {
		ResultBean resultBean = null;
		if (!SystemConstants.isNginxUsed) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "系统没有启用Nginx！");
			return resultBean;
		}
		if (StringUtils.isEmpty(nginxid)) {
			log.error("【重载Nginx配置】nginxid不存在！");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "nginxid不存在！");
			return resultBean;
		}
		
		//获取Nginx的信息
		NginxBean nBean = getNginxBean(nginxid);
		if (nBean == null) {
			log.error("【重载Nginx配置】该nginxid对应的Nginx不存在！");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "该nginxid对应的Nginx不存在！");
			return resultBean;
		}
		
		//1、生成本地文件
		Map map = getJqFwqYyByNginxId(nginxid);
		ResultBean bean = GenerateNginxConfUtils.generateNginxConfByTemplete(map, nginxid);
		if (bean == null) {
			log.error("【重载Nginx配置】生成Nginx本地文件失败，出现未知错误！");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "【"+nBean.getFwqname()+"】生成配置文件出现未知错误！");
			return resultBean;
		}
		if (!SystemConstants.STATUSCODE_OK.equals(bean.getStatusCode())) {
			log.error("【重载Nginx配置】"+bean.getMsg());
			return bean;
		}
		//2、替换线上的并reload Nginx
		String fwqip = nBean.getFwqip();
		String nginxRootPath =nBean.getNginxsrc();
		//判断文件是否存在
		File nginxConfFile = NginxUtils.getNginxConfFile(nginxid);
		if (nginxConfFile == null || !nginxConfFile.exists()) {
			//文件不存在
			log.error("【重载Nginx配置】替换线上失败！原因为【配置文件不存在】");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "【"+nBean.getFwqname()+"】配置文件不存在");
			return resultBean;
		}
		//调用工具类完成配置文件的替换
		//判断服务器是否启动
		String serverInfo = SocketUtils.getServerInfo(fwqip);
		if (StringUtils.isEmpty(serverInfo)) {
			log.error("【重载Nginx配置】替换线上失败！原因为【服务器未启动】");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "【"+nBean.getFwqname()+"】所在服务器未启动");
			return resultBean;
		}
		if (!SocketUtils.replaceNginxConf(fwqip, nginxConfFile.getAbsolutePath(), nginxRootPath)) {
			log.error("【重载Nginx配置】替换线上失败！原因为【nginx替换失败】");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "【"+nBean.getFwqname()+"】替换失败");
			return resultBean;
		}
		log.info("【重载Nginx配置】已经重载线上Nginx名称为【"+nBean.getFwqname()+"】的配置信息！");
		resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "更新成功");
		return resultBean;
	}
	
	@Override
	public List getSfbpzList(String nid) {
		Map condition = new HashMap();
		condition.put("nginxid", nid);
		return daoUtils.find(NginxSfbpzBean.class, condition);
	}
	
	@Override
	public List<ResultBean> saveSfbpzxx(String nid, String[] regexesArr) {
		List<ResultBean> res = new ArrayList<ResultBean>();
		ResultBean resultOKBean = new ResultBean(SystemConstants.STATUSCODE_OK, "成功！", "操作结果：");	//最终操作成功对象
		ResultBean resultERRORBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "操作结果：");	//最终操作失败对象
		if (StringUtils.isEmpty(nid)) {
			throw new RuntimeException("nginxid为空！");
		}
		//先删除所有的试发布配置信息
		deleteSfbpzxxByNid(nid);
		//如果有就批量添加
		if (regexesArr != null && regexesArr.length>0) {
			String[] fields = {"id","nginxid","regex"};
			List data = new ArrayList();
			Map map;
			String id;
			for (String regex : regexesArr) {
				if (!StringUtils.isEmpty(regex)) {
					id = String.valueOf(daoUtils.getNextval("Q_NGINX_SFBPZ"));
					map = new HashMap();
					map.put("id", id);
					map.put("nginxid", nid);
					map.put("regex", regex);
					data.add(map);
				}
			}
			if (data.size()>0) {
				try {
					daoUtils.executeBatchUpdate("#nginx.saveSfbpzxx", fields, data);
				} catch (Exception e) {
					res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, "失败！", "保存试发布配置信息："));
					res.add(resultERRORBean);
					return res;
				}
			}
		}
		
		//执行完成之后，更新Nginx配置
		ResultBean resultBean = updateNginxConfByNginxid(nid);
		if (resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)) {
			//更新失败
			//回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事务
			res.add(new ResultBean(SystemConstants.STATUSCODE_FALSE, resultBean.getMsg(), "更新Nginx配置："));
			res.add(resultERRORBean);
			return res;
		}
		res.add(resultOKBean);
		return res;
	}
	
	@Override
	public int deleteSfbpzxxByNid(String nid) {
		Map condition = new HashMap();
		condition.put("nid", nid);
		return daoUtils.execSql("#nginx.deleteSfbpzxxByNid", condition);
	}
	
	@Override
	public List getListByFwqid(String fwqid) {
		//根据服务器ID获取要更新配置的Nginx集合（应用状态应该为已启动的）
		Map condition = new HashMap();
		condition.put("fwqid", fwqid);
		condition.put("yyzt", SystemConstants.YYZT_YQD);
		return daoUtils.find("#nginx.getListByFwqid", condition); 
	}
	
	@Override
	public List getListByJqid(String jqid) {
		Map condition = new HashMap();
		condition.put("jqid", jqid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#nginx.getListByJqid", condition);
	}
	
	@Override
	public void updateNginxGxzt(String id, String zt, String msg) {
		NginxBean nginxBean = new NginxBean();
		nginxBean.setId(id);
		nginxBean.setGxzt(zt);
		nginxBean.setGxsbyy(msg);
		daoUtils.update(nginxBean, "gxzt,gxsbyy");
	}
	
	@Override
	public List getGxsbList() {
		Map condition = new HashMap();
		condition.put("gxzt", SystemConstants.ZT_FALSE);
		return daoUtils.find("#nginx.getGxsbList", condition);
	}
	
}
