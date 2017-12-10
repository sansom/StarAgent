package com.core.jadlwork.model.fwgl;

public class FwylgxBean{
	public static final String db_tablename = "t_fwgl_fwylgx";
	public static final String db_tablepkfields = "id";
   /*
    * 依赖服务名称
    */
    private String prefwname;
   /*
    * 依赖服务ID
    */
    private String prefwid;
   /*
    * 服务名称
    */
    private String fwname;
   /*
    * 服务ID
    */
    private String fwid;
   /*
    * 序号
    */
    private String id;
  	
    public void setPrefwname(String prefwname){
	    this.prefwname = prefwname;
    }
    public String getPrefwname(){
	    return this.prefwname;
    }
    public void setPrefwid(String prefwid){
	    this.prefwid = prefwid;
    }
    public String getPrefwid(){
	    return this.prefwid;
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
		if(prefwname != null && prefwname.getBytes().length > 100){
		    errMsg.append("依赖服务名称超长！容许长度：100。"); 
		}	   	
		if(prefwid != null && prefwid.getBytes().length > 17){
		    errMsg.append("依赖服务ID超长！容许长度：17。"); 
		}	   	
		if(fwname != null && fwname.getBytes().length > 100){
		    errMsg.append("服务名称超长！容许长度：100。"); 
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