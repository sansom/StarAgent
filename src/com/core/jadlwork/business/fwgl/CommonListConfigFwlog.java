package com.core.jadlwork.business.fwgl;

import java.util.List;

import com.core.jadlsoft.business.CommonListConfigInterface;
import com.core.jadlsoft.dbutils.DaoUtils;
import com.core.jadlsoft.model.xtgl.UserSessionBean;
import com.core.jadlsoft.utils.DateUtils;
/**
 * 
 * 服务日志list
 * @作者：吴家旭
 * @时间：Jan 4, 2016 3:40:36 PM
 */
public class CommonListConfigFwlog implements   CommonListConfigInterface {

	@Override
	public String transTableName(String tableName, List conditions,
			DaoUtils daoUtils, UserSessionBean userSessionBean) {
		String qqsj_to = "";
		String qqsj_from = "";  

		if(conditions != null){
			for(int i=0; i<conditions.size();i++){
				String condtion = String.valueOf(conditions.get(i));
				String[] conditionArr = condtion.split("~");
				if("qqsj_from".equals(conditionArr[0])){
					qqsj_from = conditionArr[2];
					conditions.remove(i);
					i-- ;
				}else if("qqsj_to".equals(conditionArr[0])){
					qqsj_to = conditionArr[2];
					conditions.remove(i);
					i-- ;
				}
			}
		}
	
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from "+tableName);
		sql.append(" where 1=1");
		if("".equals(qqsj_from) && "".equals(qqsj_to)){
			sql.append(" and qqsj >= TO_DATE ('" + DateUtils.getCurrentData().substring(0,5) + "01-01" + "', 'yyyy-MM-dd') ");
			sql.append(" and qqsj <= TO_DATE ('" + DateUtils.getCurrentData() + "', 'yyyy-MM-dd') ");
		}else {
			if(!"".equals(qqsj_from)){
				sql.append(" and qqsj >= TO_DATE ('" + qqsj_from + "', 'yyyy-MM-dd') ");
			}
			if(!"".equals(qqsj_to)){
				sql.append(" and qqsj <= TO_DATE ('" + qqsj_to + "', 'yyyy-MM-dd') ");
			}
		}
		return "("+sql.toString()+")";
	}

	@Override
	public String transTableName(String tableName, List conditions,
			DaoUtils daoUtils) {
		return tableName;

	}

}