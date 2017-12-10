<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>推送人员编辑</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript">
	
		//是否为修改界面
		var isEdit = true;
		var rid = "${tsRyBean.id}";
		if (rid == null || rid == "") {
			//添加操作
			isEdit = false;
		}
		var qyyx = "${tsRyBean.yxts}" == '0' ? true : false;
		var qydx = "${tsRyBean.dxts}" == '0' ? true : false;
		var qywx = "${tsRyBean.wxts}" == '0' ? true : false;
	
		$(function() {
			//1、如果是修改的操作，并且状态为选中就默认展示邮箱和短信的输入框，并且设置处于选中的状态
			if (isEdit && qyyx) {
				$("#yxts").attr("checked", true);
			}
			if (isEdit && qydx) {
				$("#dxts").attr("checked", true);
			}
			if (isEdit && qywx) {
				$("#wxts").attr("checked", true);
			}
			//2、根据是否绑定的有微信信息，展示不同的信息
			var wxid = '${tsRyBean.wxid}';
			if (wxid == null || wxid == "") {
				//没有绑定微信，展示绑定的信息
				$("#wxidShow").hide();
				$("#wxbindShow").show();
			}else {
				//绑定过了，直接展示
				$("#wxidShow").show();
				$("#wxbindShow").hide();
			}
		});
	  	//校验域填写是否完整
	  	function save(){
	  		$("#insert").attr("disabled", true);
			var err = checkForm(document.forms[0]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//校验用户名
			if (!checkUsernameUsable()) {
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//校验手机号
			if (!checkSjhm()) {
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//校验邮箱
			if (!checkEmail()) {
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//校验微信
			if (!checkWeChat()) {
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//设置启用邮箱或者微信的值
			var dxChecked = $('#dxts').is(':checked');
			var yxChecked = $('#yxts').is(':checked');
			var wxChecked = $('#wxts').is(':checked');
			if (dxChecked) {
				$("#dxtsVal").val("0");
			}else {
				$("#dxtsVal").val("1");
			}
			if (yxChecked) {
				$("#yxtsVal").val("0");
			}else {
				$("#yxtsVal").val("1");
			}
			if (wxChecked) {
				$("#wxtsVal").val("0");
			}else {
				$("#wxtsVal").val("1");
			}
			
			var ryid = $("#ryid").val();
			if(ryid == null || ryid == ""){
				//新建
				document.getElementById("tsryFormId").action="tsrysave.action";
				document.getElementById("tsryFormId").submit();
			}else{
				//修改
				document.getElementById("tsryFormId").action="tsryupdate.action";
				document.getElementById("tsryFormId").submit();
			}
		}
		
		/*
		 * 校验用户名是否可用
		*/
		function checkUsernameUsable() {
			var value = $("#username").val();
			var res = true;
			if (value && value != null && value != "") {
				$.ajax({
					type : "post",
					url : "tsrycheckUsernameUsable.action",
					async : false,
					data : "username=" + value + "&ryid=${tsRyBean.id}",
					dataType : "json",
					success : function(data) {
						if (data.statusCode == "0000") {
							res = true;
						}else {
							layer.alert(data.msg, function(){
								$("#username").focus();
								res = false;
							});
						}
					}
				});
			}
			return res;
		}
		 
		/*
		* 校验手机号
			先校验手机号格式  不为空并且格式错误
			校验启用短信推送后不能为空，如果启用了短信推送，手机号不能为空
		*/
		function checkSjhm() {
			var sjhm = $("#sjhm").val();
			if (sjhm != null && sjhm !="") {
				//填写了手机，校验格式是否正确
				var myreg = /^1(3|4|5|7|8)\d{9}$/;
				if (!myreg.test(sjhm)) {
					//不正确
					layer.alert("手机号格式不正确！");
					$("#sjhm").focus();
					return false;
				}
			}
			//判断是否启用短信
			var res = true;
			var checked = $('#dxts').is(':checked');
			if (checked) {
				//启用了短信,判断手机号不能为空，并且格式要正确
				if (sjhm == null || sjhm == "") {
					layer.alert("手机号不能为空！");
					$("#sjhm").focus();
					return false;
				}else {
					//填写了手机号，校验格式是否正确
					var myreg = /^1(3|4|5|7|8)\d{9}$/;
					if (!myreg.test(sjhm)) {
						//不正确
						layer.alert("手机号格式不正确！");
						$("#sjhm").focus();
						return false;
					}
				}
			}
			//校验是否可用
			$.ajax({
				type : "post",
				url : "tsrycheckSjhmUsable.action",
				async : false,
				data : "sjhm=" + sjhm + "&ryid=${tsRyBean.id}",
				dataType : "json",
				success : function(data) {
					if (data.statusCode == "0000") {
						res = true;
					}else {
						layer.alert(data.msg);
						$("#sjhm").focus();
						res = false;
					}
				}
			});
			return res;
		}
		
		/*
		* 校验邮箱
			先校验邮箱格式  不为空并且格式错误
			校验启用邮箱后不能为空，如果启用了邮箱推送，邮箱不能为空
		*/
		function checkEmail() {
			var email = $("#yxdz").val();
			if (email != null && email !="") {
				//填写了邮箱，校验格式是否正确
				var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
				if (!myreg.test(email)) {
					//不正确
					layer.alert("邮箱格式不正确！");
					$("#yxdz").focus();
					return false;
				}
			}
			//判断是否启用邮箱
			var res = true;
			var checked = $('#yxts').is(':checked');
			if (checked) {
				//启用了邮箱,判断邮箱不能为空，并且格式要正确
				if (email == null || email == "") {
					layer.alert("邮箱不能为空！");
					$("#yxdz").focus();
					return false;
				}else {
					//填写了邮箱，校验格式是否正确
					var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
					if (!myreg.test(email)) {
						//不正确
						layer.alert("邮箱格式不正确！");
						$("#yxdz").focus();
						return false;
					}
				}
			}
			//校验是否可用
			$.ajax({
				type : "post",
				url : "tsrycheckYxUsable.action",
				async : false,
				data : "email=" + email + "&ryid=${tsRyBean.id}",
				dataType : "json",
				success : function(data) {
					if (data.statusCode == "0000") {
						res = true;
					}else {
						layer.alert(data.msg);
						$("#yxdz").focus();
						res = false;
					}
				}
			});
			return res;
		}
		
		/*
		*  校验微信
			 如果启用微信，就必须在微信后台完成二维码的扫描
		*/
		function checkWeChat() {
			var res = true;
			var checked = $('#wxts').is(':checked');
			if (checked) {
				//判断是否为修改界面
				if (isEdit) {
					return true;
				}
				//不为空，需要根据生成验证码的方式去校验是否关注公众号
				var wxvercode = $("#wxvercode").val();
				if (wxvercode == null || wxvercode == "") {
					res = false;
					layer.alert("请输入微信公众号返回的验证码！");
					return res;
				}
				$.ajax({
					url : "../gzzx/tsrycheckWeChatVerCode.action",
					type: "post",
					data: "verCode="+wxvercode,
					async: false,
					dataType: "json",
					success : function(data) {
						if (data.statusCode == "0000") {
							res = true;
						}else {
							res = false;
							layer.alert(data.msg);
						}
					},
					error : function(a) {
						res = false;
						layer.alert(a);
					}
				});
			}
			return res;
		}
		
		//绑定微信
		function bindWechat(value) {
			layer.alert("开发中...敬请期待~！");
			return false;
			var ryid = '${tsRyBean.id}';
			if (value == "0") {
				//隐藏按钮
				$("#bindWx").hide();
				//显示取消按钮
				$("#qxBindWx").show();
				//去后台生成二维码信息
				changeEwm();
				//显示二维码等信息
				$("#wxerweimaInfo").show();
				$("#wxvercode").focus();
			}else {
				if (value == "1") {
					//确认绑定
					//1、校验验证码正确性
					var verCode = $("#wxvercode").val();
					if (verCode == null || verCode == "") {
						alert("请输入验证码！");
						return;
					}
					//2、校验验证码正确性以及微信是否可用
					$.ajax({
						type : "post",
						url : "tsrycheckWeidUsable.action",
						data : "code=" + verCode + "&ryid=" + ryid,
						dataType : "json",
						success : function(data) {
							if (data.statusCode == "0000") {
								var openid = data.arg1;
								//将openID设置到展示的内容中
								$("#wxh").val(openid);
								$("#wxidHidden").val(openid);
								
								$("#wxidShow").show();
								$("#wxbindShow").hide();
								alert("绑定成功！");
							}else {
								//失败
								alert(data.msg);
								return;
							}
						}
					});
					
				}else {
					//取消
					//隐藏按钮
					$("#wxvercode").val("");
					$("#bindWx").show();
					//显示取消按钮
					$("#wxerweimaInfo").hide();
				}
			}
		}
		
		//解绑微信
		function unBindWechat(ryid) {
			var username = '${tsRyBean.username}';
			var isConfirm = true;
			if (isEdit) {
				if (confirm("确定要解除该微信号与【"+username+"】之间的绑定关系吗？")) {
					$.ajax({
						type : "post",
						url : "tsryunBindWechat.action",
						data : "ryid=" + ryid,
						success : function(data) {
							if (data == "success") {
								//隐藏微信号，展示绑定按钮
								$("#wxidShow").hide();
								$("#wxbindShow").show();
								//去除微信的勾选状态
								$("#wxts").attr("checked", false);
								//去掉隐藏域中微信id的值
								$("#wxidHidden").val("");
								alert("解绑成功！");
							}else {
								alert("解绑失败！");
							}
						}
					});
				}
			} else {
				//添加界面
				//隐藏微信号，展示绑定按钮
				$("#wxidShow").hide();
				
				$("#bindWx").show();
				$("#qxBindWx").hide();
				$("#wxerweimaInfo").hide();
				$("#wxvercode").val("");
				
				$("#wxbindShow").show();
				//去除微信的勾选状态
				$("#wxts").attr("checked", false);
				//去掉隐藏域中微信id的值
				$("#wxidHidden").val("");
			}
		}

		//启用微信的单击事件
		function wxchange() {
			var checked = $('#wxts').is(':checked');
			if (checked) {
				var wxid = $("#wxidHidden").val();
				if (wxid == null || wxid=="") {
					$("#wxts").attr("checked", false);
					layer.alert("请先绑定微信号！");
				}
				return;
			}
		}
		
		//创建、更新二维码
		function changeEwm() {
			$("#wxewm").attr("src","../images/picLoading.gif");
			$.ajax({
				url : "tsrycreateEeweima.action",
				type: "post",
				dataType: "json",
				success : function(data) {
					if (data.statusCode == "0000") {
						//成功
						var ewmUrl = data.arg1;
						$("#wxewm").attr("src",ewmUrl);
					}
				}
			});
		}
  	</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">推送人员编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="tsryFormId"
              		method="post" id="tsryFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input name="tsRyBean.id" type="hidden" id="ryid" value="${tsRyBean.id}" />
              	
				<ul class="formUl" style="padding-top: 0;">
					<li class="itemTitle">信息编辑</li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>用户名：</label>
			            <div class="labelRight">
			            	<input type="text" name="tsRyBean.username" id="username"
								value="${tsRyBean.username}" class="inputText"
								title="用户名" alt="notnull;length<=50;" 
								maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>手机号码：</label>
			            <div class="labelRight">
			            	<input type="text" name="tsRyBean.sjhm" id="sjhm"
								value="${tsRyBean.sjhm}" class="inputText"
								title="手机号码" alt="length<=20;" maxlength="20" size="30"
								onkeyup="this.value=this.value.replace(/\D/g,'')"  
								onafterpaste="this.value=this.value.replace(/\D/g,'')" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>邮箱地址：</label>
			            <div class="labelRight">
			            	<input type="text" name="tsRyBean.yxdz" id="yxdz"
								value="${tsRyBean.yxdz}" class="inputText" size="30" 
								title="邮箱地址" alt="length<=50;" maxlength="50" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>微信号：</label>
			            <input type="hidden" name="tsRyBean.wxid" value="${tsRyBean.wxid}" id="wxidHidden" />
			            <div class="labelRight">
			            	<!-- 有微信号的话，就展示微信信息 -->
							<div id="wxidShow" style="display: none;">
								<input type="text" id="wxh"
									value="${tsRyBean.wxid}" class="inputText" size="30" 
									title="微信号" alt="length<=50;" maxlength="50" size="30"
									style="width: 250px;line-height: 24px;" disabled="disabled" />
								<a style="color: #2A00FF;margin-left: 10px;line-height: 24px;text-decoration: none;" 
									href="javascript:void(0)" onclick="unBindWechat('${tsRyBean.id}')">解绑</a>
							</div>
							
							<div id="wxbindShow" style="display: none;">
								<input id="bindWx" onclick="bindWechat('0')" type="button" value="绑定微信号" class="defaultBtn primaryBtn"
									style="width: 80px;height: 30px;margin: 0 auto;text-align: center;"  />
								<div id="wxerweimaInfo" style="display: none;">
									<img onclick="changeEwm()" id="wxewm" src="../images/picLoading.gif" 
										alt="" style="width: 120px;height: 120px;float: left;cursor: pointer;margin-left: 10px;" />
										
									<div>
										<input type="text" id="wxvercode" class="inputText"
												size="30" title="微信验证码" placeholder="校验码"
												alt="length<=6;" maxlength="50" size="30"
												onkeyup="this.value=this.value.replace(/\D/g,'')"  
												onafterpaste="this.value=this.value.replace(/\D/g,'')"
												style="width: 80px;line-height: 24px;margin-top: -2px;
													margin-left: 10px;" />
										<input id="bdBindWx" type="button" value="绑定" 
											onclick="bindWechat('1')"
											style="width: 96px;line-height: 24px;margin-top: 30px;
												margin-left: -95px;height: 25px;" />
										<input id="qxBindWx" type="button" value="取消" 
											onclick="bindWechat('2')"
											style="width: 96px;line-height: 24px;margin-top: 65px;
												margin-left: -95px;height: 25px;" />
									</div>
								</div>
							</div>
			            </div>
			        </li>
			        <li class="itemTitle">推送方式选择</li>
			        <li>
			        	<div class="labelRight" style="margin-left: 80px;">
			        		<label for="dxts">
								短信：
							</label>
							<input style="width: 30px;text-align: left;border: none;" type="checkbox" id="dxts" value="0" />
							<input id="dxtsVal" type="hidden" name="tsRyBean.dxts" />
							
							<label for="yxts" style="width: 60px;margin-left: 45px;">
								邮箱：
							</label>
							<input style="width: 30px;text-align: left;border: none;" type="checkbox" id="yxts" />
							<input id="yxtsVal" type="hidden" name="tsRyBean.yxts" />
							
							<label for="wxts" style="width: 60px;margin-left: 45px;">
								微信：
							</label>
							<input onclick="wxchange()" style="width: 30px;text-align: left;border: none;" type="checkbox" id="wxts" value="0" />
							<input id="wxtsVal" type="hidden" name="tsRyBean.wxts" />
			            </div>
			        </li>
			        
			        <!-- 操作按钮 -->
			        <li class="clearfix" style="margin-left: 80px;">
			            <div class="labelRight">
			                <input type="button" class="defaultBtn" onclick="save()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>
</body>
</html>

