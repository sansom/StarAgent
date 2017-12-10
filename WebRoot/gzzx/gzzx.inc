<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@page import="com.core.jadlwork.cache.gzzx.GzzxCache"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<script type="text/javascript">

	$(function(){
		//初始化设置当前的过滤时长
		$("#gzFilMinute").val('<%= GzzxCache.gzFilterMinute %>');
	});

	//删除
	function deleteGzzx(id) {
		confirmDo("确定删除该条故障信息?", function(){
			formSubmit("gzzxdeleteGzzx.action?gzzxJbxxBean.id="+id);
		});
	}
	
	//设置过滤时长
	function updateGzFilMinute(value){
		$.ajax({
			type : "post",
			url : "gzzxupdateGzFilMinute.action",
			data : "minute=" + value,
			success : function(data) {
				if (data == "success") {
					alert("修改成功！");
				}else {
					alert("修改失败！");
				}
			},
			error : function(a,b) {
				alert("修改失败！");
				console.log(b);
			}
		});
	}
	
	//查看捕获记录
	function showBhjl(bhjlA, index) {
	
		var gzlx = $("#gzlx").val();
		var colspanNum = 6;
		if (gzlx == '<%= SystemConstants.GZZX_GZLX_YY %>') {
			colspanNum = 7;
		}
	
		var trObj = $(bhjlA).parent();
		var gzid = $("#gzid"+(index)).val();
		var isFold = $("#fold"+(index)).val() == '0' ? true : false;
		
		if (isFold) {
			//处于折叠状态，展开
			$.ajax({
				type : "post",
				url : "gzzxshowBhjl.action",
				data : "gzzxJbxxBean.id="+gzid,
				dataType : "json",
				success : function(data) {
					if (data != null && data != "") {
						var trHtml = '<tr class="bhjl"><td colspan="'+colspanNum+'"><ul style="text-align:center;">';
						for(var i =0;i<data.length;i++) {
							var map = data[i];
							var sj = map["bhsj"];
							trHtml = trHtml + '<li style="line-height:30px;">捕获时间：<span style="margin-left: 20px;">'+sj+'</span></li>';
						}
						/* for(var i in data) {
							var map = data[i];
							var sj = map["bhsj"];
							trHtml = trHtml + '<li style="line-height:30px;">捕获时间：<span style="margin-left: 20px;">'+sj+'</span></li>'; 
						} */
						trHtml = trHtml +'</ul></td></tr>'; 
						trObj.after(trHtml);
						$("#fold"+(index)).val('1');
					}
				}
			});
		}else {
			//处于展开状态，折叠
			var nextObj = $(trObj).next();
			if (nextObj && $(nextObj).hasClass("bhjl")) {
				$(nextObj).remove();
			}
			$("#fold"+(index)).val('0');
		}
	}
	
	//查看推送详情
	function showPushInfo(id) {
		formSubmit("gzzxshowPushInfo.action?gzzxJbxxBean.id="+id);
	}
	
	//表单提交
    function formSubmit(action){
     	var fields =new Array("gzlx");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["gzzxForm"].action = action; 
		}
      	document.forms["gzzxForm"].submit();
    }
	
</script>
<c:set var="gzlx_fwq" value="<%= SystemConstants.GZZX_GZLX_FWQ %>" />
<c:set var="gzlx_yy" value="<%= SystemConstants.GZZX_GZLX_YY %>" />
<c:set var="gzlx_yyyx" value="<%= SystemConstants.GZZX_GZLX_YYYX %>" />
<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
	<div>
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
				<colgroup style="width: 5%" />
				<colgroup style="width: 20%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
			<tbody>
				<tr class="title">
					<th>序号</th>
					<th>服务器信息</th>
					<c:if test="${gzlx == gzlx_yy || gzlx==gzlx_yyyx}">
						<th>应用名称</th>
					</c:if>
					<c:if test="${gzlx == gzlx_yyyx}">
						<th>异常类型</th>
						<th>异常原因</th>
					</c:if>
					<th>异常信息</th>
					<th>捕获时间</th>
					<th>重复捕获次数</th>
					<th>操作</th>
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="7" align="center" style="text-align: center;" class="noData">暂时没有异常信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr class="" onclick="">
							<input type="hidden" id="gzid${status.index+1}" value='${item.id}' />
							<input type="hidden" id="fold${status.index+1}" value='0' />
							<td>${status.index + 1}</td>
							<td>
								<c:if test="${gzlx == '01' || gzlx == '02'}">
									${empty item.fwqname ? '--' : item.fwqname} 
									【${empty item.fwqip ? '--' : item.fwqip}:${empty item.fwqdk ? '--' : item.fwqdk}】
								</c:if>
								<c:if test="${gzlx == '03'}">
									【${empty item.fwqip ? '--' : item.fwqip}:${empty item.fwqdk ? '--' : item.fwqdk}】
								</c:if>
							</td>
							<c:if test="${gzlx == '02' || gzlx=='03'}">
								<td>${empty item.yyname ? '--' : item.yyname}</td>
							</c:if>
							<c:if test="${gzlx == '03'}">
								<td>${empty item.yclx ? '--' : item.yclx}</td>
								<td>${empty item.ycyy ? '--' : item.ycyy}</td>
							</c:if>
							<td>${empty item.ycxx ? '--' : item.ycxx}</td>
							<td>${empty item.bhsj ? '--' : item.bhsj}</td>
							<td style="cursor: pointer;" onclick="showBhjl(this,'${status.index+1}')"><font color="#4E93CE">${empty item.totalcf ? '--' : item.totalcf}</font></td>
							<td>
								<a href="javascript:void(0)" 
										onclick="showPushInfo('${item.id}')" >推送详情</a>
								<a href="javascript:void(0)" 
										onclick="deleteGzzx('${item.id}')" >删除</a>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="pagelist">
		<%@ include file="../include/page.inc"%>
	</div>
</section>
</body>
