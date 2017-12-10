<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="../include/meta.inc" %>
		<title>日志online</title>
	    <script type="text/javascript">
	    	var currentPos = 0;
	    	var logname = '${logname}';
	    	var fwqip = '${fwqip}';
	    	var timer;
	    	var load_index;
			$(function(){
				getFileData();
				window.setTimeout(function(){
					timer = window.setInterval(function(){
						getFileData();
					}, 1000 * 3);
				},100);
				
				function getFileData() {
					if (currentPos == 0) {
						load_index = layer.load();
					}
					$.ajax({
						type : "post",
						url : "fwqviewLogOnline.action",
						data : "pos=" + currentPos+"&logname="+logname+"&fwqip="+fwqip,
						dataType : "json",
						success : function(data) {
							layer.close(load_index);
							$("#main").show();
							if (data.statusCode == '0000') {
								//成功
								var list = new Array();
								list = data.arg1;
								currentPos = data.arg2;
								if (list.length>0) {
									for(var i=0;i<list.length;i++) {
										$("#contUl").prepend($('<li>'+list[i]+'</li>'));
									}
								}
							}else {
								layer.alert(data.msg, function(){
									//停止发送请求
									window.clearInterval(timer);
									window.opener=null;
									window.open('','_self');
									window.close();
								});
							}
						}
					});
				}
			});
			
	    </script>
	</head>
	<body>
		<div class="main" id="main" style="display: none;">
	  		<center>
				<div class="log-content">
					<ul id="contUl">
					</ul>
				</div>
			</center>
		</div>
	</body>
</html>

