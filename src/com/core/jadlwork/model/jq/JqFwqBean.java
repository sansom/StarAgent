package com.core.jadlwork.model.jq;

public class JqFwqBean{
	public static final String db_tablename = "t_jq_fwq";
	public static final String db_tablepkfields = "id";
   /*
    * ID
    */
    private String id;
   /*
    * 集群id
    */
    private String jqid;
   /*
    * 服务器id
    */
    private String fwqid;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setJqid(String jqid){
	    this.jqid = jqid;
    }
    public String getJqid(){
	    return this.jqid;
    }
    public void setFwqid(String fwqid){
	    this.fwqid = fwqid;
    }
    public String getFwqid(){
	    return this.fwqid;
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
		if(jqid != null && jqid.getBytes().length > 17){
		    errMsg.append("集群id超长！容许长度：17。"); 
		}	   	
		if(fwqid != null && fwqid.getBytes().length > 17){
		    errMsg.append("服务器id超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}