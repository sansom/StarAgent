package com.core.jadlwork.business.gzzx;

import java.util.List;

import com.core.jadlwork.model.gzzx.TsRyBean;

/**
 * 推送人员业务操作接口
 * @类名: ITsryManager
 * @作者: lcx
 * @时间: 2017-8-16 上午10:07:54
 */
public interface ITsryManager {

	/**
	 * 保存
	 * @param tsRyBean
	 * @return: int
	 */
	int save(TsRyBean tsRyBean);
	
	/**
	 * 更新
	 * @param tsRyBean
	 * @return: int
	 */
	int update(TsRyBean tsRyBean);
	
	/**
	 * 根据字段更新
	 * @param tsRyBean
	 * @param field	字段
	 * @return: int
	 */
	int update(TsRyBean tsRyBean, String field);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return: int
	 */
	int delete(String id);
	
	/**
	 * 根据id获取值
	 * @param id	id
	 * @return
	 * @return: TsRyBean
	 */
	TsRyBean get(String id);

	/**
	 * 根据用户名获取人员对象
	 * @param username	用户名
	 * @return: TsRyBean
	 */
	TsRyBean getByUsername(String username);

	/**
	 * 根据手机号码获取人员对象
	 * @param sjhm	手机号码
	 * @return: TsRyBean
	 */
	TsRyBean getBySjhm(String sjhm);

	/**
	 * 根据邮箱获取人员对象
	 * @param email	邮箱
	 * @return: TsRyBean
	 */
	TsRyBean getByEmail(String email);

	/**
	 * 根据微信openid获取人员对象
	 * @param openid 
	 * @return: TsRyBean
	 */
	TsRyBean getByWxid(String openid);

	/**
	 * 获取所有可用的人员信息
	 * @return: List
	 */
	List getAll();
	
}
