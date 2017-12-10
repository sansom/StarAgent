package com.core.jadlwork.business.yygl;

import java.util.List;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;

/**
 * 云平台应用管理的接口类
 * @类名: IYyManager
 * @描述: 完成应用相关的业务操作的功能
 * @作者: 李春晓
 * @时间: 2017-1-10 下午1:33:01
 */
public interface IYyManager {

	/**
	 * 保存应用
	 * @param yyBean
	 * @return
	 * @return: int 受影响行数
	 */
	int saveYy(YyBean yyBean);

	/**
	 * 保存应用运行记录
	 * @param appServerBean
	 * @return: int 受影响行数
	 */
	int saveYyyxjl(YyyxjlBean yyyxjlBean);
	
	/**
	 * 更新应用运行记录
	 * @param appServerBean
	 * @return: int 受影响行数
	 */
	int updateYyyxjlByFields(YyyxjlBean yyyxjlBean,String fields);
	
	/**
	 * 批量保存应用服务器
	 * @param yyid
	 * @param fwqips
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-8 上午9:42:09
	 */
	int saveYyxxjlBatch(String yyid, String[] fwqips);
	
	/**
	 * 删除应用
	 * @param yyBean
	 * @return: int 受影响行数
	 */
	int deleteYy(YyBean yyBean);
	
	/**
	 * 删除应用运行记录
	 * @param yyyxjlBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 下午5:14:24
	 */
	int deleteYyyxjl(YyyxjlBean yyyxjlBean);
	
	
	

	
	/**
	 * 根据字段修改应用
	 * @param yyBean
	 * @param fields	要更新的字段
	 * @return: int	受影响的行数
	 */
	int updateYyByFields(YyBean yyBean,String fields);
	

	/**
	 * 根据应用id获取应用bean
	 * @param id	应用id
	 * @return: YyBean	
	 */
	YyBean getYyBean(String id);
	

	/**
	 * 通过应用id获取应用所在的服务器ip和应用运行状态的集合
	 * @param yyid 应用id
	 * @return: List  map的list，放的有fwqip和yyyxzt
	 */
	List getFwqListByYyid(String yyid);
	
	/**
	 * 获取所有的应用及其对应的所有的服务器ip列表的信息
	 * @return: List	集合里面放的map,map两个key， yyname:应用的名称		fwqips：应用所在的服务器ip集合，用逗号分隔
	 */
	List getYyFwqsList();
	
	/**
	 * 判断指定应用的日志文件是否存在
	 * @param appname 应用的名称，也就是war包的名字
	 * @param ip 服务器的ip
	 * @return: ResultBean
	 */
	ResultBean isYyLogExist(String appname, String ip);
	
	

	/**
	 * 检测应用
	 * @param warname
	 * @param yyid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-13 上午10:07:39
	 */
	ResultBean checkYyIsExist( String warname, String yyid);

	/**
	 * 获取应用运行记录bean
	 * @param yxjlid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 下午5:07:51
	 */
	YyyxjlBean getYyyxjlBean(String yxjlid);

	/**
	 * 根据服务器id获取应用信息集合
	 * @param fwqid 服务器id
	 * @param ptlx 平台类型 
	 * @return: List
	 */
	List getYyInfoListByFwqid(String fwqid);

	/**
	 * 获取应用运行记录信息
	 * @return: List 包含有应用运行记录表以及服务器表中的部分数据
	 */
	List getYyyxjlInfo();
	
	/**
	 * 根据应用id获取应用信息集合
	 * @param yyid 服务器id
	 * @param ptlx 平台类型 
	 * @return: List
	 */
	List getYyInfoListByYyid(String yyid);

	/**
	 * 根据应用id获取集群信息
	 * @return: List
	 */
	List getJqListByYyid(String id);

	/**
	 * 检测应用版本号
	 * @param filepath
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-13 上午11:23:09
	 */
	ResultBean checkYyVersion(String filepath);

	
	
}
