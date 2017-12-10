<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="cm" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<div id="ucenterWrapper" class="ucenterWidth1 clearfix">
        <nav>
		    <ul class="ucenterNav" style="">
		    	<div style="float: right;width: 10px;height: 20px;color: white;">
		    		
	        	</div>
		         <li id="navItemLi000" class="navIndex ">
		            <div class="navDiv">
		                <a href="../index/indexmonitorMain.action?ptlx=01"><i class="iconfont">&#xe628;</i>监控预览</a>
		            </div>
		        </li> 
		        <li>
		        	<div style="height: 20px;"></div>
		        </li>
		        
		        <cm:if test="${empty gnlbList}">
					该用户没有任何权限
				</cm:if>
				<cm:if test="${not empty gnlbList}">
					<cm:forEach var="parentMap" items="${gnlbList}">
						<!-- 每一个的父菜单，里面包含其对应的子菜单集合 -->
						<li class="navInfoLi">
				            <span id="ucenterSubA" class="ucenterParent">
				            	<i class="iconfont">&#xe60c;</i>${parentMap.self.gnname}
				            	<i class="centerIcon subDown"></i>
				            </span>
				            <ul class="ucenterSub">
				            	<cm:forEach var="gnlbItem" items="${parentMap.children}">
				            		<li class="navItemLi" id="navItemLi${gnlbItem.gncode}">
				            			<a href="${gnlbItem.link}" onclick="return false"><i class="iconfont"></i>${gnlbItem.gnname}</a>
				            		</li>
			  					</cm:forEach>
				            </ul>
				        </li>
					</cm:forEach>
				</cm:if>
		    </ul>
		</nav>
        <!-- main content end -->
    </div>
    <script type="text/javascript">
    	$('.headerLi1').mouseenter(function(){
			$('.centerLogout').slideDown();
		}).mouseleave(function(){
			$('.centerLogout').stop(true).slideUp();
		});
		
		$('.headerLi3').mouseenter(function(){
			$('.logoCo').slideDown();
		}).mouseleave(function(){
			$('.logoCo').stop(true).slideUp();
		});
		
		
		//nav sub
		$('.ucenterParent').each(function(){
			//$(this).next('.ucenterSub').stop(true).slideUp();
			
		    $(this).click(function(){
		        $(this).next('.ucenterSub').stop(true).slideToggle();
		    })
		})
		
		$(function(){
			/**
			 *	处理菜单栏的展示
			 */
			 dealMenuActive();
			 $(".navItemLi").each(function(){	//每一个菜单的li
				$(this).click(function(){
					var gndm = this.id.substring(9);	//navItemLi010000
					//存入cookie
					addCookie(gndm);
					window.location.href = $(this).find("a").attr("href");
				});
			});
		});
		
		/**
		 * 根据当前地址栏处理菜单栏的选中状态	--需要在通过cookie获取后执行
		 *	规则：
		 		1、获取当前所有菜单dom对象（后期可以优化，遍历dom影响性能）
		 		2、获取当前地址栏地址
		 		3、循环判断该地址栏是否包含在当前的菜单列表中，如果包含就给该菜单设置选中状态
		 		4、如果没有，不作处理	--可能是不显示在菜单栏中的界面
		 */
		function dealMenuActiveByURL() {
			var url = window.location.href;		//当前地址栏
			$(".navItemLi").each(function(){	//每一个li
				var thisUrl = $(this).find("a").attr("href");		//	../sjjk/sjjklist.action
				thisUrl = thisUrl.substring(thisUrl.indexOf("/"));	//  /sjjk/sjjklist.action
				if (url.indexOf(thisUrl) != -1) {
					//包含
					$("li .active").removeClass("active");	//先清空
					$(this).addClass("active");			//再添加
					//更新cookie	navItemLi010000
					var gndm = this.id.substring(9);
					addCookie(gndm);
					return false;	//相当于break
				}
			});
			return;
		}
		
		/**
		 * 处理菜单栏的选中状态
		 	1、从cookie中获取
		 	2、根据地址栏再次校验
		 */
		function dealMenuActive() {
			/**
			 * 1、从cookie中获取
			 */
			var gndm = getCookie();
			if (isBlank(gndm)) {
				//为空，就默认设置第一个
				gndm = "000";
				//gndm = $(".navItemLi")[0].id.substring(9);
			}
			$("#navItemLi"+gndm).addClass("active");
			
			/**
			 *	2、从地址栏中校验
			 */
			 dealMenuActiveByURL();
		}
		
    </script>
</body>
</html>