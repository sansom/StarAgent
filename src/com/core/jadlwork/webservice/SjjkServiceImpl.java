package com.core.jadlwork.webservice;


import java.util.Map;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.core.jadlsoft.utils.SpringBeanFactory;
import com.core.jadlwork.business.fwgl.SjjkManager;
import com.core.jadlwork.model.fwgl.SjjkBean;
import com.core.jadlwork.utils.httpRequestProxy.HttpRequestProxy;



public class SjjkServiceImpl implements SjjkService {
	
	private static SjjkManager sjjkManager;
	private HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
	static{
		sjjkManager = (SjjkManager) SpringBeanFactory.getBean("sjjkManager");
	}
	/**
	 * 数据接口内部调用   返回数据格式xml 
	 * @param license 许可证
	 * @param fwid	服务ID
	 * @param paramMap 参数集合
	 * @return
	 * @author niutongda
	 * @Time 2017-1-3 下午04:37:42 
	 *
	 */
	public String sjjk_xml(String fwid, Map paramMap) {
		//验证许可信息
		SjjkBean sjjkBean =sjjkManager.getSjjkByJkid(fwid);
		String jklx = sjjkBean.getJklx();
		String url = sjjkBean.getUri();
//		List paramList = sjjkManager.getSjjkParamListBySjjkId(fwid);
//		for (int i = 0; i < paramList.size(); i++) {
//			Map map = (Map) paramList.get(i);
//			if("0".equals(map.get("paramlx"))){//判断入参
//				if(!paramMap.containsKey(map.get("param"))){//判断入参是否完整
//					return "{'result':'-1'}";
//				}
//			}
//		}
		if("0".equals(jklx)){//http
			
			try {
				String returnStr = httpRequestProxy.doRequest(url, paramMap, null, null);
				return returnStr;
			} catch (Exception e) {
				e.printStackTrace();
				//网络不通或http服务有问题返回-1
				return "{'result':'-1'}";
			}
		}else if("1".equals(jklx)){//webservice
			Service  service = new Service();
			Call call = null;
			try {
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(url);
				call.setTimeout(new Integer(60 * 1000));// 超时设定60秒抛出异常
				call.setOperationName(sjjkBean.getFfname());// 调用登录方法
				call.addParameter("xmlStr", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
				call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
				return (String) call.invoke(new Object[]{paramMap});// 调用服务并返回存在的对应数据
			} catch (Exception e) {
				//网络不通或webservice服务有问题返回-1
				return "{'result':'-1'}";
			}
		}
		return null;
	}

	/**
	 * 数据接口内部调用  返回数据格式json
	 * @param license
	 * @param fwid
	 * @param jsonStr
	 * @return
	 * @author niutongda
	 * @Time 2017-1-3 下午04:37:49 
	 *
	 */
	public String sjjk_json(String fwid, Map paramMap) {
		//验证许可信息
		SjjkBean sjjkBean =sjjkManager.getSjjkByJkid(fwid);
		String jklx = sjjkBean.getJklx();
		String url = sjjkBean.getUri();
//		List paramList = sjjkManager.getSjjkParamListBySjjkId(fwid);
//		for (int i = 0; i < paramList.size(); i++) {
//			Map map = (Map) paramList.get(i);
//			if("0".equals(map.get("paramlx"))){//判断入参
//				if(!paramMap.containsKey(map.get("param"))){//判断入参是否完整
//					return "";
//				}
//			}
//		}
		
		String str = "0";
		if("0".equals(jklx)){//http
			try {
				str = httpRequestProxy.doRequest(url, paramMap, null, null);
				return str;
			} catch (Exception e) {
				e.printStackTrace();
				return "{'result':'-1'}";
			}
		}else if("1".equals(jklx)){//webservice
			Service  service = new Service();
			Call call = null;
			try {
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(url);
				call.setTimeout(new Integer(60 * 1000));// 超时设定60秒抛出异常
				call.setOperationName(sjjkBean.getFfname());// 调用登录方法
				call.addParameter("xmlStr", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
				call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
				return (String) call.invoke(new Object[]{paramMap});// 调用服务并返回存在的对应数据
			} catch (Exception e) {
				return "{'result':'-1'}";
			}
		}
		return null;
	}

	public HttpRequestProxy getHttpRequestProxy() {
		return httpRequestProxy;
	}

	public void setHttpRequestProxy(HttpRequestProxy httpRequestProxy) {
		this.httpRequestProxy = httpRequestProxy;
	}
	
}
