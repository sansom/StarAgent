package com.core.jadlwork.model.fwgl;

public class FwddBean{
	public static final String db_tablename = "t_fwgl_fwdd";
	public static final String db_tablepkfields = "id";
   /*
    * 状态 0：有效 1：无效
    */
    private String zt;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 设置时间
    */
    private java.util.Date szsj;
   /*
    * 调度等级
    */
    private String dddj;
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
    public void setZhxgsj(java.util.Date zhxgsj){
	    this.zhxgsj = zhxgsj;
    }
    public java.util.Date getZhxgsj(){
	    return this.zhxgsj;
    }
    public void setSzsj(java.util.Date szsj){
	    this.szsj = szsj;
    }
    public java.util.Date getSzsj(){
	    return this.szsj;
    }
    public void setDddj(String dddj){
	    this.dddj = dddj;
    }
    public String getDddj(){
	    return this.dddj;
    }
    public void setFwid(String fwid){
	    this.fwid = fwid;
    }
    public String getFwid(){
	    return this.fwid;
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
		if(dddj != null && dddj.getBytes().length > 10){
		    errMsg.append("调度等级超长！容许长度：10。"); 
		}	   	
		if(fwid != null && fwid.getBytes().length > 17){
		    errMsg.append("服务ID超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}