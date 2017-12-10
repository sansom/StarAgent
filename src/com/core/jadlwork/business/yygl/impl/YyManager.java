package com.core.jadlwork.business.yygl.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.CreateID;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SpringBeanFactory;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.fwqgl.impl.NginxManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;
import com.core.jadlwork.utils.FileUtils;
import com.core.jadlwork.utils.SocketClient;
import com.core.jadlwork.utils.SocketUtils;
import com.core.jadlwork.utils.SystemUtils;
import com.core.jadlwork.utils.WarUtils;
import com.core.jadlwork.utils.YYUtils;

/**
 * 应用管理的实现类
 * 
 * @类名: YyManager
 * @描述: 完成应用业务功能的操作
 * @作者: 李春晓
 * @时间: 2017-1-10 下午1:33:53
 */
public class YyManager extends BaseManager implements IYyManager {

	private static Logger log = Logger.getLogger(YyManager.class);
	//注入服务器manager
	private IFwqManager fwqManager;
	public void setFwqManager(IFwqManager fwqManager) {
		this.fwqManager = fwqManager;
	}

	@Override
	public ResultBean checkYyIsExist(String warname, String yyid) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		List yylist = this.getYyListByName(warname, yyid);
		if (yylist != null && yylist.size() > 0) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "该应用已存在！");
		}
		return resultBean;
	}

	/**
	 * 根据应用名称、war包名称获取应用列表
	 * 
	 * @param yyname
	 * @param warname
	 * @param yyid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-7 上午11:44:38
	 */
	private List getYyListByName(String warname, String yyid) {
		Map condition = new HashMap();
		
		condition.put("warname", (warname == null ? "" : warname));
		condition.put("id", (yyid == null || "".equals(yyid)) ? "isNull" : yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getYyListByName", condition);
	}

	@Override
	public int saveYy(YyBean yyBean) {
		
		String id = String.valueOf(this.daoUtils.getNextval("Q_YYGL_YY"));
		yyBean.setId(id);
		yyBean.setCjsj(DateUtils.createCurrentDate());
		yyBean.setZt(SystemConstants.ZT_TRUE);
		int re =  this.daoUtils.save(yyBean);
		if(re > 0 && yyBean.getYyversion_sfb() != null && yyBean.getWarsrc_sfb() != null){
			boolean b = this.uploadSfbWar(yyBean);
			if(!b){
				re = 0;
				throw new RuntimeException("获取试发布WAR包出错!");
			}
		}
		return re;
		
	}

	@Override
	public int updateYyByFields(YyBean yyBean, String fields) {
		yyBean.setZhxgsj(DateUtils.createCurrentDate());
		int re = this.daoUtils.update(yyBean, fields);
		if(re > 0 && yyBean.getYyversion_sfb() != null && yyBean.getWarsrc_sfb() != null){
			boolean b = this.uploadSfbWar(yyBean);
			if(!b){
				re = 0;
				throw new RuntimeException("获取试发布WAR包出错!");
			}
		}
		return re;
	}
	
	/**
	 * 上传试发布包【从临时文件夹获取】
	 * 
	 * @author wujiaxu
	 * @param yyBean 
	 * @return 
	 * @Time 2017-9-14 上午9:51:43
	 */
	private boolean uploadSfbWar(YyBean yyBean) {
		if(yyBean.getWarsrc_sfb() == null){
			log.info("未找到试发布包临时存储地址！");
			return false;
		}
		String targetFile =  yyBean.getWarsrc_sfb() ;//试发布包存储地址
		String tempFile = SystemUtils.getAppSfbtempSrc(yyBean.getWarname()) +File.separator  + yyBean.getWarname();;//试发布包临时存储地址
		File temp = new File(tempFile);
		if (!temp.exists()) {
			log.info("未找到试发布临时文件！");
			return false;
		}
		FileUtils.moveFile(tempFile, targetFile, "create");
		return true;
	}


	@Override
	public int saveYyyxjl(YyyxjlBean yyyxjlBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_YYGL_YYYXJL"));
		yyyxjlBean.setId(id);
		return this.daoUtils.save(yyyxjlBean);
	}


	private List yyfwq_insert_data = new ArrayList();
	private Map _yyfwq_insert_data;
	private String yyfwq_insert_sql = "insert into t_yygl_yyyxjl"
			+ "(id, yyid, jqid,fwqid, yyzt) " + "values (?, ?, ?, ?, ?) ";
	private String[] yyfwq_insert_fields = { "id", "yyid","jqid", "fwqid", "yyzt" };

	@Override
	public int saveYyxxjlBatch(String yyid, String[] fwqids) {
		int result = 1;
		if (yyid == null || "".equals(yyid) || fwqids == null
				|| fwqids.length <= 0) {
			return 0;
		}
		try {
			String maxId = "";
			for (String fwqid : fwqids) {
				_yyfwq_insert_data = new HashMap();
				_yyfwq_insert_data.put(
						"id",
						maxId = (maxId == "" ? this.getMaxId(
								YyyxjlBean.db_tablename, "to_number("
										+ YyyxjlBean.db_tablepkfields + ")")
								+ "" : CreateID.getNextID(maxId)));
				String[] fwqxx = fwqid.split("@");
				_yyfwq_insert_data.put("yyid", yyid);
				_yyfwq_insert_data.put("jqid", fwqxx[0]);
				_yyfwq_insert_data.put("fwqid", fwqxx[1]);
				_yyfwq_insert_data.put("yyzt", SystemConstants.YYZT_WQD);
				yyfwq_insert_data.add(_yyfwq_insert_data);
			}

			if (yyfwq_insert_data.size() > 0) {
				daoUtils.executeBatchUpdate(yyfwq_insert_sql,
						yyfwq_insert_fields, yyfwq_insert_data);
			}
				
		} catch (Exception e) {
			log.error("批量保存应用运行服务器出错!", e);
			return 0;
		}
		return result;
	}

	@Override
	public int deleteYy(YyBean yyBean) {
		yyBean = this.getYyBean(yyBean.getId());
		
		//1.修改应用状态
		yyBean.setScsj(DateUtils.createCurrentDate());
		yyBean.setZt(SystemConstants.ZT_FALSE);
		int re = this.daoUtils.update(yyBean, "scsj,zt");
		if(re > 0){
			
			//2.删除应用运行记录
			this.deleteYyyxjlByYyid(yyBean.getId());
			
			//3.删除备份war包
			FileUtils.delFile(yyBean.getWarsrc());
		}
		
		return re;
	}

	

	/**
	 * 根据应用ID删除运行记录
	 * @param id
	 * @author wujiaxu
	 * @Time 2017-6-15 上午10:02:52
	 */
	private int deleteYyyxjlByYyid(String id) {
		if(id == null || "".equals(id)){
			return 0;
		}
		Map condition = new HashMap();
		condition.put(SystemConstants.DB_TABLENAME, YyyxjlBean.db_tablename);
		condition.put("yyid", id);
	
		return this.daoUtils.delete(condition);
		
	}
	public int removeAppYxztByYyidAndIp(String yyid, String fwqip) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("fwqip", fwqip);
		return this.daoUtils.execSql("#yygl.removeAppYxztByYyidAndIp", condition);
	}
	public void updateAllAppYxzt() {
		String appSql = "update t_yygl_yy set yyzt='"+SystemConstants.APPZT_WQD+"' where zt = '"+SystemConstants.ZT_TRUE+"'";
		String appYxztSql = "delete from t_yygl_yyyxjl";
		this.daoUtils.execSql(appSql);
		this.daoUtils.execSql(appYxztSql);
	}
	@Override
	public YyBean getYyBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (YyBean) this.daoUtils.findObjectCompatibleNull(YyBean.class, condition);
	}
	public List getYyListOnline() {
		String sql = "select t.id,t.yyname,t.warname,t.warsrc,(select to_char(wmsys.wm_concat(c.fwqip)) from (select distinct yyid,fwqip from t_yygl_yyyxjl a  ) c where c.yyid = t.id) fwqipstr from t_yygl_yy t where t.zt = '0' and t.yyzt = '0' order by cjsj";
		return this.daoUtils.find(sql, new HashMap());
	}
	public YyBean getYyByAppname(String appname) {
		appname = appname != null && appname.contains(".war") ? appname
				: appname + ".war";
		Map condition = new HashMap();
		condition.put("warname", appname);
		condition.put("zt", SystemConstants.ZT_TRUE);
		YyBean yyBean = (YyBean) daoUtils.findObject(YyBean.class, condition);
		return yyBean;
	}

	@Override
	public List getFwqListByYyid(String yyid) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		List<Map> maps = daoUtils.find("#yygl.getFwqListByYyid", condition);
		return maps;
	}

	@Override
	public List getYyFwqsList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getYyFwqsList", condition);
	}

	@Override
	public ResultBean isYyLogExist(String appname, String ip) {
		// 设置默认的返回结果
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,
				"success");
		
		//判断服务器是否启动
		List list = fwqManager.getFwqListByFwqIp(ip);
		if (list != null && list.size()>0) {
			Map fwqMap = (Map) list.get(0);
			if (SystemConstants.FWQSTATUS_YC.equals(fwqMap.get("fwqstatus"))) {
				//服务器异常
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,
						"服务器连接异常!");
				return resultBean;
			}
		}else {
			//服务器不存在
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,
					"指定的服务器不存在!");
			return resultBean;
		}
		// 通过应用名称获取应用对象
		YyBean yyBean = (YyBean) getYyByAppname(appname);
		// 获取日志文件路径
		String logSrc = yyBean.getYyLogSrc();
		if (logSrc == null || "".equals(logSrc)) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,
					"该应用未定义日志目录");
			return resultBean;
		}
		
		// 获取返回信息，不存在未false，存在的话返回的是success
		String yyLogInfo = SocketUtils.getAppLogPath(ip,logSrc,appname);
		if ("false".equals(yyLogInfo)) {
			// 不存在
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE,
					"该日志文件不存在");
		}
		// 成功的话，返回日志文件的绝对路径
		resultBean.setArg1(yyLogInfo);
		return resultBean;
	}

	@Override
	public int updateYyyxjlByFields(YyyxjlBean yyyxjlBean, String fields) {

		return this.daoUtils.update(yyyxjlBean, fields);
	}

	@Override
	public YyyxjlBean getYyyxjlBean(String yxjlid) {
		Map condition = new HashMap();
		condition.put("id", yxjlid);
		return (YyyxjlBean) this.daoUtils.findObject(YyyxjlBean.class,
				condition);
	}

	@Override
	public int deleteYyyxjl(YyyxjlBean yyyxjlBean) {
		return this.daoUtils.delete(yyyxjlBean);
	}

	@Override
	public List getYyInfoListByFwqid(String fwqid) {
		Map condition = new HashMap();
		condition.put("fwqid", fwqid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getYyInfoListByFwqid", condition);
	}
	
	@Override
	public List getYyyxjlInfo() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getYyyxjlInfo", condition);
	}

	@Override
	public List getYyInfoListByYyid(String yyid) {
		Map condition = new HashMap();
		condition.put("yyid", yyid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getYyInfoListByYyid", condition);
	}
	
	@Override
	public List getJqListByYyid(String id) {
		Map condition = new HashMap();
		condition.put("yyid", id);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#yygl.getJqListByYyid", condition);
	}

	@Override
	public ResultBean checkYyVersion(String filepath) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
		
		/**
		 * 1、解压war包
		 */
		String rootPath = WarUtils.unzip(filepath, null, true);
		if(rootPath == null || "".equals(rootPath)){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "WAR包解压异常！");
			return resultBean;
		}
		
		/**
		 * 2、获取应用版本号
		 */
		String version = YYUtils.getYyVersion(rootPath);
		if(version == null || "".equals(version)){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "未获取到版本号，请检查META-INF/MANIFEST.MF文件！");
			return resultBean;
		}

		/**
		 * 3、删除解压文件
		 */
	
		File file = new File(rootPath);
		FileUtils.delete(file);

		
		resultBean.setArg1(version);
		return resultBean;
	}

}
