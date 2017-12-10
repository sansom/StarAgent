package com.core.jadlwork.model.fwqgl;

public class MonitorhostBean{
	public static final String db_tablename = "t_xt_monitorhost";
	public static final String db_tablepkfields = "id";
   /*
    * 自增主键
    */
    private String id;
   /*
    * 系统ID：ip+dk
    */
    private String systemid;
   /*
    * 最后通信时间
    */
    private java.util.Date lastcontime;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setSystemid(String systemid){
	    this.systemid = systemid;
    }
    public String getSystemid(){
	    return this.systemid;
    }
    public void setLastcontime(java.util.Date lastcontime){
	    this.lastcontime = lastcontime;
    }
    public java.util.Date getLastcontime(){
	    return this.lastcontime;
    }
  	
    public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("自增主键为空！");
		}
		/* 检查长度 */
		if(id != null && id.getBytes().length > 20){
		    errMsg.append("自增主键超长！容许长度：20。"); 
		}	   	
		if(systemid != null && systemid.getBytes().length > 50){
		    errMsg.append("系统ID：ip+dk超长！容许长度：50。"); 
		}	   	
	  	return errMsg.toString();
    }
}