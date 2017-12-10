package com.core.jadlwork.model.fwgl;

import java.util.ArrayList;
import java.util.List;

public class SjjkTbLsBean{
	public static final String db_tablename = "T_FWGL_SJJKTBLS";
	public static final String db_tablepkfields = "id";
   /*
    * 状态 0：有效 1：无效
    */
    private String zt;
   /*
    * 接口状态 0：已发布 1：未发布
    */
    private String jkzt;
   /*
    * 创建时间
    */
    private java.util.Date cjsj;
   /*
    * URI
    */
    private String uri;
   /*
    * 接口名称
    */
    private String jkname;
   /*
    * 接口所属应用
    */
    private String yyid;
   /*
    * 接口ID
    */
    private String jkid;
   /*
    * 序号
    */
    private String id;
    /*
     * 接口类型
     */
    private String jklx;
    /*
     * 接口说明
     */
    private String jksm;
    /*
     * 应用名称
     */
    private String yyname;
    /*
     * 方法名称
     */
    private String ffname;
    /*
     * 服务分类
     */
    private String fwfl;
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public void setJkzt(String jkzt){
	    this.jkzt = jkzt;
    }
    public String getJkzt(){
	    return this.jkzt;
    }
    public void setCjsj(java.util.Date cjsj){
	    this.cjsj = cjsj;
    }
    public java.util.Date getCjsj(){
	    return this.cjsj;
    }
    public void setJkname(String jkname){
	    this.jkname = jkname;
    }
    public String getJkname(){
	    return this.jkname;
    }
    public void setYyid(String yyid){
	    this.yyid = yyid;
    }
    public String getYyid(){
	    return this.yyid;
    }
    public void setJkid(String jkid){
	    this.jkid = jkid;
    }
    public String getJkid(){
	    return this.jkid;
    }
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
  	
 
	public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("序号为空！");
		}
		/* 检查长度 */
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：有效 1：无效超长！容许长度：1。"); 
		}	   	
		if(jkzt != null && jkzt.getBytes().length > 1){
		    errMsg.append("接口状态 0：已发布 1：未发布超长！容许长度：1。"); 
		}	   	
		if(uri != null && uri.getBytes().length > 100){
		    errMsg.append("URl超长！容许长度：100。"); 
		}	   	
		if(jkname != null && jkname.getBytes().length > 50){
		    errMsg.append("接口名称超长！容许长度：50。"); 
		}	   	
		if(yyid != null && yyid.getBytes().length > 17){
		    errMsg.append("接口所属应用超长！容许长度：17。"); 
		}	   	
		if(jkid != null && jkid.getBytes().length > 30){
		    errMsg.append("接口ID超长！容许长度：30。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
	public String getJklx() {
		return jklx;
	}
	public void setJklx(String jklx) {
		this.jklx = jklx;
	}
	public String getJksm() {
		return jksm;
	}
	public void setJksm(String jksm) {
		this.jksm = jksm;
	}
	public String getYyname() {
		return yyname;
	}
	public void setYyname(String yyname) {
		this.yyname = yyname;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getFfname() {
		return ffname;
	}
	public void setFfname(String ffname) {
		this.ffname = ffname;
	}
	public String getFwfl() {
		return fwfl;
	}
	public void setFwfl(String fwfl) {
		this.fwfl = fwfl;
	}
}