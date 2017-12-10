package com.core.jadlwork.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.SpringBeanFactory;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.nginx.NginxBean;

/**
 * 生成Nginx配置文件工具类
 * @类名: GenerateNginxConfUtils
 * @作者: 李春晓
 * @时间: 2017-6-27 上午9:07:43
 */
public class GenerateNginxConfUtils {

	private static Logger log = Logger.getLogger(GenerateNginxConfUtils.class);
	
	/**
	 * 通过模板的方式生成Nginx配置文件
	 * @param nginxConfInfos   要生成的Nginx配置文件的集合
	 * @param nid	配置文件所属的Nginx服务器id
	 * @return: void
	 */
	public static ResultBean generateNginxConfByTemplete(Map nginxConfInfos, String nid){
		INginxManager nginxManager = (INginxManager) SpringBeanFactory.getBean("nginxManager");
		
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "生成成功!");
		
		NginxBean nginxBean = nginxManager.getNginxBean(nid);
		if (nginxBean == null) {
			log.error("该Nginx不存在！");
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "ID为【"+nid+"】的Nginx不存在！");
			return resultBean;
		}
		
		try {
			if (nid == null || nid.equals("")) {
				log.error("生成NginxConf传递的nginxid为空");
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "要生成的Nginxid为空！");
				return resultBean;
			}
			
			//1、获取模板文件
			File templeteConfFile = new File(NginxUtils.getNginxConfDir(nid), "nginxConfTemplete.conf");
			if (!templeteConfFile.isFile()) {	//模板文件不存在
				log.error("【生成Nginx】Nginx【"+nginxBean.getFwqname()+"】的Nginx服务器未找到模板文件！");
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "Nginx【"+nginxBean.getFwqname()+"】未找到对应的模板文件！");
				return resultBean;
			}
			//2、校验数据格式问题
			String msg = checkConfData(nginxConfInfos);
			if (msg != null && !msg.equals("")) {
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, msg);
				return resultBean;
			}
			//3、创建待生成文件
			File toGenerateFile = NginxUtils.getNginxConfFile(nid);
			if (toGenerateFile.isFile()) {
				//存在文件，先删除
	        	try {
	        		toGenerateFile.delete();
				} catch (Exception e) {
					try {
						toGenerateFile.delete();
					} catch (Exception e2) {
						log.error("生成Nginx配置文件之前删除当前的ngnix.conf文件失败，目录为【"+toGenerateFile.getAbsolutePath()+"】\r\n可能会导致生成信息不准！");
					}
				}
			}
			//4、创建流对象
//			InputStreamReader isr = new InputStreamReader(new FileInputStream(templeteConfFile), FileUtils.getFileEncode(templeteConfFile.getAbsolutePath()));
			InputStreamReader isr = new InputStreamReader(new FileInputStream(templeteConfFile), "UTF-8");
			OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream(toGenerateFile), "UTF-8");
			BufferedReader br = new BufferedReader(isr);	//文件读取流
			BufferedWriter bw = new BufferedWriter(osr);	//文件写入流
			//5、拿到试发布规则
			List sfbgzList = nginxManager.getSfbpzList(nid);
			
			//6、写入文件
			/*
			 * 根据读取的标识去生成，只生成反向代理的信息，读到标志之前都按照模板去写，读到标志那行就写入要加入的文件，之后再继续写模板
			 */
			String readContent = "";
			while((readContent=br.readLine())!=null){
				if (readContent.contains("+&nginx&+")) {
					//说明这一行是标志行，要写入动态生成的内容
					writeToFile(bw, nginxConfInfos, sfbgzList);
				}else {
					//其他的都读模板生成
					bw.write(readContent);
					bw.newLine();
				}
			}
			
			//关流
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultBean;
	}

	/*
	 * 校验数据格式问题
	 */
	private static String checkConfData(Map nginxConfInfos) {
		if (nginxConfInfos == null || nginxConfInfos.size()==0) {
			log.error("【生成Nginx配置】待生成的配置信息为空，生成之后可能造成用户无法访问应用！");
			return "";
		}
		
		Set<String> jqids = nginxConfInfos.keySet();
		for (String jqid : jqids) {
			Map jqMap = (Map) nginxConfInfos.get(jqid);
			//对于每一个集群信息，需要去生成 1个Server信息和 多个(应用的个数)upstream信息
			Map yyinfoMap = (Map) jqMap.get("yyinfo");
			if (yyinfoMap == null) {
				return "集群【"+jqMap.get("jqname")+"】配置信息数据格式错误！";
			}
			
			Set yyids = yyinfoMap.keySet();
			for (Object yyid : yyids) {
				Map yyMap = (Map) yyinfoMap.get(yyid);
				if (yyMap == null) {
					return "集群【"+jqMap.get("jqname")+"】配置信息数据格式错误！";
				}
				Map fbfwqinfo = (Map) yyMap.get("fbfwqinfo");
				if (fbfwqinfo == null) {
					return "集群【"+jqMap.get("jqname")+"】配置信息数据格式错误！";
				}
			}
		}
		return "";
	}
	
	/*
	 * 将动态的配置信息写入Nginx配置文件中
	 */
	private static void writeToFile(BufferedWriter bw, Map nginxConfInfos, List sfbgzList) throws Exception {
		if (nginxConfInfos == null || nginxConfInfos.size() == 0) {
			return;
		}
		Set<String> jqids = nginxConfInfos.keySet();	//集群id集合
		for (String jqid : jqids) {
			Map jqMap = (Map) nginxConfInfos.get(jqid);
			//对于每一个集群信息，需要去生成 1个Server信息和 多个(应用的个数)upstream信息
			Map yyinfoMap = (Map) jqMap.get("yyinfo");
			if (yyinfoMap.size()==0) {
				//说明没有应用信息，就跳过
				continue;
			}
			String fwym = jqMap.get("fwym")==null ? "" : (String)jqMap.get("fwym");
			String fwdk = jqMap.get("fwdk")==null ? "" : (String)jqMap.get("fwdk");
			//说明有应用信息
			//Server START=========================================================
			bw.write(SERVER_UP_TEMPLETE.replaceAll("&fwdk&", fwdk).replaceAll("&fwym&", fwym));
			//下面的根据应用的不同生成不同的location
			Set<String> yyids = yyinfoMap.keySet();
			for (String yyid : yyids) {
				Map yyMap = (Map) yyinfoMap.get(yyid);
				String locationStr = getLocationStr(fwdk, fwym, yyMap, sfbgzList);
				if (StringUtils.isEmpty(locationStr)) {
					continue;
				}
				bw.write(locationStr);
			}
			//SERVER END============================================================
			bw.write(SERVER_DOWN_TEMPLETE);
			//upstream===============================================================
			//upstream根据应用的不同会有多个
			for (String yyid : yyids) {
				Map yyMap = (Map) yyinfoMap.get(yyid);
				String upstreamStr = getUpstreamStr(fwym, fwdk, yyMap);
				if (StringUtils.isEmpty(upstreamStr)) {
					continue;
				}
				bw.write(upstreamStr);
			}
		}
	}
	
	//获取location信息
	private static String getLocationStr(String fwdk, String fwym, Map yyMap, List sfbgzList){
		/*
		 * 1、判断该应用是否有试发布的服务器信息
		 * 2、如果没有，就直接生成  proxy_pass http://&appname&.&fwym&; 即可
		 * 3、如果有，就根据规则信息生成对应的if语句，并自动将试发布的使用upstream（前面加上test）
		 */
		
		String warname = yyMap.get("warname")==null ? "" : (String)yyMap.get("warname");
		if (warname.equals("")) {
			//应用war包名称为空
			return null;
		}
		String appname = warname.substring(0, warname.length()-4);
		String dkStr = "";
		if (!"80".equals(fwdk)) {
			dkStr = ":"+fwdk;
		}
		
		Map sfbfwqinfo = (Map) yyMap.get("sfbfwqinfo");
		if (sfbfwqinfo == null || sfbfwqinfo.size()==0) {
			//没有试发布保持之前的即可
			return LOCATION_TEMPLETE.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr);
		}else {
			//根据规则设置访问地址的if语句
			if (sfbgzList == null || sfbgzList.size()==0) {
				//没有规则，没有规则说明走的全部都是正式的，因此跟之前一样
				return LOCATION_TEMPLETE.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr);
			}else {
				StringBuffer sb = new StringBuffer();
				//追加试发布头部
				sb.append(LOCATION_SFB_FIX_UP_TEMPLETE.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
				//遍历追加if语句
				//追加默认if
//				sb.append(LOCATION_SFB_IF_DEFAULT_TEMPLETE.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
				for (Map sfbgzMap : (List<Map>)sfbgzList) {
					Object regex = sfbgzMap.get("regex");
					if (regex != null && !regex.equals("")) {
						sb.append(LOCATION_SFB_IF_TEMPLETE.replaceAll("&regex&", (String) regex).replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
					}
				}
				/**
				 * 如果有正式发布的信息，就追加试发布的底部
				 */
				Map fbfwqinfoMap = (Map) yyMap.get("fbfwqinfo");	//发布的服务器信息
				if (fbfwqinfoMap != null && fbfwqinfoMap.size()>0) {
					sb.append(LOCATION_SFB_ZSFBDZ_DOWN_TEMPLETE.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
				}
				sb.append(LOCATION_SFB_FIX_DOWN_TEMPLETE);
				return sb.toString();
			}
		}
	}
	
	//获取upstream语句
	private static String getUpstreamStr(String fwym, String fwdk, Map yyMap) {
		StringBuffer upstreamSb = new StringBuffer();
		String warname = yyMap.get("warname")==null ? "" : (String)yyMap.get("warname");
		if (warname.equals("")) {
			//应用war包名称为空
			return null;
		}
		String appname = warname.substring(0, warname.length()-4);
		String dkStr = "";
		if (!"80".equals(fwdk)) {
			dkStr = ":"+fwdk;
		}
		
		Map fbfwqinfoMap = (Map) yyMap.get("fbfwqinfo");	//发布的服务器信息
		Map sfbfwqinfoMap = (Map) yyMap.get("sfbfwqinfo");	//试发布的服务器信息
		
		//先写发布的信息
		//1、获取server item的值
		StringBuffer sb = new StringBuffer();
		if (fbfwqinfoMap != null && fbfwqinfoMap.size()>0) {
			Set<String> fwqids = fbfwqinfoMap.keySet();
			for (String fwqid : fwqids) {
				Map fwqMap = (Map) fbfwqinfoMap.get(fwqid);
				String fwqip = fwqMap.get("fwqip")==null ? "" : (String)fwqMap.get("fwqip");
				String dk = fwqMap.get("dk")==null ? "" : (String)fwqMap.get("dk");
				sb.append(SERVER_ITEM_TEMPLETE.replaceAll("&fwqip&", fwqip).replaceAll("&dk&", dk));
			}
			//2、获取upstream的值
			upstreamSb.append(UPSTREAM_TEMPLETE.replaceAll("&serveritem&", sb.toString())
					.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
		}
		
		
		//如果没有试发布的，直接将这个返回即可，如果有试发布的加上试发布的信息
		if (sfbfwqinfoMap == null || sfbfwqinfoMap.size()==0) {
			return upstreamSb.toString();
		}
		//试发布的信息
		StringBuffer sfbsb = new StringBuffer();
		if (sfbfwqinfoMap != null && sfbfwqinfoMap.size()>0) {
			Set<String> fwqids = sfbfwqinfoMap.keySet();
			for (String fwqid : fwqids) {
				Map fwqMap = (Map) sfbfwqinfoMap.get(fwqid);
				String fwqip = fwqMap.get("fwqip")==null ? "" : (String)fwqMap.get("fwqip");
				String dk = fwqMap.get("dk")==null ? "" : (String)fwqMap.get("dk");
				sfbsb.append(SERVER_ITEM_TEMPLETE.replaceAll("&fwqip&", fwqip).replaceAll("&dk&", dk));
			}
			if (sfbsb.length()>0) {
				upstreamSb.append(UPSTREAM_SFB_TEMPLETE.replaceAll("&serveritem&", sfbsb.toString())
						.replaceAll("&appname&", appname).replaceAll("&fwym&", fwym).replaceAll("&fwdk&", dkStr));
			}
		}
		return upstreamSb.toString();
	}
	
	/**
	 * 系统换行符
	 */
	private static String liner = System.getProperty("line.separator");
	
	/**
	 * SERVER开始模板
	 */
	private static String SERVER_UP_TEMPLETE = "        server {" + liner +
			"        	listen       &fwdk&;" + liner + 
			"        	server_name  &fwym&;" + liner;
	/**
	 * SERVER结束模板
	 */
	private static String SERVER_DOWN_TEMPLETE = "        error_page   500 502 503 504  /50x.html;" + liner +
			"        location = /50x.html {" + liner + 
			"            root   html;" + liner + 
			"        }" + liner +
			"    }" + liner;
	
	/**
	 * Location模板
	 */
	private static String LOCATION_TEMPLETE = "        location /&appname& {" +liner+
			"            proxy_pass http://&appname&.&fwym&;" +liner+
			"            proxy_set_header Host $host&fwdk&;" +liner+
			"       	    proxy_set_header X-Real-IP $remote_addr;" +liner+
			"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;" +liner+
			"            root   html;" +liner+
			"            index  index.html index.htm;" +liner+
			"            proxy_connect_timeout 3;" +liner+
			"        }" + liner;
	
	/**
	 * 试发布的location的固定部分(头部)的模板
	 */
	private static String LOCATION_SFB_FIX_UP_TEMPLETE =  "        location /&appname& {" +liner+
			"            proxy_set_header Host $host&fwdk&;" +liner+
			"       	    proxy_set_header X-Real-IP $remote_addr;" +liner+
			"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;" +liner+
			"            root   html;" +liner+
			"            index  index.html index.htm;" +liner+
			"            proxy_connect_timeout 3;" +liner;
	/**
	 * 试发布的location的正式发布地址部分的模板
	 */
	private static String LOCATION_SFB_ZSFBDZ_DOWN_TEMPLETE = "            proxy_pass http://&appname&.&fwym&;" +liner;
			
	/**
	 * 试发布的location的固定部分(底部)的模板
	 */
	private static String LOCATION_SFB_FIX_DOWN_TEMPLETE = "        }" + liner;
	
	/**
	 * 试发布的location的默认IF（默认规则（如果$http_x_forwarded_for为空，认为是内网，也访问试发布的地址））的模板
	 */
	private static String LOCATION_SFB_IF_DEFAULT_TEMPLETE = "            if ($http_x_forwarded_for ~ \"\")" + liner +
			"            	{" + liner +
			"            		proxy_pass http://test.&appname&.&fwym&;" + liner +
            "		            break;" + liner +
			"				}" + liner; 
	/**
	 * 试发布的location的每一个IF（规则）的模板
	 */
	private static String LOCATION_SFB_IF_TEMPLETE = "            if ($http_x_forwarded_for ~ \"&regex&\")" + liner +
			"            	{" + liner +
			"            		proxy_pass http://test.&appname&.&fwym&;" + liner +
            "		            break;" + liner +
			"				}" + liner; 
	
	/**
	 * 正式发布的upstream模板
	 */
	private static String UPSTREAM_TEMPLETE = "        upstream &appname&.&fwym& {" + liner +
			"        	# simple round-robin" + liner +
			"        	ip_hash;" + liner + 
			"        	&serveritem&" + liner + 
			"        	#check interval=3000 rise=2 fall=5 timeout=1000;" + liner + 
			"        }" + liner;
	/**
	 * 试发布的upstream模板（前面加了个test）
	 */
	private static String UPSTREAM_SFB_TEMPLETE = "        upstream test.&appname&.&fwym& {" + liner +
			"        	# simple round-robin" + liner +
			"        	ip_hash;" + liner + 
			"        	&serveritem&" + liner + 
			"        	#check interval=3000 rise=2 fall=5 timeout=1000;" + liner + 
			"        }" + liner;
	
	/**
	 * SERVER（UPSTREAM中）模板
	 */
	private static String SERVER_ITEM_TEMPLETE = "server &fwqip&:&dk&;"+liner;
}
