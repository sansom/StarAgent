/**
 * <p>Title:SearchManager.java</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 京安丹灵<p>
 * @date Nov 4, 2009
 * @author zhouxl
 * @version 3.0
 */
package com.core.jadlsoft.business.search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.jadlsoft.business.BaseManager;


public class SearchManager extends BaseManager{
	

	/**
	 * 返回符合条件的记录条数
	 * @param sql
	 * @return
	 */
	public int getSearchCount(String sql){
		return daoUtils.queryForInt("select count(*) from (" + sql + ")");
	}
	
	/**
	 * 返回指定跳过行数、返回条数的list
	 * @param sql
	 * @param skip
	 * @param count
	 * @return
	 */
	public List getSearchList(String sql,int skip,int count){
		return daoUtils.find(sql, new HashMap(), skip, count);
	}
	
	/**
	 * 根据sql返回list
	 * @param sql
	 * @return
	 */
	public List getSearchList(String sql){
		return daoUtils.find(sql, Collections.EMPTY_MAP);
	}
}
