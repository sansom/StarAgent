package com.core.jadlwork.business.gzzx;

import java.util.List;

/**
 * 推送规则业务接口
 * @类名: ITsgzManager
 * @作者: lcx
 * @时间: 2017-8-21 上午10:47:19
 */
public interface ITsgzManager {

	/**
	 * 删除指定规则下的所有人员信息
	 * @param gzid
	 * @return: int
	 */
	int deleteRyByGzid(String gzid);
	
	/**
	 * 向指定规则批量添加人员
	 * @param gzid	规则id
	 * @param ryids 人员id集合
	 * @return: void
	 */
	void batchSaveRyByGzid(String gzid, String[] ryids);

	/**
	 * 获取所有的规则信息，包含人员信息
	 * @return: List
	 */
	List getAll();

	/**
	 * 改变规则启用状态（待修改）
	 * @param gzid	
	 * @param qyzt
	 * @return: void
	 */
	int changeGzQyzt(String gzid, String qyzt);
}
