package com.core.jadlwork.struts.action.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.SpringBeanFactory;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.ITsryManager;
import com.core.jadlwork.cache.wechat.WeChatCache;
import com.core.jadlwork.model.gzzx.TsRyBean;
import com.core.jadlwork.utils.httputils.HttpRequestProxy;

/**
 * 处理接入服务中的微信的各种事件
 * 	 在接入服务后，用户的所有交互信息都会传递给服务的后台，通过设置该地址，可以接管这个功能
 * @类名: WeChatDealServlet
 * @作者: lcx
 * @时间: 2017-8-9 下午4:10:34
 */
public class WeChatDealServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(WeChatDealServlet.class);
	
	private static final long serialVersionUID = 6893627094496137921L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String openid = request.getParameter("FromUserName");	//用户的openID
		String ticket = request.getParameter("Ticket");	//ticket
		//参数信息    之前未关注，通过扫参数二维码关注的话会在前面加上 qrscene_   eg: qrscene_checkUserVerCode&652372
		String eventKey = request.getParameter("EventKey");
		/*
		 * 事件名称
		 * 	 关注：subscribe	只是扫描或者关注的话，没有ticket属性，并且eventKey值为空
		 * 	 取消关注 ： unsubscribe
		 * 	扫描二维码： SCAN
		 */
		String event = request.getParameter("Event");	
		String appno = request.getParameter("ToUserName");	//微信公众号的微信号
		/*
		 * 消息类型
		 * 	text 文本
		 * 	image
		 *	。。。
		 *	event 事件
		 */
		String msgType = request.getParameter("MsgType");
		
		/*
		 * 判断，根据不同的事件进入不同的事件处理方法
		 */
		//1、事件
		if ("event".equals(msgType)) {
			//1.1、关注事件
			if ("subscribe".equals(event)) {
				//判断是否为扫参关注
				if (!StringUtils.isEmpty(ticket) && !StringUtils.isEmpty(eventKey) && eventKey.startsWith("qrscene_")) {
					//解析参数
					String paraInfo = eventKey.substring(8, eventKey.length());
					parseToFun(paraInfo, openid, ticket, request, response);
					return;
				}
			}
			//1.2、扫描二维码
			if ("SCAN".equals(event)) {
				//解析参数
				parseToFun(eventKey, openid, ticket, request, response);
				return;
			}
		}
		//2、非事件（用户主动发送消息，可以是图片、文字、语音等）
		dealZdhf(request, response);
		
		return;
	}
	
	/*
	 * 解析参数，调用不同的功能方法
	 */
	private void parseToFun(String paraInfo, String openid, String ticket, HttpServletRequest request
			, HttpServletResponse response) {
		if (StringUtils.isEmpty(paraInfo)) {
			return;
		}
		//校验用户验证码
		if (paraInfo.startsWith("checkUserVerCode")) {
			
			//判断该ticket是否已经被其他人扫描过
			Object mapObj = WeChatCache.ticketOpenidMapping.get(ticket);
			if (mapObj != null) {
				Map map = (Map) mapObj;
				
				String currentOpenid = (String) map.get("openid");
				long scantime = (Long) map.get("scantime");
				
				//不是同一个openID或者并且上一个时间是在10分钟之内
				if (!openid.equals(currentOpenid) && System.currentTimeMillis()-scantime <= 1000 * 60 * 10) {
					try {
						ResponseUtils.render(response, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			
			//校验该微信是否已经绑定的有用户
			ITsryManager tsryManager = (ITsryManager) SpringBeanFactory.getBean("tsryManager");
			TsRyBean ryBean = tsryManager.getByWxid(openid);
			if (ryBean != null) {
				//已经绑定的有
				StringBuffer sb = new StringBuffer();
				sb.append("抱歉！该微信号已经绑定了用户名为【"+ryBean.getUsername()+"】的用户信息！");
				try {
					ResponseUtils.render(response, sb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			
			
			//将新的保存
			Map newInfo = new HashMap();
			newInfo.put("openid", openid);
			newInfo.put("scantime", System.currentTimeMillis());
			WeChatCache.ticketOpenidMapping.put(ticket, newInfo);
			
			//将openID信息存储到session中
			request.getSession(true).setAttribute("openid", openid);
			
			
			if (!paraInfo.contains("&")) {
				//格式不正确
				logger.error("【微信校验验证码】参数格式错误！");
				return;
			}
			String[] split = paraInfo.split("&");
			if (split == null || split.length != 2) {
				logger.error("【微信校验验证码】参数格式错误！");
				return;
			}
			String verCode = split[1];
			//调用校验验证码方法
			try {
				checkUserVerCode(request, response, verCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理自动回复功能
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @return: void
	 */
	public void dealZdhf(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String defaultTipStr = "请选择：" +
				"		1：发送异常信息" +
				"		2：返回HELLO WORLD";
		
		String appid = "wxfbd19e4f6f8796d9";
		String appno = "gh_998b22f2adad";
		String appsecret = "0b89e63850462ca8f72ad0468aa66424";
		
		String msg = request.getParameter("msg");
		String resStr = defaultTipStr;
		if (!StringUtils.isEmpty(msg)) {
			if (msg.equals("1")) {
				//发送模板信息
				String url = "http://192.168.20.124:8080/service/wechat/msgpush.do?method=sendTemplateMsg";
				Map data = new HashMap();
				
				data.put("appid", appid);
				data.put("appno", appno);
				data.put("appsecret", appsecret);
				data.put("template_id", "p7SOKBBUJsziVvICJ5sTOD15lLkamlfNySM7hVU-l9E");
				
				Map varData = new HashMap();
				varData.put("nickname", "晓寒轻");
				varData.put("fwqip", "192.168.20.124");
				varData.put("fwqname", "测试服务器");
				data.put("varData", JsonUtil.map2json(varData));
				
				List<String> touseres = new ArrayList();
				touseres.add("oqXx50jlXG5nzOiKfW4NpuE5Noso");
				
				data.put("touseres", JsonUtil.list2json(touseres));
				
				try {
					ResponseUtils.render(response, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				HttpRequestProxy hrp = new HttpRequestProxy();
				try {
					System.out
							.println(hrp.doRequest(url, data, null, "UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				if (msg.equals("2")) {
					//直接返回
					resStr = "HELLO WORLD !";
				}
			}
		}
		try {
			ResponseUtils.render(response, resStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理用户校验信息
	 * @param verCode 验证码
	 * @param req
	 * @param resp
	 * @return: void
	 * @throws Exception 
	 */
	public void checkUserVerCode(HttpServletRequest request, HttpServletResponse response, String verCode)
			throws Exception {
		//将得到的参数信息(校验码)，返回即可
		if (StringUtils.isEmpty(verCode)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("验证码信息为：【"+verCode+"】，有效期为10分钟，请尽快进行验证！")
			.append(SystemConstants.LINE_SEPARATER)
			.append("注意：该信息只对你一个人使用，如果想使用其他微信号作为推送号，请单击验证码刷新后使用其它微信扫描！");
		ResponseUtils.render(response, sb.toString());
		return;
	}
}
