package com.core.jadlwork.business.fwgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.dbutils.DicMapUtils;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.MBConstant;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.fwgl.SjjkBean;
import com.core.jadlwork.model.fwgl.SjjkTbLsBean;
import com.core.jadlwork.model.fwgl.SjjkparamBean;
/**
 * 数据接口Manager
 * TODO
 * @作者：吴家旭
 * @时间：Aug 27, 2015 2:53:25 PM
 */
public class SjjkManager extends BaseManager { 
	
	private static Logger logger = Logger.getLogger(SjjkManager.class);
	
	/**
	 * 保存数据接口
	 * @param paramlist 
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public  void saveSjjk(SjjkBean sjjkBean, List<SjjkparamBean> paramlist) {
		
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWGL_SJJK"));
		sjjkBean.setId(id);
		int b =  this.daoUtils.save(sjjkBean);
		if(b > 0){
			this.insertJkParam(sjjkBean,paramlist);
		}
	}
	

	/**
	 * 删除数据接口
	 * @参数：@param sjjkBean
	 * @参数：@return
	 * @返回值：int
	 */
	public int deleteSjjk(SjjkBean sjjkBean){
		int b =  this.daoUtils.update(sjjkBean,"zt,scsj");
		if(b > 0){
			removeJkparam(sjjkBean.getId());
		}
		return b;
	}
	
	/**
	 * 修改数据接口
	 * @参数：@param sjjkBean
	 * @参数：@param fields
	 * @参数：@return
	 * @返回值：int
	 */
	public int updateSjjkByFields(SjjkBean sjjkBean,String fields){
		return this.daoUtils.update(sjjkBean,fields);
	}
	
	/**
	 * 查询数据接口详情
	 * @参数：@param sjjkid
	 * @参数：@return
	 * @返回值：SjjkBean
	 */
	public SjjkBean getSjjkBean(String  sjjkid){
		Map condition = new HashMap();
		condition.put("id", sjjkid);
		SjjkBean sjjkBean = (SjjkBean) daoUtils.findObject(SjjkBean.class, condition);
		 return sjjkBean;
	}
	
	/**
	 * 获取接口购买单位列表
	 * @参数：@param fwsqBean
	 * @参数：@return
	 * @返回值：List
	 */
	public List getJkgmdwjkList(String  sjjkid){
		Map condition = new HashMap();
		condition.put("id", sjjkid);
		return this.daoUtils.find("",condition);
	}

	/**
	 * 根据接口ID获取接口参数
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：List
	 */
	public List getSjjkParamListBySjjkId(String id) {
		Map condition = new HashMap();
		condition.put("jkid", id);
		return this.daoUtils.find("#fwgl.getSjjkParamListBySjjkId", condition);
	}


	/**
	 * 接口修改
	 * @参数：@param sjjkBean
	 * @参数：@param paramlist
	 * @返回值：void
	 */
	public void updateJkxx(SjjkBean sjjkBean, List<SjjkparamBean> paramlist) {
		String fields = "zhxgsj,uri,jkname,yyid,yyname,ffname,jklx,jksm,fwfl";
		int b = this.updateSjjkByFields(sjjkBean, fields);
		
		if(b > 0){
			this.removeJkparam(sjjkBean.getJkid());
			
			this.insertJkParam(sjjkBean,paramlist);
		}
	}


	/**
	 * 根据接口ID删除接口参数
	 * @参数：@param jkid
	 * @返回值：void
	 */
	private void removeJkparam(String jkid) {
		Map map = new HashMap();
		map.put("jkid", jkid);
		this.daoUtils.execSql("#fwgl.deleteSjjkParamByJkid",map);
	}


	/**
	 * 添加接口参数
	 * @param sjjkBean 
	 * @参数：
	 * @返回值：void
	 */
	private void insertJkParam(SjjkBean sjjkBean, List<SjjkparamBean> paramlist) {
		String sql = "delete from T_FWGL_SJJKPARAM where jkid='"+sjjkBean.getId()+"'"; 
		daoUtils.execSql(sql);
		String batchsql_save = "insert into T_FWGL_SJJKPARAM(id,jkid,param,paramname,paramlx) values(?,?,?,?,?)";
		String[] savefield = {"id","jkid","param","paramname","paramlx"};// 待插入值的字段
		List save_data = new ArrayList();// 批量提交保存信息时,用于暂存待插入的值
		Map _save_data = null;// 用于暂存单条待保存值
		
		for(int i=0 ; i<paramlist.size() ;i++){
			String paramid = String.valueOf(this.daoUtils.getNextval("Q_FWGL_SJJKPARAM")); 
			String jkid = sjjkBean.getId();//接口ID
			String param = paramlist.get(i).getParam();//参数
			String paramname = paramlist.get(i).getParamname();//参数中文名
			String paramlx = paramlist.get(i).getParamlx();//参数类型
			
			_save_data = new HashMap();
			_save_data.put("id", paramid);
			_save_data.put("jkid", jkid);
			_save_data.put("param", param);
			_save_data.put("paramname", paramname);
			_save_data.put("paramlx", paramlx);
			save_data.add(_save_data);
		}
		
		if(save_data.size()>0){
			daoUtils.executeBatchUpdate(batchsql_save, savefield,
					save_data);
		}
		
	}


	/**
	 * 根据应用ID获取数据接口列表
	 * @参数：@param yyid
	 * @参数：@return
	 * @返回值：List
	 */
	public List getSjjkListByYyid(String yyid) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return  this.daoUtils.find("#fwgl.getSjjkListByYyid", condition);
	}


	/**
	 * 数据接口信息同步到云平台
	 * @param sjjkBean
	 * @return
	 * @author niutongda
	 * @Time 2016-12-29 下午01:58:49 
	 *
	 */
	public Object sjjkTb(SjjkBean sjjkBean) {
		Map map = new HashMap();
		map.put("jkid", sjjkBean.getJkid());
		map.put("jkname", sjjkBean.getJkname());
		map.put("yyname", sjjkBean.getYyname());
		map.put("url", sjjkBean.getUri());
		map.put("jklx", sjjkBean.getJklx());
		map.put("jkzt", sjjkBean.getJkzt());
		map.put("cjsj", DateUtils.tranDate2String(sjjkBean.getCjsj()));
		map.put("jksm", sjjkBean.getJksm());
		map.put("ffname", sjjkBean.getFfname());
		map.put("fwfl", sjjkBean.getFwfl());
		List<String> list = getSjjkParamListBySjjkId(sjjkBean.getId());
		Map map_param = new HashMap();
		
		map_param.put("params", list);
		map.put("paramlist", map_param);
		String jsonStr = JsonUtil.map2json(map);
		String url = com.core.jadlsoft.utils.SysConfigUtils.getProperty("mbcp_url");
		Service  service = new Service();
		Call call = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			call.setTimeout(new Integer(60 * 1000));// 超时设定30秒抛出异常
			call.setOperationName("sjjkTb");// 调用登录方法
			call.addParameter("jsonStr", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
			return call.invoke(new Object[]{jsonStr});// 调用服务并返回存在的对应数据
		} catch (Exception e) {
			logger.error("调用webservice出错！url：" + url , e);
			//网络不通或webservice服务有问题返回-1
			return "{'result':'-1'}";
		}
	}


	/**
	 * 保存数据接口同步记录信息
	 * @param sjjkBean
	 * @author niutongda
	 * @Time 2016-12-30 上午10:23:18 
	 *
	 */
	public void saveSjjkTb(SjjkBean sjjkBean) {
		SjjkTbLsBean sjjkTbLsBean = new SjjkTbLsBean();
		sjjkTbLsBean.setId(daoUtils.getNextval("Q_FWGL_SJJKTBLS")+"");
		sjjkTbLsBean.setJkid(sjjkBean.getJkid());
		sjjkTbLsBean.setJklx(sjjkBean.getJklx());
		sjjkTbLsBean.setJkname(sjjkBean.getJkname());
		sjjkTbLsBean.setJksm(sjjkBean.getJksm());
		sjjkTbLsBean.setJkzt(sjjkBean.getJkzt());
		sjjkTbLsBean.setUri(sjjkBean.getUri());
		sjjkTbLsBean.setYyid(sjjkBean.getYyid());
		sjjkTbLsBean.setYyname(sjjkBean.getYyname());
		sjjkTbLsBean.setCjsj(sjjkBean.getCjsj());
		sjjkTbLsBean.setCjsj(sjjkBean.getCjsj());
		sjjkTbLsBean.setFfname(sjjkBean.getFfname() == null ? "": sjjkBean.getFfname());
		sjjkTbLsBean.setZt("0");
		sjjkTbLsBean.setFwfl(sjjkBean.getFwfl());
		daoUtils.save(sjjkTbLsBean);
	}

	/**
	 *  根据接口ID查询接口信息 
	 * @param fwid
	 * @author niutongda
	 * @Time 2017-1-3 下午05:09:59 
	 *
	 */
	public SjjkBean getSjjkByJkid(String fwid) {
		Map condition =  new HashMap();
		condition.put("jkid", fwid);
		return (SjjkBean) daoUtils.findObjectCompatibleNull(SjjkBean.class, condition);
	}

	
	
	
}
