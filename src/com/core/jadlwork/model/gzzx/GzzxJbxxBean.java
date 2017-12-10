package com.core.jadlwork.model.gzzx;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Arrays;

public class GzzxJbxxBean{
	public static final String db_tablename = "t_gzzx_jbxx";
	public static final String db_tablepkfields = "id";
   /*
    * ID
    */
    private String id;
   /*
    * 故障类型  字典表：01：服务器故障	02：应用故障	03：应用运行异常
    */
    private String gzlx;
   /*
    * 服务器IP
    */
    private String fwqip;
   /*
    * 服务器端口
    */
    private String fwqdk;
   /*
    * 应用名称
    */
    private String yyname;
   /*
    * 异常类型
    */
    private String yclx;
   /*
    * 异常原因
    */
    private String ycyy;
   /*
    * 异常信息
    */
    private String ycxx;
   /*
    * 优先级，字典表t_dm_gzzx_yxj
    */
    private String yxj;
   /*
    * 捕获时间
    */
    private java.util.Date bhsj;
    /*
     * 推送主ID
     */
    private String tszid;
    /*
     * 是否为重复故障
     */
    private String iscfgz;
   /*
    * 状态 0：可用   1：不可用
    */
    private String zt;
  	
  	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGzlx() {
		return gzlx;
	}

	public void setGzlx(String gzlx) {
		this.gzlx = gzlx;
	}

	public String getFwqip() {
		return fwqip;
	}

	public void setFwqip(String fwqip) {
		this.fwqip = fwqip;
	}

	public String getFwqdk() {
		return fwqdk;
	}

	public void setFwqdk(String fwqdk) {
		this.fwqdk = fwqdk;
	}

	public String getYyname() {
		return yyname;
	}

	public void setYyname(String yyname) {
		this.yyname = yyname;
	}

	public String getYclx() {
		return yclx;
	}

	public void setYclx(String yclx) {
		this.yclx = yclx;
	}

	public String getYcyy() {
		return ycyy;
	}

	public void setYcyy(String ycyy) {
		this.ycyy = ycyy;
	}

	public String getYcxx() {
		return ycxx;
	}

	public void setYcxx(String ycxx) {
		this.ycxx = ycxx;
	}

	public String getYxj() {
		return yxj;
	}

	public void setYxj(String yxj) {
		this.yxj = yxj;
	}

	public java.util.Date getBhsj() {
		return bhsj;
	}

	public void setBhsj(java.util.Date bhsj) {
		this.bhsj = bhsj;
	}

	public String getTszid() {
		return tszid;
	}

	public void setTszid(String tszid) {
		this.tszid = tszid;
	}

	public String getIscfgz() {
		return iscfgz;
	}

	public void setIscfgz(String iscfgz) {
		this.iscfgz = iscfgz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("ID为空！");
		}
		/* 检查长度 */
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("ID超长！容许长度：17。"); 
		}	   	
		if(gzlx != null && gzlx.getBytes().length > 2){
		    errMsg.append("故障类型  字典表：01：服务器故障	02：应用故障	03：应用运行异常超长！容许长度：2。"); 
		}	   	
		if(fwqip != null && fwqip.getBytes().length > 17){
		    errMsg.append("服务器IP超长！容许长度：17。"); 
		}	   	
		if(fwqdk != null && fwqdk.getBytes().length > 5){
		    errMsg.append("服务器端口超长！容许长度：5。"); 
		}	   	
		if(yyname != null && yyname.getBytes().length > 50){
		    errMsg.append("应用名称超长！容许长度：50。"); 
		}	   	
		if(yclx != null && yclx.getBytes().length > 120){
		    errMsg.append("异常类型超长！容许长度：120。"); 
		}	   	
		if(ycyy != null && ycyy.getBytes().length > 255){
		    errMsg.append("异常原因超长！容许长度：255。"); 
		}	   	
		if(ycxx != null && ycxx.getBytes().length > 255){
		    errMsg.append("异常信息超长！容许长度：255。"); 
		}	   	
		if(yxj != null && yxj.getBytes().length > 2){
		    errMsg.append("优先级，字典表t_dm_gzzx_yxj超长！容许长度：2。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：可用   1：不可用超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
    
    /**
     * 根据传递过来的map（里面包含有故障中心对应的信息）,校验是否是同一个故障信息
     * @param gzxxMap	故障信息map
     * @return: void
     */
    public boolean isSameGzxx(Map gzxxMap) {
    	String[] exceptFields = {
    			"db_tablename","db_tablepkfields","id","bhsj","tssj","zt"
    	};
    	/*
    	 * 校验项包括：
    	 * 	gzlx、fwqip、fwqdk、yyname、yclx、ycyy、ycxx、yxj、bz
    	 */
    	Field[] fields = GzzxJbxxBean.class.getDeclaredFields();
    	for (Field field : fields) {
    		if (Arrays.asList(exceptFields).contains(field.getName())) {
				continue;
			}
    		
    		try {
    			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),  
                        this.getClass());  
                Method method = pd.getReadMethod();//获得get方法  
        		if (!fieldValidate(method.invoke(this), gzxxMap.get(field.getName()))) {
    				return false;
    			}
			} catch (Exception e) {
				return false;
			}
		}
    	return true;
    }
    
    /*
     * 校验两个值，为空或者一个为空，一个为字符串都属于相同
     */
    private boolean fieldValidate(Object o1, Object o2) {
    	boolean flag = true;
    	if (o1 == null && o2 != null) {
    		if ("".equals(o2)) {
				//一个为null一个为""
    			return true;
			}else {
				return false;
			}
		}
    	if (o1 != null && o2 == null) {
    		if ("".equals(o1)) {
				//一个为null一个为""
    			return true;
			}else {
				return false;
			}
		}
    	if (o1 == null && o2 == null) {
			return true;
		}
    	if (o1 != null && o2 != null) {
    		return o1.equals(o2);
		}
    	return flag;
    }
}