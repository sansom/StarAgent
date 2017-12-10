package com.core.jadlwork.business.jqgl;

import java.util.List;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.jq.JqFwqBean;
import com.core.jadlwork.model.jq.JqJbxxBean;

/**
 * 集群管理的业务接口
 * @类名: IJqManager
 * @作者: 李春晓
 * @时间: 2017-6-27 上午10:39:23
 */
public interface IJqManager {

	/**
	 * 根据集群id获取集群对象
	 * @param jqid
	 * @return: JqJbxxBean
	 */
	JqJbxxBean getJqJbxxBean(String jqid);

	/**
	 * 保存集群
	 * @param jqJbxxBean
	 * @return: int 返回成功的个数
	 */
	ResultBean saveJq(JqJbxxBean jqJbxxBean, String[] fwqids);

	/**
	 * 修改集群信息
	 * @param jqJbxxBean
	 * @return: int 返回成功的个数
	 */
	List<ResultBean> updateJq(JqJbxxBean jqJbxxBean, String[] fwqids);
	
	/**
	 * 撤销集群
	 * @param jqid 要撤销的集群的id
	 * @return: int 返回成功的个数
	 */
	List<ResultBean> deleteJq (String jqid);

	/**
	 * 获取所有可用的集群集合
	 * @return: List
	 */
	List getJqList();

	/**
	 * 根据fwqid获取集群list集合（应用未已启动的）
	 * @param fwqid 服务器id
	 * @return: List
	 */
	List getYqdJqListByFwqId(String fwqid);

	/**
	 * 根据集群id和服务器id获取集群服务器对象
	 * @param jqid	集群id
	 * @param fwqid	服务器id
	 * @return: JqFwqBean
	 */
	JqFwqBean getJqFwqBeanByJqidAndFwqId(String jqid, String fwqid);
	
	/**
	 * 根据集群id和fwqid增加集群服务器对象
	 * @param jqid	集群id
	 * @param fwqid 服务器id
	 * @return: void
	 */
	void saveJqFwq(String jqid, String fwqid);
	
	/**
	 * 根据集群id和fwqid修改集群服务器对象
	 * @param jqid 集群id
	 * @param fwqid 服务器id
	 * @return: void
	 */
	void updateJqFwq(String jqid, String fwqid);

	/**
	 * 通过服务器id删除集群服务器
	 * @param fwqid
	 * @return: void
	 */
	void deleteJqFwqByFwqId(String fwqid);
	
	/**
	 * 通过集群id删除对应的集群服务器信息
	 * @param jqid	集群id
	 * @return: void
	 */
	void deleteJqFwqByJqId(String jqid);

	/**
	 * 根据Nginxid获取该Nginx所对应的集群集合
	 * @param nid nginx 
	 * @return: List
	 */
	List getjqListByNginxId(String nid);

	/**
	 * 更新Nginx集群信息
	 * @param jqxxs 集群id数组，如果没有为Null
	 * @param nid ngin的id
	 * @return: void
	 */
	void updateNginxJqxx(String[] jqxxs, String nid);
	
	/**
	 * 根据Nginxid删除Nginx集群表中的对应信息
	 * @param nid nginxid
	 * @return: void
	 */
	void deleteNginxJqByNid(String nid);
	
	/**
	 * 获取所有的集群应用信息
	 * @return: List
	 */
	List getJqYyInfoList();

	/**
	 * 获取所有可用服务器（不包含本身的）
	 * @return: List
	 */
	List getKyFwqList(String jqid);

	/**
	 * 根据集群id获取对应的服务器
	 * @param jqid	集群ID
	 * @return: List
	 */
	List getFwqListByJqid(String jqid);

	/**
	 * 根据应用id获取对应的集群
	 * @param yyid	
	 * @return: List
	 */
	List getJqListByYyId(String yyid);
	
}
