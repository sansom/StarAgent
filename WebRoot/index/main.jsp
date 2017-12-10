<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="com.core.jadlsoft.utils.MBConstant"%>
<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>服务器列表</title>
		<%@ include file="../include/head.inc"%>
		<script type="text/javascript">
		var ctimer_fwq;
	    ctimer_fwq = setInterval(getServerControlInfo,1000*60*1);
	    //每1分钟更新一次服务器连接情况
	    function getServerControlInfo(){
	    	$.ajax({
	    		  type: "POST",
		   		  url: "../fwqgl/fwqgetServerControlInfo.action",
		   		  dataType:"json",
		   		  async:true,
				  dataType:"json",
				  success: function(msg){
				  		var serverHtml = "";
		  				for(var a=0;a<msg.length;a++){
		  					serverHtml += "<tr class=\"mb-ta-head\">";
		  					var fwqzt = msg[a].fwqzt;
							if(fwqzt != null && fwqzt == '<%=SystemConstants.FWQYXZT_ZC%>'){
								
								serverHtml += "<td>"+msg[a].fwqname+"("+msg[a].fwqip+")</td>";
								serverHtml += "<td>"+msg[a].threahcount+"&nbsp;/&nbsp;"+msg[a].cpuused+"%</td>";
								serverHtml += "<td>";
			  					var yynamestr = msg[a].yynamestr;
								if(yynamestr != null && yynamestr != ''){
									var yyArr = yynamestr.split(",")
									for(var j = 0; j < yyArr.length ; j++){
										serverHtml += "应用"+(j+1)+"："+yyArr[j]+"";
										if(j < yyArr.length-1){
											serverHtml += "<br/>";
										}
									}
								}
								serverHtml += "</td>";
								serverHtml += "<td style=\"color: green\">正常</td>";
							}else{
								serverHtml += "<td style=\"color: red\">"+msg[a].fwqname+"("+msg[a].fwqip+")</td>";
								serverHtml += "<td>--</td>";
								serverHtml += "<td>--</td>";
								serverHtml += "<td style=\"color: red\">异常</td>";
							}
							serverHtml += "</tr>";
					  	}
					  	$("#fwqTab tr").eq(0).nextAll().remove();
					  	$("#fwqTab").append(serverHtml);
				  }								
			});
	    }
	    
	  
	    
	    var ctimer_fwtj;
	    ctimer_fwtj = setInterval(getDataControlTjInfo,1000*10);
	    //每5秒更新一次数据监控情况
	    function getDataControlTjInfo(){
	    	
	    	$.ajax({
	    		  type: "POST",
		   		  url: "../fwgl/fwgetDataControlTjInfo.action",
		   		  async:true,
				  dataType:"json",
				  success: function(msg){
				  		var fwtj =  msg.fwtjlist;
				  		var fwtjHtml = "";
		  				for(var a=0;a<fwtj.length;a++){
		  					fwtjHtml += "<tr class=\"mb-ta-head\">";
							fwtjHtml += "<td>"+fwtj[a].fwid+"</td>";
							fwtjHtml += "<td>"+fwtj[a].fwname+"</td>";
							fwtjHtml += "<td><a herf=\"javascript:void(0)\" onclick=\"seefwList('"+fwtj[a].fwid+"')\" style=\"text-decoration:underline;cursor:pointer;\" >"+fwtj[a].fwcs+"</a></td>";
							fwtjHtml += "</tr>";
					  	}
					  	$("#fwtjTab tr").eq(0).nextAll().remove();
					  	$("#fwtjTab").append(fwtjHtml);
				  }								
			});
	    }
	    
	    var ctimer_fwlist;
	    ctimer_fwlist = setInterval(getDataControlListInfo,1000*10);
	    //每5秒更新一次数据监控情况
	    function getDataControlListInfo(){
	    	var fwid = document.getElementById("fwid").value;
	    	$.ajax({
	    		  type: "POST",
		   		  url: "../fwgl/fwgetDataControlListInfo.action?fwid="+fwid,
		   		  async:true,
				  dataType:"json",
				  success: function(msg){
				  		
					  	var fwfwlist =  msg.fwlist;
				  		var fwfwHtml = "";
		  				for(var a=0;a<fwfwlist.length;a++){
		  					fwfwHtml += "<tr class=\"mb-ta-head\">";
							fwfwHtml += "<td>"+fwfwlist[a].qqsj+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].userid+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].clientip+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].lylx_dicvalue+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].fwid+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].qqdz+"</td>";
							
							fwfwHtml += "</tr>";
					  	}
					  	$("#fwfwlbTab tr").eq(0).nextAll().remove();
					  	$("#fwfwlbTab").append(fwfwHtml);
					  
				  }								
			});
	    }
	    
	    function seefwList(fwid){
	    
	    	document.getElementById("fwid").value = fwid;
	    	getDataControlListInfo();
	    }
	    
	    var ctimer_jdtj;
	    ctimer_jdtj = setInterval(getNodeControlTjInfo,1000*10);
	    //每5秒更新一次数据监控情况
	    function getNodeControlTjInfo(){
	    	$.ajax({
	    		  type: "POST",
		   		  url: "../fwgl/fwgetNodeControlTjInfo.action",
		   		  async:true,
				  dataType:"json",
				  success: function(msg){
				  		var fwtj =  msg.fwtjlist;
				  		var fwtjHtml = "";
		  				for(var a=0;a<fwtj.length;a++){
		  					fwtjHtml += "<tr class=\"mb-ta-head\">";
							fwtjHtml += "<td>"+fwtj[a].dwmc+"</td>";
							fwtjHtml += "<td><a herf=\"javascript:void(0)\" onclick=\"seejdList('"+fwtj[a].dwdm+"')\" style=\"text-decoration:underline;cursor:pointer;\" >"+fwtj[a].jdsl+"</a></td>";
							fwtjHtml += "</tr>";
					  	}
					  	$("#jdtjTab tr").eq(0).nextAll().remove();
					  	$("#jdtjTab").append(fwtjHtml);
				  }								
			});
	    }
	    
	    var ctimer_jdlist;
	    ctimer_jdlist = setInterval(getNodeControlListInfo,1000*10);
	    //每5秒更新一次数据监控情况
	    function getNodeControlListInfo(){
	   	 	var jdid = document.getElementById("jdid").value;
	    	$.ajax({
	    		  type: "POST",
		   		  url: "../fwgl/fwgetNodeControlListInfo.action?dwdm="+jdid,
		   		  async:true,
				  dataType:"json",
				  success: function(msg){
				  		
					  	var fwfwlist =  msg.fwlist;
				  		var fwfwHtml = "";
		  				for(var a=0;a<fwfwlist.length;a++){
		  					fwfwHtml += "<tr class=\"mb-ta-head\">";
							fwfwHtml += "<td>"+fwfwlist[a].dwmc+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].fwid+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].fwname+"</td>";
							fwfwHtml += "<td>"+fwfwlist[a].fwljzt+"</td>";
						
							fwfwHtml += "</tr>";
					  	}
					  	$("#jdcwlbTab tr").eq(0).nextAll().remove();
					  	$("#jdcwlbTab").append(fwfwHtml);
					  
				  }								
			});
	    }
	    
	    function seejdList(dwdm){
	    	
	    	document.getElementById("jdid").value = dwdm;
	    	getNodeControlListInfo();
	    }
	    
	    //界面初始化
	    function init(){
	    	getServerControlInfo();
	    	getDataControlTjInfo();
	    	getDataControlListInfo();
	    	getNodeControlTjInfo();
	    	getNodeControlListInfo();
	    }
	</script>
	</head>
	<body onload="init();">
		<div class="bmain-round" style="border: 1px solid #111;">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx" style="width: 100%">
						<b>当前位置：</b> &gt
						<span>总线监控</span>
						<span style="float: right; padding-right: 50px"> </span>
					</div>
				</div>
				<!--boxtop pos end-->
				<table class="mb-tm" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<div class="min400">
								<div class="boxb indboxt">
									<h3>
										<span class="qylie" style="width: 80%">服务器监控</span>
									</h3>

									<div class="boxb_main boxb_main100" style="height: 140px;overflow-y: auto" >
										<table id="fwqTab"  class="mb-table show-td" border="0" cellspacing="0"
											cellpadding="0">

											<colgroup style="width: 10%" />
											<colgroup style="width: 10%" />
											<colgroup style="width: 20%" />
											<colgroup style="width: 10%" />

											<tr class="mb-ta-head">
												<th>
													服务器名称(ip)
												</th>
												<th>
													线程数量/CPU使用率
												</th>
												<th>
													部署应用
												</th>
												<th>
													连接情况
												</th>
											</tr>
											<tr class="mb-ta-head" style="display: none">
												<td>
													服务器一（192.168.20.124）
												</td>
												<td>
													50&nbsp;/&nbsp;55%
												</td>
												<td>
													应用一：爆破作业管理
													<br />
													应用二：外部服务接入
												</td>
												<td style="color: green">
													正常
												</td>
											</tr>
											<tr class="mb-ta-head"  style="display: none">

												<td style="color: red">
													服务器二(192.168.20.15)
												</td>
												<td>
													--
												</td>
												<td>
													--
												</td>
												<td style="color: red">
													异常
												</td>
											</tr>
										</table>
										<div class="j10"></div>
									</div>
								</div>
								<div class="boxb indboxt">
									<div class="boxb_main boxb_main100">
										<h3>
											<span class="qylie" style="width: 50%">服务监控</span>
											<input type="hidden" id="fwid" name="fwid" />
										</h3>
										<table width="100%">
											<tr>
												<td width="34%">
													<table width="100%" style="border: 1px solid #e0e0de;">
														<tr>
															<td style="font-family: Arial, Helvetica, sans-serif;"
																width="5%">
																
																访
																<br />
																问
																<br />
																统
																<br />
																计
															</td>
															<td>
																<div style="height: 165px;overflow-y: auto;">
																<table id="fwtjTab" class="mb-table show-td" border="0"
																	cellspacing="0" cellpadding="0">
																	<colgroup style="width: 7%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 7%" />

																	<tr class="mb-ta-head">
																		<th>
																			服务ID
																		</th>
																		<th>
																			服务名称
																		</th>

																		<th>
																			最近访问次数
																		</th>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			MBSGFA
																		</td>
																		<td>
																			民爆爆破作业施工方案
																		</td>
																		<td>
																			1111
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			MBFWZX
																		</td>
																		<td>
																			民爆服务注册
																		</td>
																		<td>
																			222
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			MBKFGL
																		</td>
																		<td>
																			民爆库房管理
																		</td>
																		<td>
																			11
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			MBYSCGL
																		</td>
																		<td>
																			民爆运输车管理
																		</td>
																		<td>
																			2
																		</td>
																	</tr>
																</table>
																</div>
															</td>
														</tr>
													</table>
												</td>
												<td width="1%"></td>
												<td width="64%">
													<table width="100%" style="border: 1px solid #e0e0de;">
														<tr>
															<td style="font-family: Arial, Helvetica, sans-serif;"
																width="3%">
																访
																<br />
																问
																<br />
																列
																<br />
																表
															</td>
															<td>
															<div style="height: 165px;overflow-y: auto;">
																<table id="fwfwlbTab" class="mb-table show-td" border="0"
																	cellspacing="0" cellpadding="0">
																	<colgroup style="width: 15%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 25%" />
																	<tr class="mb-ta-head">
																		<th>
																			时间
																		</th>
																		<th>
																			访问用户
																		</th>
																		<th>
																			用户IP
																		</th>
																		<th>
																			路由方式
																		</th>
																		<th>
																			请求服务
																		</th>
																		<th>
																			请求地址
																		</th>
																	</tr>

																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			2015-10-10 11:11:11
																		</td>
																		<td>
																			0000001
																		</td>
																		
																		<td>
																			202.112.27.99
																		</td>
																		<td>
																			中心路由
																		</td>
																		<td>
																			MBSGFA
																		</td>
																		<td>
																			http://192.168.20.124:8080/mbxcbp
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			2015-10-10 10:10:10
																		</td>
																		<td>
																			0000002
																		</td>
																		<td>
																			201.122.21.99
																		</td>
																		<td>
																			中心路由
																		</td>
																		<td>
																			MBFWZC
																		</td>
																		<td>
																			http://192.168.20.124:8080/mbfwzc
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			2015-10-09 11:11:11
																		</td>
																		<td>
																			0000001
																		</td>
																		<td>
																			202.112.27.99
																		</td>
																		<td>
																			中心路由
																		</td>
																		<td>
																			MBSGFA
																		</td>
																		<td>
																			http://192.168.20.124:8080/mbxcbp
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			2015-09-10 10:10:10
																		</td>
																		<td>
																			0000002
																		</td>
																		<td>
																			201.122.21.99
																		</td>
																		<td>
																			中心路由
																		</td>
																		<td>
																			MBFWZC
																		</td>
																		<td>
																			http://192.168.20.124:8080/mbfwzc
																		</td>
																	</tr>
																</table>
																</div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
										<div class="j10"></div>
									</div>
								</div>
								<div class="boxb indboxt">
									<div class="boxb_main boxb_main100">
										<h3>
											<span class="qylie" style="width: 50%">节点监控</span>
										</h3>
										<table width="100%">
											<tr>
												<td width="34%">
													<table width="100%" style="border: 1px solid #e0e0de;">
														<tr>
															<td style="font-family: Arial, Helvetica, sans-serif;"
																width="5%">
																节
																<br />
																点
																<br />
																统
																<br />
																计
																<input type="hidden" id="jdid" name="jdid" />
															</td>
															<td>
																<div style="height: 165px;overflow-y: auto;">
																<table id="jdtjTab" class="mb-table show-td" border="0"
																	cellspacing="0" cellpadding="0">
																	<colgroup style="width: 17%" />
																	
																	<colgroup style="width: 7%" />

																	<tr class="mb-ta-head">
																		<th>
																			厂商
																		</th>
																		<th>
																			接入节点数
																		</th>

																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			HSNHA 
																		</td>
																		<td>
																			测试节点一 
																		</td>
																		<td>
																			1111
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			KWNSH 
																		</td>
																		<td>
																			测试节点二 
																		</td>
																		<td>
																			222
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			IWJJEE
																		</td>
																		<td>
																			测试节点三 
																		</td>
																		<td>
																			11
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			OWSLLD
																		</td>
																		<td>
																			测试节点四
																		</td>
																		<td>
																			2
																		</td>
																	</tr>
																</table>
																</div>
															</td>
														</tr>
													</table>
												</td>
												<td width="1%"></td>
												<td width="64%">
													<table width="100%" style="border: 1px solid #e0e0de;">
														<tr>
															<td style="font-family: Arial, Helvetica, sans-serif;"
																width="3%">
																节
																<br />
																点
																<br />
																列
																<br />
																表
															</td>
															<td>
																<div style="height: 165px;overflow-y: auto;">
																<table id="jdcwlbTab" class="mb-table show-td" border="0"
																	cellspacing="0" cellpadding="0">
																	<colgroup style="width: 15%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 10%" />
																	<colgroup style="width: 10%" />
																	
																	<tr class="mb-ta-head">
																		<th>
																			所属厂商
																		</th>
																		<th>
																			节点ID
																		</th>
																		<th>
																			节点名称
																		</th>
																		<th>
																			节点状态
																		</th>
																		
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			HSNHA
																		</td>
																		<td>
																			测试节点一
																		</td>
																		<td>
																			http错误 404
																		</td>
																		<td>
																			000001
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			KWNSH
																		</td>
																		<td>
																			测试节点二
																		</td>
																		<td>
																			http错误 401
																		</td>
																		<td>
																			000002
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			IWJJEE
																		</td>
																		<td>
																			测试节点三
																		</td>
																		<td>
																			http错误 402
																		</td>
																		<td>
																			000003
																		</td>
																	</tr>
																	<tr class="mb-ta-head"  style="display: none">
																		<td>
																			OWSLLD
																		</td>
																		<td>
																			测试节点四
																		</td>
																		<td>
																			http错误 405
																		</td>
																		<td>
																			000004
																		</td>
																	</tr>
																</table>
																</div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
										<div class="j10"></div>
									</div>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html>
