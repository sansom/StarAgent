package com.core.jadlwork.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlwork.utils.httputils.HttpRequestProxy;

public class WeChatTest {

	private String appid = "wxfbd19e4f6f8796d9";
	private String appno = "gh_998b22f2adad";
	private String appsecret = "0b89e63850462ca8f72ad0468aa66424";
	private String accessToken = "EJffGSpgMskbIA2xahucn2ceivVSj4vmg6peHZ-wDoN6wOyyvPkUiDaeUQy244VlfzpA_nytyWejYI77etMj9yg7Q1j5JAiOYcVpjLZ3e2NGafkyq3_Q3D0M8XO8UOZ8GGZcAGAZZS";
	private String user_1 = "oqXx50ra0u_5FZPNMlEq6WaIUSNY";
	private String user_2 = "oqXx50nyuQ56TiKc6KlnVSqkOCtw";
	private String user_3 = "oqXx50jlXG5nzOiKfW4NpuE5Noso";
	private String template_id = "p7SOKBBUJsziVvICJ5sTOD15lLkamlfNySM7hVU-l9E";
	private String template_fwq = "qrS9s1gBj5sff-xWydWqudhVKQ5aNC78QkzyjFp11Pg";
	
	/*
	 *  * 		要求参数：
	 * 			appid、appno、appsecret
	 * 			String template_id 模板id（通过微信开发平台获取）
	 * 			Map varData 数据，简单处理，一个key对应一个变量，value为对应值
	 * 			List<String> touseres 要推送的粉丝 的openid
	 */
	public void testTemplate() throws Exception {
		String url = "http://192.168.20.124:8080/service/wechat/msgpush.do?method=sendTemplateMsg";
		Map data = new HashMap();
		data.put("appid", appid);
		data.put("appno", appno);
		data.put("appsecret", appsecret);
		data.put("template_id", template_fwq);
		
		Map varData = new HashMap();
		varData.put("nickname", "服务中心管理员");
//		varData.put("fwqname", "云平台服务器");
		varData.put("fwqip", "192.168.20.72");
		varData.put("fwqdk", "8080");
		varData.put("ycxx", "socket通讯失败！");
		
		
		List<String> touseres = new ArrayList<String>();
		touseres.add(user_3);
		touseres.add(user_1);
		
		data.put("varData", JsonUtil.map2json(varData));
		data.put("touseres", JsonUtil.list2json(touseres));
		
		HttpRequestProxy hrp = new HttpRequestProxy();
		String doRequest = hrp.doRequest(url, data, null, "UTF-8");
		System.out.println(doRequest);
		
	}
	
	public static void main(String[] args) throws Exception {
		WeChatTest test = new WeChatTest();
		test.testTemplate();
//		for (int i = 0; i < 5; i++) {
//			test.testTemplate();
//		}
	}
	
}
