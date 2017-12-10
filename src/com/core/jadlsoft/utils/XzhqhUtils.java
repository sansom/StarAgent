package com.core.jadlsoft.utils;

import com.core.jadlsoft.model.xtgl.UserSessionBean;




/**
 * <p>
 * Title:XzhqhUtils.java
 * </p>
 * <p>
 * Description: 行政区划处理类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 京安丹灵
 * </p>
 * 
 * @date 2006-10-12
 * @author Zong
 * @version 1.0
 */

public class XzhqhUtils {

	/**
	 * Method getLevel
	 * 
	 * @param xzqh:行政区划
	 * @description:获取行政区划级别
	 * @return
	 */
	public static int getXZHQHLevel(String xzqh) {
		if ((xzqh != null) && (xzqh.trim().length() != 6)) {// 传入空参数或参数位数不匹配
			return IConstants.ERRORLEVEL;
		}
		if ("000000".equals(xzqh)) {
			return IConstants.MINISTRY; // 公安部级
		} else if (xzqh.substring(2, 6).equals("0000")) {
			return IConstants.PROVINCE; // 省级
		} else if (xzqh.substring(4, 6).equals("00")) {
			return IConstants.CITY; // 地市级
		} else {
			return IConstants.COUNTRY; // 县级
		}
	}

	public static int getXZHQHLevel_Old(String xzqh) {
		if ((xzqh != null) && (xzqh.trim().length() != 6)) {// 传入空参数或参数位数不匹配
			return IConstants.ERRORLEVEL;
		}
		if ("000000".equals(xzqh)) {
			return IConstants.MINISTRY; // 公安部级
		} else if (xzqh.substring(2, 6).equals("0000")) {
			return IConstants.PROVINCE; // 省级
		} else if (xzqh.substring(4, 6).equals("00")) {
			return IConstants.CITY; // 地市级
		} else if ("11,12,31,50".indexOf(xzqh.substring(0, 2)) != -1) {
			return IConstants.CITY; // 直辖市的县级按照地市级处理
		} else {
			return IConstants.COUNTRY; // 县级
		}
	}

	/**
	 * Method getXZQH
	 * 
	 * @param xzqh:行政区划代码
	 * @description:根据传入行政区划的代码,返回模糊查询所用的行政区划.该结果用于 like %% 查询中,查询结果中包括自身及其下属单位
	 * @return
	 */
	public static String getXZHQH(String xzqh) {
		if ((xzqh == null) || (xzqh.trim().length() != 6)) {
			return "999999";// 出错,返回一个没有的行政区划代码
		} else {
			xzqh = xzqh.trim();
		}
		if (xzqh.equals("000000")) {// 公安部的行政区划
			return "";
		} else if (xzqh.substring(2, 6).equals("0000")) {// 省级的行政区划
			return xzqh.substring(0, 2);
		} else if (xzqh.substring(4, 6).equals("00")) {// 市级的行政区划
			return xzqh.substring(0, 4);
		} else {// 县级的行政区划
			return xzqh;
		}
	}

	/**
	 * Method getFatherXZHQH
	 * 
	 * @param xzqh:行政区划代码
	 * @description:根据传入行政区划的代码,获取其上级行政区划代码
	 * @return
	 */
	public static String getFatherXZHQH(String xzqh) {
		if ((xzqh == null) || (xzqh.trim().length() != 6)) {
			return IConstants.ERRORXZHQH;// 出错,返回一个没有的行政区划代码
		} else {
			xzqh = xzqh.trim();
		}
		final String especialXZQH = "11,12,31,50,71,81,82";
		String xzqhLeft2 = xzqh.substring(0, 2);
		String xzqhRight4 = xzqh.substring(2, 6);
		xzqh = xzqh.trim();
		if ((especialXZQH.indexOf(xzqhLeft2) >= 0)
				&& (!xzqhRight4.equals("0000"))) {
			return (xzqhLeft2 + "0000");
		}
		if (xzqh.equals("000000")) {// 公安部的行政区划
			return IConstants.SUPERIORXZHQH;
		} else if (xzqh.substring(2, 6).equals("0000")) {// 省级的行政区划
			return "000000";
		} else if (xzqh.substring(4, 6).equals("00")) {// 市级的行政区划
			return xzqh.substring(0, 2) + "0000";
		} else {// 县级的行政区划
			return xzqh.substring(0, 4) + "00";
		}
	}

	/**
	 * Method getFatherXZHQH
	 * 
	 * @param xzqh:行政区划代码
	 * @param level:要求生成上级代码的级别
	 * @description:根据传入行政区划的代码,获取其上级行政区划代码
	 * @return
	 */
	public static String getFatherXZHQH(String xzqh, int level) {
		if (level == IConstants.MINISTRY) {
			return "000000";
		} else if (level == IConstants.PROVINCE) {
			return xzqh.substring(0, 2) + "0000";
		} else if (level == IConstants.CITY) {
			return xzqh.substring(0, 4) + "00";
		} else if (level == IConstants.COUNTRY) {
			return xzqh;
		} else {
			return xzqh;
		}
	}
	
	/**
	 * 
	 * @param yhlx 
	 * @描述：根据用户类型获取过滤字段
	 *
	 * @作者：吴家旭
	 *
	 * @时间：Sep 12, 2013 3:59:18 PM
	 *
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String getPermissColumn(String yhlx) {
		String returncolumn = "";
		if(yhlx.equals(MBConstant.ADMIN)||yhlx.equals(MBConstant.BPXH)
				||yhlx.equals(MBConstant.GXB)||yhlx.equals(MBConstant.PXJG)||yhlx.equals(MBConstant.SGB)){
				returncolumn = "xzqh";
			
			
		}else if (yhlx.equals(MBConstant.JT)||yhlx.equals(MBConstant.QYFGS)
				){
				returncolumn = "qydm";
		}
		return returncolumn;
	}
	
	
}
