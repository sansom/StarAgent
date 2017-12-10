<%@ page import="com.core.jadlsoft.model.xtgl.UserSessionBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>左侧菜单</title>
<%@ include file="../include/meta.inc"%>
<link href="../css/index_new.css" rel="stylesheet" type="text/css" />
<link href="../css/load_new.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../common/jquery-1.6.2.min.js"></script>
<script type="text/javascript">
	var url_a;
	function changeurl(url) {
		window.frames.parent.main.location = url;
	}
</script>
</head>
<body style="background:#386698;">
	<div class="main-lbox">
	
		<c:if test="${empty gnlbList}">
			该用户没有任何权限
		</c:if>
		<c:if test="${not empty gnlbList}">
			<ul id="menuListId">
				<c:forEach var="parentMap" items="${gnlbList}">
					<!-- 每一个的父菜单，里面包含其对应的子菜单集合 -->
					<li class="syind">
						<em class="syem" onclick="changeurl('${parentMap.self.link}');">
							<span  class="lico1">
								${parentMap.self.gnname}
							</span>
						</em>
  						<ul>
		  					<c:forEach var="gnlbItem" items="${parentMap.children}">
		  						<li>
		  							<em onclick="changeurl('${gnlbItem.link}');">
										${gnlbItem.gnname}
									</em>
		  						</li>
		  					</c:forEach>
	  					</ul>
	  				</li>
				</c:forEach>
			</ul>
		</c:if>
	</div>
<script type="text/javascript">

function loadMenu(){
	(function($){
        $('.main-lbox li').click(function(event){ 
            var _this = $(this);  
			if(_this.parent().parent('li').length){
				if(_this.is('.haver')){
				   _this.removeClass('haver');
				   var cul = _this.children('ul'); 
					if(cul.length){
						 cul.hide()
					}
				}else{
				   _this.addClass('haver').siblings().removeClass('haver').children('ul').hide(); 
				   var cul = _this.children('ul'); 
					if(cul.length){
						 cul.show()
					}
				} 
			   
			}else{
				_this.addClass('haver').siblings().removeClass('haver').children('ul').hide(); 
				  var cul = _this.children('ul');
					if(cul.length){
						if(cul.is(":hidden")){
							cul.show();
						   _this.addClass('hsvadd');
						}else if(_this.is('.hsvadd')){
						    cul.hide();
						   _this.removeClass('hsvadd')
						}else{
						   cul.show();
						   _this.addClass('hsvadd');
					    }
					}
			}   
			event.stopPropagation();
        });
      
    })(jQuery)
}
 loadMenu();
</script>
<script type="text/javascript" src="../common/tab.js"></script>
</body>
</html>

