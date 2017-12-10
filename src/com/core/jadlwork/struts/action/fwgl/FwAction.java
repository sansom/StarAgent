package com.core.jadlwork.struts.action.fwgl;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.model.fwgl.FwBean;
import com.core.jadlwork.model.fwgl.FwddBean;
import com.core.jadlwork.model.fwgl.FwsqBean;
import com.core.jadlwork.model.fwgl.FwylgxBean;
import com.core.jadlwork.model.fwgl.WbfwBean;
import com.core.jadlwork.model.yygl.YyBean;
/**
 * 服务
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 8:09:49 PM
 */
public class FwAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(FwAction.class);

	private FwBean fwBean;
	private FwddBean fwddBean;
	private FwsqBean fwsqBean;
	private WbfwBean wbfwBean;
	private FwManager fwManager; 
	private IYyManager yyManager; 
	
	
	/**
	 * 进入服务添加界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String edit()  {
		return "edit";
	}
	
	
	
	/**
	 * 进入服务修改界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String get()  {
		String id = request.getParameter("id");
		fwBean = fwManager.getFwBean(id);
		YyBean yyBean = yyManager.getYyBean(fwBean.getYyid());
		request.setAttribute("yyname", yyBean.getYyname());
		
		return "edit";
	}
	
	/**
	 * 进入服务关系编辑页面
	 * @参数：@return
	 * @返回值：String
	 */
	public String gxedit()  {
		String id = request.getParameter("id");
		fwBean = fwManager.getFwBean(id);
		List fwList = fwManager.getFwListExceptId(id);
		List fwgxlist = fwManager.getFwgxListFwid(fwBean.getFwid());
		request.setAttribute("fwList", fwList);
		request.setAttribute("fwgxlist", fwgxlist);
		return "gxedit";
	}
	
	
	/**
	 * 进入服务授权界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String fwsq()  {
		String id = request.getParameter("id");
		fwBean = fwManager.getFwBean(id);
		List fwsqList = fwManager.getFwsqListByFwid(fwBean.getFwid());
		request.setAttribute("fwsqList", fwsqList);
		return "sq";
	}
	
	/**
	 * 添加服务
	 * @参数：@return
	 * @返回值：String
	 */
	public String save()  {
		fwBean.setUri(fwBean.getUri());
		fwBean.setCjsj(DateUtils.createCurrentDate());
		fwBean.setFwzt(MBConstant.FBZT_WFB);
		fwBean.setLylx(MBConstant.LYLX_ZXLY);
		fwBean.setZt(MBConstant.ZT_YX);
		fwManager.saveFw(fwBean);
		return "list";
	}
	
	/**
	 * 服务修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String update()  {
		fwBean.setLylx(MBConstant.LYLX_ZXLY);
		fwBean.setUri(fwBean.getUri());
		fwBean.setZhxgsj(DateUtils.createCurrentDate());
		String fields = "zhxgsj,uri,fwname,yyid,fwfl,fwsr,fwsc,lylx,price,priceunit,fwms,sort";
		fwManager.updateYyByFields(fwBean, fields);
		return "list";
	}
	
	/**
	 * 删除服务
	 * @参数：@return
	 * @返回值：String
	 */
	public String remove()  {
		fwBean.setZt(SystemConstants.ZT_FALSE);
		fwBean.setScsj(DateUtils.createCurrentDate());
		fwManager.deleteFw(fwBean);
		return "list";
	}
	
	/**
	 * 启动服务
	 * @参数：@return 
	 * @返回值：String
	 */
	public String qdFw()  {
		String  res = "failed";
		fwBean.setFwzt(SystemConstants.FWZT_YQD);
		fwBean.setZhxgsj(DateUtils.createCurrentDate());
		int r = fwManager.updateYyByFields(fwBean, "fwzt,zhxgsj");
		if(r > 0){
			res = "success";
		}
		try {
			ResponseUtils.render(response,res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 停止服务
	 * @参数：@return
	 * @返回值：String
	 */
	public String tzFw()  {
		String  res = "failed";
		fwBean.setFwzt(SystemConstants.FWZT_WQD);
		fwBean.setZhxgsj(DateUtils.createCurrentDate());
		int r = fwManager.updateYyByFields(fwBean, "fwzt,zhxgsj");
		if(r > 0){
			res = "success";
		}
		try {
			ResponseUtils.render(response,res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 添加服务依赖关系
	 * @参数：@return
	 * @返回值：String
	 */
	public String gxsave()  {
		List<FwylgxBean> ylgxlist = this.getYlgxList();
		fwManager.saveFwylgx(ylgxlist);
		return "list";
	}
	
	/**
	 * 获取前台传过来的依赖关系list
	 * @参数：@return
	 * @返回值：List
	 */
	private List getYlgxList() {
		String[] prefwidxArr = request.getParameterValues("prefwid");
		String[] prefwnameArr = request.getParameterValues("prefwname");
		
		List <FwylgxBean> list = new ArrayList<FwylgxBean>();
		FwylgxBean bean ;
		if(prefwidxArr != null && prefwidxArr.length > 0){
			for(int i=0 ;i<prefwidxArr.length;i++){
				bean = new FwylgxBean();
				bean.setFwid(fwBean.getFwid());
				bean.setFwname(fwBean.getFwname());
				bean.setPrefwid(prefwidxArr[i]);
				bean.setPrefwname(prefwnameArr[i]);
				list.add(bean);
			}
		}
		return list;
	}


	/**
	 * 获取前台传过来的iplist
	 * @参数：@return
	 * @返回值：List<FwsqBean>
	 */
	private List getFwsqList() {
		String[] ipArr = request.getParameterValues("ip");
		String[] fwsqArr = request.getParameterValues("sqfw");
		
		List  list = new ArrayList<FwsqBean>();
		Map map ;
		if(ipArr != null && ipArr.length > 0){
			for(int i=0 ;i<ipArr.length;i++){
				map = new HashMap();
				map.put("zt", SystemConstants.ZT_TRUE);
				map.put("ip", ipArr[i]);
				map.put("fwid",fwBean.getFwid());
				map.put("sqfw", fwsqArr[i]);
				
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 服务关系修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String gxupdate()  {
		List<FwylgxBean> ylgxlist = this.getYlgxList();
		fwManager.updateFwylgx(fwBean,ylgxlist);
		return "list";
	}
	
	
	
	/**
	 * 服务授权修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String squpdate()  {
		List fwsqlist = this.getFwsqList();
		fwManager.updateFwsq(fwBean,fwsqlist);
		return "list";
	}
	
	
	
	/**
	 * 进入服务添加界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String dledit()  {
		return "dledit";
	}
	
	/**
	 * 添加服务队列
	 * @参数：@return
	 * @返回值：String
	 */
	public String dlsave()  {
		fwddBean.setSzsj(DateUtils.createCurrentDate());
		fwddBean.setZt(MBConstant.ZT_YX);
		fwManager.saveFwdd(fwddBean);
		return "dllist";
	}

	/**
	 * 进入服务队列修改界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String dlget()  {
		fwddBean = fwManager.getFwdlBean(fwddBean.getId());
		Map fwMap = fwManager.getFwBeanByFwid(fwddBean.getFwid());
		request.setAttribute("fwname", fwMap.get("fwname"));
		return "dledit";
	}
	
	/**
	 * 服务队列修改
	 * @参数：@return
	 * @返回值：String
	 */
	public String dlupdate()  {
		fwddBean.setZhxgsj(DateUtils.createCurrentDate());
		String fields = "fwid,dddj";
		fwManager.updateFwdlByFields(fwddBean, fields);
		return "dllist";
	}
	
	/**
	 * 注销服务队列
	 * @参数：@return
	 * @返回值：String
	 */
	public String dlremove()  {
		fwddBean.setZt(SystemConstants.ZT_FALSE);
		fwddBean.setZhxgsj(DateUtils.createCurrentDate());
		fwManager.updateFwdlByFields(fwddBean,"zt,zhxgsj");
		return "dllist";
	}
	
	
	
	/**
	 * 取消服务授权
	 * @参数：@return
	 * @返回值：String
	 */
	public String qxfwsq()  {
		String [] res = new String[2]; 
		try {
			ResponseUtils.render(response, JsonUtil.array2json(res));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 进入服务审核界面
	 * @参数：@return
	 * @返回值：String
	 */
	public String wbfwget()  {
		wbfwBean = fwManager.getWbfwBean(wbfwBean.getId());
		return "wbfwsh";
	}

	
	/**
	 * 外部服务审核
	 * @参数：@return
	 * @返回值：String
	 */
	public String wbfwsh()  {
		wbfwBean.setZhxgsj(DateUtils.createCurrentDate());
		fwManager.updateWbfwByFields(wbfwBean,"fwzt,spsm,zhxgsj");
		return "wbfwlist";
	}
	/**
	 * 删除外部服务
	 * @参数：@return
	 * @返回值：String
	 */
	public String wbfwremove()  {
		wbfwBean.setZt(SystemConstants.ZT_FALSE);
		wbfwBean.setZxsj(DateUtils.createCurrentDate());
		fwManager.updateWbfwByFields(wbfwBean,"zt,zxsj");
		return "wbfwlist";
	}
	
	/**
	 * 获取服务监控统计信息
	 * @参数：@return
	 * @返回值：String
	 */
	public String getDataControlTjInfo(){
		Map map = new HashMap();
		List fwtjlist = fwManager.getNbFwTj();
		map.put("fwtjlist", fwtjlist);
		try {
			ResponseUtils.render(response, JsonUtil.map2json(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取服务监控列表信息
	 * @参数：@return
	 * @返回值：String
	 */
	public String getDataControlListInfo(){
		String fwid = request.getParameter("fwid");
		Map map = new HashMap();
		List fwlist = fwManager.getFwListByTopNumAndFwly(10,SystemConstants.LYLX_ZXLY,fwid);
		map.put("fwlist", fwlist);
		try {
			ResponseUtils.render(response, JsonUtil.map2json(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取节点监控统计信息
	 * @参数：@return
	 * @返回值：String
	 */
	public String getNodeControlTjInfo(){
		Map map = new HashMap();
		List fwtjlist = fwManager.getWbFwTj(10);
		map.put("fwtjlist", fwtjlist);
		try {
			ResponseUtils.render(response, JsonUtil.map2json(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取节点监控列表信息
	 * @参数：@return
	 * @返回值：String
	 */
	public String getNodeControlListInfo(){
		Map map = new HashMap();
		String dwdm = request.getParameter("dwdm");
		List fwlist = fwManager.getWbfwListByDwdm(dwdm);
		map.put("fwlist", fwlist);
		try {
			ResponseUtils.render(response, JsonUtil.map2json(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public FwBean getFwBean() {
		return fwBean;
	}

	public void setFwBean(FwBean fwBean) {
		this.fwBean = fwBean;
	}

	public FwsqBean getFwsqBean() {
		return fwsqBean;
	}

	public void setFwsqBean(FwsqBean fwsqBean) {
		this.fwsqBean = fwsqBean;
	}

	public FwManager getFwManager() {
		return fwManager;
	}

	public void setFwManager(FwManager fwManager) {
		this.fwManager = fwManager;
	}

	public IYyManager getYyManager() {
		return yyManager;
	}

	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}

	public WbfwBean getWbfwBean() {
		return wbfwBean;
	}

	public void setWbfwBean(WbfwBean wbfwBean) {
		this.wbfwBean = wbfwBean;
	}



	public FwddBean getFwddBean() {
		return fwddBean;
	}



	public void setFwddBean(FwddBean fwddBean) {
		this.fwddBean = fwddBean;
	}
	
	

}
