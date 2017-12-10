package com.core.jadlwork.model.gzzx;

public class TsRyBean{
	public static final String db_tablename = "t_ts_ry";
	public static final String db_tablepkfields = "id";
   /*
    * ID
    */
    private String id;
   /*
    * 用户名
    */
    private String username;
   /*
    * 是否启用短信推送   0 启用     1 不启用
    */
    private String dxts;
   /*
    * 是否启用微信推送   0 启用     1 不启用
    */
    private String wxts;
   /*
    * 是否启用邮箱推送   0 启用     1 不启用
    */
    private String yxts;
   /*
    * 手机号码
    */
    private String sjhm;
   /*
    * 邮箱地址
    */
    private String yxdz;
   /*
    * 微信id，就是openID
    */
    private String wxid;
   /*
    * 添加时间
    */
    private java.util.Date tjsj;
   /*
    * 注销时间
    */
    private java.util.Date zxsj;
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
    public void setUsername(String username){
	    this.username = username;
    }
    public String getUsername(){
	    return this.username;
    }
    public void setDxts(String dxts){
	    this.dxts = dxts;
    }
    public String getDxts(){
	    return this.dxts;
    }
    public void setWxts(String wxts){
	    this.wxts = wxts;
    }
    public String getWxts(){
	    return this.wxts;
    }
    public void setYxts(String yxts){
	    this.yxts = yxts;
    }
    public String getYxts(){
	    return this.yxts;
    }
    public void setSjhm(String sjhm){
	    this.sjhm = sjhm;
    }
    public String getSjhm(){
	    return this.sjhm;
    }
    public void setYxdz(String yxdz){
	    this.yxdz = yxdz;
    }
    public String getYxdz(){
	    return this.yxdz;
    }
    public void setWxid(String wxid){
	    this.wxid = wxid;
    }
    public String getWxid(){
	    return this.wxid;
    }
    public void setTjsj(java.util.Date tjsj){
	    this.tjsj = tjsj;
    }
    public java.util.Date getTjsj(){
	    return this.tjsj;
    }
    public void setZxsj(java.util.Date zxsj){
	    this.zxsj = zxsj;
    }
    public java.util.Date getZxsj(){
	    return this.zxsj;
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
		if(username != null && username.getBytes().length > 30){
		    errMsg.append("用户名超长！容许长度：30。"); 
		}	   	
		if(dxts != null && dxts.getBytes().length > 1){
		    errMsg.append("是否启用短信推送   0 启用     1 不启用超长！容许长度：1。"); 
		}	   	
		if(wxts != null && wxts.getBytes().length > 1){
		    errMsg.append("是否启用微信推送   0 启用     1 不启用超长！容许长度：1。"); 
		}	   	
		if(yxts != null && yxts.getBytes().length > 1){
		    errMsg.append("是否启用邮箱推送   0 启用     1 不启用超长！容许长度：1。"); 
		}	   	
		if(sjhm != null && sjhm.getBytes().length > 20){
		    errMsg.append("手机号码超长！容许长度：20。"); 
		}	   	
		if(yxdz != null && yxdz.getBytes().length > 50){
		    errMsg.append("邮箱地址超长！容许长度：50。"); 
		}	   	
		if(wxid != null && wxid.getBytes().length > 50){
		    errMsg.append("微信id，就是openID超长！容许长度：50。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：可用   1：不可用超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}