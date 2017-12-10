package com.core.jadlwork.business.gzzx.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.core.jadlsoft.business.BaseManager;
import com.core.jadlsoft.utils.DateUtils;
import com.core.jadlsoft.utils.StringUtils;
import com.core.jadlsoft.utils.SystemConstants;
import com.core.jadlwork.business.gzzx.IGzPushManager;
import com.core.jadlwork.business.gzzx.IGzzxManager;
import com.core.jadlwork.cache.gzzx.GzzxCache;
import com.core.jadlwork.model.gzzx.GzzxJbxxBean;
import com.core.jadlwork.utils.BeanUtils;

/**
 * 故障中心业务操作实现类
 * @类名: GzzxManager
 * @作者: 李春晓
 * @时间: 2017-7-4 下午1:29:53
 */
public class GzzxManager extends BaseManager implements IGzzxManager {
	
	private IGzPushManager gzPushManager;
	public void setGzPushManager(IGzPushManager gzPushManager) {
		this.gzPushManager = gzPushManager;
	}
	
	//============================基本业务操作===================================
	
	@Override
	public int saveGzzx(GzzxJbxxBean gzzxJbxxBean) {
		return daoUtils.save(gzzxJbxxBean);
	}
	
	@Override
	public int changeToYcl(String id) {
		GzzxJbxxBean jbxxBean = new GzzxJbxxBean();
		jbxxBean.setId(id);
//		jbxxBean.setClzt(SystemConstants.GZZX_CLZT_YCL);
		return daoUtils.update(jbxxBean, "clzt");
	}
	
	@Override
	public int changeToYts(String id) {
		GzzxJbxxBean jbxxBean = new GzzxJbxxBean();
		jbxxBean.setId(id);
//		jbxxBean.setClzt(SystemConstants.GZZX_CLZT_YTS);
		return daoUtils.update(jbxxBean, "clzt");
	}
	
	@Override
	public int deleteGzxx(String id) {
		GzzxJbxxBean jbxxBean = new GzzxJbxxBean();
		jbxxBean.setId(id);
		jbxxBean.setZt(SystemConstants.ZT_FALSE);
		return daoUtils.update(jbxxBean, "zt");
	}

	//=============================捕获故障操作====================================
	
	@Override
	public List getFwqYyGzxxList() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_FALSE);	//找的是运行状态为异常的（其他的如判断服务器是否可用的zt直接在sql中写出来了）
//		condition.put("isalarm", SystemConstants.GZZX_ISALARM_NOTALARM);
		return daoUtils.find("#gzzx.getFwqYyGzxxList", condition);
	}
	
	/*
	 * 校验是否是重复故障
	 */
	private Object[] isRepeatGzxx(Map gzMap) {
		Object gzlx = gzMap.get("gzlx");
		if (SystemConstants.GZZX_GZLX_FWQ.equals(gzlx)) {
			//服务器故障
			//根据   服务器IP/服务器端口/异常信息  作为判定相同的依据
			/*
			 * 拿着时间与这几个信息进行去库中查询，并且查询不是重复故障的信息
			 * 如果有就说明已经有了主推送信息，就直接返回true
			 * 没有就说明还没有主推送信息，返回false
			 */
			String fwqip = gzMap.get("fwqip") == null || gzMap.get("fwqip").equals("") ? "" : (String)gzMap.get("fwqip");
			String fwqdk = gzMap.get("fwqdk") == null || gzMap.get("fwqdk").equals("") ? "" : (String)gzMap.get("fwqdk");
			String ycxx = gzMap.get("ycxx") == null || gzMap.get("ycxx").equals("") ? "" : (String)gzMap.get("ycxx");
			if (StringUtils.isEmpty(fwqip) || StringUtils.isEmpty(fwqdk) || StringUtils.isEmpty(ycxx)) {
				return new Object[]{false, null};
			}
			
			Map condition = new HashMap();
			condition.put("fwqip", fwqip);
			condition.put("fwqdk", fwqdk);
			condition.put("ycxx", ycxx);
			condition.put("iscfgz", SystemConstants.ZT_FALSE);
			condition.put("zt", SystemConstants.ZT_TRUE);
			
			String currentDate = DateUtils.getCurrentData("yyyy-MM-dd HH:mm:ss");	//当前时间
			String fromDate = DateUtils.getDateAddMin(currentDate, GzzxCache.gzFilterMinute * (-1));	//指定时间之前的时间
			
			condition.put("fromdate", fromDate);
			condition.put("todate", currentDate);
			
			List find = daoUtils.find("#gzzx.getRepeatGzxxForFwq", condition);
			if (find == null || find.size()==0) {
				return new Object[]{false, null};
			}else {
				return new Object[]{true, find.get(0)};
			}
		}else if (SystemConstants.GZZX_GZLX_YY.equals(gzlx)) {
			//应用故障
			//根据   服务器IP/服务器端口/应用名称/异常信息  作为判定相同的依据
			/*
			 * 拿着时间与这几个信息进行去库中查询，并且查询不是重复故障的信息
			 * 如果有就说明已经有了主推送信息，就直接返回true
			 * 没有就说明还没有主推送信息，返回false
			 */
			String fwqip = gzMap.get("fwqip") == null || gzMap.get("fwqip").equals("") ? "" : (String)gzMap.get("fwqip");
			String fwqdk = gzMap.get("fwqdk") == null || gzMap.get("fwqdk").equals("") ? "" : (String)gzMap.get("fwqdk");
			String yyname = gzMap.get("yyname") == null || gzMap.get("yyname").equals("") ? "" : (String)gzMap.get("yyname");
			String ycxx = gzMap.get("ycxx") == null || gzMap.get("ycxx").equals("") ? "" : (String)gzMap.get("ycxx");
			if (StringUtils.isEmpty(fwqip) || StringUtils.isEmpty(fwqdk) 
					|| StringUtils.isEmpty(ycxx) || StringUtils.isEmpty(yyname)) {
				return new Object[]{false, null};
			}
			
			Map condition = new HashMap();
			condition.put("fwqip", fwqip);
			condition.put("fwqdk", fwqdk);
			condition.put("yyname", yyname);
			condition.put("ycxx", ycxx);
			condition.put("iscfgz", SystemConstants.ZT_FALSE);
			condition.put("zt", SystemConstants.ZT_TRUE);
			
			String currentDate = DateUtils.getCurrentData("yyyy-MM-dd HH:mm:ss");	//当前时间
			String fromDate = DateUtils.getDateAddMin(currentDate, GzzxCache.gzFilterMinute * (-1));	//指定时间之前的时间
			
			condition.put("fromdate", fromDate);
			condition.put("todate", currentDate);
			
			List find = daoUtils.find("#gzzx.getRepeatGzxxForYy", condition);
			if (find == null || find.size()==0) {
				return new Object[]{false, null};
			}else {
				return new Object[]{true, find.get(0)};
			}
		}else if (SystemConstants.GZZX_GZLX_YYYX.equals(gzlx)) {
			//应用运行故障
			return new Object[]{true, null};	//TODO
		}else {
			return new Object[]{false, null};
		}
	}
	
	@Override
	public int[] saveGzInfo(List gzxxList) {
		
		/*
		 * 1、遍历每一个要保存的故障信息
		 * 2、根据不同的种类进行校验判断是否在指定时间内重复出现
		 * 3、如果不是重复，就按照要推送的信息格式进行保存，并且在保存成功之后将信息保存在推送的信息中
		 * 4、如果是重复的，就按照重复的格式去保存，不保存对应的推送信息
		 */
		if (gzxxList == null || gzxxList.size() == 0) {
			return new int[]{0,0};
		}
		
		int i = 0;
		int[] res = {0,0};
		List ztsGzList = new ArrayList();
		
		for (Map gzMap : (List<Map>)gzxxList) {
			//根据不同的故障类型，进行校验
			Object[] repeatGzxx = isRepeatGzxx(gzMap);
			boolean isRepeatGz = (Boolean) repeatGzxx[0];
			if (isRepeatGz) {
				//是重复的
				if (repeatGzxx[1] != null) {
					Map tszGzMap = (Map) repeatGzxx[1];
					Object gzBeanObj = BeanUtils.populate(GzzxJbxxBean.class, tszGzMap);
					if (gzBeanObj != null) {
						GzzxJbxxBean jbxxBean = (GzzxJbxxBean) gzBeanObj;
						//只有id、捕获时间、是否为重复故障不同，其余的都是相同的
						String id;
						synchronized (jbxxBean) {
							id = String.valueOf(daoUtils.getNextval("q_gzzx_jbxx"));
						}
						jbxxBean.setId(id);
						jbxxBean.setBhsj(DateUtils.createCurrentDate());
						jbxxBean.setIscfgz(SystemConstants.ZT_TRUE);
						int save = daoUtils.save(jbxxBean);
						res[0] += save;
					}
				}
			}else {
				//不是重复的
				//正常保存即可，设置推送主ID ，和是否为重复故障
				String id;
				String tszid;
				synchronized (gzMap) {
					id = String.valueOf(daoUtils.getNextval("q_gzzx_jbxx"));
					tszid = String.valueOf(daoUtils.getNextval("q_gzzx_tszid"));
				}
				GzzxJbxxBean jbxxBean = new GzzxJbxxBean();
				jbxxBean.setId(id);
				jbxxBean.setTszid(tszid);
				jbxxBean.setIscfgz(SystemConstants.ZT_FALSE);
				
				jbxxBean.setGzlx(gzMap.get("gzlx")==null ? "" : (String)gzMap.get("gzlx"));
				jbxxBean.setFwqip(gzMap.get("fwqip")==null ? "" : (String)gzMap.get("fwqip"));
				jbxxBean.setFwqdk(gzMap.get("fwqdk")==null ? "" : (String)gzMap.get("fwqdk"));
				jbxxBean.setYyname(gzMap.get("yyname")==null ? "" : (String)gzMap.get("yyname"));
				jbxxBean.setYclx(gzMap.get("yclx")==null ? "" : (String)gzMap.get("yclx"));
				jbxxBean.setYcyy(gzMap.get("ycyy")==null ? "" : (String)gzMap.get("ycyy"));
				jbxxBean.setYcxx(gzMap.get("ycxx")==null ? "" : (String)gzMap.get("ycxx"));
				jbxxBean.setYxj(gzMap.get("yxj")==null ? "" : (String)gzMap.get("yxj"));
				jbxxBean.setBhsj(DateUtils.createCurrentDate());
				jbxxBean.setZt(SystemConstants.ZT_TRUE);
				
				int save = daoUtils.save(jbxxBean);
				i += save;
				res[1] += save;
				if (save>0) {
					//保存故障信息成功之后，将推送信息也保存
					gzMap.put("id", id);
					ztsGzList.add(gzMap);
				}
			}
		}
		if (ztsGzList.size()>0) {
			gzPushManager.savePushMsgByGzzx(ztsGzList);
		}
		return res;
	}
	
	@Override
	public int[] saveFwqYyGzxx() {
		int[] result = {0,0};
		List tosaveGzList = new ArrayList();
		//1、获取所有的服务器和应用异常信息
		List fwqYyGzxxList = this.getFwqYyGzxxList();
		if (fwqYyGzxxList == null || fwqYyGzxxList.size()==0) {
			return result;
		}
		//2、按照服务器优先级的方式排除掉由服务器异常引起的应用异常（sql中已经过滤掉了）
		tosaveGzList = fwqYyGzxxList;
		//3、将筛选下的信息进行批量保存到库中
		result = saveGzInfo(tosaveGzList);
		return result;
	}

	/**
	 * 更改服务器和应用表中对应的isalarm属性
	 * @功能: 这里在这儿去修改服务器应用信息，并不是很好的低耦合的操作方式，只不过暂时没有更好的方案，这样操作了
	 * @param gzxxList	
	 * @return: int
	 */
	private void changeAlarm(List gzxxList) {
		//采用批量修改的方法
		String[] fwqxxField = { "isalarm","id" }; //服务器字段
		List fwqxxList = new ArrayList();	//服务器数据
		
		String[] yyxxField = { "isalarm","id" }; //应用字段
		List yyxxList = new ArrayList();	//应用数据
		
		int count = 0;
		if (gzxxList != null && gzxxList.size()>0) {
			for (Map gzxxMap : (List<Map>)gzxxList) {
				Map data = null;
				if (SystemConstants.GZZX_GZLX_FWQ.equals(gzxxMap.get("gzlx"))) {
					//是服务器故障类型
					data = new HashMap();
					data.put("id", gzxxMap.get("fwqid"));
					data.put("isalarm", SystemConstants.GZZX_ISALARM_ALARM);
					fwqxxList.add(data);
				}else {
					if (SystemConstants.GZZX_GZLX_YY.equals(gzxxMap.get("gzlx"))) {
						//是应用故障类型
						data = new HashMap();
						data.put("id", gzxxMap.get("yyid"));
						data.put("isalarm", SystemConstants.GZZX_ISALARM_ALARM);
						yyxxList.add(data);
					}
				}
			}
			int[] fwqCountArr = null;
			int[] yyCountArr = null;
			if (fwqxxList.size()>0) {
				fwqCountArr = daoUtils.executeBatchUpdate("#gzzx.update_alarm_t_fwqgl_fwq", fwqxxField, fwqxxList);
			}
			if (yyxxList.size()>0) {
				yyCountArr = daoUtils.executeBatchUpdate("#gzzx.update_alarm_t_yygl_yy", yyxxField, yyxxList);
			}
		}
	}
	
	@Override
	public void updateFwqYyAlarm(List fwqidList, List yyidList) {
		//采用批量修改的方法
		String[] fwqxxField = { "isalarm","id" }; //服务器字段
		List fwqxxList = new ArrayList();	//服务器数据
		
		String[] yyxxField = { "isalarm","id" }; //应用字段
		List yyxxList = new ArrayList();	//应用数据
		
		if (fwqidList != null && fwqidList.size()>0) {
			Map data = null;
			for (String fwqid : (List<String>)fwqidList) {
				data = new HashMap();
				data.put("id", fwqid);
				data.put("isalarm", SystemConstants.GZZX_ISALARM_NOTALARM);
				fwqxxList.add(data);
			}
		}
		if (yyidList != null && yyidList.size()>0) {
			Map data = null;
			for (String yyid : (List<String>)yyidList) {
				data = new HashMap();
				data.put("id", yyid);
				data.put("isalarm", SystemConstants.GZZX_ISALARM_NOTALARM);
				yyxxList.add(data);
			}
		}
		if (fwqxxList.size()>0) {
			daoUtils.executeBatchUpdate("#gzzx.update_alarm_t_fwqgl_fwq", fwqxxField, fwqxxList);
		}
		if (yyxxList.size()>0) {
			daoUtils.executeBatchUpdate("#gzzx.update_alarm_t_yygl_yy", yyxxField, yyxxList);
		}
	}
	
	@Override
	public List getGzBhXx(String gzid) {
		//获取该故障id下面的所有重复故障信息
		Map condition = new HashMap();
		condition.put("gzid", gzid);
		condition.put("iscfgz", SystemConstants.ZT_TRUE);
		return daoUtils.find("#gzzx.getGzBhXx", condition);
	}
	
	

}
