package com.core.jadlwork.model.fwgl;

public class SjjkparamBean{
	public static final String db_tablename = "t_fwgl_sjjkparam";
	public static final String db_tablepkfields = "id";
   /*
    * 参数类型
    */
    private String paramlx;
   /*
    * 参数名称
    */
    private String paramname;
   /*
    * 参数
    */
    private String param;
   /*
    * 所属接口
    */
    private String jkid;
   /*
    * 序号
    */
    private String id;
  	
    public void setParamlx(String paramlx){
	    this.paramlx = paramlx;
    }
    public String getParamlx(){
	    return this.paramlx;
    }
    public void setParamname(String paramname){
	    this.paramname = paramname;
    }
    public String getParamname(){
	    return this.paramname;
    }
    public void setParam(String param){
	    this.param = param;
    }
    public String getParam(){
	    return this.param;
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
		if(paramlx != null && paramlx.getBytes().length > 1){
		    errMsg.append("参数类型超长！容许长度：1。"); 
		}	   	
		if(paramname != null && paramname.getBytes().length > 20){
		    errMsg.append("参数名称超长！容许长度：20。"); 
		}	   	
		if(param != null && param.getBytes().length > 20){
		    errMsg.append("参数超长！容许长度：20。"); 
		}	   	
		if(jkid != null && jkid.getBytes().length > 30){
		    errMsg.append("所属接口超长！容许长度：30。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}