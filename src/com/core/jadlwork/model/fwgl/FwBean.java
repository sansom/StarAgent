package com.core.jadlwork.model.fwgl;

import java.math.BigDecimal;

public class FwBean{
	public static final String db_tablename = "t_fwgl_fw";
	public static final String db_tablepkfields = "id";
   /*
    * 状态 0：有效 1：无效
    */
    private String zt;
   /*
    * 服务状态 0：已发布 1：未发布
    */
    private String fwzt;
   /*
    * 删除时间
    */
    private java.util.Date scsj;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 创建时间
    */
    private java.util.Date cjsj;
   /*
    * 价格单位
    */
    private String priceunit;
   /*
    * 价格
    */
    private String  price;
    /*
     * 路由类型
     */
     private String lylx;
    /*
     * 服务输出
     */
     private String fwsc;
     /*
      * 服务输入
      */
      private String fwsr;
   /*
    * 服务描述
    */
    private String fwms;
   /*
    * URI
    */
    private String uri;
   /*
    * 服务分类
    */
    private String fwfl;
   /*
    * 服务名称
    */
    private String fwname;
   /*
    * 服务ID
    */
    private String fwid;
   /*
    * 服务所在应用
    */
    private String yyid;
   /*
    * 序号
    */
    private String id;
    
    /*
     * 排序
     */
    private BigDecimal sort;
    
    
	public BigDecimal getSort() {
		return sort;
	}
	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public void setFwzt(String fwzt){
	    this.fwzt = fwzt;
    }
    public String getFwzt(){
	    return this.fwzt;
    }
    public void setScsj(java.util.Date scsj){
	    this.scsj = scsj;
    }
    public java.util.Date getScsj(){
	    return this.scsj;
    }
    public void setZhxgsj(java.util.Date zhxgsj){
	    this.zhxgsj = zhxgsj;
    }
    public java.util.Date getZhxgsj(){
	    return this.zhxgsj;
    }
    public void setCjsj(java.util.Date cjsj){
	    this.cjsj = cjsj;
    }
    public java.util.Date getCjsj(){
	    return this.cjsj;
    }
    public void setPriceunit(String priceunit){
	    this.priceunit = priceunit;
    }
    public String getPriceunit(){
	    return this.priceunit;
    }


	public String getLylx() {
		return lylx;
	}
	public void setLylx(String lylx) {
		this.lylx = lylx;
	}
	public String getFwsc() {
		return fwsc;
	}
	public void setFwsc(String fwsc) {
		this.fwsc = fwsc;
	}
	public String getFwsr() {
		return fwsr;
	}
	public void setFwsr(String fwsr) {
		this.fwsr = fwsr;
	}
	public void setFwms(String fwms){
	    this.fwms = fwms;
    }
    public String getFwms(){
	    return this.fwms;
    }
    public void setUri(String uri){
	    this.uri = uri;
    }
    public String getUri(){
	    return this.uri;
    }
    public void setFwfl(String fwfl){
	    this.fwfl = fwfl;
    }
    public String getFwfl(){
	    return this.fwfl;
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
    public void setYyid(String yyid){
	    this.yyid = yyid;
    }
    public String getYyid(){
	    return this.yyid;
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
		if(fwzt != null && fwzt.getBytes().length > 1){
		    errMsg.append("服务状态 0：已发布 1：未发布超长！容许长度：1。"); 
		}	   	
		if(priceunit != null && priceunit.getBytes().length > 2){
		    errMsg.append("价格单位超长！容许长度：2。"); 
		}	   	
		if(fwms != null && fwms.getBytes().length > 500){
		    errMsg.append("服务描述超长！容许长度：500。"); 
		}	   	
		if(uri != null && uri.getBytes().length > 100){
		    errMsg.append("URI超长！容许长度：100。"); 
		}	   	
		if(fwfl != null && fwfl.getBytes().length > 10){
		    errMsg.append("服务分类超长！容许长度：10。"); 
		}	   	
		if(fwname != null && fwname.getBytes().length > 50){
		    errMsg.append("服务名称超长！容许长度：50。"); 
		}	   	
		if(fwid != null && fwid.getBytes().length > 30){
		    errMsg.append("服务ID超长！容许长度：30。"); 
		}	   	
		if(yyid != null && yyid.getBytes().length > 17){
		    errMsg.append("服务所在应用超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}