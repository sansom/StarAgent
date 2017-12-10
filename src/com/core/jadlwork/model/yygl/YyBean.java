package com.core.jadlwork.model.yygl;

/**
 * 应用bean
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 9:55:39 AM
 */
public class YyBean {
	public static final String db_tablename = "t_yygl_yy";
	public static final String db_tablepkfields = "id";
	
	private String id;					//序号
	private String yyname;				//应用名称
	private String warname;				//上传war包名称
	private String warsrc;				//war包备份路径
	private String warsrc_sfb;				//war包备份路径
	private String yyLogSrc;			//应用日志位置
	private java.util.Date cjsj;		//创建时间
	private java.util.Date zhxgsj;		//最后修改时间
	private java.util.Date scsj;		//删除时间
	private String zt;					//状态 0：有效 1：无效
	private String yyversion;			//应用版本号
	private String yyversion_sfb;			//应用版本号
	private String yyzt;				//应用状态，表中有这个字段，暂时还不能删除
	private String ptlx;				//平台类型   01:云平台  02:托管平台 03:第三方平台

	//=======================get/set=======================
	
	public String getId() {
		return id;
	}

	public String getWarsrc_sfb() {
		return warsrc_sfb;
	}

	public String getYyversion_sfb() {
		return yyversion_sfb;
	}

	public void setYyversion_sfb(String yyversion_sfb) {
		this.yyversion_sfb = yyversion_sfb;
	}

	public void setWarsrc_sfb(String warsrc_sfb) {
		this.warsrc_sfb = warsrc_sfb;
	}

	public String getYyversion() {
		return yyversion;
	}

	public void setYyversion(String yyversion) {
		this.yyversion = yyversion;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYyname() {
		return yyname;
	}

	public void setYyname(String yyname) {
		this.yyname = yyname;
	}

	public String getWarname() {
		return warname;
	}

	

	public String getPtlx() {
		return ptlx;
	}

	public void setPtlx(String ptlx) {
		this.ptlx = ptlx;
	}

	public void setWarname(String warname) {
		this.warname = warname;
	}

	public String getWarsrc() {
		return warsrc;
	}

	public void setWarsrc(String warsrc) {
		this.warsrc = warsrc;
	}

	public String getYyLogSrc() {
		return yyLogSrc;
	}

	public void setYyLogSrc(String yyLogSrc) {
		this.yyLogSrc = yyLogSrc;
	}

	public java.util.Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(java.util.Date cjsj) {
		this.cjsj = cjsj;
	}

	public java.util.Date getZhxgsj() {
		return zhxgsj;
	}

	public void setZhxgsj(java.util.Date zhxgsj) {
		this.zhxgsj = zhxgsj;
	}

	public java.util.Date getScsj() {
		return scsj;
	}

	public void setScsj(java.util.Date scsj) {
		this.scsj = scsj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	
	public String getYyzt() {
		return yyzt;
	}

	public void setYyzt(String yyzt) {
		this.yyzt = yyzt;
	}

	

	// --------校验------------
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
		if(warsrc != null && warsrc.getBytes().length > 200){
		    errMsg.append("war包备份路径超长！容许长度：200。"); 
		}	   	
		if(warname != null && warname.getBytes().length > 30){
		    errMsg.append("上传war包名称超长！容许长度：30。"); 
		}	   	
		if(yyname != null && yyname.getBytes().length > 50){
		    errMsg.append("应用名称超长！容许长度：50。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}