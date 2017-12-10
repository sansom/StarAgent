package com.core.jadlwork.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;

/**
 * 完成执行cmd的工具类
 * @类名: SystemUtils
 * @描述: TODO
 * @作者: 李春晓
 * @时间: 2017-2-4 下午5:01:46
 */
public class SystemUtils {

	/**
	 * Java执行命令的工具类
	 * @param osType 	操作系统类型
	 * @param commandStr	执行的命令语句,不要带上cmd /C ，只输入内容
	 * @param openDir	在指定的文件目录中执行命令
	 * @return: String	返回的信息
	 */
	public static String execCommond(String osType, String commandStr, File openDir){
		
		//要查询的参数
		String[] para = {"cmd","/C",commandStr};
		//判断操作系统类型
		if (SystemConstants.OS_CODE_LINUX.equals(osType)) {
			para[0] = "/bin/sh";
			para[1] = "-c";
		}
		BufferedReader br = null;
		try {
			Process p = null;
			if (openDir != null) {
				p = Runtime.getRuntime().exec(para,null,openDir);
			}else {
				p = Runtime.getRuntime().exec(para);
			}
			p.waitFor();
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),Charset.forName("GBK")));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 判断系统版本
	 * @return: String
	 */
    public static String getOSType(){
    	try {
    		String osName = System.getProperty("os.name");
        	if (osName.startsWith("Windows")) {
    			// Windows系统
        		return SystemConstants.OS_CODE_WINDOWS;
    		}else if (osName.startsWith("Linux")) {
    			// Linux系统
    			return SystemConstants.OS_CODE_LINUX;
    		}else {
    			// 未知系统
    			return SystemConstants.OS_CODE_UNKNOWN;
    		}
		} catch (Exception e) {
			return SystemConstants.OS_CODE_UNKNOWN;
		}
    }
    
    /**
     * 获取上传的根目录，既是在struts.properties中配置的目录
     * @return: String
     */
    public static String getUploadRootPath() {
    	String uploadRootPath = SysConfigUtils.getProperty("waruploadpath_window");
    	if (getOSType().equals(SystemConstants.OS_CODE_LINUX)) {
			uploadRootPath = SysConfigUtils.getProperty("waruploadpath_linux");
		}
    	return uploadRootPath;
    }

    /**
     * 获取Tomcat下的备份路径
     * @return: String
     */
    public static String getTomcatBackUpUrl() {
		return System.getProperty("catalina.home") + File.separator + SysConfigUtils.getProperty("war.dir.backup","backup");
	}
    
    /**
     * 获取应用试发布文件存储路径
     * @return: String
     */
    public static String getAppSfbSrc(String appName) {
    	appName = appName.replace(".war", "");
		return getTomcatBackUpUrl() + File.separator + appName + File.separator + SysConfigUtils.getProperty("war.dir.sfb","sfb");
	}
    
    
    /**
     * 获取应用试发布临时文件存储路径
     * @return: String
     */
    public static String getAppSfbtempSrc(String appName) {
    	appName = appName.replace(".war", "");
		return getTomcatBackUpUrl() + File.separator + appName + File.separator + SysConfigUtils.getProperty("war.dir.sfb","sfb") + File.separator + "temp";
	}
    
    /**
     * 获取应用正式版本文件存储路径
     * @return: String
     */
    public static String getAppWarSrc(String appName,String version) {
    	appName = appName.replace(".war", "");
		return getTomcatBackUpUrl() + File.separator + appName + File.separator + version + File.separator ;
	}
    
}
