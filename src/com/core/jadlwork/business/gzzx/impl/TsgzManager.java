package com.core.jadlwork.business.gzzx.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlwork.business.gzzx.ITsgzManager;

/**
 * 推送规则业务实现类
 * @类名: TsgzManager
 * @作者: lcx
 * @时间: 2017-8-21 上午10:47:48
 */
public class TsgzManager extends BaseManager implements ITsgzManager {

	@Override
	public int deleteRyByGzid(String gzid) {
		Map condition = new HashMap();
		condition.put("gzid", gzid);
		return daoUtils.execSql("#gzzx.deleteRyByGzid", condition);
	}
	
	@Override
	public void batchSaveRyByGzid(String gzid, String[] ryids) {
		
		if (ryids == null || ryids.length==0) {
			return;
		}
		//批量添加
		String[] field = {"gzid,ryid"};
		List data = new ArrayList();
		
		Map condition = new HashMap();
		condition.put("gzid", gzid);
		for (String ryid : ryids) {
			condition.put("ryid", ryid);
			daoUtils.execSql("#gzzx.insert_t_gz_tsgzry", condition);
		}
	}
	
	@Override
	public List getAll() {
		return daoUtils.find("#gzzx.getAllTsgz", null);
	}
	
	@Override
	public int changeGzQyzt(String gzid, String qyzt) {
		Map condition = new HashMap();
		condition.put("gzid", gzid);
		condition.put("qyzt", qyzt);
		return daoUtils.execSql("#gzzx.changeGzQyzt", condition);
	}
}
