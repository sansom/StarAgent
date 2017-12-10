package com.core.jadlwork.struts.action.fwgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.MBConstant;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwgl.FwManager;
import com.core.jadlwork.business.fwgl.SjjkManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.fwgl.SjjkBean;
import com.core.jadlwork.model.fwgl.SjjkparamBean;
import com.core.jadlwork.model.yygl.YyBean;
/**
 * 数据接口
 * TODO
 * @作者：吴家旭
 * @时间：Aug 27, 2015 2:46:16 PM
 */
public class SjjkAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(SjjkAction.class);

	private SjjkBean sjjkBean;
	private IYyManager yyManager; 
	private SjjkManager sjjkManager; 
	private FwManager fwManager;

	
	/**
	 * 进入接口添加界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String edit()  {
		return "edit";
	}
	
	/**
	 * 进入接口修改界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String get()  {
		String id = request.getParameter("id");
		sjjkBean = sjjkManager.getSjjkBean(id);
		
		YyBean yyBean = yyManager.getYyBean(sjjkBean.getYyid() == null ? "": sjjkBean.getYyid());
		List  paramlist = sjjkManager.getSjjkParamListBySjjkId(sjjkBean.getId());
		if(yyBean != null ){
			request.setAttribute("yyname", yyBean.getYyname());
		}
		request.setAttribute("paramlist", paramlist);
		return "edit";
	}
	/**
	 * 添加数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String save()  {
		sjjkBean.setCjsj(DateUtils.createCurrentDate());
		sjjkBean.setJkzt(MBConstant.FBZT_WFB);
		sjjkBean.setZt(MBConstant.ZT_YX);
		List<SjjkparamBean> paramlist = this.setParamList();
		if(sjjkBean.getLylx().equals(SystemConstants.LYLX_ZXLY)){
			//查询服务部署地址IP
			List<Map> fwqBeanList = yyManager.getFwqListByYyid(sjjkBean.getYyid());
			String ipWildcard = "[ip]";
			String portWildcard = "[port]";
			String realURI = "";
			for (int i = 0; i < fwqBeanList.size(); i++) {
				Map map = fwqBeanList.get(0);
				realURI  = sjjkBean.getUri().replace(ipWildcard, map.get("fwqip").toString());
				realURI  = realURI.replace(portWildcard, "8080");//默认8080
			}
			sjjkBean.setUri(realURI);
		}else if(sjjkBean.getLylx().equals(SystemConstants.LYLX_ZYLY)){
			sjjkBean.setUri(sjjkBean.getUri());
		}
		sjjkManager.saveSjjk(sjjkBean,paramlist);
		return "list";
	}
	
	/**
	 * 设置接口参数
	 * @参数：
	 * @返回值：void
	 */
	private List<SjjkparamBean> setParamList() {
		String[] paramlxArr = request.getParameterValues("paramlx");
		String[] paramArr = request.getParameterValues("param");
		String[] paramnameArr = request.getParameterValues("paramname");
		List <SjjkparamBean> list = new ArrayList<SjjkparamBean>();
		SjjkparamBean bean ;
		if(paramlxArr != null && paramlxArr.length > 0){
			for(int i=0 ;i<paramlxArr.length;i++){
				bean = new SjjkparamBean();
				bean.setParam(paramArr[i]);
				bean.setParamlx(paramlxArr[i]);
				bean.setParamname(paramnameArr[i]);
				list.add(bean);
			}
		}
		
		return list;
	}

	/**
	 * 接口修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String update()  {
		sjjkBean.setZhxgsj(DateUtils.createCurrentDate());
		List<SjjkparamBean> paramlist = this.setParamList();
		if(sjjkBean.getLylx().equals(SystemConstants.LYLX_ZXLY)){
			//查询服务部署地址IP
			List<Map> fwqBeanList = yyManager.getFwqListByYyid(sjjkBean.getYyid());
			String ipWildcard = "[ip]";
			String portWildcard = "[port]";
			String realURI = "";
			for (int i = 0; i < fwqBeanList.size(); i++) {
				Map map = fwqBeanList.get(0);
				realURI  = sjjkBean.getUri().replace(ipWildcard, map.get("fwqip").toString());
				realURI  = realURI.replace(portWildcard, "8080");//默认8080
			}
			sjjkBean.setUri(realURI);
		}else if(sjjkBean.getLylx().equals(SystemConstants.LYLX_ZYLY)){
			sjjkBean.setUri(sjjkBean.getUri());
		}
		sjjkManager.updateJkxx(sjjkBean,paramlist);
		return "list";
	}
	
	/**
	 * 删除数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String remove()  {
		sjjkBean.setZt(SystemConstants.ZT_FALSE);
		sjjkBean.setScsj(DateUtils.createCurrentDate());
		sjjkManager.deleteSjjk(sjjkBean);
		return "list";
	}
	
	/**
	 * 查看数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String view()  {
		String sjjkid = request.getParameter("sjjkid");
		sjjkBean = sjjkManager.getSjjkBean(sjjkid);
		List gmdwList = sjjkManager.getJkgmdwjkList(sjjkid);
		request.setAttribute("gmdwList", gmdwList);
		return "view";
	}
	
	/**
	 * 发布数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String fbSjjk()  {
		String  res = "failed";
		sjjkBean.setJkzt(SystemConstants.FWZT_YQD);
		sjjkBean.setZhxgsj(DateUtils.createCurrentDate());
		int r = sjjkManager.updateSjjkByFields(sjjkBean, "jkzt,zhxgsj");
		SjjkBean _sjjkBean= sjjkManager.getSjjkBean(sjjkBean.getId());
		String jsonStr = (String) sjjkManager.sjjkTb(_sjjkBean);
		Map map = JsonUtil.parserToMap(jsonStr);
		if(r > 0 && Integer.valueOf((String) map.get("result")) > 0){
			sjjkManager.saveSjjkTb(_sjjkBean);
			res = "success";
		}else{
			sjjkBean.setJkzt(MBConstant.FBZT_WFB);
			sjjkManager.updateSjjkByFields(sjjkBean, "jkzt");
		}
		try {
			ResponseUtils.render(response,res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 停止数据接口
	 * @参数：@return
	 * @返回值：String
	 */
	public String tzSjjk()  {
		String  res = "failed";
		sjjkBean.setJkzt(SystemConstants.FWZT_WQD);
		sjjkBean.setZhxgsj(DateUtils.createCurrentDate());
		int r = sjjkManager.updateSjjkByFields(sjjkBean, "jkzt,zhxgsj");
		SjjkBean _sjjkBean= sjjkManager.getSjjkBean(sjjkBean.getId());
		String jsonStr = (String) sjjkManager.sjjkTb(_sjjkBean);
		Map map = JsonUtil.parserToMap(jsonStr);
		if(r > 0 && Integer.valueOf((String) map.get("result")) > 0){
			res = "success";
			sjjkManager.saveSjjkTb(_sjjkBean);
		}else{
			sjjkBean.setJkzt(MBConstant.FBZT_YFB);
			sjjkManager.updateSjjkByFields(sjjkBean, "jkzt");
		}
		try {
			ResponseUtils.render(response,res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验接口ID是否已存在
	 * @return
	 * @author niutongda
	 * @Time 2017-4-11 上午11:09:59 
	 *
	 */
	public String checkJkid()  {
		String  res = "failed";
		SjjkBean _sjjkBean= sjjkManager.getSjjkByJkid(sjjkBean.getJkid());
		if(_sjjkBean == null){
			res = "success";
		}
		try {
			ResponseUtils.render(response,res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public SjjkBean getSjjkBean() {
		return sjjkBean;
	}

	public void setSjjkBean(SjjkBean sjjkBean) {
		this.sjjkBean = sjjkBean;
	}

	public SjjkManager getSjjkManager() {
		return sjjkManager;
	}

	public void setSjjkManager(SjjkManager sjjkManager) {
		this.sjjkManager = sjjkManager;
	}

	public IYyManager getYyManager() {
		return yyManager;
	}

	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}

	public FwManager getFwManager() {
		return fwManager;
	}

	public void setFwManager(FwManager fwManager) {
		this.fwManager = fwManager;
	}


	

	
}
