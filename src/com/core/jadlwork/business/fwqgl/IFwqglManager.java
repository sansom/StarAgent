package com.core.jadlwork.business.fwqgl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.fwqgl.FwqBean;

/**
 * 服务器管理的接口
 * @ClassName: IFwqglManager
 * @Description: TODO
 * @author: 李春晓
 * @date: 2016-12-22 上午10:19:59
 */
public interface IFwqglManager {

	/**
	 * 保存服务器操作
	 * @Title: saveFwq
	 * @param fwqBean 要保存的服务器bean
	 * @return
	 * @return: int 返回受影响的行数
	 */
	int saveFwq(FwqBean fwqBean);
	
	/**
	 * 通过服务器的id查询得到服务器对象
	 * @Title: getFwqBeanById
	 * @param id 服务器的id
	 * @return
	 * @return: FwqBean 查询得到的服务器对象，如果没有查询到就返回null
	 */
	FwqBean getFwqBeanById(String id);
	
	/**
	 * 根据字段更新服务器
	 * @Title: updateFwqByFields 
	 * @param fwqBean  要更新的服务器bean对象
	 * @param fields 要更新的字段
	 * @return: int 受影响的行数
	 */
	int updateFwqByFields(FwqBean fwqBean, String fields);
	
	/**
	 * 撤销服务器
	 * @Title: deleteFwq
	 * @param fwqBean 要撤销的服务器bean对象 
	 * @return: int 受影响的行数
	 */
	int deleteFwq(FwqBean fwqBean);
	
	/**
	 * 通过服务器的ip获取服务器列表
	 * @Title: getFwqListByFwqip
	 * @param fwqip 服务器ip
	 * @return: List 查询得到的服务器的列表
	 */
	List getFwqListByFwqip(String fwqip);
	
	/**
	 * 获取所有的服务器
	 * @Title: getAllFwqList
	 * @return: List 所有的服务器列表
	 */
	List getAllFwqList();

	/**
	 * 通过服务器ip校验服务器
	 * @Title: checkFwqByIp
	 * @param fwqip	要校验的服务器ip
	 * @param response 
	 * @return: {@link ResultBean} 封装信息的result对象
	 */
	ResultBean checkFwqByIp(String fwqip, HttpServletResponse response);

	/**
	 * 通过服务器ip获得服务器下的日志列表
	 * @param fwqip 服务器ip
	 * @return String 返回的文件名的list集合的json格式
	 */
	String getLogsByFwqip(String fwqip);

	/**
	 * 查看指定服务器的指定的日志文件
	 * @Title: showLog
	 * @Description: 调用SocketUtils中的查看日志的方法，将日志内容读取后返回
	 * @param fwqip 服务器的ip
	 * @param logName 日志的文件名
	 * @return: String 日志的内容
	 */
	String showLog(String fwqip, String logName);

	/**
	 * 获取所有的可用服务器的集合
	 * @return: List
	 */
	List getFwqList();
	
}