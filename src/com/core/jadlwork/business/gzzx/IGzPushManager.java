package com.core.jadlwork.business.gzzx;

import java.util.List;

import com.core.jadlwork.model.gzzx.TsJbxxBean;

public interface IGzPushManager {

	/**
	 * 根据推送id获取推送基本信息
	 * @param tsid
	 * @return: TsJbxxBean
	 */
	TsJbxxBean getTsBean(String tsid);
	
	TsJbxxBean getTsBeanBySsidAndWxid(String ssid, String wxid);
	
	/**
	 * 推送故障信息
	 * @return: void
	 */
//	void pushGzMsg();
	
	/**
	 * 根据推送类型获取待推送的用户信息的集合
	 * @param tslx	推送类型
	 * @param gzid 规则id
	 * @return: List
	 */
	List getPendingPushUserInfoByTslx(String tslx, String gzid);
	
	/**
	 * 根据推送类型获取待推送的推送信息
	 * @param tslx	推送类型
	 * @return: List
	 */
	List getDtsPushXxByTslx(String tslx);

	/**
	 * 批量保存故障中心产生的推送信息
	 * @param gzxxs 故障中心信息
	 * @return: void
	 */
	void savePushMsgByGzzx(List gzxxs);

	/**
	 * 推送微信消息
	 * @return: void
	 */
	void pushWeChatMsg();
	
	/**
	 * 根据故障信息id获取微信推送的列表
	 * @param gzid
	 * @return: List
	 */
	List getWeChatPushByGzid(String gzid);
	
	/**
	 * 获取推送表中所有推送失败的信息
	 * @return: List
	 */
	List getWeChatPushByGzidAndErr(String gzid);

	/**
	 * 推送邮箱消息
	 * @return: void
	 */
	void pushEmailMsg();
	
	/**
	 * 根据推送所属id和touser还有推送类型获取推送bean
	 * @param tsssid 推送所属id
	 * @param touser touser
	 * @param tslx 推送类型
	 * @return: TsJbxxBean
	 */
	TsJbxxBean getTsxxBySsidAndTouserAndTslx(String tsssid, String touser, String tslx);
}
