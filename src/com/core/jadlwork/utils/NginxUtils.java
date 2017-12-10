package com.core.jadlwork.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.SysConfigUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.nginx.NginxConfigBean;

/**
 * Nginx操作的工具类
 * @类名: NginxUtils
 * @作者: 李春晓
 * @时间: 2017-2-20 下午1:13:06
 */
public class NginxUtils {

	private static Logger log = Logger.getLogger(NginxUtils.class);
	
	/**
	 * 根据Nginxid获取Nginx配置文件保存的目录   (Nginx配置文件保存在Tomcat目录下的backup/nginxConfs/nginxid下)
	 * @param nid	nginxid，配置文件保存根据nginxid进行分类保存
	 * @return: File
	 */
	public static File getNginxConfDir(String nid) {
		try {
			File nginxConfFile = new File(SystemUtils.getTomcatBackUpUrl()+File.separator+"nginxConfs"+File.separator+nid);
			return nginxConfFile;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据Nginxid获取本地Nginx配置文件的目录地址
	 * @param nid nginx id
	 * @return: File
	 */
	public static File getNginxConfFile(String nid) {
		try {
			File nginxConfFile = new File(SystemUtils.getTomcatBackUpUrl()+File.separator+"nginxConfs"+File.separator+nid, "nginx.conf");
			return nginxConfFile;
		} catch (Exception e) {
			return null;
		}
	}
}
