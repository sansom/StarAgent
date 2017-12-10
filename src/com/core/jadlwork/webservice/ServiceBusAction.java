package com.core.jadlwork.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwgl.FwManager;
import com.core.jadlwork.cache.FwzxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.fwgl.FwBean;
import com.core.jadlwork.model.fwgl.FwlogBean;
import com.core.jadlwork.model.mbypt.Service;
import com.core.jadlwork.model.mbypt.ServiceDependency;
/**
 * 服务
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 8:09:49 PM
 */
public class ServiceBusAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ServiceBusAction.class);

	private FwBean fwBean;
	private FwlogBean fwlogBean;
	private Service service; 
	private ServiceDependency dependency;
	private FwManager fwManager; 
	private Map reMap;
	private static List permissIpList;
	private static  String ipWildcard = "[ip]";
	static{
		permissIpList = new ArrayList();
		permissIpList.add("192.168.20.21");
		permissIpList.add("192.168.20.34");
		permissIpList.add("192.168.20.29");
		permissIpList.add("192.168.20.15");
		permissIpList.add("192.168.20.31");
		permissIpList.add("127.0.0.1");
	}
	
	
	/**
	 * 获取所有服务
	 * @参数：@return
	 * @返回值：String
	 */
	public void getServerList()  {
		//1.IP过滤
		if(!permissId()){
			try {
				ResponseUtils.render(response, "非法IP请求:"+getClientIp()+"!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//2.业务处理
		List reList = new ArrayList();
		List fwlist =  fwManager.getAllFwList();
		List fwylgxlist =  fwManager.getAllFwylgxList();
		for(int i = 0; i < fwlist.size() ; i++){
			Map map =  (Map) fwlist.get(i);
			service = new Service();
			service.setPrice(Float.valueOf(String.valueOf(map.get("price"))));
			service.setPriceUnit(String.valueOf(map.get("priceunit")));
			service.setServiceDesc(String.valueOf(map.get("fwms")));
			service.setServiceId(String.valueOf(map.get("fwid")));
			service.setServiceName(String.valueOf(map.get("fwname")));
			service.setServiceType(String.valueOf(map.get("fwfl")));
			service.setServiceTypeName(String.valueOf(map.get("fwfl_dicvalue")));
			service.setStatus("1");
			service.setUrl(String.valueOf(map.get("uri")));
			service.setServiceSource(String.valueOf(map.get("lylx")));
			service.setOutput(String.valueOf(map.get("fwsc")));
			service.setInput(String.valueOf(map.get("fwsr")));
			service.setSort(String.valueOf(map.get("sort")));
		
		
			for(int j = 0; j < fwylgxlist.size() ; j++){
				Map gxmap =  (Map) fwylgxlist.get(j);
				String fwid = (String) gxmap.get("fwid");
				if(fwid.equals(service.getServiceId())){
					
					dependency = new ServiceDependency();
					dependency.setPreServiceId(String.valueOf(gxmap.get("prefwid")));
					dependency.setPreServiceName(String.valueOf(gxmap.get("prefwname")));
					dependency.setServiceId(service.getServiceId());
					dependency.setServiceName(service.getServiceName());
					service.getDependencyList().add(dependency);
				}
				
			}
			reList.add(service);
		}

		String rejson = JsonUtil.list2json(reList);
		
		try {
			ResponseUtils.render(response, rejson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 根据服务ID获取服务URI
	 * @参数：@return
	 * @返回值：String
	 */
	public void getServerUrlByServerId()  {
		
		String url = "";
		String fwid = request.getParameter("serviceId");
		String serviceSource = request.getParameter("serviceSource");
		String ip  = request.getParameter("clientIp");
		String userid  = request.getParameter("userId");
		//1.IP过滤
		if(!permissId()){
			this.setResult(SystemConstants.FWZX_GETURL_FFIP,"");
		}
		//2.业务处理
		
		if(fwid == null || fwid.equals("")){
			this.setResult(SystemConstants.FWZX_GETURL_FWIDNULL,"");
			
		}
		
		if(serviceSource == null || serviceSource.equals("")){
			this.setResult(SystemConstants.FWZX_GETURL_FWLYNULL,"");
		}
		
		if(!serviceSource.equals(SystemConstants.LYLX_ZXLY) && !serviceSource.equals(SystemConstants.LYLX_ZYLY)){
			this.setResult(SystemConstants.FWZX_GETURL_LYLXNOTFOUND,"");
		}
		
		//3.IP是否授权访问该服务
		//if(serviceSource.equals(SystemConstants.LYLX_ZXLY) &&!isAllow(ip,fwid)){
		//	this.setResult(SystemConstants.FWZX_GETURL_CLIENTIPNOTALLOW,"");
		//}
		//4.获取真实的url
		if(serviceSource.equals(SystemConstants.LYLX_ZXLY)){
			//4.1内部服务需要去内存中查找真是的IP地址
			Map fwmap =  fwManager.getFwBeanByFwid(fwid);
			if(fwmap != null && fwmap.get("uri") != null){
				String yyid =  (String) fwmap.get("yyid") ;
				if(yyid == null || "".equals(yyid)){
					url = (String) fwmap.get("uri");
				}else{
					String realIp = FwzxCache.getInstance().getRealIpFromServiceBus(yyid);
					if (realIp != null && !realIp.equals("")) {
						url  = ((String) fwmap.get("uri")).replace(ipWildcard, realIp);
						//说明成功
						this.addLog(userid,ip,fwid,String.valueOf(fwmap.get("fwname")),serviceSource,url);
					}else {
						//获取失败
						this.setResult(SystemConstants.FWZX_GETURL_NOKYJQ,"");
					}
				}
				
			}else{
				this.setResult(SystemConstants.FWZX_GETURL_FWNULL,"");
				
			}
		}else if(serviceSource.equals(SystemConstants.LYLX_ZYLY)){
			//4.1外部服务直接获取url返回
			Map fwmap =  fwManager.getWbfwBeanByFwid(fwid);
			if(fwmap != null && fwmap.get("uri") != null){
				url  = (String) fwmap.get("uri");
				this.addLog(userid,ip,fwid,String.valueOf(fwmap.get("fwname")),serviceSource,url);
			}else{
				this.setResult(SystemConstants.FWZX_GETURL_FWNULL,"");
				
			}
		}
		
		this.setResult(SystemConstants.FWZX_GETURL_SUCCESS,url);
	}
	
	/**
	 * 设置放回结果
	 * @参数：@param fwzxGeturlFfip
	 * @参数：@param object
	 * @参数：@param string
	 * @返回值：void
	 */
	private synchronized void setResult(String code, String url) {
		reMap = new HashMap();
		try {
			reMap.put("code",code);
			reMap.put("ms", SystemConstants.FWZX_CODE_MS.get(code));
			reMap.put("url", url);
			ResponseUtils.render(response, JsonUtil.map2json(reMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reMap = null;
	}

	/**
	 * 根据服务URI获取服务ID
	 * @参数：@return
	 * @返回值：String
	 */
	public void getServerIdByServerUrl()  {
		//1.IP过滤
		if(!permissId()){
			try {
				ResponseUtils.render(response, "非法IP请求:"+getClientIp()+"!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//2.业务处理
		String serverId = "";
		String Url = request.getParameter("serviceUrl");
		if(Url == null || Url.equals("")){
			try {
				ResponseUtils.render(response, serverId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String realUrl = this.dealwithUrl(Url);
		Map fwmap =  fwManager.getFwBeanByUrl(realUrl);
		if(fwmap != null && fwmap.get("fwid") != null){
			serverId = String.valueOf(fwmap.get("fwid"));
		}
		try {
			ResponseUtils.render(response, serverId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * 添加访问日志
     * @param fwly 
     * @param url 
     * @参数：@param userid  用户ID
     * @参数：@param ip   用户IP地址
     * @参数：@param fwid  请求服务ID
     * @参数：@param fwname  请求服务名称
     * @返回值：void
     */
	private void addLog(String userid, String ip, String fwid, String fwname, String fwly, String url) {
		fwlogBean = new FwlogBean();
		fwlogBean.setClientip(ip);
		fwlogBean.setFwid(fwid);
		fwlogBean.setFwly(fwly);
		fwlogBean.setFwname(fwname);
		fwlogBean.setUserid(userid);
		fwlogBean.setQqsj(DateUtils.createCurrentDate());
		fwlogBean.setQqdz(url);
		fwManager.saveFwLog(fwlogBean);
	}

	

	/**
	 * 验证访问权限
	 * @参数：@return
	 * @返回值：boolean
	 */
	private boolean permissId() {
		String ip = getClientIp();
		return true;//permissIpList == null || permissIpList.size() == 0 || permissIpList.contains(ip);
	}
	
	/**
	 * 处理url
	 * @参数：@param url
	 * @参数：@return
	 * @返回值：String
	 */
	private String dealwithUrl(String url) {
		String clientIp = this.getClientIp();
		return url.replaceAll(clientIp, ipWildcard);
	}

	/**
	 * 获取远程访问地址
	 * @参数：@return
	 * @返回值：String
	 */
	private String getClientIp() {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null && !ip.equals("") && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (ip != null && !ip.equals("") && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
	
	/**
	 * 验证客户端是否具有访问服务的权限
	 * @参数：@param ip
	 * @参数：@param fwid
	 * @参数：@return
	 * @返回值：boolean
	 */
	private boolean isAllow(String ip, String fwid) {
		
		if(FwzxCache.getService_allow_Cache().get(fwid) == null && FwzxCache.getService_unallow_Cache().get(fwid) == null){
			//1.没有设置权限则默认所有ip都可以访问
			return true;
		}else if(FwzxCache.getService_allow_Cache().get(fwid) != null ){
			//2.如果都设置了权限则以允许的为主
			if(((List)FwzxCache.getService_allow_Cache().get(fwid)).contains(ip)){
				return true;
			}
		}else if(FwzxCache.getService_allow_Cache().get(fwid) == null && FwzxCache.getService_unallow_Cache().get(fwid) != null){
			//3.如果禁止中不存在则可以访问
			if(!((List)FwzxCache.getService_unallow_Cache().get(fwid)).contains(ip)){
				return true;
			}
		}

		return false;
	}
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceDependency getDependency() {
		return dependency;
	}

	public void setDependency(ServiceDependency dependency) {
		this.dependency = dependency;
	}

	public FwManager getFwManager() {
		return fwManager;
	}

	public void setFwManager(FwManager fwManager) {
		this.fwManager = fwManager;
	}

	public FwBean getFwBean() {
		return fwBean;
	}

	public void setFwBean(FwBean fwBean) {
		this.fwBean = fwBean;
	}


	public FwlogBean getFwlogBean() {
		return fwlogBean;
	}


	public void setFwlogBean(FwlogBean fwlogBean) {
		this.fwlogBean = fwlogBean;
	}
	

}
