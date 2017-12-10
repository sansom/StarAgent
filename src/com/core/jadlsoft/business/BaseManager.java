package com.core.jadlsoft.business;

import com.core.jadlsoft.dbutils.DaoUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlwork.cache.ICacheDao;

public class BaseManager {

	protected DaoUtils daoUtils;
	
	protected ICacheDao cacheDao;

	public BaseManager() {
		super();
	}

	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}
	
	

	public void setCacheDao(ICacheDao cacheDao) {
		this.cacheDao = cacheDao;
	}

	/**
	 * 查询代码所对应的文字,主要用于查询没有缓冲的字典表,如t_dm_xzhqh
	 * @param tableName:代码表
	 * @param textField:文字字段
	 * @param codeField:代码字段
	 * @param codeValue:代码值
	 * @return 代码-文字
	 */
	public String queryNameByCode(String tableName, String textField,
			String codeField, String codeValue) {
		if (StringUtils.isEmpty(codeValue)) {
			return "";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(textField).append(" FROM ")
				.append(tableName).append(" WHERE ").append(codeField)
				.append(" = '").append(codeValue.trim()).append("'");
		Object name = daoUtils.queryForObject(sql.toString(), String.class);
		return name == null ? "" : name.toString();
	}
	
	/**
	 * 获取最大的ID
	 * @参数：@return
	 * @返回值：String
	 */
	public synchronized int getMaxId(String tablename,String tablepkfields) {
		try {
			Integer maxid = (Integer) daoUtils.queryForObject("select max("+tablepkfields+") id from "+tablename,
					Integer.class);
			if (maxid == null || maxid == 0 ) 
			{	
				maxid = 1;
			} 
			else 
			{
				maxid = Integer.valueOf(maxid)+1;
			}
			return maxid;
		} catch (Exception e) {
			throw new RuntimeException("获取当前最大Iid出错！");
		}
	}
	
}