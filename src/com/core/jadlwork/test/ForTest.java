package com.core.jadlwork.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.jadlsoft.utils.JsonUtil;
import com.core.jadlwork.utils.httputils.HttpRequestProxy;

public class ForTest {
	
	private void a() {
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			for (int i = 0; i < 20; i++) {
				proxy.doRequest("http://96bcbc4b.ngrok.io/", null, null, "UTF-8");
				Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void b() {
		String s = "";
		List list = new ArrayList();
		Map map = new HashMap();
		try {
//			list = JsonUtil.parserToList(s);
			map = JsonUtil.parserToMap(s);
		} catch (Exception e) {
			System.out.println("异常");
		}
		System.out.println(map);
	}
	
	public static void main(String[] args) throws Exception {
		ForTest test = new ForTest();
//		test.a();
		test.b();
	}
}
