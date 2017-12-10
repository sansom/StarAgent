package com.core.jadlwork.business.gzzx.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.ITsryManager;
import com.core.jadlwork.model.gzzx.TsRyBean;

/**
 * 推送人员操作的业务实现类
 * @类名: TsryManager
 * @作者: lcx
 * @时间: 2017-8-16 上午10:10:37
 */
public class TsryManager extends BaseManager implements ITsryManager {

	@Override
	public int save(TsRyBean tsRyBean) {
		
		String id;
		synchronized (tsRyBean) {
			id = String.valueOf(daoUtils.getNextval("q_ts_ry"));
		}
		tsRyBean.setId(id);
		tsRyBean.setZt(SystemConstants.ZT_TRUE);
		tsRyBean.setTjsj(DateUtils.createCurrentDate());
		
		return daoUtils.save(tsRyBean);
	}

	@Override
	public int update(TsRyBean tsRyBean) {
		return daoUtils.update(tsRyBean);
	}

	@Override
	public int update(TsRyBean tsRyBean, String field) {
		return daoUtils.update(tsRyBean, field);
	}

	@Override
	public int delete(String id) {
		TsRyBean tsRyBean = get(id);
		if (tsRyBean != null) {
			tsRyBean.setZxsj(DateUtils.createCurrentDate());
			tsRyBean.setZt(SystemConstants.ZT_FALSE);
			return update(tsRyBean, "zt,zxsj");
		}
		return 0;
	}

	@Override
	public TsRyBean get(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(TsRyBean.class, condition);
		if (object == null) {
			return null;
		}else {
			return (TsRyBean) object;
		}
	}
	
	@Override
	public TsRyBean getByUsername(String username) {
		Map condition = new HashMap();
		condition.put("username", username);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(TsRyBean.class, condition);
		if (object == null) {
			return null;
		}
		return (TsRyBean) object;
	}
	
	@Override
	public TsRyBean getBySjhm(String sjhm) {
		Map condition = new HashMap();
		condition.put("sjhm", sjhm);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(TsRyBean.class, condition);
		if (object == null) {
			return null;
		}
		return (TsRyBean) object;
	}
	
	@Override
	public TsRyBean getByEmail(String email) {
		Map condition = new HashMap();
		condition.put("yxdz", email);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(TsRyBean.class, condition);
		if (object == null) {
			return null;
		}
		return (TsRyBean) object;
	}
	
	@Override
	public TsRyBean getByWxid(String openid) {
		Map condition = new HashMap();
		condition.put("wxid", openid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(TsRyBean.class, condition);
		if (object == null) {
			return null;
		}
		return (TsRyBean) object;
	}
	
	@Override
	public List getAll() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find(TsRyBean.class, condition);
	}
}
