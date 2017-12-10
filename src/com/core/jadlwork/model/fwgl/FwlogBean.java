package com.core.jadlwork.model.fwgl;
/**
 * 服务请求日志
 * TODO
 * @作者：吴家旭
 * @时间：Nov 4, 2015 10:09:51 AM
 */
public class FwlogBean{
	public static final String db_tablename = "t_fwgl_fwlog";
	public static final String db_tablepkfields = "id";
   /*
    * 请求时间
    */
    private java.util.Date qqsj;
   /*
    * 服务来源 0：内部服务  1：外部服务
    */
    private String fwly;
   /*
    * 服务名称
    */
    private String fwname;
   /*
    * 服务ID
    */
    private String fwid;
   /*
    * 用户IP
    */
    private String clientip;
   /*
    * 用户ID
    */
    private String userid;
   /*
    * 序号
    */
    private String id;
    
    /*
     * 请求地址
     */
    private String qqdz;
    
    
  	
    public String getQqdz() {
		return qqdz;
	}
	public void setQqdz(String qqdz) {
		this.qqdz = qqdz;
	}
	public void setQqsj(java.util.Date qqsj){
	    this.qqsj = qqsj;
    }
    public java.util.Date getQqsj(){
	    return this.qqsj;
    }
    public void setFwly(String fwly){
	    this.fwly = fwly;
    }
    public String getFwly(){
	    return this.fwly;
    }
    public void setFwname(String fwname){
	    this.fwname = fwname;
    }
    public String getFwname(){
	    return this.fwname;
    }
    public void setFwid(String fwid){
	    this.fwid = fwid;
    }
    public String getFwid(){
	    return this.fwid;
    }
    public void setClientip(String clientip){
	    this.clientip = clientip;
    }
    public String getClientip(){
	    return this.clientip;
    }
    public void setUserid(String userid){
	    this.userid = userid;
    }
    public String getUserid(){
	    return this.userid;
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
		if(fwly != null && fwly.getBytes().length > 1){
		    errMsg.append("服务来源 0：内部服务  1：外部服务超长！容许长度：1。"); 
		}	   	
		if(fwname != null && fwname.getBytes().length > 100){
		    errMsg.append("服务名称超长！容许长度：100。"); 
		}	   	
		if(fwid != null && fwid.getBytes().length > 17){
		    errMsg.append("服务ID超长！容许长度：17。"); 
		}	   	
		if(clientip != null && clientip.getBytes().length > 32){
		    errMsg.append("用户IP超长！容许长度：32。"); 
		}	   	
		if(userid != null && userid.getBytes().length > 32){
		    errMsg.append("用户ID超长！容许长度：32。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}