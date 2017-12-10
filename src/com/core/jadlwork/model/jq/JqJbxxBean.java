package com.core.jadlwork.model.jq;

public class JqJbxxBean{
	public static final String db_tablename = "t_jq_jbxx";
	public static final String db_tablepkfields = "id";
   /*
    * ID
    */
    private String id;
   /*
    * 集群名称
    */
    private String jqname;
   /*
    * 访问域名
    */
    private String fwym;
   /*
    * 访问端口
    */
    private String fwdk;
   /*
    * 创建时间
    */
    private java.util.Date cjsj;
   /*
    * 撤销时间
    */
    private java.util.Date cxsj;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 状态 0：可用   1：不可用
    */
    private String zt;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setJqname(String jqname){
	    this.jqname = jqname;
    }
    public String getJqname(){
	    return this.jqname;
    }
    public void setFwym(String fwym){
	    this.fwym = fwym;
    }
    public String getFwym(){
	    return this.fwym;
    }
    public void setFwdk(String fwdk){
	    this.fwdk = fwdk;
    }
    public String getFwdk(){
	    return this.fwdk;
    }
    public void setCjsj(java.util.Date cjsj){
	    this.cjsj = cjsj;
    }
    public java.util.Date getCjsj(){
	    return this.cjsj;
    }
    public void setCxsj(java.util.Date cxsj){
	    this.cxsj = cxsj;
    }
    public java.util.Date getCxsj(){
	    return this.cxsj;
    }
    public void setZhxgsj(java.util.Date zhxgsj){
	    this.zhxgsj = zhxgsj;
    }
    public java.util.Date getZhxgsj(){
	    return this.zhxgsj;
    }
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
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
		if(jqname != null && jqname.getBytes().length > 50){
		    errMsg.append("集群名称超长！容许长度：50。"); 
		}	   	
		if(fwym != null && fwym.getBytes().length > 30){
		    errMsg.append("访问域名超长！容许长度：30。"); 
		}	   	
		if(fwdk != null && fwdk.getBytes().length > 6){
		    errMsg.append("访问端口超长！容许长度：6。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：可用   1：不可用超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}