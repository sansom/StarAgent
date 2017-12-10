package com.core.jadlwork.business.yygl;

import java.util.List;

import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.yygl.YyBean;
import com.core.jadlwork.model.yygl.YyyxjlBean;

/**
 * 
 * 应用控制器业务类接口
 * @author wujiaxu
 * @Time 2017-6-12 下午4:40:08
 *
 */
public interface IYyControlManager {

	/**
	 * 部署应用
	 * @param yyid 要部署的应用的id
	 * @param fwqip 服务器的ip
	 * @param fwqip2 
	 * @return: ResultBean
	 */
	ResultBean deploy(String yxjlid,String yyid, String fwqip);
	
	
	/**
	 * 启动应用
	 * @param yyid 要启动的应用的id
	 * @param fwqip 服务器的ip
	 * @return: ResultBean
	 */
	ResultBean start(String yxjlid,String yyid, String fwqip);
	
	
	/**
	 * 停止应用
	 * @param appname 应用名称
	 * @param ip 服务器的ip
	 * @return: ResultBean
	 */
	ResultBean stop(String yxjlid,String yyid, String fwqip);
	
	/**
	 * 删除应用
	 * @param appname 应用名称
	 * @param ip 服务器的ip
	 * @return: ResultBean
	 */
	ResultBean remove(String yxjlid,String yyid, String fwqip);


	/**
	 * 版本试发布
	 * @param yyid
	 * @param isAutoRun
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-15 下午3:30:22
	 */
	ResultBean versionToPriavte( String yyid, String isAutoRun);

	
	/**
	 * 试发布版本回退
	 * @param yyid
	 * @param isAutoRun
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-18 下午1:16:38
	 */
	ResultBean versionToBack(String yyid, String isAutoRun);
	
	/**
	 * 版本正式发布
	 * @param yyid
	 * @param isAutoRun
	 * @return
	 * @author wujiaxu
	 * @Time 2017-9-18 下午1:16:38
	 */
	ResultBean versionToPublic(String yyid, String isAutoRun);

}
