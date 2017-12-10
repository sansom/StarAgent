package com.core.jadlwork.model.fwqgl;

public class FwqBean{
	public static final String db_tablename = "t_fwqgl_fwq";
	public static final String db_tablepkfields = "id";
   /*
    * 主键
    */
    private String id;
   /*
    * 服务器名称
    */
    private String fwqname;
   /*
    * 服务器IP
    */
    private String fwqip;
   /*
    * 创建时间
    */
    private java.util.Date cjsj;
   /*
    * 撤销时间
    */
    private java.util.Date cxsj;
   /*
    * 数据状态 0: 有效   1:无效
    */
    private String zt;
   /*
    * 服务器域名
    */
    private String fwqym;
   /*
    * 服务器操作系统，字典表
    */
    private String fwqczxt;
   /*
    * 数据备份地址 window默认d:/Upload_JadlWar , linux默认Upload_JadlWar
    */
    private String backupsrc;
   /*
    * 端口号
    */
    private String dk;
   /*
    * cpu使用率
    */
    private String cpuused;
   /*
    * 服务器所在机房
    */
    private String szjf;
   /*
    * 内存使用率
    */
    private String memeryused;
   /*
    * 线程数量
    */
    private String threadcount;
   /*
    * JVM可用堆内存
    */
    private String jvmmemory;
   /*
    * JVM运行线程数
    */
    private String jvmthreadcount;
   /*
    * JVM加载的类数量
    */
    private String jvmloadedclasscount;
   /*
    * 服务器运行状态 0:正常 1:异常
    */
    private String fwqstatus;
   /*
    * 平台类型  01:云平台  02:托管平台 03:第三方平台
    */
    private String ptlx;
   /*
    * 服务器更新时间
    */
    private java.util.Date updatetime;
   /*
    * 状态信息
    */
    private String statusinfo;
   /*
    * 最后通信时间
    */
    private java.util.Date lastcontime;
   /*
    * 外网服务器IP
    */
    private String fwqip_ww;
   /*
    * 第一联系人电话
    */
    private String dylxrdh;
   /*
    * 第一联系人
    */
    private String dylxr;
   /*
    * 第二联系人
    */
    private String delxr;
   /*
    * 第二联系人电话
    */
    private String delxrdh;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setFwqname(String fwqname){
	    this.fwqname = fwqname;
    }
    public String getFwqname(){
	    return this.fwqname;
    }
    public void setFwqip(String fwqip){
	    this.fwqip = fwqip;
    }
    public String getFwqip(){
	    return this.fwqip;
    }
    public void setCjsj(java.util.Date cjsj){
	    this.cjsj = cjsj;
    }
    public java.util.Date getCjsj(){
	    return this.cjsj;
    }
    public void setCxsj(java.util.Date cxsj){
	    this.cxsj = cxsj;
    }
    public java.util.Date getCxsj(){
	    return this.cxsj;
    }
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public void setFwqym(String fwqym){
	    this.fwqym = fwqym;
    }
    public String getFwqym(){
	    return this.fwqym;
    }
    public void setFwqczxt(String fwqczxt){
	    this.fwqczxt = fwqczxt;
    }
    public String getFwqczxt(){
	    return this.fwqczxt;
    }
    public void setBackupsrc(String backupsrc){
	    this.backupsrc = backupsrc;
    }
    public String getBackupsrc(){
	    return this.backupsrc;
    }
    public void setDk(String dk){
	    this.dk = dk;
    }
    public String getDk(){
	    return this.dk;
    }
    public void setCpuused(String cpuused){
	    this.cpuused = cpuused;
    }
    public String getCpuused(){
	    return this.cpuused;
    }
    public void setSzjf(String szjf){
	    this.szjf = szjf;
    }
    public String getSzjf(){
	    return this.szjf;
    }
    public void setMemeryused(String memeryused){
	    this.memeryused = memeryused;
    }
    public String getMemeryused(){
	    return this.memeryused;
    }
    public void setThreadcount(String threadcount){
	    this.threadcount = threadcount;
    }
    public String getThreadcount(){
	    return this.threadcount;
    }
    public void setJvmmemory(String jvmmemory){
	    this.jvmmemory = jvmmemory;
    }
    public String getJvmmemory(){
	    return this.jvmmemory;
    }
    public void setJvmthreadcount(String jvmthreadcount){
	    this.jvmthreadcount = jvmthreadcount;
    }
    public String getJvmthreadcount(){
	    return this.jvmthreadcount;
    }
    public void setJvmloadedclasscount(String jvmloadedclasscount){
	    this.jvmloadedclasscount = jvmloadedclasscount;
    }
    public String getJvmloadedclasscount(){
	    return this.jvmloadedclasscount;
    }
    public void setFwqstatus(String fwqstatus){
	    this.fwqstatus = fwqstatus;
    }
    public String getFwqstatus(){
	    return this.fwqstatus;
    }
    public void setPtlx(String ptlx){
	    this.ptlx = ptlx;
    }
    public String getPtlx(){
	    return this.ptlx;
    }
    public void setUpdatetime(java.util.Date updatetime){
	    this.updatetime = updatetime;
    }
    public java.util.Date getUpdatetime(){
	    return this.updatetime;
    }
    public void setStatusinfo(String statusinfo){
	    this.statusinfo = statusinfo;
    }
    public String getStatusinfo(){
	    return this.statusinfo;
    }
    public void setLastcontime(java.util.Date lastcontime){
	    this.lastcontime = lastcontime;
    }
    public java.util.Date getLastcontime(){
	    return this.lastcontime;
    }
    public void setFwqip_ww(String fwqip_ww){
	    this.fwqip_ww = fwqip_ww;
    }
    public String getFwqip_ww(){
	    return this.fwqip_ww;
    }
    public void setDylxrdh(String dylxrdh){
	    this.dylxrdh = dylxrdh;
    }
    public String getDylxrdh(){
	    return this.dylxrdh;
    }
    public void setDylxr(String dylxr){
	    this.dylxr = dylxr;
    }
    public String getDylxr(){
	    return this.dylxr;
    }
    public void setDelxr(String delxr){
	    this.delxr = delxr;
    }
    public String getDelxr(){
	    return this.delxr;
    }
    public void setDelxrdh(String delxrdh){
	    this.delxrdh = delxrdh;
    }
    public String getDelxrdh(){
	    return this.delxrdh;
    }
  	
    public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("主键为空！");
		}
		/* 检查长度 */
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("主键超长！容许长度：17。"); 
		}	   	
		if(fwqname != null && fwqname.getBytes().length > 50){
		    errMsg.append("服务器名称超长！容许长度：50。"); 
		}	   	
		if(fwqip != null && fwqip.getBytes().length > 16){
		    errMsg.append("服务器IP超长！容许长度：16。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("数据状态 0: 有效   1:无效超长！容许长度：1。"); 
		}	   	
		if(fwqym != null && fwqym.getBytes().length > 30){
		    errMsg.append("服务器域名超长！容许长度：30。"); 
		}	   	
		if(fwqczxt != null && fwqczxt.getBytes().length > 2){
		    errMsg.append("服务器操作系统，字典表超长！容许长度：2。"); 
		}	   	
		if(backupsrc != null && backupsrc.getBytes().length > 30){
		    errMsg.append("数据备份地址 window默认d:/Upload_JadlWar , linux默认Upload_JadlWar超长！容许长度：30。"); 
		}	   	
		if(dk != null && dk.getBytes().length > 6){
		    errMsg.append("端口号超长！容许长度：6。"); 
		}	   	
		if(cpuused != null && cpuused.getBytes().length > 20){
		    errMsg.append("cpu使用率超长！容许长度：20。"); 
		}	   	
		if(szjf != null && szjf.getBytes().length > 50){
		    errMsg.append("服务器所在机房超长！容许长度：50。"); 
		}	   	
		if(memeryused != null && memeryused.getBytes().length > 20){
		    errMsg.append("内存使用率超长！容许长度：20。"); 
		}	   	
		if(threadcount != null && threadcount.getBytes().length > 10){
		    errMsg.append("线程数量超长！容许长度：10。"); 
		}	   	
		if(jvmmemory != null && jvmmemory.getBytes().length > 20){
		    errMsg.append("JVM可用堆内存超长！容许长度：20。"); 
		}	   	
		if(jvmthreadcount != null && jvmthreadcount.getBytes().length > 5){
		    errMsg.append("JVM运行线程数超长！容许长度：5。"); 
		}	   	
		if(jvmloadedclasscount != null && jvmloadedclasscount.getBytes().length > 5){
		    errMsg.append("JVM加载的类数量超长！容许长度：5。"); 
		}	   	
		if(fwqstatus != null && fwqstatus.getBytes().length > 1){
		    errMsg.append("服务器运行状态 0:正常 1:异常超长！容许长度：1。"); 
		}	   	
		if(ptlx != null && ptlx.getBytes().length > 2){
		    errMsg.append("平台类型  01:云平台  02:托管平台 03:第三方平台超长！容许长度：2。"); 
		}	   	
		if(statusinfo != null && statusinfo.getBytes().length > 100){
		    errMsg.append("状态信息超长！容许长度：100。"); 
		}	   	
		if(fwqip_ww != null && fwqip_ww.getBytes().length > 50){
		    errMsg.append("外网服务器IP超长！容许长度：50。"); 
		}	   	
		if(dylxrdh != null && dylxrdh.getBytes().length > 15){
		    errMsg.append("第一联系人电话超长！容许长度：15。"); 
		}	   	
		if(dylxr != null && dylxr.getBytes().length > 20){
		    errMsg.append("第一联系人超长！容许长度：20。"); 
		}	   	
		if(delxr != null && delxr.getBytes().length > 20){
		    errMsg.append("第二联系人超长！容许长度：20。"); 
		}	   	
		if(delxrdh != null && delxrdh.getBytes().length > 15){
		    errMsg.append("第二联系人电话超长！容许长度：15。"); 
		}	   	
	  	return errMsg.toString();
    }
}