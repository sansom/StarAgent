package com.core.jadlwork.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.utils.FileUtils;

/**
 * 处理文件上传（分片上传）
 * @类名: FileUploadServlet
 * @作者: lcx
 * @时间: 2017-9-12 上午8:52:43
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 3176356347931219285L;
	private static Logger logger = Logger.getLogger(FileUploadServlet.class);

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String methodName = request.getParameter("method");
		Method method;
		try {
			method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this,request, response);
		} catch (NoSuchMethodException e) {
			logger.error("No parameter named methodName is defined");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分片单个上传
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @return: void
	 */
	public void cellUpload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResultBean resultBean = null;
		//将临时文件上传到项目根目录/upload/temp/uid下，每个文件会创建不同的文件目录
		//1、获取文件要上传到的目录
		//1.1、获取项目发布后的根目录
		String projectRootPath = FileUtils.getProjectRootPath();	//发布后项目的根目录 D:\soft\install\MyTomcat6\webapps\ywglpt
		//1.2、获取/upload/temp目录
		File toUploadRootDir = new File(projectRootPath+File.separator+"upload"+File.separator+"temp");
		if (!toUploadRootDir.isDirectory()) {
			toUploadRootDir.mkdirs();	//没有就创建
		}
		//2、使用DiskFileItemFactory上传
		File tmpDir = new File(toUploadRootDir, "tmpDir");	//Apache上传需要的缓存目录
		if (!tmpDir.isDirectory()) {
			tmpDir.mkdirs();
		}
		
        try {  
            if (ServletFileUpload.isMultipartContent(request)) {  
                DiskFileItemFactory factory = new DiskFileItemFactory();  
                //指定在内存中缓存数据大小,单位为byte,这里设为10Mb  
                factory.setSizeThreshold(10 * 1024 * 1024);   
                //设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录  
                factory.setRepository(tmpDir);   
                ServletFileUpload sfu = new ServletFileUpload(factory);  
                 // 指定单个上传文件的最大尺寸,单位:字节，这里设为500Mb  
                sfu.setFileSizeMax(500 * 1024 * 1024);  
                //指定一次上传多个文件的总尺寸,单位:字节，这里设为1024Mb  
                sfu.setSizeMax(1024 * 1024 * 1024);   
                sfu.setHeaderEncoding("UTF-8"); //设置编码，因为我的jsp页面的编码是utf-8的  
                List<FileItem> parseRequest = sfu.parseRequest(request);
                if (parseRequest != null && parseRequest.size()>0) {
                	/*
                	 * 需要拿到guid、chunk、chunks、去上传
                	 * 因此，在循环外面定义好，循环完成之后上传
                	 */
                	String guid = "";
                	String chunk = "";
                	String chunks = "";
                	InputStream is = null;
					for (FileItem fileItem : parseRequest) {
						if (fileItem.isFormField()) {
							//不是二进制流（是表单的其他属性，包含的就有guid、chunk、chunks）
							String fieldName = fileItem.getFieldName();
							if ("guid".equals(fieldName)) {
								guid = fileItem.getString();
							}
							if ("chunk".equals(fieldName)) {
								chunk = fileItem.getString();
							}
							if ("chunks".equals(fieldName)) {
								chunks = fileItem.getString();
							}
						}else {
							//是文件信息
							is = fileItem.getInputStream();
						}
					}
					//循环完成，理论上已经拿到所有需要的属性
					if (StringUtils.isEmpty(guid) || is == null) {
						ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "得到的参数为空！");
						return;
					}
					
					if (StringUtils.isEmpty(chunk) || StringUtils.isEmpty(chunks)) {
						//可能没有分片，就一片
						chunk = "0";
						chunks = "1";
					}
					/*
					 * 保存单个文件
					 * 	1、在temp目录下，以guid为名称创建目录
					 *  2、将每个文件以chunk.tmp命名保存到对应的guid目录下
					 */
					//1、创建目录
					File guidDir = new File(toUploadRootDir, guid);
					if (!guidDir.isDirectory()) {
						guidDir.mkdirs();
					}
					//2、创建目标文件
					File destFile = new File(guidDir, chunk+".tmp");
					//2、保存
					org.apache.commons.io.FileUtils.copyInputStreamToFile(is, destFile);
					ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, destFile.getAbsolutePath());
					return;
				}
            }  
        } catch (Exception e) {
        	logger.error("文件上传失败！");
        	try {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "文件上传失败！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
        }  
	}
	
	/**
	 * 分片文件整合
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @return: void
	 */
	public void cellMerge(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResultBean resultBean = null;
		String guid = request.getParameter("guid");
		String destFileDir = request.getParameter("destDir");
		String filename = request.getParameter("filename");
		if (StringUtils.isEmpty(destFileDir)) {
			//如果目标目录为空，就使用默认路径  项目目录/upload/temp
			destFileDir = FileUtils.getProjectRootPath() + "upload" + File.separator + "temp";
		}
		//先获取文件夹
		String tempDirStr = FileUtils.getProjectRootPath() + "upload" + File.separator + "temp" + File.separator + guid;
		File tempDir = new File(tempDirStr);
		if (!tempDir.isDirectory()) {
			try {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "不存在该文件的缓存目录！");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//合并文件，就直接写在这里了
		File[] files = tempDir.listFiles();
		File destFile = new File(destFileDir, filename);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));	//待生成的文件流
		//只合并文件，文件夹不处理
		try {
			for (File file : files) {
				if (file.isDirectory()) {
					continue;
				}
				writeFileToBos(bos, file);
			}
			//全部成功之后，删除缓存的每一片的目录
			FileUtils.delFolder(tempDirStr);
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
			resultBean.setArg1(destFile.getAbsolutePath());	//arg1存储文件的绝对路径
			resultBean.setArg2(destFile.getName());	//arg2存储文件名
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		} catch (Exception e) {
			try {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "上传失败，出现异常情况！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	private void writeFileToBos(BufferedOutputStream bos, File file) throws Exception {
		if (bos == null || file==null || !file.isFile()) {
			return;
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[1024];
		int len;
		while((len=bis.read(bytes)) != -1){
			bos.write(bytes, 0, len);
		}
		bis.close();
	}
}
