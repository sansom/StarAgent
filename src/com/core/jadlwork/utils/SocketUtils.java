package com.core.jadlwork.utils;

import java.io.File;

import com.core.jadlsoft.utils.StringUtils;
import com.ctc.wstx.util.StringUtil;


/**
 * Socket操作的工具类
 * @ClassName: SocketUtils
 * @author: 李春晓
 * @date: 2016-12-22 下午04:46:49
 */
public class SocketUtils {


	/**
	 * 获取服务器信息
	 * @param fwqip
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 上午10:20:53
	 */
	public static String getServerInfo(String fwqip){
		if (StringUtils.isEmpty(fwqip)) {
			return "";
		}
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getServerInfo'}");
	}
	
	
	/**
	 * 得到指定服务器tomcat日志
	 * @param fwqip
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-14 上午9:09:21
	 */
	public static String getTomcatLogs(String fwqip){
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getTomcatLogs'}");
	}
	

	/**
	 * 获取指定服务器应用日志文件
	 * @param fwqip
	 * @param logSrc
	 * @param appname
	 * @author wujiaxu
	 * @Time 2017-6-14 上午10:29:57
	 */
	public static String getAppLogPath(String fwqip,String logSrc, String appname) {
		// 通过socket，获取该应用的日志文件信息
		String info = "{do:'getAppLogPath',applogsrc:'" + logSrc + "',appname:'"+ appname + "'}";
		return SocketClient.getInstance().socketTest(fwqip,info);
		
	}
	
	/**
	 * 替换Nginx配置文件
	 * @param ip	服务器ip
	 * @param nginxConfPath	上传文件的本地路径
	 * @param nginxRootPath Nginx所在服务器的根目录
	 * @return: boolean
	 */
	public static boolean replaceNginxConf(String ip, String nginxConfPath, String nginxRootPath){
		//先将Nginx配置文件上传
		boolean uploadWar = SocketClient.getInstance().uploadWar(ip, nginxConfPath);
		if (uploadWar) {
			//发送替换的消息给服务器
			String info = SocketClient.getInstance().socketTest(ip, "{do:'replaceNginxConf&"+nginxRootPath+"'}");		//TODO  替换Nginx的配置文件
			if ("success".equals(info)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取指定服务器中的Nginx启动状态
	 * @param fwqip	服务器ip
	 * @return: String 
	 */
	public static String getNginxStatus(String fwqip) {
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getNginxStatus'}");
	}

	/**
	 * 获取指定服务器的Nginx配置信息
	 * 		使用socket下载，将Nginx服务器中的Nginx配置文件下载下来
	 * @param fwqip	服务器的ip
	 * @param nginxRootPath	Nginx根目录
	 * @return: String
	 */
	public static String getNginxConfig(String fwqip, String nginxRootPath, String nid) {
		
		//测试是否正常通信
		String info = SocketUtils.getServerInfo(fwqip);
		if (info == null || info.equals("")) {
			return "服务器未开启！";
		}
		
		String destDir = SystemUtils.getTomcatBackUpUrl() + File.separator + "nginxConfs" + File.separator + nid;
		boolean isDownload = SocketClient.getInstance().downloadStream(fwqip, "currentNginxConfig&"+nginxRootPath, destDir, "nginx_fwq.conf");
		if (isDownload) {
			File file = new File(destDir, "nginx_fwq.conf");
			if (!file.exists()) {
				return "下载线上配置出错！";
			}
		}else {
			return "下载线上配置出错！";
		}
		return "success";
	}
	
	/**
	 * 部署应用
	 * @param fwqip
	 * @param warname
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 上午9:23:54
	 */
	public static boolean webapp_deploy(String fwqip,String warsrc) {
		return SocketClient.getInstance().uploadWar(fwqip, warsrc);
	}
	
	/**
	 * 启动应用
	 * @param fwqip
	 * @param warname
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 上午9:15:59
	 */
	public static String webapp_start(String fwqip,String warname) {
		if(warname != null && warname.indexOf(".war") != -1){
			warname = warname.substring(0, warname.lastIndexOf("."));
		}
		String info = "{do:'start',warname:'"+warname+"'}";
		return SocketClient.getInstance().socketTest(fwqip, info);
	}
	
	/**
	 * 停止应用
	 * @param fwqip
	 * @param warname
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 上午9:27:51
	 */
	public static String webapp_stop(String fwqip,String warname) {
		if(warname != null && warname.indexOf(".war") != -1){
			warname = warname.substring(0, warname.lastIndexOf("."));
		}
		String info = "{do:'stop',warname:'"+warname+"'}";
		return SocketClient.getInstance().socketTest(fwqip, info);
	}
	
	/**
	 * 应用是否已部署
	 * @param fwqip
	 * @param warname
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-13 上午9:15:59
	 */
	public static String webapp_isDeployed(String fwqip,String warname) {
		if(warname != null && warname.indexOf(".war") != -1){
			warname = warname.substring(0, warname.lastIndexOf("."));
		}
		String info = "{do:'isDeployed',warname:'"+warname+"'}";
		return SocketClient.getInstance().socketTest(fwqip, info);
	}


	/**
	 * 测试指定的ip是否可以进行socket通信，可以通信则将该服务器的操作系统类型返回
	 * @Title: getCommunicateInfo
	 * @param ip 指定的服务器ip
	 * @return: 返回success+":"+fwqczxtDm
	 */
	public static String getCommunicateInfo(String ip){
		return SocketClient.getInstance().socketTest(ip, "{do:'testLink'}");
	}

	/**
	 * 得到指定的服务器的运行日志
	 * @param fwqip 指定的服务器的ip
	 * @return 包含文件列表名和对应大小的set集合的json格式
	 */
	public static String getFwqLogs(String fwqip){
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getLogs'}");
	}


	/**
	 * 按开始位置获取Nginx的log信息
	 * @param fwqip	服务器ip
	 * @param pos 起始位置
	 * @param nginxRootPath Nginx根目录
	 * @return: void
	 */
	public static String getNginxLogByPos(String fwqip, String pos, String nginxRootPath) {
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getNginxLogByPos&"+nginxRootPath+"&"+pos+"'}");
	}
	
	/**
	 * 按开始位置获取文件的信息（在线动态预览的效果）
	 * @param fwqip fwqip
	 * @param pos	开始位置
	 * @param logname 日志名称
	 * @return: String
	 */
	public static String getTomcatLogByPos(String fwqip, String pos, String logname) {
		return SocketClient.getInstance().socketTest(fwqip, "{do:'getTomcatLogByPos&"+logname+"&"+pos+"'}");
	}
	
	/**
	 * 下载Nginx日志到 tomcat/backup/nginxLogs/nid下
	 * @param fwqip
	 * @param nginxRootPath
	 * @param nid
	 * @return: String
	 */
	public static String downloadNginxLog(String fwqip, String nginxRootPath, String nid) {
		//测试是否正常通信
		String info = SocketUtils.getServerInfo(fwqip);
		if (info == null || info.equals("")) {
			return "服务器未开启！";
		}
		String destDir = SystemUtils.getTomcatBackUpUrl() + File.separator + "nginxLogs" + File.separator + nid;
		boolean isDownload = SocketClient.getInstance().downloadStream(fwqip, "nginxAccessLog&"+nginxRootPath, destDir, "access.log");
		if (isDownload) {
			File file = new File(destDir, "access.log");
			if (!file.exists()) {
				return "下载日志出错！";
			}
		}else {
			return "下载日志出错！";
		}
		return "success";
	}
	
}
