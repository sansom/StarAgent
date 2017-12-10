package com.core.jadlwork.model.yygl;

import java.util.Date;

/**
 * 应用运行记录bean
 * TODO
 * @作者：吴家旭
 * @时间：Aug 26, 2015 9:55:49 AM
 */
public class YyyxjlBean {
	public static final String db_tablename = "t_yygl_yyyxjl";
	public static final String db_tablepkfields = "id";
	private String id;			//序号
	private String yyid;		//应用ID
	private String fwqid;		//服务器ID
	private String yyyxzt;		//应用运行状态
	private String yyyxztinfo ; //应用运行状态信息
	private String yyzt;		//应用状态 0:未启动  1:正常  2:异常
	private String fbzt;		//发布状态  0:正式   1:试运行
	private String isdeploy;	//war包是否已部署  0:已部署 1：未部署
	private String version;		//运行应用版本号
	private Date updateTime;	//最后更新时间
    private Date lastcontime;	//最后通讯时间
    
    
	public String getIsdeploy() {
		return isdeploy;
	}
	public void setIsdeploy(String isdeploy) {
		this.isdeploy = isdeploy;
	}
	public String getFbzt() {
		return fbzt;
	}
	public void setFbzt(String fbzt) {
		this.fbzt = fbzt;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getLastcontime() {
		return lastcontime;
	}
	public void setLastcontime(Date lastcontime) {
		this.lastcontime = lastcontime;
	}
	public String getYyyxztinfo() {
		return yyyxztinfo;
	}
	public void setYyyxztinfo(String yyyxztinfo) {
		this.yyyxztinfo = yyyxztinfo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getFwqid() {
		return fwqid;
	}
	public void setFwqid(String fwqid) {
		this.fwqid = fwqid;
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
  	
    public String getYyzt() {
		return yyzt;
	}
	public void setYyzt(String yyzt) {
		this.yyzt = yyzt;
	}
	public String getYyyxzt() {
		return yyyxzt;
	}
	public void setYyyxzt(String yyyxzt) {
		this.yyyxzt = yyyxzt;
	}
	public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("序号为空！");
		}
		/* 检查长度 */
		if(fwqid != null && fwqid.getBytes().length > 16){
		    errMsg.append("服务器ID超长！容许长度：16。"); 
		}	   	
		if(yyid != null && yyid.getBytes().length > 17){
		    errMsg.append("应用ID超长！容许长度：17。"); 
		}	   	
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("序号超长！容许长度：17。"); 
		}	   	
	  	return errMsg.toString();
    }
}