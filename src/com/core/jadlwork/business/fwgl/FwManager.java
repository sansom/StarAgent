package com.core.jadlwork.business.fwgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.dbutils.SqlMapUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.fwgl.FwBean;
import com.core.jadlwork.model.fwgl.FwddBean;
import com.core.jadlwork.model.fwgl.FwlogBean;
import com.core.jadlwork.model.fwgl.FwsqBean;
import com.core.jadlwork.model.fwgl.FwylgxBean;
import com.core.jadlwork.model.fwgl.SjjkBean;
import com.core.jadlwork.model.fwgl.WbfwBean;
/**
 * 服务Manager
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 8:10:13 PM
 */
public class FwManager extends BaseManager { 
	
	private static Logger logger = Logger.getLogger(FwManager.class);
	
	/**
	 * 保存服务
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public  int saveFw(FwBean fwBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FW"));
		fwBean.setId(id);
		
		return this.daoUtils.save(fwBean);
	}
	

	/**
	 * 删除服务
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public int deleteFw(FwBean fwBean){
		return this.daoUtils.update(fwBean,"zt,scsj");
	}
	
	/**
	 * 修改服务
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public int updateYyByFields(FwBean fwBean,String fields){
		return this.daoUtils.update(fwBean,fields);
	}
	
	/**
	 * 服务授权访问
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public int saveFwsq(FwsqBean fwsqBean){
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FWSQ"));
		fwsqBean.setId(id);
		return this.daoUtils.save(fwsqBean);
	}
	
	/**
	 * 取消服务授权访问
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public int deleteFwsq(FwsqBean fwsqBean){
		return this.daoUtils.delete(fwsqBean);
	}
	
	/**
	 * 保存服务日志
	 * @参数：@param fwBean
	 * @参数：@return
	 * @返回值：int
	 */
	public synchronized int saveFwLog(FwlogBean fwlogBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FWLOG"));
		fwlogBean.setId(id);
		return this.daoUtils.save(fwlogBean);
	}

	
	/**
	 * 获取服务对象
	 * @param id 
	 * @参数：@return
	 * @返回值：FwBean
	 */
	public FwBean getFwBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (FwBean) this.daoUtils.findObject(FwBean.class, condition);
	}
	
	/**
	 * 获取服务队列bean
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：FwddBean
	 */
	public FwddBean getFwdlBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (FwddBean) this.daoUtils.findObject(FwddBean.class, condition);
	}

	
	/**
	 * 根据ID获取数据接口BEAN
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：SjjkBean
	 */
	public SjjkBean getSjjkBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (SjjkBean) this.daoUtils.findObject(SjjkBean.class, condition);
	}


	/**
	 * 根据应用ID获取服务列表
	 * @param yyid 
	 * @参数：@return
	 * @返回值：List
	 */
	public List getFwListByYyid(String yyid) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return  this.daoUtils.find("#fwgl.getFwListByYyid", condition);
	}


	/**
	 * 剔除服务ID获取服务列表
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：List
	 */
	public List getFwListExceptId(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return  this.daoUtils.find("#fwgl.getFwListExceptId", condition);
	}


	/**
	 * 保存服务依赖关系
	 * @参数：@param list
	 * @返回值：void
	 */
	public void saveFwylgx(List<FwylgxBean> list) {
		insertFwylgx(list);
	}
	
	/**
	 * 服务依赖
	 * @param sjjkBean 
	 * @参数：
	 * @返回值：void
	 */
	private void insertFwylgx(List<FwylgxBean> ylfwlist) {
		String batchsql_save = "insert into T_FWGL_FWYLGX(id,fwid,fwname,prefwid,prefwname) values(?,?,?,?,?)";
		String[] savefield = {"id","fwid","fwname","prefwid","prefwname"};// 待插入值的字段
		List save_data = new ArrayList();// 批量提交保存信息时,用于暂存待插入的值
		Map _save_data = null;// 用于暂存单条待保存值
		
		for(int i=0 ; i<ylfwlist.size() ;i++){
			String addid = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FWYLGX")); 
			String fwid = ylfwlist.get(i).getFwid();//服务ID
			String fwname =  ylfwlist.get(i).getFwname();//服务名称
			String prefwid = ylfwlist.get(i).getPrefwid();//依赖服务ID
			String prefwname = ylfwlist.get(i).getPrefwname();//依赖服务名称
			
			_save_data = new HashMap();
			_save_data.put("id", addid);
			_save_data.put("fwid", fwid);
			_save_data.put("fwname", fwname);
			_save_data.put("prefwid", prefwid);
			_save_data.put("prefwname", prefwname);
			save_data.add(_save_data);
		}
		
		if(save_data.size()>0){
			daoUtils.executeBatchUpdate(batchsql_save, savefield,
					save_data);
		}
		
	}
	
	/**
	 * 保存服务授权
	 * @参数：@param fwsqlist
	 * @返回值：void
	 */
	private void insertFwsq(List fwsqlist) {
		String batchsql_save = "insert into t_fwgl_fwsq(id,fwid,ip,sqfw,zt) values(?,?,?,?,?)";
		String[] savefield = {"id","fwid","ip","sqfw","zt"};// 待插入值的字段
		List save_data = new ArrayList();// 批量提交保存信息时,用于暂存待插入的值
		Map _save_data = null;// 用于暂存单条待保存值
		
		for(int i=0 ; i<fwsqlist.size() ;i++){
			Map map = (Map) fwsqlist.get(i);
			String addid = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FWSQ")); 
			String fwid = (String) map.get("fwid");//服务ID
			String ip =  (String) map.get("ip");//ip
			String sqfw = (String) map.get("sqfw");//授权类型
			String zt = (String) map.get("zt");//状态
			
			_save_data = new HashMap();
			_save_data.put("id", addid);
			_save_data.put("fwid", fwid);
			_save_data.put("ip", ip);
			_save_data.put("sqfw", sqfw);
			_save_data.put("zt", zt);
			save_data.add(_save_data);
		}
		
		if(save_data.size()>0){
			daoUtils.executeBatchUpdate(batchsql_save, savefield,
					save_data);
		}
		
	}


	/**
	 * 服务依赖关系变更
	 * @param fwBean 
	 * @参数：@param ylgxlist
	 * @返回值：void
	 */
	public void updateFwylgx(FwBean fwBean, List<FwylgxBean> ylgxlist) {
		this.removeFeylgx(fwBean.getFwid());
		this.insertFwylgx(ylgxlist);
	}
	
	/**
	 * 服务授权变更
	 * @参数：@param fwBean
	 * @参数：@param fwsqlist
	 * @返回值：void
	 */
	public void updateFwsq(FwBean fwBean, List<FwsqBean> fwsqlist) {
		this.removeFwsq(fwBean.getFwid());
		this.insertFwsq(fwsqlist);
		
		
	}
	
	


	/**
	 * 根据服务ID删除服务授权
	 * @参数：@param fwid
	 * @返回值：void
	 */
	private void removeFwsq(String fwid) {
		Map map = new HashMap();
		map.put("fwid", fwid);
		this.daoUtils.execSql("#fwgl.removeFwsq",map);
		
	}


	/**
	 * 根据服务ID删除服务依赖关系
	 * @参数：@param jkid
	 * @返回值：void
	 */
	private void removeFeylgx(String fwid) {
		Map map = new HashMap();
		map.put("fwid", fwid);
		this.daoUtils.execSql("#fwgl.removeFeylgx",map);
	}


	/**
	 * 根据服务ID获取服务依赖关系
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：List
	 */
	public List getFwgxListFwid(String fwid) {
		Map condition = new HashMap();
		condition.put("fwid", fwid);
		return  this.daoUtils.find("#fwgl.getFwgxListFwid", condition);
	}


	/**
	 * 获取所有服务
	 * @参数：@return
	 * @返回值：List
	 */
	public List getAllFwList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWZT_YQD);
		return  this.daoUtils.find("#fwgl.getAllFwList", condition);
	}


	/**
	 * 获取所有服务依赖关系
	 * @参数：@return
	 * @返回值：List
	 */
	public List getAllFwylgxList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWZT_YQD);
		return  this.daoUtils.find("#fwgl.getAllFwylgxList", condition);
	}


	

	/**
	 * 根据服务ID获取服务Map
	 * @参数：@param fwid
	 * @参数：@return
	 * @返回值：String
	 */
	public Map getFwBeanByFwid(String fwid) {
		Map condition = new HashMap();
		condition.put("fwid", fwid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWZT_YQD);
		List list = this.daoUtils.find("#fwgl.getFwBeanByFwid", condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
		
		
	}
	
	
	/**
	 * 根据服务ID获取外部服务
	 * @参数：@param fwid
	 * @参数：@return
	 * @返回值：String
	 */
	public Map getWbfwBeanByFwid(String fwid) {
		Map condition = new HashMap();
		condition.put("fwid", fwid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWSQZT_YX);
		List list = this.daoUtils.find("#fwgl.getWbfwBeanByFwid", condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
		
		
	}
	/**
	 * 根据服务URL获取服务Map
	 * @参数：@param fwid
	 * @参数：@return
	 * @返回值：String
	 */
	public Map getFwBeanByUrl(String url) {
		Map condition = new HashMap();
		condition.put("uri", url);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWZT_YQD);
		List list = this.daoUtils.find("#fwgl.getFwBeanByUri", condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
		
		
	}


	/**
	 * 获取外部服务bean
	 * @参数：@param id
	 * @参数：@return
	 * @返回值：WbfwBean
	 */
	public WbfwBean getWbfwBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (WbfwBean) this.daoUtils.findObject(WbfwBean.class, condition);
	}


	/**
	 * 根据字段更新外部服务
	 * @参数：@param wbfwBean
	 * @参数：@param string
	 * @返回值：void
	 */
	public int updateWbfwByFields(WbfwBean wbfwBean, String fields) {
		return this.daoUtils.update(wbfwBean,fields);
		
	}

	

	/**
	 * 根据服务ID获取授权列表
	 * @参数：@param fwid
	 * @参数：@return
	 * @返回值：List
	 */
	public List getFwsqListByFwid(String fwid) {
		Map condition = new HashMap();
		condition.put("fwid", fwid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return  this.daoUtils.find("#fwgl.getFwsqListByFwid", condition);
	}


	/**
	 * 获取所有外部服务
	 * @参数：@return
	 * @返回值：List
	 */
	public List getAllWbfwList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWSQZT_YX);
		return  this.daoUtils.find("#fwgl.getAllWbfwList", condition);
	}


	/**
	 * 内部服务访问统计
	 * @参数：@return
	 * @返回值：List
	 */
	public List getNbFwTj() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWZT_YQD);
		condition.put("fwly", SystemConstants.LYLX_ZXLY);
		return  this.daoUtils.find("#fwgl.getNbFwTj", condition);
	}


	/**
	 * 根据条数和服务来源获取内部服务列表
	 * @param fwid 
	 * @param i 
	 * @参数：@return
	 * @返回值：List
	 */
	public List getFwListByTopNumAndFwly(int topNum,String fwly, String fwid) {
		Map condition = new HashMap();
		String fwlyCond = " and fwly = '"+fwly+"'";
		String topNumCond = " and rowNum <= "+topNum;
		String fwidCond = " and fwid = '"+fwid+"'";
		condition.put("fwly", fwly);
		condition.put("topNum", topNum);
		String sql = SqlMapUtils.getSql("select", "#fwgl.getFwListByTopNumAndFwly");
		if(fwid != null && !fwid.equals("")){
			topNumCond = "";
		}else{
			fwidCond = "";
		}
		
		return this.daoUtils.find(sql.replaceAll(":fwly", fwlyCond).replaceAll(":topNum", topNumCond).replaceAll(":fwid", fwidCond), new HashMap());
		
	}


	/**
	 * 获取外部服务统计
	 * @param i 
	 * @参数：@return
	 * @返回值：List
	 */
	public List getWbFwTj(int topNum) {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWSQZT_YX);
		condition.put("fwly", SystemConstants.LYLX_ZYLY);
		condition.put("topNum", topNum);
		return  this.daoUtils.find("#fwgl.getWbFwTj", condition);
	}


	/**
	 * 根据单位代码获取接入节点列表
	 * @参数：@return
	 * @返回值：List
	 */
	public List getWbfwListByDwdm(String dwdm) {
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("fwzt", SystemConstants.FWSQZT_YX);
		return  this.daoUtils.find("#fwgl.getWbfwListByDwdm", condition);
	}



	/**
	 * 更新外部服务连接状态
	 * @参数：
	 * @param list 
	 * @返回值：void
	 */
	public void updateFwljzt(List list) {
		if(list != null && list.size() > 0 ){
			String batchsql_update = "update  t_fwgl_wbfw set fwljzt = ? where id = ?";
			String[] updatefield = {"fwljzt","id"};// 待更新值的字段
			List update_data = new ArrayList();// 批量提交修改信息时,用于暂存待修改的值
			Map _update_data = null;// 用于暂存单条待修改值
			for(int i = 0; i < list.size() ; i++){
				Map map = (Map) list.get(i);
				String fwljzt = (String) map.get("fwljzt");//服务连接状态
				String id =  (String) map.get("id");//ip
				Object isChange =  map.get("isChange");//isChange
				if(isChange != null && isChange.equals("0")){
					_update_data = new HashMap();
					_update_data.put("id", id);
					_update_data.put("fwljzt", fwljzt);
						
					update_data.add(_update_data);
				}
			}
				
			if(update_data.size()>0){
				daoUtils.executeBatchUpdate(batchsql_update, updatefield,
						update_data);
			}
		}
	}

	/**
	 * 保存服务队列
	 * @参数：@param fwddBean
	 * @返回值：void
	 */
	public int saveFwdd(FwddBean fwddBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_FWGL_FWDD"));
		fwddBean.setId(id);
		return this.daoUtils.save(fwddBean);
	}


	/**
	 * 根据字段更新服务队列
	 * @参数：@param wbfwBean
	 * @参数：@param string
	 * @返回值：void
	 */
	public int updateFwdlByFields(FwddBean FwddBean, String fields) {
		return this.daoUtils.update(FwddBean,fields);
		
	}


	

	
}
