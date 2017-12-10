package com.core.jadlsoft.business;

import java.util.List;

import com.core.jadlsoft.dbutils.DaoUtils;
import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SysConfigUtils;
/**
 * 针对listConfig.xml中datasource进行相应的默认处理。
 * 默认的处理包括：
 * 1、替换当前的日期
 * @author 张方俊 2008-7-11 : 上午10:10:33
 *
 */
public class CommonListConfigDefault implements CommonListConfigInterface {

	public String transTableName(String tableName, List conditions, DaoUtils daoUtils)
	{
		int days = Integer.parseInt(SysConfigUtils.getProperty("jqcxts", "30"));
		tableName = tableName.replaceAll(":today", "'" + DateUtils.getCurrentData() + "'");
		tableName = tableName.replaceAll(":nowtime", "'" + DateUtils.getCurrentDataTime() + "'");
		tableName = tableName.replaceAll(":lastMonthToday", "'" + DateUtils.getLastFewDaysAgo(days) + "'");
		tableName = tableName.replaceAll(":zhrcjcrq", "'" + DateUtils.getLastFewDaysAgo(Integer.parseInt(SysConfigUtils.getProperty("bjxx_recentday", "30"))) + "'");
		
		return tableName;
	}

	public String transTableName(String tableName, List conditions,
			DaoUtils daoUtils, UserSessionBean userSessionBean) {
		int days = Integer.parseInt(SysConfigUtils.getProperty("jqcxts", "30"));
		tableName = tableName.replaceAll(":today", "'" + DateUtils.getCurrentData() + "'");
		tableName = tableName.replaceAll(":nowtime", "'" + DateUtils.getCurrentDataTime() + "'");
		tableName = tableName.replaceAll(":lastMonthToday", "'" + DateUtils.getLastFewDaysAgo(days) + "'");
		tableName = tableName.replaceAll(":zhrcjcrq", "'" + DateUtils.getLastFewDaysAgo(Integer.parseInt(SysConfigUtils.getProperty("bjxx_recentday", "30"))) + "'");
		
		return tableName;
	}

	

	 

}
