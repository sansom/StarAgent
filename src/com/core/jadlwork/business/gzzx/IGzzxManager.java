package com.core.jadlwork.business.gzzx;

import java.util.List;

import com.core.jadlwork.model.gzzx.GzzxJbxxBean;

/**
 * 故障中心业务操作接口
 * @类名: IGzzxManager
 * @作者: 李春晓
 * @时间: 2017-7-4 下午1:29:31
 */
public interface IGzzxManager {

	//============================基本业务操作===================================
	
	/**
	 * 保存故障中心基本信息
	 * @param gzzxJbxxBean
	 * @return: int
	 */
	int saveGzzx(GzzxJbxxBean gzzxJbxxBean);
	
	/**
	 * 修改状态为已处理
	 * @param id	故障id
	 * @return: int
	 */
	int changeToYcl(String id);
	
	/**
	 * 修改状态为已推送
	 * @param id	故障id
	 * @return: int
	 */
	int changeToYts(String id);
	
	/**
	 * 删除故障中心
	 * @param id 故障中心id
	 * @return: int
	 */
	int deleteGzxx(String id);
	
	//=============================捕获故障操作====================================
	
	/**
	 * 获取所有的服务器应用故障信息
	 * @return: List
	 */
	List getFwqYyGzxxList();
	
	/**
	 * 根据异常信息向故障中心中保存数据<br />
	 * 	防止相同信息保存多条，在保存的时候判断20分钟以内出现同样的异常信息，不再存储<br />
	 * @param gzxxList
	 * @return: int
	 */
	int[] saveGzInfo(List gzxxList);

	/**
	 * 捕获服务器应用故障信息，并保存到故障表中
	 * @return: int
	 */
	int[] saveFwqYyGzxx();
	
	/**
	 * 更新服务器和应用的状态
	 * @param fwqidList	要更新的服务器的id的集合
	 * @param yyidList	要更新的应用的id的集合
	 * @return: void
	 */
	void updateFwqYyAlarm(List fwqidList, List yyidList);

	/**
	 * 获取故障的捕获信息
	 * @param gzid	故障id
	 * @return: List
	 */
	List getGzBhXx(String gzid);

	
}
