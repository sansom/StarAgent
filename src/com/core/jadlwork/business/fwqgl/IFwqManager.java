package com.core.jadlwork.business.fwqgl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.model.fwqgl.FwqBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;

/**
 * 云平台服务器管理的manager接口
 * @类名: IFwqManager
 * @描述: 完成服务器相关业务操作的功能
 * @作者: 李春晓
 * @时间: 2017-2-15 上午11:01:50
 */
public interface IFwqManager {

	/**
	 * 保存服务器的操作
	 * @param fwqBean
	 * @return: int	返回受影响的行数
	 */
	int saveFwq(FwqBean fwqBean);
	
	/**
	 * 撤销服务器
	 * @param fwqBean
	 * @return: int	返回受影响的行数
	 */
	List<ResultBean> deleteFwq(FwqBean fwqBean);
	
	/**
	 * 根据字段更新服务器
	 * @param fwqBean	要更新的服务器bean对象
	 * @param fields	要更新的字段
	 * @return: int		受影响的行数
	 */
	int updateFwqByFields(FwqBean fwqBean, String fields);
	
	/**
	 * 通过服务器的id查询得到服务器对象
	 * @param id	服务器id
	 * @return: FwqBean	查询得到的服务器的bean对象
	 */
	FwqBean getFwqBeanById(String id);
	
	/**
	 * 通过服务器的ip获取服务器列表	
	 * @param fwqip		服务器ip
	 * @return: List	返回的list集合，其实也就一个值
	 */
	List getFwqListByFwqIp(String fwqip);

	
	
	
	/**
	 * 通过服务器ip校验服务器
	 * @Title: checkFwqByIp
	 * @param fwqip	要校验的服务器ip
	 * @param response 
	 * @return: {@link ResultBean} 封装信息的result对象
	 */
	ResultBean checkFwqByIp(String fwqip, HttpServletResponse response);

	/**
	 * 获取应用可用服务器列表 
	 * @param yyid
	 * @param ptlx
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-7 下午5:45:41
	 */
	List getKyFwqByYyid(String yyid, String ptlx);
	
	
	/**
	 * 获取服务器应用运行情况
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-19 下午3:44:52
	 */
	List getServerAppInfo();

	/**
	 * 更新服务器信息
	 * @param fwqBean
	 * @param fwqUpdate_Fields
	 * @param yyyxjlList
	 * @param yyyxjlUpdate_fields
	 * @author wujiaxu
	 * @Time 2017-6-19 下午5:42:41
	 */
	void updateServerInfo(FwqBean fwqBean, 
			List<YyyxjlBean> yyyxjlList);

	/**
	 * 
	 * 获取系统监控主机
	 * @author wujiaxu
	 * @Time 2017-6-22 上午11:31:28
	 */
	Map getMoniterHost();

	/**
	 * 更新监控主机
	 * @param systemId
	 * @param host
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-22 下午4:15:10
	 */
	int updateMonitorHost(String hostId, String newHost);

	
	/**
	 * 添加监控主机
	 * @param host
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-22 下午4:15:24
	 */
	int addMonitorHost(String newHost);

	/**
	 * 根据服务器类型获取所有的服务器信息
	 * @param fwqlx 服务器类型
	 * @return: List
	 */
	List getFwqList(String fwqlx);

	/**
	 * 根据服务器id删除对应的应用运行记录表
	 * @param fwqid	服务器id
	 * @return: void
	 */
	void deleteYyyxjlByFwqid(String fwqid);
}
