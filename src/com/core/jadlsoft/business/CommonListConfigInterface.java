package com.core.jadlsoft.business;

import java.util.List;

import com.core.jadlsoft.dbutils.DaoUtils;
import com.core.jadlsoft.model.xtgl.UserSessionBean;

public interface CommonListConfigInterface {
	public String transTableName(String tableName , List conditions , DaoUtils daoUtils);
	public String transTableName(String tableName , List conditions , DaoUtils daoUtils,UserSessionBean userSessionBean);
}
