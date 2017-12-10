package com.core.jadlwork.struts.action.yygl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwgl.FwManager;
import com.core.jadlwork.business.fwgl.SjjkManager;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;
import com.core.jadlwork.utils.SystemUtils;
/**
 * 应用
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 8:09:49 PM
 */
public class YyAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(YyAction.class);

	//注入服务器的manager
	private IYyManager yyManager ; 
	private IFwqManager fwqManager;
	private FwManager fwManager ;
	private SjjkManager sjjkManager ; 
	
	private YyBean yyBean ;
	private YyyxjlBean yyyxjlBean;
	
	 // myFile属性用来封装上传的文件  
    private File myFile;  
    // myFileContentType属性用来封装上传文件的类型  
    private String myFileContentType;  
    // myFileFileName属性用来封装上传文件的文件名  
    private String myFileFileName;  
	

	/**
	 * 进入修改页面
	 * @参数：@return
	 * @返回值：String
	 */
	public String edit()  {	
	
		if (yyBean != null && yyBean.getId() != null && !"".equals(yyBean.getId())) {
			yyBean = (YyBean) yyManager.getYyBean(yyBean.getId());
		}
		return "edit";
	}
	
	/**
	 * 添加应用
	 * @throws Exception 
	 * @参数：@return
	 * @返回值：String
	 */
	public String save() throws Exception  {
		ResultBean resultBean = this.checkYy();
		if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}
		
		int r = yyManager.saveYy(yyBean);
		if(r <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "保存应用出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}
	
	/**
	 * ajax_检测应用
	 * @throws Exception 
	 * @参数：@return
	 * @返回值：String
	 */
	public ResultBean checkYy() {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		if(myFile != null){
			/**
			 * 1、校验应用是否已存在【包名不能一样】
			 */
			resultBean = yyManager.checkYyIsExist(this.myFileFileName,yyBean.getId());
			if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				return resultBean;
			}
			
			/**
			 * 2、上传试发布文件
			 */
			//试发布临时文件夹
			String tempDir = SystemUtils.getAppSfbtempSrc(this.myFileFileName); //+ System.currentTimeMillis();
			
			resultBean = this.uploadWar(tempDir);
			if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				return resultBean;
			}
			
			/**
			 * 3、检测应用版本号
			 */
			resultBean = yyManager.checkYyVersion(tempDir + File.separator +this.myFileFileName);
			
			/**
			 * 4、设置试发布版本、试发布包存储路径
			 */
			if(resultBean != null && resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				yyBean.setWarname(this.myFileFileName);
				yyBean.setWarsrc_sfb(SystemUtils.getAppSfbSrc(this.myFileFileName) + File.separator + this.myFileFileName );
				yyBean.setYyversion_sfb(resultBean.getArg1().toString());
			}
		}
		return resultBean;
		
	}
	
	/**
	 * 修改应用
	 * @throws Exception 
	 * @参数：@return
	 * @返回值：String
	 */
	public String update() throws Exception  {
		ResultBean resultBean = this.checkYy();
		if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}
		
		
		//定义要更新的字段
		String fields = "yyname,zhxgsj,yyLogSrc";
		if(myFile != null){
			fields += ",warname,warsrc_sfb,yyversion_sfb";
		}
		
		int i = yyManager.updateYyByFields(yyBean, fields);
		if(i <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新应用出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}
	
	/**
	 * 删除应用
	 * 
	 * @参数：@return
	 * @返回值：String
	 */
	public String remove()  {
		List jqList = yyManager.getJqListByYyid(yyBean.getId());
		int i = yyManager.deleteYy(yyBean);
		//删除之后，更新Nginx配置
		if (jqList != null && jqList.size()>0) {
			for (Map jqMap : (List<Map>)jqList) {
				String jqid = jqMap.get("id") == null ? "" : (String) jqMap.get("id");
				if (!StringUtils.isEmpty(jqid)) {
					NginxCache.getInstance().changedJqids.add(jqid);
				}
			}
		}
		return this.toListPage();
	}
	
	/**
	 * 获取应用可用服务器
	 * 
	 * @author wujiaxu
	 * @Time 2017-6-7 下午5:33:02
	 */
	public String getKyFwqByYyid(){
		String yyid = request.getParameter("yyid");
		String ptlx = request.getParameter("ptlx");
		List jqList = fwqManager.getKyFwqByYyid(yyid,ptlx);

		try {
			ResponseUtils.render(response, JsonUtil.list2json(jqList));
		} catch (Exception e) {
			logger.error("获取应用可用服务器报错!", e);
		}
		return null;
	}
	
	/**
	 * 增加应用运行服务器
	 * @return: String
	 */
	public String addYyyxfwq(){
		
		String[] fwqids = request.getParameterValues("fwqids");
		String yyid = request.getParameter("yyidfwq");
		int re = yyManager.saveYyxxjlBatch(yyid, fwqids);
		//向Nginx缓存中加入该应用id，让定时器更新Nginx配置
		NginxCache.getInstance().changedYyids.add(yyid);
		return this.toListPage();
	}
	
	/**
	 * 跳转到list界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-6 下午4:48:40
	 */
	private String toListPage() {
		
		if(yyBean != null && yyBean.getPtlx()!= null && yyBean.getPtlx().equals(SystemConstants.PTLX_TG)){
			return "tglist";
		}
		return "list";
	}


	/**
	 * ajax请求，获取应用日志文件路径
	 * @return: void
	 */
	public void isYyLogExist(){
		String appname = request.getParameter("appname");
		String ip = request.getParameter("ip");
		ResultBean resultBean = yyManager.isYyLogExist(appname, ip);
		try {
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("获取应用日志文件路径报错！", e);
		}
	}
	
	/**
	 * 检测应用下是否有服务或数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String isHasFw()  {
		
		String  res = "success"; 
		String yyid = request.getParameter("yyid");
		try {
			List fwlist = fwManager.getFwListByYyid(yyid);
			if(fwlist != null && fwlist.size() > 0){
				res = "fwfailed";
				ResponseUtils.render(response,res);
				return null;
			}
			List sjjklist = sjjkManager.getSjjkListByYyid(yyid);
			if(sjjklist != null && sjjklist.size() > 0){
				res = "sjjkfailed";
				ResponseUtils.render(response,res);
				return null;
			}

				ResponseUtils.render(response,res);
		} catch (Exception e) {
			logger.error("检测应用下是否有服务或数据接口报错！", e);
		}
		return null;
	}
	

	
	

	
	/**
	 * war包上传到服务器
	 * @参数：
	 * @返回值：void
	 */
	private ResultBean uploadWar(String uploadPath) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		if(myFile == null){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "上传的文件不存在！");
			return resultBean;
		}
		
		
		try {
			//1.创建文件夹
			File f = new File(uploadPath);
			if(!f.exists()){
				f.mkdirs();
			}
			
			//2.上传文件
			InputStream is = new FileInputStream(myFile);
			File toFile = new File(uploadPath, this.getMyFileFileName());
			OutputStream os = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int length = 0;

			// 读取myFile文件输出到toFile文件中
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();

			return resultBean;
		} catch (Exception e) {
			logger.error("上传war包失败！", e);
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "WAR包上传出错！");
			return resultBean;
		}
		
	}
	
	public YyBean getYyBean() {
		return yyBean;
	}

	public void setYyBean(YyBean yyBean) {
		this.yyBean = yyBean;
	}
	
	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}

	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	public FwManager getFwManager() {
		return fwManager;
	}

	public void setFwManager(FwManager fwManager) {
		this.fwManager = fwManager;
	}
	public SjjkManager getSjjkManager() {
		return sjjkManager;
	}
	public void setSjjkManager(SjjkManager sjjkManager) {
		this.sjjkManager = sjjkManager;
	}
	public YyyxjlBean getYyyxjlBean() {
		return yyyxjlBean;
	}
	public void setYyyxjlBean(YyyxjlBean yyyxjlBean) {
		this.yyyxjlBean = yyyxjlBean;
	}
	public void setFwqManager(IFwqManager fwqManager) {
		this.fwqManager = fwqManager;
	}
	
}
