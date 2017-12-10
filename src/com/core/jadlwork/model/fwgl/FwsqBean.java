package com.core.jadlwork.model.fwgl;

/**
 * 服务授权访问BEAN
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 9:50:31 AM
 */
public class FwsqBean{
	public static final String db_tablename = "t_fwgl_fwsq";
	public static final String db_tablepkfields = "id";
   /*
    * 状态 0：有效 1：无效
    */
    private String zt;
   /*
    * 授权方位 0：允许访问 1：禁止访问
    */
    private String sqfw;
   /*
    * IP地址
    */
    private String ip;
    /*
     * 服务ID
     */
    private String fwid;
   /*
    * 序号
    */
    private String id;
  	
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public void setSqfw(String sqfw){
	    this.sqfw = sqfw;
    }
    public String getSqfw(){
	    return this.sqfw;
    }
    public void setIp(String ip){
	    this.ip = ip;
    }
    public String getIp(){
	    return this.ip;
    }
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
  	
    
    public String getFwid() {
		return fwid;
	}
	public void setFwid(String fwid) {
		this.fwid = fwid;
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
		if(sqfw != null && sqfw.getBytes().length > 1){
		    errMsg.append("授权方位 0：允许访问 1：禁止访问超长！容许长度：1。"); 
		}	   	
		if(ip != null && ip.getBytes().length > 17){
		    errMsg.append("IP地址超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}