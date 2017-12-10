package com.core.jadlwork.struts.action.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.yygl.IYyManager;

/**
 * 首页启动相关的action操作
 * @类名: IndexAction
 * @作者: 李春晓
 * @时间: 2017-1-13 上午9:55:59
 */
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(IndexAction.class);

	//注入fwqManager
	private IFwqManager fwqManager;
	public void setFwqManager(IFwqManager fwqManager) {
		this.fwqManager = fwqManager;
	}
	//注入应用manager
	private IYyManager yyManager;
	public void setYyManager(IYyManager yyManager) {
		this.yyManager = yyManager;
	}

	//展示监控内容
	public String monitorMain() {
		String ptlx = request.getParameter("ptlx");
		
		Map fwqInfo = getFwqInfo(ptlx);	//服务器信息
		
		if (fwqInfo != null && fwqInfo.size()>0) {
			request.setAttribute("fwqAll", fwqInfo.get("fwqAll"));
		}
		request.setAttribute("ptlx", ptlx);
//		return "monitorMain";
		return "main";
		
		/*Map fwqInfo = getFwqInfo(SystemConstants.PTLX_YUN);	//云平台服务器信息
		Map tgfwqInfo = getFwqInfo(SystemConstants.PTLX_TG);	//托管服务器信息
		
		if (fwqInfo != null && fwqInfo.size()>0) {
			request.setAttribute("yyAll", fwqInfo.get("yyAll"));
			request.setAttribute("fwqAll", fwqInfo.get("fwqAll"));
			request.setAttribute("connectInfo", JsonUtil.map2json((Map) fwqInfo.get("connectInfo")));
		}
		
		if (tgfwqInfo != null && tgfwqInfo.size()>0) {
			request.setAttribute("tgyyAll", tgfwqInfo.get("yyAll"));
			request.setAttribute("tgfwqAll", tgfwqInfo.get("fwqAll"));
			request.setAttribute("tgconnectInfo", JsonUtil.map2json((Map) tgfwqInfo.get("connectInfo")));
		}
		
		request.setAttribute("ptlx", ptlx);
		return "monitorMain";*/
	}
	
	//进入首页，也就是监控页面
	public String getMain(){
		Map info = getFwqInfo(SystemConstants.PTLX_YUN);	//服务器信息
		if (info != null && info.size()>0) {
			request.setAttribute("errInfo", info.get("errInfo"));
		}
		return "main";
		/*Map fwqInfo = getFwqInfo(SystemConstants.PTLX_YUN);	//云平台服务器信息
		Map tgfwqInfo = getFwqInfo(SystemConstants.PTLX_TG);	//托管服务器信息
		
		if (fwqInfo != null && fwqInfo.size()>0) {
			request.setAttribute("yyAll", fwqInfo.get("yyAll"));
			request.setAttribute("fwqAll", fwqInfo.get("fwqAll"));
			request.setAttribute("connectInfo", JsonUtil.map2json((Map) fwqInfo.get("connectInfo")));
		}
		
		if (tgfwqInfo != null && tgfwqInfo.size()>0) {
			request.setAttribute("tgyyAll", tgfwqInfo.get("yyAll"));
			request.setAttribute("tgfwqAll", tgfwqInfo.get("fwqAll"));
			request.setAttribute("tgconnectInfo", JsonUtil.map2json((Map) tgfwqInfo.get("connectInfo")));
		}
		return "main";*/
	}
	
	/**
	 * 定时更新获取服务器信息
	 * @return: String
	 */
	public String getRefreshInfo(){
		String ptlx = request.getParameter("ptlx");
		Map fwqInfo = getFwqInfo(ptlx);	//根据平台类型获取对应的服务器信息
		try {
			ResponseUtils.render(response, JsonUtil.map2json(fwqInfo));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Map getFwqInfo(String ptlx) {
		Map infoMap = new HashMap();
		
		List fwqList = fwqManager.getFwqList(ptlx);
		if (fwqList == null || fwqList.size()==0) {
			return null;
		}
		Set yyIdTemp = new HashSet();	//临时set
		
		//存储所有的预警信息（用于在tab标签上面展示服务器异常的信息）
		Map errInfo = new HashMap();
		List yunFwqList = null;
		List tgFwqList = null;
		if (SystemConstants.PTLX_YUN.equals(ptlx)) {
			yunFwqList = fwqList;
			tgFwqList = fwqManager.getFwqList(SystemConstants.PTLX_TG);
		}else {
			tgFwqList = fwqList;
			yunFwqList = fwqManager.getFwqList(SystemConstants.PTLX_YUN);
		}
		
		int yunErrNum = 0;	//云异常个数
		int yunInfoNum = 0;	//云警告个数
		
		int tgErrNum = 0;	//托管异常个数
		int tgInfoNum = 0;	//托管警告个数
		
		int[] is = getErrNumByPtlx(SystemConstants.PTLX_YUN, yunFwqList);
		yunErrNum = is[0];
		yunInfoNum = is[1];
		
		int[] isTg = getErrNumByPtlx(SystemConstants.PTLX_TG, tgFwqList);
		tgErrNum = isTg[0];
		tgInfoNum = isTg[1];
		
		if (yunErrNum>0 || yunInfoNum>0) {
			Map yunMap = new HashMap();
			yunMap.put("errNum", yunErrNum);
			yunMap.put("infoNum", yunInfoNum);
			errInfo.put("errInfo01", yunMap);
		}
		if (tgErrNum>0 || tgInfoNum>0) {
			Map tgMap = new HashMap();
			tgMap.put("errNum", tgErrNum);
			tgMap.put("infoNum", tgInfoNum);
			errInfo.put("errInfo02", tgMap);
		}
		
		if (errInfo.size()>0) {
			infoMap.put("errInfo", errInfo);
		}
		
		//加上预警信息   List<Map>  每一个map是一条信息  [ {level:"error",msg:"服务器异常"} {level:"info",msg:"cpu占用过高"}]
		List msgInfoList = null;
		for (Map fwqMap : (List<Map>)fwqList) {
			msgInfoList = new ArrayList();
			Map msgInfo;
			
			/*
			 * 处理IP	--托管的话fwqip设置为fwqip_ww
			 */
			if (SystemConstants.PTLX_TG.equals(fwqMap.get("ptlx"))) {
				fwqMap.put("fwqip", fwqMap.get("fwqip_ww"));
			}
			
			if (fwqMap.get("fwqstatus").equals(SystemConstants.FWQSTATUS_YC)) {
				//服务器异常，就只显示这个信息
				msgInfo = new HashMap();
				msgInfo.put("level", "error");
				msgInfo.put("msg", "服务器连接异常!");
				msgInfoList.add(msgInfo);
			}else {
				//cpu使用率
				String cpuusedStr = fwqMap.get("cpuused") == null ? null : (String) fwqMap.get("cpuused");
				if (cpuusedStr != null && !cpuusedStr.equals("")) {
					try {
						if (Double.parseDouble(cpuusedStr.substring(0, cpuusedStr.length()-1)) > 80) {
							//cpu占用大于80%
							msgInfo = new HashMap();
							msgInfo.put("level", "info");
							msgInfo.put("msg", "cpu占用过高!");
							msgInfoList.add(msgInfo);
						}
					} catch (Exception e) {
						//出错就不再增加这个信息
					}
				}
				//内存占用率
				String memeryusedStr = fwqMap.get("memeryused") == null ? null : (String) fwqMap.get("memeryused");
				if (memeryusedStr != null && !memeryusedStr.equals("")) {
					try {
						if (Double.parseDouble(memeryusedStr.substring(0, memeryusedStr.length()-1)) > 80) {
							//内存占用大于80%
							msgInfo = new HashMap();
							msgInfo.put("level", "info");
							msgInfo.put("msg", "内存占用过高!");
							msgInfoList.add(msgInfo);
						}
					} catch (Exception e) {
						//出错就不再增加这个信息
					}
				}
				//应用信息
				List yyInfoList = (List) fwqMap.get("yylist");
//				List yyInfoList = yyManager.getYyInfoListByFwqid((String)fwqMap.get("id"));
				if (yyInfoList != null && yyInfoList.size()>0) {
					for (Map yyInfoMap : (List<Map>)yyInfoList) {
						if (yyIdTemp.contains(yyInfoMap.get("id"))) {
							//包含的有，不再往所有应用信息中放
						}else {
//							yyAll.add(yyInfoMap);
							yyIdTemp.add(yyInfoMap.get("id"));
						}
//						Map connectValueMap = new HashMap();
//						connectValueMap.put("from", yyInfoMap);	//起始信息
//						connectValueMap.put("to", fwqMap);	//结束信息
//						connectValueMap.put("type", "yyfwq");	//类别 （什么跟什么的连接：应用-服务器  服务器-Nginx...）
//						connectInfo.put(yyInfoMap.get("id")+"-"+fwqMap.get("id"), connectValueMap);
						
						if (SystemConstants.YYYYZT_YC.equals(yyInfoMap.get("yyyxzt"))) {
							//应用运行异常
							msgInfo = new HashMap();
							msgInfo.put("level", "info");
							msgInfo.put("msg", "应用【"+yyInfoMap.get("yyname")+"】运行异常！");
							msgInfoList.add(msgInfo);
						}
					}
				}
				fwqMap.put("yyInfo", yyInfoList);
			}
			//如果预警信息为空，就说明一切正常
			if (msgInfoList.size()==0) {
				msgInfo = new HashMap();
				msgInfo.put("level", "success");
				msgInfo.put("msg", "一切正常！");
				msgInfoList.add(msgInfo);
			}
			fwqMap.put("msgInfoList", msgInfoList);
		}
		
		//整理应用中的服务器的信息
		/*if (yyAll != null && yyAll.size()>0 && fwqList!=null && fwqList.size()>0) {
			List yyFwqInfoList = null;
			for (Map yyInfoMap : (List<Map>)yyAll) {
				yyFwqInfoList = new ArrayList();
				for (Map fwqInfoMap : (List<Map>)fwqList) {
					Map fwqInfoMapClone = new HashMap();
					fwqInfoMapClone.putAll(fwqInfoMap);
					fwqInfoMapClone.remove("yyInfo");
					List<Map> fwqYyInfoList = fwqInfoMap.get("yyInfo")==null ? null : (List<Map>)fwqInfoMap.get("yyInfo");
					if (fwqYyInfoList != null && fwqYyInfoList.size()>0) {
						for (Map fwqYyInfo : fwqYyInfoList) {
							if (yyInfoMap.get("id").equals(fwqYyInfo.get("id"))) {
								//该服务器中包含的有该应用的信息
								//设置运行状态
								fwqInfoMapClone.put("yyyxzt", fwqYyInfo.get("yyyxzt"));
								yyFwqInfoList.add(fwqInfoMapClone);
								break;
							}
						}
					}else {
						//该服务器中不包含应用信息
						continue;
					}
				}
				yyInfoMap.put("fwqInfoList", yyFwqInfoList);
			}
		}*/
		
//		infoMap.put("yyAll", yyAll);
		infoMap.put("fwqAll", fwqList);
//		infoMap.put("connectInfo", connectInfo);
		return infoMap;
	}
	
	private int[] getErrNumByPtlx(String ptlx, List fwqList) {
		int errNum = 0;
		int infoNum = 0;
		
		for (Map fwqMap : (List<Map>)fwqList) {
			if (fwqMap.get("fwqstatus").equals(SystemConstants.FWQSTATUS_YC)) {
				//异常
				errNum ++;
				continue;
			}else {
				//cpu使用率
				String cpuusedStr = fwqMap.get("cpuused") == null ? null : (String) fwqMap.get("cpuused");
				if (cpuusedStr != null && !cpuusedStr.equals("")) {
					try {
						if (Double.parseDouble(cpuusedStr.substring(0, cpuusedStr.length()-1)) > 80) {
							//cpu占用大于80%
							infoNum ++;
							continue;
						}
					} catch (Exception e) {
						//出错就不再增加这个信息
					}
				}
				//内存占用率
				String memeryusedStr = fwqMap.get("memeryused") == null ? null : (String) fwqMap.get("memeryused");
				if (memeryusedStr != null && !memeryusedStr.equals("")) {
					try {
						if (Double.parseDouble(memeryusedStr.substring(0, memeryusedStr.length()-1)) > 80) {
							//内存占用大于80%
							infoNum ++;
							continue;
						}
					} catch (Exception e) {
						//出错就不再增加这个信息
					}
				}
				//应用信息
				List yyInfoList = (List) fwqMap.get("yylist");
//				List yyInfoList = yyManager.getYyInfoListByFwqid((String)fwqMap.get("id"));
				if (yyInfoList != null && yyInfoList.size()>0) {
					for (Map yyInfoMap : (List<Map>)yyInfoList) {
						if (SystemConstants.YYYYZT_YC.equals(yyInfoMap.get("yyyxzt"))) {
							//应用运行异常
							infoNum ++;
							continue;
						}
					}
				}
			}
		}
		
		return new int[]{errNum,infoNum};
	}
	
}
