package com.core.jadlwork.model.fwgl;

public class WbfwBean{
	public static final String db_tablename = "t_fwgl_wbfw";
	public static final String db_tablepkfields = "id";
   /*
    * 状态 0：有效 1：无效
    */
    private String zt;
   /*
    * 服务状态 0：完成 1：未提交 2：申请中 3：申请未通过
    */
    private String fwzt;
   /*
    * 注销时间
    */
    private java.util.Date zxsj;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 录入时间
    */
    private java.util.Date lrsj;
   /*
    * 审批说明
    */
    private String spsm;
   /*
    * 价格单位
    */
    private String priceunit;
   /*
    * 价格
    */
    private String price;
   /*
    * 服务描述
    */
    private String fwms;
   /*
    * 服务路径
    */
    private String uri;
   /*
    * 
    */
    private String fwname;
   /*
    * 服务ID
    */
    private String fwid;
   /*
    * 服务名称
    */
    private String sqrxm;
   /*
    * 申请人
    */
    private String sqr;
   /*
    * 序号
    */
    private String id;
    
    private String fwljzt;
    
    
    private String lylx;
    
    private String fwsr;
    
    private String fwsc;
  	
    
    public String getFwljzt() {
		return fwljzt;
	}
	public void setFwljzt(String fwljzt) {
		this.fwljzt = fwljzt;
	}
	public String getLylx() {
		return lylx;
	}
	public void setLylx(String lylx) {
		this.lylx = lylx;
	}
	public String getFwsr() {
		return fwsr;
	}
	public void setFwsr(String fwsr) {
		this.fwsr = fwsr;
	}
	public String getFwsc() {
		return fwsc;
	}
	public void setFwsc(String fwsc) {
		this.fwsc = fwsc;
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
    public void setZxsj(java.util.Date zxsj){
	    this.zxsj = zxsj;
    }
    public java.util.Date getZxsj(){
	    return this.zxsj;
    }
    public void setZhxgsj(java.util.Date zhxgsj){
	    this.zhxgsj = zhxgsj;
    }
    public java.util.Date getZhxgsj(){
	    return this.zhxgsj;
    }
    public void setLrsj(java.util.Date lrsj){
	    this.lrsj = lrsj;
    }
    public java.util.Date getLrsj(){
	    return this.lrsj;
    }
    public void setSpsm(String spsm){
	    this.spsm = spsm;
    }
    public String getSpsm(){
	    return this.spsm;
    }
    public void setPriceunit(String priceunit){
	    this.priceunit = priceunit;
    }
    public String getPriceunit(){
	    return this.priceunit;
    }
    public void setPrice(String price){
	    this.price = price;
    }
    public String getPrice(){
	    return this.price;
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
    public void setSqrxm(String sqrxm){
	    this.sqrxm = sqrxm;
    }
    public String getSqrxm(){
	    return this.sqrxm;
    }
    public void setSqr(String sqr){
	    this.sqr = sqr;
    }
    public String getSqr(){
	    return this.sqr;
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
		if(fwzt != null && fwzt.getBytes().length > 2){
		    errMsg.append("服务状态 01：有效 02：未提交 03：申请中 04：申请未通过超长！容许长度：1。"); 
		}	   	
		if(spsm != null && spsm.getBytes().length > 100){
		    errMsg.append("审批说明超长！容许长度：100。"); 
		}	   	
		if(priceunit != null && priceunit.getBytes().length > 2){
		    errMsg.append("价格单位超长！容许长度：2。"); 
		}	   	
		if(price != null && price.getBytes().length > 10){
		    errMsg.append("价格超长！容许长度：10。"); 
		}	   	
		if(fwms != null && fwms.getBytes().length > 500){
		    errMsg.append("服务描述超长！容许长度：500。"); 
		}	   	
		if(uri != null && uri.getBytes().length > 100){
		    errMsg.append("服务路径超长！容许长度：100。"); 
		}	   	
		if(fwname != null && fwname.getBytes().length > 50){
		    errMsg.append("fwname超长！容许长度：50。"); 
		}	   	
		if(fwid != null && fwid.getBytes().length > 30){
		    errMsg.append("服务ID超长！容许长度：30。"); 
		}	   	
		if(sqrxm != null && sqrxm.getBytes().length > 50){
		    errMsg.append("服务名称超长！容许长度：50。"); 
		}	   	
		if(sqr != null && sqr.getBytes().length > 17){
		    errMsg.append("申请人超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}