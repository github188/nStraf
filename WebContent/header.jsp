<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ include file="/inc/taglib.inc"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<style type=text/css>
	#topBannerLeft{
		background:url("images_new/main/topBannerLeftBg.jpg") left top; 
		background-repeat:repeat-x ;
	}
	#topBannerRight{
		background:url("images_new/main/topBannerRightBg.jpg") left top; 
		background-repeat:repeat-x ;
	}
	#navLoginUser,#navMain,#navLogout{
		background:url("images_new/main/navBg.jpg") left top; 
		background-repeat:repeat-x ;
	}
</style>
<STYLE type=text/css>

A.global:link {
	COLOR: #000000;
	TEXT-DECORATION: none
}

A.global:visited {
	COLOR: #fffa00;
	TEXT-DECORATION: none
}

A.global:hover {
	COLOR: #fffa00;
	TEXT-DECORATION: none
}

a {
	TEXT-DECORATION: none;
	FONT-SIZE: 10.5pt;
	COLOR: #FFFFFF;
}
</STYLE>
</HEAD>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript">
	$(function(){
		$("td[name='navItem']").click(function(){
			//设置该父节点下所有的td背景色为蓝色，字体为白色
			$("#navMain").find("td").css({"background":'#205092'});
			$("#navMain").find("a").css("color","#ffffff");
			
			//设置该节点背景色灰色，字体蓝色
			$(this).css({"background":'#E6E6E6 left top'});
			$(this).find("a").css("color","#205092");
		});
	});
</script>
<script language="javascript">
	function titlestyle(thistitle) {
		var showMenu = true;
		//火狐浏览器
		if (navigator.userAgent.indexOf("Firefox") > 0){
			if($.trim(thistitle.text) == "首页"){
				showMenu = false;
			}
			else {
				showMenu = true;
			}
		}else{
	 		for (i = 0; i < titleli.length; i++) {
				if (titleli[i] == thistitle){
					if (i == 0){
						showMenu = false;
					}else{
						showMenu = true;
					}
				}
			}
		}
		if (!showMenu) {
			parent.document.getElementById("left").cols = "0,0,*";
		} else {
			parent.document.getElementById("left").cols = "180,10,*";
		}

	}

	function Logout() {
		window.parent.mainframe.location = 'login!logout.action';
	}
	
	function changeTdBg(a){
		/* var aa =document.getElementsByName("navItem"); */
		//设置该父节点下所有的td背景色为蓝色，字体为白色
			$("#navMain").find("td").css({"background":'#205092'});
			$("#navMain").find("a").css("color","#ffffff");
			//设置该节点背景色灰色，字体蓝色	
			$($("td[name='navItem']")[a]).css({"background":'#E6E6E6 left top'});
			$($("td[name='navItem']")[a]).find("a").css("color","#205092");
	}
	
	
	 //调用redirect时，判断是否同一目录，决定是否该调用location跳转
	 function trueOrFalse(topMenuOrder){
		 var flag = true;
		 if(topMenuOrder!=undefined){
			 var color = $("#navMain").find("td")[topMenuOrder].style.backgroundColor;
			 color = color.colorHex();
				 if("#E6E6E6" == color.toUpperCase() ){
					flag = false;
				} 
		 }
		 return flag;
	 }
	//十六进制颜色值的正则表达式
		var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
		/*RGB颜色转换为16进制*/
		String.prototype.colorHex = function(){
			var that = this;
			if(/^(rgb|RGB)/.test(that)){
				var aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g,"").split(",");
				var strHex = "#";
				for(var i=0; i<aColor.length; i++){
					var hex = Number(aColor[i]).toString(16);
					if(hex === "0"){
						hex += hex;	
					}
					strHex += hex;
				}
				if(strHex.length !== 7){
					strHex = that;	
				}
				return strHex;
			}else if(reg.test(that)){
				var aNum = that.replace(/#/,"").split("");
				if(aNum.length === 6){
					return that;	
				}else if(aNum.length === 3){
					var numHex = "#";
					for(var i=0; i<aNum.length; i+=1){
						numHex += (aNum[i]+aNum[i]);
					}
					return numHex;
				}
			}else{
				return that;	
			}
		};
</script>
<BODY leftMargin=0 topMargin=0 rightMargin=0 onUnload="Logout()">
	<!-- 顶部banner -->
	<table width="100%"  height="80" border=0 cellpadding="0" cellspacing="0" >
		<tr>
			<td width="8%" id="topBannerLeft" style="text-align: right;">
			</td>
			<td width="92%" height="80" id="topBannerRight" style="text-align:left" cellpadding="0" cellspacing="0">
				<img alt="" style="display:block;" src="images_new/main/topBanner.jpg">
			</td>
		</tr>
	</table>
	<!-- 导航栏 -->
	<table id="navigationBar" width="100%" width="100%"  border=0 cellPadding=0 cellSpacing=0 border=0 cellPadding=0 cellSpacing=0 >
		<tr>
			<!-- 左侧用户时间 -->
			<td id="navLoginUser" width="25%"  height="27px;" nowrap style="overflow:hidden;color:#FFFFFF;font-size:14px;font-weight:bold;font-family:'宋体';" cellspacing="0" cellpadding="0" padding="0">
				&nbsp;用户:
				<span title=""><bean:write name="tm.loginUser" property="username" /></span>
				&nbsp;时间:
				<%Date loginTime=new Date();%>
				<span title="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(loginTime)%>"><%=new SimpleDateFormat("HH:mm").format(loginTime)%></span>
			</td>
			<!-- 左侧分隔图 -->
			<td id="navLeftSeparator"  cellspacing="0" cellpadding="0" padding="0">
				<img alt="" src="images_new/main/navLeftSeparator.jpg" />
			</td>
			<!-- 导航部分 -->
			<td id="navMain" width="63%" height="27px;"  cellspacing="0" cellpadding="0" padding="0">
				<table  border=0 cellPadding=0 cellSpacing=0>
					<tr>
						<td align="center" nowrap cellspacing="0" cellpadding="0" width="60"  name="navItem">
							<a href="login!mainpage.action" target="mainframe" id="titleli" onClick="titlestyle(this)" style="color: #ffffff"> 
								<b><s:text name="index.mainpage" /></b>
							</a>
						</td>

						<logic:present name="firstMenulist">
							<logic:iterate id="listobj" name="firstMenulist" indexId="index">
								<td align="center" height="27px;" nowrap border="0" padding="0" cellspacing="0" cellpadding="0" width="90"  name="navItem">
									<a href="login!leftMenu.action?menuid=<bean:write name="listobj" property="menuid" />" target="leftFrame" id="titleli" onClick="titlestyle(this)" style="color: #ffffff"> 
									<b><bean:write name="listobj" property="menuitem" /></b>
									</a>
								</td>
							</logic:iterate>
						</logic:present>

					</tr>

				</table>
			</td>
			<!-- 右侧分隔图 -->
			<td id="navRightSparator" >
				<img alt="" src="images_new/main/navRightSeparator.jpg" />
			</td>
			<!-- 注销部分 -->
			<td id="navLogout" width="12%"  style="text-align: center;">
				<a href="login!logout.action" target=_parent style="color:#FFFFFF;font-size:14px;font-weight:bold;font-family:'宋体'">
					<s:text name="main.ui.logout" />
				</a>
			</td>
		</tr>
	</table>
</BODY>
</HTML>