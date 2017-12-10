package com.core.jadlwork.struts.action.fwqgl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.fwqgl.IFwqManager;
import com.core.jadlwork.business.fwqgl.INginxManager;
import com.core.jadlwork.business.jqgl.IJqManager;
import com.core.jadlwork.business.yygl.IYyManager;
import com.core.jadlwork.cache.nginx.NginxCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.nginx.NginxBean;
import com.core.jadlwork.model.nginx.NginxConfigBean;
import com.core.jadlwork.utils.CmdUtils;
import com.core.jadlwork.utils.FileUtils;
import com.core.jadlwork.utils.GenerateNginxConfUtils;
import com.core.jadlwork.utils.NginxUtils;
import com.core.jadlwork.utils.SocketUtils;
import com.core.jadlwork.utils.SystemUtils;

/**
 * Nginx操作的action
 * @类名: NginxAction
 * @作者: 李春晓
 * @时间: 2017-2-16 下午4:28:33
 */
public class NginxAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(NginxAction.class);

	// myFile属性用来封装上传的文件  
    private File myFile;  
    // myFileContentType属性用来封装上传文件的类型  
    private String myFileContentType;  
    // myFileFileName属性用来封装上传文件的文件名  
    private String myFileFileName;
    //统一使用的名字
    private String fname = "nginxConfTemplete.conf"; 
	
	/**
	 * 进入编辑界面
	 * @return: String
	 */
	public String edit(){
		String nid = request.getParameter("nid");
		if (nid != null) {
			//更改
			nginxBean = nginxManager.getNginxBean(nid);
			//需要将该Nginx所管理的集群信息返回
			List jqNginxList = jqManager.getjqListByNginxId(nid);
			request.setAttribute("nginxBean", nginxBean);
			request.setAttribute("jqNginxList", jqNginxList);
		}
		//将集群信息放入
		List jqList = jqManager.getJqList();
		request.setAttribute("jqList", jqList);
		
		return "edit";
	}
	
	/**
	 * 检测服务器ip是否可用
	 * @return: void
	 */
	public void checkfwq() {
		try {
			//设置默认统一返回对象
			ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
			String fwqip = request.getParameter("fwqip");
			// 1.检测服务器是否已接入
			List<Map> nginxList = nginxManager.getNginxList();
			if (nginxList != null && nginxList.size()>0) {
				for (Map nginxMap : nginxList) {
					if (fwqip != null && fwqip.equals(nginxMap.get("fwqip"))) {
						//说明已经存在
						resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "该服务器的ip已经存在");
						ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
						return;
					}
				}
			}
			// 2、检测ip是否ping通
			if (!CmdUtils.isPing(fwqip)) {
				// 说明ping不通
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_PINGERROR,"此服务器ip不能ping通");
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}
			
		
			// 3、检测socket是否正常
			String serverInfo = SocketUtils.getServerInfo(fwqip);
			if (serverInfo == null ) {
				//socket不能正常通信
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE_PINGERROR,"此服务器不能正常socket通信");
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}
			//将返回的服务器操作系统值作为参数返回给页面
			try {

				JSONObject jsonObject = JSONObject.fromObject(serverInfo);
				resultBean.setArg1(jsonObject.get("fwqczxt"));
			} catch (Exception e) {
				logger.error("获取服务器操作系统失败");
			}
			//ip检测通过
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有Nginx服务器的运行状态
	 * @功能: TODO
	 * @return: void
	 */
	public void getNginxStatus() {
		try {
			//获取要获取的信息，id和服务器ip，格式为： id~fwqip，多个中间用逗号分隔
			String fwqInfos = request.getParameter("fwqInfos");
			String[] fwqInfosArr = fwqInfos.split(",");

			StringBuilder sb = new StringBuilder();	//最终返回给页面的内容
			
			if (fwqInfosArr != null && fwqInfosArr.length>0) {
				for (String fwqInfo : fwqInfosArr) {	//每一条信息，id~fwqip
					if (fwqInfo.equals("")) {
						continue;
					}
					String fwqip = fwqInfo.split("~")[1];
//					String fwqczxt = fwqInfo.split("~")[2];
					//每一条信息通过通信工具类获取Nginx的状态，拼接信息返回
					String status = SocketUtils.getNginxStatus(fwqip);
					//如果为空的话，说明服务器没有开
					if (status == null || status.equals("")) {
						status = "2";
					}
					sb.append(fwqInfo).append("~").append(status).append(","); 	// id~fwqip~status
				}
			}
			//去除最后一个逗号
			if (sb.length()>0) {
				sb.deleteCharAt(sb.length()-1);
			}
			//将结果返回
			ResponseUtils.render(response, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增Nginx服务器
	 * @return: String
	 */
	public String save(){
		//在上传Nginx配置文件的时候，需要根据上传的Nginx的id放在不同的目录中，因此需要在这里获取Nginxid
		String id = null;
		synchronized (nginxBean) {
			id = String.valueOf(nginxManager.getNextval());
		}
		nginxBean.setId(id);
		this.uploadNginxConf(nginxBean.getId());
		int save = nginxManager.saveNginx(nginxBean);
		if (save>0) {
			updateNginxJqxx(request, nginxBean.getId());
			updateNginxConfAfterEdit(nginxBean.getId());
		}
		return "list";
	}
	
	/**
	 * 更改Nginx服务器
	 * @return: String
	 */
	public String update(){
		this.uploadNginxConf(nginxBean.getId());
		int i = nginxManager.updateNginx(nginxBean);
		if (i>0) {
			updateNginxJqxx(request, nginxBean.getId());
			updateNginxConfAfterEdit(nginxBean.getId());
		}
		return "list";
	}
	
	private void updateNginxConfAfterEdit(String nid) {
		//更新Nginx配置信息
		Map map = nginxManager.getJqFwqYyByNginxId(nid);
		if (map == null || map.size() == 0) {
			//没有对应的信息
		}else {
			//更新Nginx
			ResultBean bean = nginxManager.updateNginxConfByNginxid(nid);
			if (bean == null) {
				//出现未知错误
				nginxManager.updateNginxGxzt(nginxBean.getId(), SystemConstants.ZT_FALSE, "出现未知错误！");
			}else {
				if (!SystemConstants.STATUSCODE_OK.equals(bean.getStatusCode())) {
					//更新失败
					nginxManager.updateNginxGxzt(nginxBean.getId(), SystemConstants.ZT_FALSE, (String) bean.getMsg());
					//交给队列管理
					NginxCache.getInstance().changedNginxids.add(nid);
				}
			}
		}
	}
	
	/*
	 * 更新Nginx集群信息
	 */
	private void updateNginxJqxx(HttpServletRequest req, String nid){
		String[] jqxxs = req.getParameterValues("jqxx");
		jqManager.updateNginxJqxx(jqxxs, nid);
	}
	
	/*
	 * 上传Nginx配置文件 
	 */
	private void uploadNginxConf(String nid) {
		if(myFile != null){
			try {
				//1.创建文件保存目录
				File f = NginxUtils.getNginxConfDir(nid);
				if(!f.exists()){
					f.mkdirs();
				}
				
				//2.上传文件
				InputStream is = new FileInputStream(myFile);
				File toFile = new File(f, fname);
				OutputStream os = new FileOutputStream(toFile);
				byte[] buffer = new byte[1024];
				int length = 0;
	
				// 读取myFile文件输出到toFile文件中
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			} catch (Exception e) {
				throw new RuntimeException("上传nginx配置文件失败！",e);
			}
		}
	}
	
	/**
	 * 移除Nginx服务器
	 * @return: String
	 */
	public String remove(){
		String nid = request.getParameter("nid");
		nginxManager.deleteNginx(nid);
		return "list";
	}
	
	/**
	 * 配置Nginx服务器，进入指定Nginx服务器的配置列表
	 * @return: String
	 */
	public String config() {
		String nid = request.getParameter("nid");
		if (nid == null || nid.equals("")) {
			nid = request.getAttribute("nid")==null ? null : (String) request.getAttribute("nid");
		}
		if (nid == null || nid.equals("")) {
			throw new RuntimeException("进入Nginx配置出错，没有对应的Nginxid");
		}
		NginxBean nginxBean = nginxManager.getNginxBean(nid);
		
		//配置直接从动态信息中生成
		Map jqyyfwqInfo = nginxManager.getJqFwqYyByNginxIdForShow(nid);
		request.setAttribute("nginxBean", nginxBean);
		request.setAttribute("jqyyfwqInfo", jqyyfwqInfo);
		return "configlistPage";
	}
	
	/**
	 * 生成Nginx配置文件
	 * @return: void
	 */
	public void createNginxConf(){
		String nid = request.getParameter("nid");
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
		Map map = nginxManager.getJqFwqYyByNginxId(nid);
		resultBean = GenerateNginxConfUtils.generateNginxConfByTemplete(map, nid);
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 替换Nginx配置文件
	 * @return: void
	 */
	public void replaceNginxConf(){
		//设置默认返回对象
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
		String fwqip = request.getParameter("fwqip");
		String nid = request.getParameter("nid");
		String nginxRootPath = request.getParameter("nginxRootPath");
		//判断文件是否存在
		File nginxConfFile = NginxUtils.getNginxConfFile(nid);
		if (nginxConfFile == null || !nginxConfFile.exists()) {
			//文件不存在
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "配置文件不存在");
			try {
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//调用工具类完成配置文件的替换
		//TODO:
		//判断服务器是否启动
		String serverInfo = SocketUtils.getServerInfo(fwqip);
		if (StringUtils.isEmpty(serverInfo)) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "服务器未启动");
			try {
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		if (!SocketUtils.replaceNginxConf(fwqip, nginxConfFile.getAbsolutePath(), nginxRootPath)) {
			try {
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "nginx替换失败");
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Nginx服务器的当前配置信息
	 * @return: void
	 */
	public void downloadFwqNginxConfig() {
		// 设置contentType
		response.setContentType("text/html;charset=UTF-8");
		String nginxRootPath = request.getParameter("nginxRootPath");
		String nid = request.getParameter("nid");
		String fwqip = request.getParameter("fwqip");
		String resultInfo = SocketUtils.getNginxConfig(fwqip, nginxRootPath, nid);
		if (resultInfo == null || "".equals(resultInfo)) {
			resultInfo = "服务器未开启";
		}
		try {
			ResponseUtils.render(response, resultInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取线上Nginx配置（直接从本地文件中读，已经下载到了本地）
	 * @return: void
	 */
	public void showFwqNginxConfig() {
		String nid = request.getParameter("nid");
		//获取Nginx配置文件		//根据nid获取
		File nginxConfFile = new File(SystemUtils.getTomcatBackUpUrl()+File.separator+"nginxConfs"+File.separator+nid, "nginx_fwq.conf");
		String contentIfFileNotExist = "配置文件不存在！";
		showFileContent(response, nginxConfFile, "UTF-8", contentIfFileNotExist);
	}
	
	/**
	 * 查看本地生成的Nginx配置文件
	 * @return: String
	 */
	public void viewLocalNginxConf() throws Exception {
		String nid = request.getParameter("nid");
		//获取Nginx配置文件		//根据nid获取
		File nginxConfFile = new File(SystemUtils.getTomcatBackUpUrl()+File.separator+"nginxConfs"+File.separator+nid, "nginx.conf");
		String contentIfFileNotExist = "配置文件不存在！";
		showFileContent(response, nginxConfFile, "UTF-8", contentIfFileNotExist);
	}
	
	/**
	 * 将指定文件显示到页面中
	 * @param response
	 * @param readFile	要读取的文件
	 * @param charSet 写入使用的字符编码
	 * @param contentIfFileNotExist 如果文件不存在写入的内容
	 * @return: void
	 */
	private void showFileContent(HttpServletResponse response, File readFile, String charSet, String contentIfFileNotExist) {
		BufferedWriter bw = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			// 设置contentType
			response.setContentType("text/html;charset=UTF-8");
			// 获得高效输出流
			bw = new BufferedWriter(response.getWriter());
			if (readFile == null || !readFile.isFile()) {
				bw.write(contentIfFileNotExist);
			}
			// 获得高效输入流
			isr = new InputStreamReader(new FileInputStream(readFile), charSet);
			br = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			String readLine;
			while((readLine=br.readLine()) != null){
				readLine = readLine.replaceAll(" ", "&nbsp;");
				sb.append(readLine+"<br/>");
			}
			bw.write(sb.toString());
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 关流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 异步上传Nginx配置文件模板
	 * @return: String
	 */
	public String uploadNginxConfTemplete() {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
		try {
			String nid = request.getParameter("nid");
			if (nid == null || nid.equals("")) {
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "Nginxid为空，上传失败！");
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return null;
			}
			uploadNginxConf(nid);
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 异步判断模板文件是否存在
	 * @return: String
	 */
	public String isNginxConfTempleteExist() {
		try {
			String nid = request.getParameter("nid");
			String resultMsg = "success";
			if (nid == null || nid.equals("")) {
				resultMsg = "nginxid 为空！";
				ResponseUtils.render(response, resultMsg);
				return null;
			}
			File file = new File(NginxUtils.getNginxConfDir(nid)+File.separator+"nginxConfTemplete.conf");
			if (!file.isFile()) {
				//文件不存在
				resultMsg = "配置文件不存在!";
				ResponseUtils.render(response, resultMsg);
				return null;
			}
			ResponseUtils.render(response, resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 下载配置文件
	 * @return: String
	 */
	public String downloadNginxConfTemplete() throws Exception {
		String nid = request.getParameter("nid");
		if (nid != null && !nid.equals("")) {
			File file = new File(NginxUtils.getNginxConfDir(nid)+File.separator+"nginxConfTemplete.conf");
			if (file.isFile()) {
				FileUtils.downloadFile(request, response, file, null);
			}
		}
		return null;
	}
	
	/**
	 * 处理ajax，获取自动更新Nginx失败的信息
	 * @return: void
	 */
	public void getUpdateConfErrMsg() {
		List<Map> errMsgList = NginxCache.getInstance().updateErrMsg;
		StringBuffer sb = new StringBuffer();
		if (errMsgList.size()>0) {
			for (Map errMap : errMsgList) {
				if (errMap.get("msg") != null && !errMap.get("msg").equals("")) {
					sb.append(errMap.get("msg")).append(SystemConstants.LINE_SEPARATER);
				}
			}
		}
		String msg = "";
		if (sb.length()>0) {
			msg = sb.substring(0, sb.lastIndexOf(SystemConstants.LINE_SEPARATER));
		}
		try {
			NginxCache.getInstance().updateErrMsg.clear();
			ResponseUtils.render(response, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 试发布规则配置
	 * @return: String
	 */
	public String sfbgzpz() {
		String nid = request.getParameter("nid");
		nginxBean = nginxManager.getNginxBean(nid);
		List sfbList = nginxManager.getSfbpzList(nid);
		request.setAttribute("sfbpzList", sfbList);
		request.setAttribute("nginxBean", nginxBean);
		return "sfbgzpz";
	}
	
	/**
	 * ajax操作 保存试发布配置
	 * @return: String
	 * @throws Exception 
	 */
	public void saveSfbpz() throws Exception {
		String nid = request.getParameter("nginxid");
		String[] regexesArr = request.getParameterValues("regexes");
		List<ResultBean> res = nginxManager.saveSfbpzxx(nid, regexesArr);
		ResponseUtils.render(response, JsonUtil.list2json(res));
	}
	
	/**
	 * 更新NGINX前的校验
	 * @throws Exception 
	 */
	public void checkForUpdate() throws Exception {
		ResultBean resultBean = null;
		if (!SystemConstants.isNginxUsed) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "系统没有启用Nginx，已跳过！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		}
		String fwqid = request.getParameter("fwqid");
		String jqid = request.getParameter("jqid");
		String nginxid = request.getParameter("nginxid");
		//1、校验是否有对应的需要更新配置的Nginx
		List nginxList = null;
		if (!StringUtils.isEmpty(fwqid)) {
			//根据服务器获取的
			nginxList = nginxManager.getListByFwqid(fwqid);
		}
		if (!StringUtils.isEmpty(jqid)) {
			//集群获取
			nginxList = nginxManager.getListByJqid(jqid);
		}
		if (!StringUtils.isEmpty(nginxid)) {
			//Nginx获取，这里为了统一获取list，直接赋值的方式
			NginxBean bean = nginxManager.getNginxBean(nginxid);
			if (bean != null) {
				Map map = new HashMap();
				map.put("id", bean.getId());
				map.put("fwqip", bean.getFwqip());
				map.put("fwqname", bean.getFwqname());
				nginxList = new ArrayList();
				nginxList.add(map);
			}
		}
		//校验
		if (nginxList == null || nginxList.size()==0) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "没有对应的要更新配置的Nginx服务器，已跳过！");
			return;
		}
		for (Map nginxMap : (List<Map>)nginxList) {
			//2、校验模板
			File templeteConfFile = new File(NginxUtils.getNginxConfDir((String)nginxMap.get("id")), "nginxConfTemplete.conf");
			if (!templeteConfFile.isFile()) {	//模板文件不存在
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "Nginx【"+nginxMap.get("fwqname")+"】没有模板文件！");
				return;
			}
			//3、校验服务器状态
			String serverInfo = SocketUtils.getServerInfo((String)nginxMap.get("fwqip"));
			if (StringUtils.isEmpty(serverInfo)) {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "Nginx【"+nginxMap.get("fwqname")+"】服务器未启动！");
				return;
			}
		}
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK,"校验通过！");
	}
	
	/**
	 * 前往在线查看日志界面
	 * @return: String
	 */
	public String toshowLogOnline() {
		String nid = request.getParameter("nid");
		request.setAttribute("nid", nid);
		return "logview";
	}
	
	/**
	 * 查看Nginx在线日志
	 * @throws Exception
	 * @return: void
	 */
	public void viewLogOnline() throws Exception {
		ResultBean resultBean = null;
		String nid = request.getParameter("nid");
		String pos = request.getParameter("pos");
		NginxBean bean = nginxManager.getNginxBean(nid);
		if (bean == null) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "Nginx不存在！");
			return;
		}
		//判断服务器是否启动
		String info = SocketUtils.getServerInfo(bean.getFwqip());
		if (StringUtils.isEmpty(info)) {
			//服务器未开启
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "服务器未开启！");
			return;
		}
		
		String resStr = SocketUtils.getNginxLogByPos(bean.getFwqip(), pos, bean.getNginxsrc());
		if (StringUtils.isEmpty(resStr)) {
			//获取信息失败，提示异常，稍后再试!
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "获取数据异常，稍后再试！");
			return;
		}else {
			Map map = JsonUtil.parserToMap(resStr);
			List logList = JsonUtil.parserToList((String) map.get("arg1"));
			if (map == null || map.size()==0) {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "解析数据出错！");
				return;
			}
			if (SystemConstants.STATUSCODE_OK.equals(map.get("statusCode"))) {
				//成功
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("statusCode", map.get("statusCode"));
				jsonObject.put("arg1", logList);
				jsonObject.put("arg2", map.get("arg2"));
				ResponseUtils.render(response, jsonObject.toString());
				return;
			}else {
				//出错
				resultBean = new ResultBean((String)map.get("statusCode"), map.get("msg"));
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}
		}
	}
	
	/**
	 * 下载日志到本地
	 * @throws Exception
	 * @return: void
	 */
	public void downloadLog() throws Exception {
		// 设置contentType
		response.setContentType("text/html;charset=UTF-8");
		String nid = request.getParameter("nid");
		NginxBean bean = nginxManager.getNginxBean(nid);
		if (bean == null) {
			ResponseUtils.render(response, "nginx不存在！");
			return;
		}
		String resultInfo = SocketUtils.downloadNginxLog(bean.getFwqip(), bean.getNginxsrc(), nid);
		if (resultInfo == null || "".equals(resultInfo)) {
			resultInfo = "服务器未开启";
		}
		try {
			ResponseUtils.render(response, resultInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从本地下载日志到客户端
	 * @return: void
	 * @throws Exception
	 */
	public void downloadLogFromLocal() throws Exception {
		String nid = request.getParameter("nid");
		String destDir = SystemUtils.getTomcatBackUpUrl() + File.separator + "nginxLogs" + File.separator + nid;
		File logFile = new File(destDir, "access.log");
		if (!logFile.isFile()) {
			ResponseUtils.render(response, "文件不存在！");
			return;
		}
		try {
			FileUtils.downloadFile(request, response, logFile, null);
		} catch (Exception e) {
			ResponseUtils.render(response, "下载失败！");
			return;
		}
	}
	
	//==============================注入  get/set==================================
	private NginxBean nginxBean;
	public NginxBean getNginxBean() {
		return nginxBean;
	}
	public void setNginxBean(NginxBean nginxBean) {
		this.nginxBean = nginxBean;
	}
	private NginxConfigBean nginxConfigBean;
	public NginxConfigBean getNginxConfigBean() {
		return nginxConfigBean;
	}
	public void setNginxConfigBean(NginxConfigBean nginxConfigBean) {
		this.nginxConfigBean = nginxConfigBean;
	}
	//注入nginxManager
	private INginxManager nginxManager;
	public void setNginxManager(INginxManager nginxManager) {
		this.nginxManager = nginxManager;
	}
	//注入集群的manager
	private IJqManager jqManager;
	public void setJqManager(IJqManager jqManager) {
		this.jqManager = jqManager;
	}
	//文件上传相关
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileContentType() {
		return myFileContentType;
	}
	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
}
