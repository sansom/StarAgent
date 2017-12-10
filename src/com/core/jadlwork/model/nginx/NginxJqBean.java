package com.core.jadlwork.model.nginx;

public class NginxJqBean{
	public static final String db_tablename = "t_nginx_jq";
	public static final String db_tablepkfields = "id";
   /*
    * ID
    */
    private String id;
   /*
    * Nginxid
    */
    private String nginxid;
   /*
    * 集群id
    */
    private String jqid;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setNginxid(String nginxid){
	    this.nginxid = nginxid;
    }
    public String getNginxid(){
	    return this.nginxid;
    }
    public void setJqid(String jqid){
	    this.jqid = jqid;
    }
    public String getJqid(){
	    return this.jqid;
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
		if(nginxid != null && nginxid.getBytes().length > 17){
		    errMsg.append("Nginxid超长！容许长度：17。"); 
		}	   	
		if(jqid != null && jqid.getBytes().length > 17){
		    errMsg.append("集群id超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}