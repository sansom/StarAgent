package com.core.jadlwork.struts.action.gzzx;

import java.util.Map;

import org.apache.log4j.Logger;

import com.core.jadlsoft.struts.action.BaseAction;
import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlsoft.utils.ResponseUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.ITsryManager;
import com.core.jadlwork.cache.wechat.WeChatCache;
import com.core.jadlwork.model.ResultBean;
import com.core.jadlwork.model.gzzx.TsRyBean;
import com.core.jadlwork.utils.MsgPushUtils;

public class TsryAction extends BaseAction {

	private Logger logger = Logger.getLogger(TsryAction.class);
	
	private static final long serialVersionUID = 1L;
	private TsRyBean tsryBean;
	public TsRyBean getTsRyBean() {
		return tsryBean;
	}
	public void setTsRyBean(TsRyBean tsryBean) {
		this.tsryBean = tsryBean;
	}
	
	//注入tsryManager
	private ITsryManager tsryManager;
	public void setTsryManager(ITsryManager tsryManager) {
		this.tsryManager = tsryManager;
	}
	/**
	 * 前往编辑界面
	 * @return: String
	 */
	public String edit() {
		if (tsryBean == null || StringUtils.isEmpty(tsryBean.getId())) {
			//添加
		}else {
			//编辑
			TsRyBean ryBean = tsryManager.get(tsryBean.getId());
			if (ryBean != null) {
				request.setAttribute("tsRyBean", ryBean);
			}
		}
		return "edit";
	}
	
	/**
	 * 添加推送人员信息
	 * @return: String
	 */
	public String save() {
		
//		boolean wxts = SystemConstants.ZT_TRUE.equals(tsryBean.getWxts()) ? true : false;
//		//设置openID
//		String openid = getOpenidFromSession();
//		if (!StringUtils.isEmpty(openid)) {
//			tsryBean.setWxid(openid);
//		}
		
		tsryManager.save(tsryBean);
		return "list";
	}
	
	/**
	 * 更新推送人员信息
	 * @return: String
	 */
	public String update() {
//		boolean wxts = SystemConstants.ZT_TRUE.equals(tsryBean.getWxts()) ? true : false;
//		//设置openID
//		//如果之前有，就直接从传递过来的获取
//		if (StringUtils.isEmpty(tsryBean.getWxid())) {
//			//如果之前没有启用，要去session中获取
//			String openid = getOpenidFromSession();
//			if (!StringUtils.isEmpty(openid)) {
//				tsryBean.setWxid(openid);
//			}
//		}
		
//		tsryManager.update(tsryBean, "username,dxts,wxts,yxts,sjhm,yxdz,wxid");
		tsryManager.update(tsryBean);
		return "list";
	}
	
	/**
	 * 删除人员信息
	 * @return: String
	 */
	public String delete() {
		if (tsryBean == null || StringUtils.isEmpty(tsryBean.getId())) {
			logger.error("要删除的推送人员id不存在！");
			return "list";
		}
		tsryManager.delete(tsryBean.getId());
		return "list";
	}
	
	/**
	 * 校验用户名可用性
	 * @return: void
	 * @throws Exception 
	 */
	public void checkUsernameUsable() throws Exception {
		ResultBean resultBean = null;
		String username = request.getParameter("username");
		String ryid = request.getParameter("ryid");
		//1、校验非空
		if (StringUtils.isEmpty(username)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "用户名不能为空！");
			return;
		}
		//2、是否已存在
		TsRyBean bean = tsryManager.getByUsername(username);
		if (bean != null && !bean.getId().equals(ryid)) {
			//已经存在
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "用户名已经存在！");
			return;
		}
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "");
	}
	/**
	 * 校验手机号可用性
	 * @return: void
	 * @throws Exception 
	 */
	public void checkSjhmUsable() throws Exception {
		ResultBean resultBean = null;
		String sjhm = request.getParameter("sjhm");
		String ryid = request.getParameter("ryid");
		//1、校验非空
		if (StringUtils.isEmpty(sjhm)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "手机号不能为空！");
			return;
		}
		//2、正则校验
		String reg = "^[1][3,4,5,7,8][0-9]{9}$";
		if (!sjhm.matches(reg)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "手机号格式不正确！");
			return;
		}
		
		//3、是否已存在
		TsRyBean bean = tsryManager.getBySjhm(sjhm);
		if (bean != null && !bean.getId().equals(ryid)) {
			//已经存在
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "该手机号已被使用！");
			return;
		}
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "");
	}
	/**
	 * 校验邮箱可用性
	 * @return: void
	 * @throws Exception 
	 */
	public void checkYxUsable() throws Exception {
		ResultBean resultBean = null;
		String email = request.getParameter("email");
		String ryid = request.getParameter("ryid");
		//1、校验非空
		if (StringUtils.isEmpty(email)) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "邮箱不能为空！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		}
		//2、正则校验
		String reg = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if (!email.matches(reg)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "邮箱格式不正确！");
			return;
		}
		
		//3、是否已存在
		TsRyBean bean = tsryManager.getByEmail(email);
		if (bean != null && !bean.getId().equals(ryid)) {
			//已经存在
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "该邮箱已被使用！");
			return;
		}
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "");
	}
	
	/**
	 * 处理ajax请求，生成临时二维码
	 * @throws Exception
	 * @return: void
	 */
	public void createEeweima() throws Exception {
		ResultBean resultBean = null;
		//1、生成6位随机数
		String verCode = StringUtils.createRandomNumber(6);
		String paraInfo = "checkUserVerCode&" + verCode;
		String res = MsgPushUtils.getEwmWithPara(paraInfo);
		if (StringUtils.isEmpty(res)) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "获取公众号二维码信息失败！请稍后再试！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return ;
		}
		Map resMap = JsonUtil.parserToMap(res);
		if ("00000".equals(resMap.get("statusCode"))) {
			//成功
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "");
			resultBean.setArg1(resMap.get("msg"));
			Object[] verCodeInfo = {verCode, System.currentTimeMillis()};
			request.getSession(true).setAttribute("weChatUserVerCode", verCodeInfo);	//TODO
			//将ticket信息也保存在session中，给用户设置标签时候使用
			request.getSession(true).setAttribute("weChatTicket", resMap.get("arg1"));	//TODO
			
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return ;
		}else {
			//失败
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, resMap.get("msg"));
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return ;
		}
	}
	
	/**
	 * 校验微信验证码
	 * @return: void
	 * @throws Exception 
	 */
	public void checkWeChatVerCode() throws Exception {
		ResultBean resultBean = null;
		String verCode = request.getParameter("verCode");
		if (StringUtils.isEmpty(verCode)) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "验证码不能为空！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		}
		Object sessionVerCode = request.getSession(true).getAttribute("weChatUserVerCode");
		if (sessionVerCode == null || sessionVerCode.equals("")) {
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "生成验证码异常，请稍后再试！");
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
			return;
		}
		//校验是否失效
		Object[] verCodeInfo = (Object[]) sessionVerCode;
		if (verCodeInfo != null && verCodeInfo.length == 2) {
			String code = (String) verCodeInfo[0];
			long lastMillis = (Long) verCodeInfo[1];
			if (System.currentTimeMillis() - lastMillis > 10 * 60 * 1000) {
				//超过10分钟就失效
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "验证码已失效，请点击二维码刷新并重新获取！");
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}else {
				//校验是否正确
				if (code.equals(verCode)) {
					resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "");
					ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
					return;
				}else {
					resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "验证码错误！");
					ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
					return;
				}
			}
		}
	}
	
	/*
	 * 从session中获取openID
	 */
	private String getOpenidFromSession() {
		//1、先从session中获取用户的openID
		Object ticket = request.getSession(true).getAttribute("weChatTicket");
		if (ticket == null || ticket.equals("")) {
			//session 中没有ticket信息，不用处理
			return null;
		}
		
		//根据ticket获取openID
		Object object = WeChatCache.ticketOpenidMapping.get(ticket);
		if (object == null) {
			return null;
		}
		Map ticketMap = (Map) object;
		Object openidObj = ticketMap.get("openid");
		
		//使用完成后将该ticket对应的信息从缓存中去除掉
		WeChatCache.ticketOpenidMapping.remove(ticket);
		
		if (openidObj == null || openidObj.equals("")) {
			return null;
		}
		return (String) openidObj;
	}
	
	/**
	 * ajax解绑微信
	 * @return: String
	 * @throws Exception 
	 */
	public void unBindWechat() throws Exception {
		String ryid = request.getParameter("ryid");
		if (StringUtils.isEmpty(ryid)) {
			ResponseUtils.render(response, "error");
			return;
		}
		TsRyBean bean = new TsRyBean();
		bean.setId(ryid);
		bean.setWxid("");
		bean.setWxts(SystemConstants.ZT_FALSE);
		tsryManager.update(bean, "wxid,wxts");
		
		ResponseUtils.render(response, "success");
		return;
	}

	/**
	 * 校验微信是否可用
	 * @return: void
	 * @throws Exception 
	 */
	public void checkWeidUsable() throws Exception {
		ResultBean resultBean = null;
		
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "请输入微信上面的二维码！");
			return;
		}
		
		Object sessionVerCode = request.getSession(true).getAttribute("weChatUserVerCode");
		if (sessionVerCode == null || sessionVerCode.equals("")) {
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "请先扫描二维码并查看微信提示信息！");
			return;
		}
		
		//校验是否失效
		Object[] verCodeInfo = (Object[]) sessionVerCode;
		if (verCodeInfo != null && verCodeInfo.length == 2) {
			String vcode = (String) verCodeInfo[0];
			long lastMillis = (Long) verCodeInfo[1];
			if (System.currentTimeMillis() - lastMillis > 10 * 60 * 1000) {
				//超过10分钟就失效
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "验证码已失效，请点击二维码刷新并重新获取！");
				return;
			}else {
				//校验是否正确
				if (!vcode.equals(code)) {
					ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "验证码错误！");
					return;
				}
			}
		}
		
		//从session中获取是否有openID信息
		String openid = getOpenidFromSession();
		if (StringUtils.isEmpty(openid)) {
			//当前还没有验证码信息
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "请先扫描二维码并查看微信提示信息！");
			return;
		}
		TsRyBean bean = tsryManager.getByWxid(openid);
		if (bean != null) {
			//不可用
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, 
					"该微信号已经绑定用户名为【"+bean.getUsername()+"】的用户");
			return;
		}
		
		//将openID返回
		resultBean = new ResultBean();
		resultBean.setArg1(openid);
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "");
	}
	
}
