package com.core.jadlwork.business.fwqgl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.nginx.NginxBean;
import com.core.jadlwork.model.nginx.NginxConfigBean;

/**
 * Nginx服务器操作的manager接口
 * @类名: INginxManager
 * @作者: 李春晓
 * @时间: 2017-2-17 上午9:38:51
 */
public interface INginxManager {

	/**
	 * 增加Nginx服务器
	 * @param nginxBean
	 * @return: int 返回受影响的行数
	 */
	int saveNginx(NginxBean nginxBean);
	
	/**
	 * 撤销Nginx服务器
	 * @param nid  服务器id
	 * @return: int	返回受影响的行数
	 */
	int deleteNginx(String nid);
	
	/**
	 * 更新Nginx服务器
	 * @param nginxBean
	 * @return: int 返回受影响的行数
	 */
	int updateNginx(NginxBean nginxBean);
	
	String getNextval();
	
	/**
	 * 按字段更新Nginx服务器
	 * @param nginxBean
	 * @param fields	字段
	 * @return: int 返回受影响的行数
	 */
	int updateNginxByFields(NginxBean nginxBean, String fields);
	
	/**
	 * 查询所有的Nginx服务器的列表集合
	 * @return: List 返回map的list集合
	 */
	List getNginxList();
	
	/**
	 * 通过id查询Nginx对象
	 * @param id	Nginx的id
	 * @return: NginxBean
	 */
	NginxBean getNginxBean(String id);
	
	/**
	 * 根据服务器ip获取Nginx服务器bean对象
	 * @param ip	服务器ip
	 * @return: NginxBean
	 */
	NginxBean getNginxBeanByIp(String ip);
	
	/**
	 * 通过nginxid获取对应的集群、服务器、应用信息（生成Nginx配置时候使用）
	 * @param nid Nginxid
	 * @return: List
	 */
	Map getJqFwqYyByNginxId(String nid);
	
	/**
	 * 通过nginxid获取对应的集群、服务器、应用信息（页面展示使用）
	 * @param nid Nginxid
	 * @return: List
	 */
	Map getJqFwqYyByNginxIdForShow(String nid);

	/**
	 * 根据集群id删除Nginx-集群表中的相关信息
	 * @param nginxid
	 * @return: int
	 */
	int deleteNginxJqByNginxId(String nginxid);
	
	/**
	 * 根据集群id删除Nginx-集群表中的相关信息
	 * @param jqid 集群id
	 * @return: int
	 */
	int deleteNginxJqByJqid(String jqid);
	
	/**
	 * 根据nginxid更新Nginx的配置信息
	 * @param nid	nginxid
	 * @return: void
	 */
	ResultBean updateNginxConfByNginxid(String nid);
	
	/**
	 * 根据集群id更新Nginx的配置信息
	 * @param jqid	集群id
	 * @return: void
	 */
	ResultBean updateNginxConfByJqid(String jqid);
	
	/**
	 * 根据应用id更新该应用所在的Nginx的配置信息
	 * @param yyid	应用id
	 * @return: void
	 */
	ResultBean updateNginxConfByYyid(String yyid);
	
	/**
	 * 根据服务器的id更新该服务器对应的Nginx的配置信息
	 * @param fwqid	服务器id
	 * @return: void
	 */
	ResultBean updateNginxConfByFwqid(String fwqid);

	/**
	 * 根据集群id获取Nginx信息
	 * @param jqid
	 * @return: List
	 */
	List getNginxListByJqid(String jqid);

	/**
	 * 根据nginxid获取试发布配置信息
	 * @param nid
	 * @return: List
	 */
	List getSfbpzList(String nid);

	/**
	 * 保存试发布配置信息
	 * @param nid nginxid
	 * @param regexesArr 正则数组
	 * @return: void
	 */
	List<ResultBean> saveSfbpzxx(String nid, String[] regexesArr);
	
	/**
	 * 根据Nginxid删除试发布配置信息
	 * @return: int
	 */
	int deleteSfbpzxxByNid(String nid);

	/**
	 * 根据服务器ID获取要更新的Nginx信息
	 * @param fwqid
	 * @return
	 */
	List getListByFwqid(String fwqid);

	/**
	 * 根据集群id获取要更新的Nginx信息
	 * @param jqid	集群id
	 * @return: List
	 */
	List getListByJqid(String jqid);
	
	/**
	 * 更改Nginx的更新状态信息
	 * @param id nginx id
	 * @param zt 要更新为的状态
	 * @param msg 失败信息
	 * @return: void
	 */
	void updateNginxGxzt(String id, String zt, String msg);

	/**
	 * 获取所有更新失败的Nginx信息
	 * @return: List
	 */
	List getGxsbList();

	/**
	 * 根据应用获取Nginx信息
	 * @param yyid 应用id
	 * @return: List
	 */
	List getNginxListByYyid(String yyid);
}
