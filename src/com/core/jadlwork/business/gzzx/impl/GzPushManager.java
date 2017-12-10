package com.core.jadlwork.business.gzzx.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.IGzPushManager;
import com.core.jadlwork.model.gzzx.TsJbxxBean;
import com.core.jadlwork.utils.MsgPushUtils;

public class GzPushManager extends BaseManager implements IGzPushManager {

	private Logger logger = Logger.getLogger(GzPushManager.class);
	
	@Override
	public TsJbxxBean getTsBean(String tsid) {
		Map condition = new HashMap();
		condition.put("id", tsid);
		Object obj =  daoUtils.findObjectCompatibleNull(TsJbxxBean.class, condition);
		return obj == null ? null : (TsJbxxBean) obj;
	}
	
	@Override
	public TsJbxxBean getTsBeanBySsidAndWxid(String ssid, String wxid) {
		Map condition = new HashMap(); 
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("TOUSER", wxid);
		condition.put("tsssid", ssid);
		Object object = daoUtils.findObjectCompatibleNull(TsJbxxBean.class, condition);
		return object == null ? null : (TsJbxxBean)object;
	}
	
	@Override
	public List getDtsPushXxByTslx(String tslx) {
		Map condition = new HashMap();
		condition.put("tslx", tslx);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#gzzx.getDtsPushXxByTslx", condition);
	}
	
	@Override
	public List getPendingPushUserInfoByTslx(String tslx, String gzid) {
		
		if (StringUtils.isEmpty(tslx)) {
			return null;
		}
		
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("gzid", gzid);
		
		if (SystemConstants.TS_TSLX_EMAIL.equals(tslx)) {
			return daoUtils.find("#gzzx.getPendingPushEmailUserInfo", condition);
		}else {
			if (SystemConstants.TS_TSLX_WECHAT.equals(tslx)) {
				return daoUtils.find("#gzzx.getPendingPushWeChatUserInfo", condition);
			}
		}
		return null;
	}
	
	@Override
	public void savePushMsgByGzzx(List gzxxs) {
		if (gzxxs == null || gzxxs.size()==0) {
			return;
		}
		//1、获取所有待推送的用户信息列表
//		List emailPushInfo = getPendingPushUserInfoByTslx(SystemConstants.TS_TSLX_EMAIL);
//		List wechatPushInfo = getPendingPushUserInfoByTslx(SystemConstants.TS_TSLX_WECHAT);
		
		//2、遍历故障信息
		for (Map gzMap : (List<Map>)gzxxs) {
			//3、解析到为可以向推送信息表中插入的数据格式
			String guizeid = (String) gzMap.get("gzlx");
			List emailPushInfo = getPendingPushUserInfoByTslx(SystemConstants.TS_TSLX_EMAIL, guizeid);
			List wechatPushInfo = getPendingPushUserInfoByTslx(SystemConstants.TS_TSLX_WECHAT, guizeid);
			
			
			List parserEmailPushList = parsePushInfo(emailPushInfo, SystemConstants.TS_TSLX_EMAIL, gzMap);
			List parserWeChatPushList = parsePushInfo(wechatPushInfo, SystemConstants.TS_TSLX_WECHAT, gzMap);
			//4、批量保存到推送信息表中
			List pushList = new ArrayList();
			if (parserEmailPushList != null && parserEmailPushList.size()>0) {
				pushList.addAll(parserEmailPushList);
			}
			if (parserWeChatPushList != null && parserWeChatPushList.size()>0) {
				pushList.addAll(parserWeChatPushList);
			}
			
			batchSavePushInfo(pushList);
		}
	}
	
	/*
	 * 批量将推送信息保存到推送表中
	 */
	private void batchSavePushInfo(List pushList) {
		if (pushList == null || pushList.size()==0) {
			return ;
		}
		// 采用批量添加的方式
		String[] pushField = { "id", "tslx", "title", "msg", "touser", "tousername", "tsssid", "intime", "sendtime", "tszt", "cfcs", "zt"}; // 待插入值的字段
		List pushInsertList = new ArrayList();	//待插入的数据，里面放的是map
		
		synchronized (GzPushManager.class) {
			for (Map pushMap : (List<Map>)pushList) {
				String id = String.valueOf(daoUtils.getNextval("q_ts_jbxx"));
				
				pushMap.put("id", id);
				pushMap.put("sendtime", "");
				pushMap.put("cfcs", "0");	//重发次数
				
				//如果为空  批量插入会失败
				pushMap.put("tslx", pushMap.get("tslx")==null ? "" : pushMap.get("tslx"));
				pushMap.put("title", pushMap.get("title")==null ? "" : pushMap.get("title"));
				pushMap.put("msg", pushMap.get("msg")==null ? "" : pushMap.get("msg"));
				pushMap.put("touser", pushMap.get("touser")==null ? "" : pushMap.get("touser"));
				pushMap.put("tousername", pushMap.get("tousername")==null ? "" : pushMap.get("tousername"));
				pushMap.put("tsssid", pushMap.get("tsssid")==null ? "" : pushMap.get("tsssid"));
				pushMap.put("intime", pushMap.get("intime")==null ? "" : pushMap.get("intime"));
				pushMap.put("tszt", pushMap.get("tszt")==null ? "" : pushMap.get("tszt"));
				pushMap.put("zt", pushMap.get("zt")==null ? "" : pushMap.get("zt"));
				pushInsertList.add(pushMap);
			}
		}
		if (pushInsertList.size()>0) {
			daoUtils.executeBatchUpdate("#gzzx.insert_t_ts_jbxx", pushField, pushInsertList);
		}
	}
	
	/*
	 * 组装要推送的信息
	 */
	private List parsePushInfo(List pushInfo, String tslx, Map gzMap) {
		if (pushInfo == null || pushInfo.size()==0 || 
				StringUtils.isEmpty(tslx) || 
				gzMap == null || gzMap.size()==0) {
			return null;
		}
		List pendingPushInfo = new ArrayList();
		Map pendingPushMap = null;
		
		/**
		 *	    gzxxMap.put("id", id);
				gzxxMap.put("bhsj", DateUtils.createCurrentDate());
				gzxxMap.put("clzt", SystemConstants.GZZX_CLZT_DTS);	//待推送状态
				gzxxMap.put("zt", SystemConstants.ZT_TRUE);
				//如果为空，批量插入会失败
				gzxxMap.put("gzlx", gzxxMap.get("gzlx")==null ? "" : gzxxMap.get("gzlx"));
				gzxxMap.put("fwqip", gzxxMap.get("fwqip")==null ? "" : gzxxMap.get("fwqip"));
				gzxxMap.put("fwqdk", gzxxMap.get("fwqdk")==null ? "" : gzxxMap.get("fwqdk"));
				gzxxMap.put("yyname", gzxxMap.get("yyname")==null ? "" : gzxxMap.get("yyname"));
				gzxxMap.put("yclx", gzxxMap.get("yclx")==null ? "" : gzxxMap.get("yclx"));
				gzxxMap.put("ycyy", gzxxMap.get("ycyy")==null ? "" : gzxxMap.get("ycyy"));
				gzxxMap.put("ycxx", gzxxMap.get("ycxx")==null ? "" : gzxxMap.get("ycxx"));
				gzxxMap.put("yxj", gzxxMap.get("yxj")==null ? "" : gzxxMap.get("yxj"));
				gzxxMap.put("bz", gzxxMap.get("bz")==
		 */
		String gzlx = (String) gzMap.get("gzlx");
		String fwqip = (String) gzMap.get("fwqip");
		String fwqdk = (String) gzMap.get("fwqdk");
		String yyname = (String) gzMap.get("yyname");
		String ycxx = (String) gzMap.get("ycxx");
		
		for (Map pushMap : (List<Map>)pushInfo) {
			pendingPushMap = new HashMap();
			
			//公共部分
			pendingPushMap.put("tsssid", gzMap.get("id"));
			pendingPushMap.put("intime", DateUtils.createCurrentDate());
			pendingPushMap.put("tszt", SystemConstants.TS_TSZT_DTS);
			pendingPushMap.put("zt", SystemConstants.ZT_TRUE);
			pendingPushMap.put("tousername", pushMap.get("username"));
			
			gzMap.put("username", pushMap.get("username"));
			
			//标题
			String title = getTitleByGzlx(gzlx);
			pendingPushMap.put("title", title);
			//内容
			String msg = getMsgByGzlx(gzlx, gzMap);
			pendingPushMap.put("msg", msg);
			
			if (SystemConstants.TS_TSLX_EMAIL.equals(tslx)) {
				//邮箱推送
				pendingPushMap.put("tslx", SystemConstants.TS_TSLX_EMAIL);
				pendingPushMap.put("touser", pushMap.get("yxdz"));
				
			}else {
				//微信推送
				pendingPushMap.put("tslx", SystemConstants.TS_TSLX_WECHAT);
				pendingPushMap.put("touser", pushMap.get("wxid"));
			}
			pendingPushInfo.add(pendingPushMap);
		}
		
		return pendingPushInfo;
	}
	
	/*
	 * 根据故障类型获取对应的推送标题
	 */
	private String getTitleByGzlx(String gzlx) {
		String title = SystemConstants.TS_TITLE_FWQ;
		if (SystemConstants.GZZX_GZLX_YY.equals(gzlx)) {
			title = SystemConstants.TS_TITLE_YY;
		}
		return title;
	}
	
	/*
	 * 根据故障类型获取对应的推送的信息
	 */
	private String getMsgByGzlx(String gzlx, Map data) {
		
		String nickname = data.get("username")==null ? "" : (String) data.get("username");
		String fwqip = data.get("fwqip")==null ? "" : (String) data.get("fwqip");
		String fwqdk = data.get("fwqdk")==null ? "" : (String) data.get("fwqdk");
		String ycxx = data.get("ycxx")==null ? "" : (String) data.get("ycxx");
		String yyname = data.get("yyname")==null ? "" : (String) data.get("yyname");
		
		String templateMsg;
		String msg;
		if (SystemConstants.GZZX_GZLX_FWQ.equals(gzlx)) {
			//服务器
			templateMsg = SystemConstants.TS_PUSHTEMPLATE.get(SystemConstants.TS_TEMPLATE_FWQERROR);
			msg = templateMsg.replace("${nickname}", nickname)
					.replace("${fwqip}", fwqip)
					.replace("${fwqdk}", fwqdk)
					.replace("${ycxx}", ycxx);
			return msg;
		}else if (SystemConstants.GZZX_GZLX_YY.equals(gzlx)) {
			//应用
			templateMsg = SystemConstants.TS_PUSHTEMPLATE.get(SystemConstants.TS_TEMPLATE_YYERROR);
			msg = templateMsg.replace("${nickname}", nickname)
					.replace("${yyname}", yyname)
					.replace("${fwqip}", fwqip)
					.replace("${fwqdk}", fwqdk)
					.replace("${ycxx}", ycxx);
			return msg;
		}else if (SystemConstants.GZZX_GZLX_YYYX.equals(gzlx)) {
			//应用运行
			templateMsg = SystemConstants.TS_PUSHTEMPLATE.get(SystemConstants.TS_TEMPLATE_YYYXERROR);
			msg = templateMsg;	//TODO
			return msg;
		}else {
			return "";
		}
	}
	
	@Override
	public void pushWeChatMsg() {
		/**
		 * 1、获取所有待推送的微信消息信息
		 */
//		List dtsPushXx = getDtsPushXxByTslx(SystemConstants.TS_TSLX_WECHAT);
//		
//		/**
//		 * 2、组装为待推送的数据格式
//		 */
//		if (dtsPushXx == null || dtsPushXx.size()==0) {
//			return;
//		}
//		List<Map> msgData = new ArrayList<Map>();
//		Map tmpMap;
//		for (Map dtsMap : (List<Map>)dtsPushXx) {
//			tmpMap = new HashMap();
//			tmpMap.put("key", dtsMap.get("id"));
//			tmpMap.put("template_id", getTemplateIdByGzlx((String)dtsMap.get("gzlx")));
//			tmpMap.put("touser", dtsMap.get("touser"));
//			tmpMap.put("data", JsonUtil.map2json(getTemplateDataMap(dtsMap)));
//			
//			msgData.add(tmpMap);
//		}
//		
//		/**
//		 * 3、调用API完成推送请求
//		 */
//		Map respMap = WeChatAPI.getInstance().sendMulitMsg(msgData);
//		
//		/**
//		 * 4、解析返回信息
//		 */
//		List succInfo = new ArrayList();	//推送成功的信息
//		List failInfo = new ArrayList();	//推送失败的信息
//		if (respMap == null || respMap.size()==0) {
//			//出现异常情况
//			logger.error("【微信推送】，调用API出现异常情况");
//			return;
//		}
//		if (APIConstants.STATUSCODE_SUCC.equals(respMap.get("statusCode"))) {
//			//全部推送成功
//			succInfo = dtsPushXx;
//		}else {
//			Object errinfo = respMap.get("errinfo");
//			if (errinfo == null || errinfo.equals("")) {
//				//没有推送成功，可能是远程服务不通等原因【没有调用微信服务器】，不处理
//				logger.error("【微信推送】，推送出现错误，具体原因为【"+respMap.get("errmsg")+"】");
//				return;
//			}else {
//				//推送有失败的信息【调用微信服务器遇到有失败的信息】
//				for (Map dtsMap : (List<Map>)dtsPushXx) {
//					Object tsid = dtsMap.get("id");	//推送id
//					boolean flag = true;
//					for (Map errMap : (List<Map>)errinfo) {
//						if (tsid.equals(errMap.get("key"))) {
//							//失败信息
//							failInfo.add(tsid);
//							flag = false;
//							break;
//						}
//					}
//					if (flag) {
//						//成功的消息
//						succInfo.add(dtsMap);
//					}
//				}
//			}
//		}
//		
//		/**
//		 * 5、处理推送结果
//		 */
//		if (succInfo.size()>0) {
//			for (Map succMap : (List<Map>)succInfo) {
//				String tsid = (String) succMap.get("id");	//推送id
//				//发送成功，保存成功状态
//				TsJbxxBean jbxxBean = new TsJbxxBean();
//				jbxxBean.setId(tsid);
//				jbxxBean.setTszt(SystemConstants.TS_TSZT_TSCG);
//				jbxxBean.setSendtime(DateUtils.createCurrentDate());
//				daoUtils.update(jbxxBean, "tszt,sendtime");
//			}
//		}
//		if (failInfo.size()>0) {
//			for (String tsid : (List<String>)failInfo) {
//
//				TsJbxxBean tsBean = this.getTsBean(tsid);
//				if (tsBean == null) {
//					continue;
//				}
//				int cfcs = StringUtils.isEmpty(tsBean.getCfcs()) ? 0 : Integer.parseInt(tsBean.getCfcs());
//				tsBean.setCfcs((cfcs+1)+"");
//				tsBean.setTszt(SystemConstants.TS_TSZT_TSSB);
//				daoUtils.update(tsBean, "cfcs,tszt");
//			}
//		}
	}
	
	/*
	 * 根据故障类型获取要使用的微信模板id
	 */
	private String getTemplateIdByGzlx(String gzlx) {
		String tid = SystemConstants.WECHAT_MSG_TEMPLATE_FWQ;
		if (SystemConstants.GZZX_GZLX_YY.equals(gzlx)) {
			tid = SystemConstants.WECHAT_MSG_TEMPLATE_YY;
		}
		return tid;
	}
	
	/*
	 * 根据待推送map提取出模板中所需要的信息
	 */
	private Map getTemplateDataMap(Map dtsMap) {
		Map templateMap = new HashMap();
		templateMap.put("nickname", dtsMap.get("tousername"));
		templateMap.put("fwqip", dtsMap.get("fwqip"));
		templateMap.put("fwqdk", dtsMap.get("fwqdk"));
		templateMap.put("yyname", dtsMap.get("yyname"));
		return templateMap;
	}
	
	@Override
	public List getWeChatPushByGzid(String gzid) {
		Map condition = new HashMap();
		condition.put("tsssid", gzid);
		condition.put("tslx", SystemConstants.TS_TSLX_WECHAT);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find(TsJbxxBean.class, condition);
	}
	
	@Override
	public List getWeChatPushByGzidAndErr(String gzid) {
		Map condition = new HashMap();
		condition.put("tslx", SystemConstants.TS_TSLX_WECHAT);
		condition.put("tszt", SystemConstants.TS_TSZT_TSSB);
		condition.put("tsssid", gzid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find(TsJbxxBean.class, condition);
	}
	
	@Override
	public void pushEmailMsg() {
		
		/**
		 * 1、获取待推送信息
		 */
		List dtsPushXxByTslx = getDtsPushXxByTslx(SystemConstants.TS_TSLX_EMAIL);
		if (dtsPushXxByTslx == null || dtsPushXxByTslx.size() == 0) {
			return;
		}
		/*
		 * 邮箱一次发送过多邮件有导致失败，在这里进行过滤，一次发送2条
		 */
		int size = dtsPushXxByTslx.size();
		if (size > 2) {
			List list = new ArrayList();
			for (int i = 0; i < 2; i++) {
				list.add(dtsPushXxByTslx.get(i));
			}
			dtsPushXxByTslx = list;
		}
		
		/**
		 * 2、组装为接口需要的参数格式
		 */
		List msgData = new ArrayList();
		Map data;
		for (Map pushMap : (List<Map>)dtsPushXxByTslx) {
			data = new HashMap();
			data.put("touser", pushMap.get("touser"));
			data.put("msg", pushMap.get("msg"));
			data.put("title", pushMap.get("title"));
			data.put("key", pushMap.get("tsssid"));
			msgData.add(JsonUtil.map2json(data));
		}
		
		/**
		 * 3、推送
		 */
		String res;
		try {
			res = MsgPushUtils.pushEmailhMultiMsg(msgData);
		} catch (Exception e) {
			logger.error("推送出现异常！");
			return;
		}
		
		/**
		 * 4、解析结果
		 */
		Map resMap = new HashMap();
		try {
			resMap = JsonUtil.parserToMap(res);
		} catch (Exception e) {
			logger.error("解析邮箱推送结果失败！");
			return;
		}
		
		/**
		 * 5、根据结果处理推送消息状态信息
		 */
		List succ = new ArrayList();
		List err = new ArrayList();
		
		/**
		 * {
		 *			"statusCode": 结果码,
		 *			"errMsg": 失败信息,
		 *			"erruseres": 发送失败的信息{
		 *								"email"		邮箱
		 *								"errcode"	失败码
		 *								"errmsg"	失败详情
		 *								"key"	key
		 *							}
		 *		}
		 */
		
		/**
		 * 5.1、整理失败的和成功的
		 */
		if (SystemConstants.STATUSCODE_OK.equals(resMap.get("statusCode"))) {
			//全部成功
			succ = dtsPushXxByTslx;
		}else if (SystemConstants.STATUSCODE_ERR_COM_HASPUSHERR.equals(resMap.get("statusCode"))) {
			//有失败的
			List resErrList = new ArrayList();
			try {
				resErrList = JsonUtil.parserToList((String) resMap.get("erruseres"));
			} catch (Exception e) {
				logger.error("解析邮箱推送结果失败！");
				return;
			}
			for (Map srcMap : (List<Map>)dtsPushXxByTslx) {
				String tsssid = (String) srcMap.get("tsssid");
				String touser = (String) srcMap.get("touser");
			
				boolean isSucc = true;
				for (Map errMap : (List<Map>)resErrList) {
					String touser_err = (String) errMap.get("email");
					String tsssid_err = (String) errMap.get("key");
					if (tsssid.equals(tsssid_err) && touser.equals(touser_err)) {
						err.add(srcMap);
						isSucc = false;
						break;
					}
				}
				if (isSucc) {
					succ.add(srcMap);
				}
			}
		}
		
		/**
		 * 5.2 设置推送的状态信息
		 */
		for (Map succMap : (List<Map>)succ) {
			//成功
			TsJbxxBean jbxxBean = new TsJbxxBean();
			jbxxBean.setId((String) succMap.get("id"));
			jbxxBean.setTszt(SystemConstants.TS_TSZT_TSCG);
			jbxxBean.setSendtime(DateUtils.createCurrentDate());
			daoUtils.update(jbxxBean, "tszt,sendtime");
		}
		
		for (Map errMap : (List<Map>)err) {
			//失败
			TsJbxxBean bean = this.getTsxxBySsidAndTouserAndTslx((String)errMap.get("tsssid"), 
					(String)errMap.get("touser"), SystemConstants.TS_TSLX_EMAIL);
			if (bean == null) {
				continue;
			}
			bean.setTszt(SystemConstants.TS_TSZT_TSSB);
			int cfcss = 0;
			try {
				cfcss = Integer.parseInt(bean.getCfcs());
			} catch (Exception e) {
				
			}
			bean.setCfcs((cfcss + 1) + "");
			daoUtils.update(bean, "cfcs,tszt");
		}
	}
	
	@Override
	public TsJbxxBean getTsxxBySsidAndTouserAndTslx(String tsssid,
			String touser, String tslx) {
		Map condition = new HashMap();
		condition.put("tsssid", tsssid);
		condition.put("touser", touser);
		condition.put("tslx", tslx);
		
		Object object = daoUtils.findObjectCompatibleNull(TsJbxxBean.class, condition);
		if (object == null) {
			return null;
		}
		return (TsJbxxBean) object;
	}
}
