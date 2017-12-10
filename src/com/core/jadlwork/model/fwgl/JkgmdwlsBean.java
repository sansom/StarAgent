package com.core.jadlwork.model.fwgl;

/**
 * 数据接口购买单位历史
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 9:52:33 AM
 */
public class JkgmdwlsBean{
	public static final String db_tablename = "t_fwgl_jkgmdwls";
	public static final String db_tablepkfields = "id";
   /*
    * 有效截至时间
    */
    private java.util.Date yxjzsj;
   /*
    * 有效开始时间
    */
    private java.util.Date yxkssj;
   /*
    * KEY
    */
    private java.util.Date key;
   /*
    * 购买单位名称
    */
    private String gmdwmc;
   /*
    * 购买单位代码
    */
    private String gmdwdm;
   /*
    * 接口ID
    */
    private String jkid;
   /*
    * 序号
    */
    private String id;
  	
    public void setYxjzsj(java.util.Date yxjzsj){
	    this.yxjzsj = yxjzsj;
    }
    public java.util.Date getYxjzsj(){
	    return this.yxjzsj;
    }
    public void setYxkssj(java.util.Date yxkssj){
	    this.yxkssj = yxkssj;
    }
    public java.util.Date getYxkssj(){
	    return this.yxkssj;
    }
    public void setKey(java.util.Date key){
	    this.key = key;
    }
    public java.util.Date getKey(){
	    return this.key;
    }
    public void setGmdwmc(String gmdwmc){
	    this.gmdwmc = gmdwmc;
    }
    public String getGmdwmc(){
	    return this.gmdwmc;
    }
    public void setGmdwdm(String gmdwdm){
	    this.gmdwdm = gmdwdm;
    }
    public String getGmdwdm(){
	    return this.gmdwdm;
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
		if(jkid == null || jkid.equals("")){	  		
		    errMsg.append("接口ID为空！");
		}
		if(id == null || id.equals("")){	  		
		    errMsg.append("序号为空！");
		}
		/* 检查长度 */
		if(gmdwmc != null && gmdwmc.getBytes().length > 100){
		    errMsg.append("购买单位名称超长！容许长度：100。"); 
		}	   	
		if(gmdwdm != null && gmdwdm.getBytes().length > 50){
		    errMsg.append("购买单位代码超长！容许长度：50。"); 
		}	   	
		if(jkid != null && jkid.getBytes().length > 17){
		    errMsg.append("接口ID超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}