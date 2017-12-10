package com.core.jadlsoft.utils;

import java.util.HashMap;
import java.util.Map;

public class SystemConstants {
	
	/**
	 * 系统换行符
	 */
	public static final String LINE_SEPARATER = System.getProperty("line.separator", "\n");
	
	/**
	 * 默认主键序列表
	 */
	public static final String DB_PK_SEQUENCE = "q_table_pkid";
	
	public static final String DB_TABLEPKFIELDS = "db_tablepkfields";
	/**
	 * 表名
	 */
	public static final String DB_TABLENAME = "db_tablename";
	/**
	 * 排序字段，可以加上desc等
	 */
	public static final String DB_ORDERBY = "db_resultorderby";
	/**
	 * 条件字段
	 */
	public static final String DB_CONDITIONFIELDS = "condition_fields";
	/**
	 * Blob字段
	 */
	public static final String DB_BLOBFIELD = "db_blobfields";
	/**
	 * TIMESTAMP字段
	 */
	public static final String DB_TIMESTAMPFIELD = "qztimestamp";
	
	public static final String BLOBFIELDS = "ry_zp,ry_qm,zp";
	

	
	public static final String SUCCESS_KEY = "Success";

	public static final String SYSTEM_FAILURE_KEY = "SystemFailure";

	public static final String SESSION_TIME_OUT_KEY = "SessionTimeOut";

	public static final String FAILURE_KEY = "Failure";

	public static final String LOGIN_KEY = "Login";

	public static final String GET_KEY = "Get";

	public static final String CONTINUE_KEY = "Continue";

	public static final String RESULT_KEY = "Result";
	
	public static final String VIEW_KEY = "View";

	public static final String FILL_KEY = "Fill";

	public static final String EQUIP_KEY = "Equip";

	public static final String QUERY_KEY = "Query";

	public static final String SYSMESSAGE_KEY = "systemmessage"; //返回消息

	public static final String GNCODE_KEY = "gncode"; 	//功能代码

	public static final String XXZT_ZC = "0"; 	//正常,有效的

	public static final String XXZT_ZX = "1"; 	//注销的

	public static final String SAVELOG_BEAN = "log_bean";

	public static final int PAGESIZE = 10;	//页码设置
	
	public static final String MESSAGE_KEY = "ApplicationMessage";	//消息设置
	
	/**
	 * 数据状态 0:无效  1:有效
	 */
	public static final String ZT_FALSE = "1"; 
	public static final String ZT_TRUE = "0"; 
	/*
	 * 行政区划级别
	 */	
	public static final int MINISTRY = 0;//公安部
    public static final int PROVINCE = 1;//省级
    public static final int CITY = 2;//地市
    public static final int COUNTRY = 3;//县级
    public static final int PQDW = 4;//配枪单位
    public static final int ERRORLEVEL = -1;//县级
    public static final String ERRORXZHQH = "999999";//产生错误时候的代码
	public static final String SUPERIORXZHQH = "888888";//公安部的上级代码
	public static final String MINISTRYXZHQH = "000000";//公安部的代码
	
	
    
	public static final String SAVELIST_KEY = "Savelist";	//列表保存
	public static final String COMMOMLIST_KEY = "Commonlist";	//列表公共页
	
	
	
	/**
	 * 系统运行模式   zhucong:主从    bingxing:并行
	 */
	public static final String RUNMODEL_ZC = "zhucong";
	public static final String RUNMODEL_BX = "bingxing";
	
	/**
	 * 服务器操作系统 00 unknown   01 Windows 02 Linux
	 */
	public static final String OS_CODE_UNKNOWN = "00";
	public static final String OS_CODE_WINDOWS = "01";
	public static final String OS_CODE_LINUX = "02";
	
	/**
	 * 平台类型  01:云平台  02:托管平台  03:第三方平台
	 */
	public static final String PTLX_YUN = "01"; 
	public static final String PTLX_TG = "02"; 
	public static final String PTLX_DSF = "03"; 
	

	/**
	 * 服务器检测返回状态码
	 */
	public static String STATUSCODE_OK = "0000";	//成功
	public static String STATUSCODE_FALSE = "1111";	//失败
	public static String STATUSCODE_FALSE_IPEXIST = "1001";		//ip已存在
	public static String STATUSCODE_FALSE_PINGERROR = "1011";	//ping不通
	public static String STATUSCODE_FALSE_SOCKETERROR = "1012";	//socket通信失败
	public static String STATUSCODE_FALSE_UNEXPECT = "1000";	//未知失败类型

	
	/**
	 * 服务器状态 0:正常  1:异常
	 */
	public static final String FWQSTATUS_ZC = "0"; 
	public static final String FWQSTATUS_YC = "1"; 
	
	
	/**
	 * 应用状态  0:已启动  1:未启动 
	 */
	public static final String YYZT_YQD = "0"; 
	public static final String YYZT_WQD = "1"; 
	
	/**
	 * 应用运行状态  0:正常  1:异常
	 */
	public static final String YYYYZT_ZC = "0"; 
	public static final String YYYYZT_YC = "1"; 
	
	/**
	 * 应用发布状态   0:正式  1:试运行
	 */
	public static final String FBZT_FB = "0"; 
	public static final String FBZT_SFB = "1"; 
	
	
	/**
	 * 后台自动完成状态  0:后台执行  1：前台执行
	 */
	public static final String isAutoRun_true = "0"; 
	public static final String isAutoRun_false = "1"; 
	
	/**
	 * 应用部署状态
	 */
	public static final String isDeploy_true = "0"; 
	public static final String isDeploy_false = "1"; 
	
	
	
	/**
	 * 应用操作   部署:DEPLOY  启动:START  停止:STOP   删除:REMOVE
	 */
	public static final String CONTROL_DEPLOY = "DEPLOY"; 
	public static final String CONTROL_START = "START"; 
	public static final String CONTROL_STOP = "STOP"; 
	public static final String CONTROL_REMOVE = "REMOVE"; 
	

	/**
	 * 试发布操作   试发布:SFB  回退:BACK  正式发布:ZSFB   
	 */
	public static final String RELEASE_SFB = "SFB"; 
	public static final String RELEASE_BACK = "BACK"; 
	public static final String RELEASE_ZSFB = "ZSFB"; 

	/**
	 * 是否启用Nginx
	 */
	public static final boolean isNginxUsed = "true".equals(SysConfigUtils.getProperty("nginx.isUsed")) ? true : false;
	
	/**
	 * Nginx服务器代理类型  01:静态地址      02：动态应用
	 */
	public static final String NGINX_DLLX_STATIC = "01";
	public static final String NGINX_DLLX_DTYY = "02";
	
	/**
	 * HTTP协议类型   01：http   02：https
	 */
	public static final String HTTP_XY_HTTP = "01";
	public static final String HTTP_XY_HTTPS = "02"; 
	
	/**
	 * Nginx服务器的代理方式   01:反向代理    02：重定向
	 */
	public static final String NGINX_DLFS_FXDL = "01";
	public static final String NGINX_DLFS_CDX = "02";
	
	/**
	 * Nginx服务器的配置类型  01: http     02: stream
	 */
	public static final String NGINX_PZLX_HTTP = "01";
	public static final String NGINX_PZLX_STREAM = "02";
	
	
	/**
	 * 服务路径默认前缀
	 */
	public static final String SERVERPATHPREFIX = "http://[ip]";
	
	
	/**
	 * 参数类型 0:入参 1：出参
	 */
	public static final String paramlx_in = "0";
	public static final String paramlx_out = "1";
	
	/**
	 * 服务、接口状态 0：已发布 1：未发布
	 */
	public static final String FWZT_YQD = "0";
	public static final String FWZT_WQD = "1";
	
	/**
	 * 服务访问权限 0:允许  1：禁止
	 */
	public static final String FWFWQX_ALLOW = "0";
	public static final String FWFWQX_UNALLOW = "1";
	
	/**
	 * 应用状态 0:已启动  1:未启动
	 */
	public static final String APPZT_YQD = "0"; 
	public static final String APPZT_WQD = "1"; 
	
	/**
	 * 路由类型 01：中心路由  02 ：自由路由
	 */
	public static final String LYLX_ZXLY = "01";
	public static final String LYLX_ZYLY = "02";
	
	/**
	 * 服务申请状态 01:有效 02：未提交	03：申请中	04：未通过
	 */
	public static final String FWSQZT_YX = "01";
	public static final String FWSQZT_DSH = "02";
	public static final String FWSQZT_SQZ = "03";
	public static final String FWSQZT_WTG = "04";
	
	 
	/**********************服务总线调度异常代码********************************/
	public static final String FWZX_GETURL_SUCCESS = "600000";//获取url成功!
	public static final String FWZX_GETURL_FFIP = "600001";//非法IP请求!
	public static final String FWZX_GETURL_FWIDNULL = "600002";//服务ID为空!
	public static final String FWZX_GETURL_FWLYNULL = "600003";//服务来源为空!
	public static final String FWZX_GETURL_CLIENTIPNOTALLOW = "600004";//客户端IP未授权访问该服务!
	public static final String FWZX_GETURL_FWNULL = "600005";// 服务未发布或已失效!
	public static final String FWZX_GETURL_YYWQD = "600006";// 应用未启动!
	public static final String FWZX_GETURL_LYLXNOTFOUND = "600007";// 未找到对应的路由类型!
	public static final String FWZX_GETURL_NOKYJQ = "600008";	//无可用集群
	
	
	public static final Map FWZX_CODE_MS = new HashMap();
	static{
		FWZX_CODE_MS.put("600000", "获取url成功!");
		FWZX_CODE_MS.put("600001", "非法IP请求!");
		FWZX_CODE_MS.put("600002", "服务ID为空!");
		FWZX_CODE_MS.put("600003", "服务来源为空!");
		FWZX_CODE_MS.put("600004", "客户端IP未授权访问该服务!");
		FWZX_CODE_MS.put("600005", "服务未发布或已失效!");
		FWZX_CODE_MS.put("600006", "应用未启动!");
		FWZX_CODE_MS.put("600007", "未找到对应的路由类型!");
		FWZX_CODE_MS.put("600008", "无可用集群!");
	}
	
	
	/**********************应用操作代码********************************/
	public static final String WEBAPP_SUCCESS = "700000";//操作成功
	public static final String WEBAPP_ISONLINE = "700001";//应用已启动
	public static final String WEBAPP_WARNOEXISTS = "700002";//未找到war包
	public static final String WEBAPP_WARERROR = "700003";//启动遇到500错误
	public static final String WEBAPP_FAILED = "799999";//操作失败
	
	
	public static final Map MAP_WEBAPP_MS = new HashMap();
	static{
		MAP_WEBAPP_MS.put(WEBAPP_SUCCESS, "操作成功!");
		MAP_WEBAPP_MS.put(WEBAPP_ISONLINE, "应用已启动!");
		MAP_WEBAPP_MS.put(WEBAPP_WARNOEXISTS, "未找到war包!");
		MAP_WEBAPP_MS.put(WEBAPP_FAILED, "操作失败!");
		MAP_WEBAPP_MS.put(WEBAPP_WARERROR, "启动遇到500错误!");
		
	}
	
	/**********************故障中心操作代码********************************/
	public static final String GZZX_ISALARM_ALARM = "0";	//服务器应用处于报警状态
	public static final String GZZX_ISALARM_NOTALARM = "1";	//服务器应用不处于报警状态
	
	public static final String GZZX_GZLX_FWQ = "01";	//故障类型-服务器故障
	public static final String GZZX_GZLX_YY = "02";	//故障类型-应用故障
	public static final String GZZX_GZLX_YYYX = "03";	//故障类型-应用运行异常（log4j捕获）
	
	public static final String TS_TSLX_EMAIL = "01";	//推送类型-邮箱
	public static final String TS_TSLX_WECHAT = "02";	//推送类型-微信
	
	public static final String TS_TSZT_DTS = "01";		//推送状态-待推送
	public static final String TS_TSZT_CFZ = "02";		//推送状态-重发中
	public static final String TS_TSZT_TSSB = "03";		//推送状态-推送失败
	public static final String TS_TSZT_TSCG = "04";		//推送状态-推送成功
	
	public static final String TS_TITLE_FWQ = "故障中心-服务器异常";	//推送标题-服务器异常标题
	public static final String TS_TITLE_YY = "故障中心-应用异常"; //推送标题-应用异常标题
	
	public static final Map<String, String> TS_PUSHTEMPLATE = new HashMap();  //推送模板信息
	public static final String TS_TEMPLATE_FWQERROR = "TS_TEMPLATE_FWQERROR";
	public static final String TS_TEMPLATE_YYERROR = "TS_TEMPLATE_YYERROR";
	public static final String TS_TEMPLATE_YYYXERROR = "TS_TEMPLATE_YYYXERROR";
	static {
		TS_PUSHTEMPLATE.put(TS_TEMPLATE_FWQERROR, "${nickname},您好！" +
				"服务器【${fwqip}:${fwqdk}】发生异常信息，信息为【${ycxx}】。请尽快处理！");
		
		TS_PUSHTEMPLATE.put(TS_TEMPLATE_YYERROR, "${nickname}}您好，应用【${yyname}】发生异常信息，" +
				"所在服务器为：【${fwqip}:${fwqdk}】，异常信息为：${ycxx}。请尽快处理！");
	}
	
	/**
	 * 推送统一返回码
	 * 	失败码规则如下：
	 * 		4位数字
	 * 			首位数字代表返回结果的服务类型
	 * 				1xxx：公共通用信息（如缺少参数）
	 * 				2xxx：短信单独的信息
	 * 				3xxx：邮箱单独的信息
	 * 				4xxx：微信单独的信息
	 * 			第二位代表具体的失败原因分类
	 * 				?1xx：参数相关
	 * 				?2xx：推送结果相关
	 * 				?3xx：内容相关
	 * 				?4xx：异常信息相关
	 */
//	public static final String STATUSCODE_OK = "0000";	//通用成功
	public static final String STATUSCODE_ERR = "9999";	//通用失败
	/*
	 * 公共通用信息
	 */
	public static final String STATUSCODE_ERR_COM_ARGLOSS = "1100";		//缺失参数
	public static final String STATUSCODE_ERR_COM_ARGERR = "1101";		//参数格式不对
	public static final String STATUSCODE_ERR_COM_PUSHERR = "1200";		//推送失败
	public static final String STATUSCODE_ERR_COM_HASPUSHERR = "1210";		//有推送失败的
	
	/*
	 * 邮箱相关
	 */
	public static final String STATUSCODE_ERR_EMAIL_NOHEADORMSG	 = "3300";		//没有标题或者内容
	
	/**
	 * 应用默认原始版本号
	 */
	public static final String YYVERSION_DEFAULT = "1.0";
	
	/**
	 * 推送相关
	 */
	public static final String WECHAT_MSG_TEMPLATE_FWQ = SysConfigUtils.getProperty("wechat.msg.template.fwq");
	public static final String WECHAT_MSG_TEMPLATE_YY = SysConfigUtils.getProperty("wechat.msg.template.yy");
	
}
