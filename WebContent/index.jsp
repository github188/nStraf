<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>运通信息作战系统</title>
<style type='text/css'>
.lefttitle {
	font-family: 幼圆, sans-serif;
	font-weight: bold;
	FONT-SIZE: 12pt;
	COLOR: #FFFFFF;
}
body{ background: url("images_new/login/bgLeft.jpg") left center; background-repeat:repeat-x ; }
</style>
<meta http-equiv="Page-Enter"
	content="revealTrans(Duration=1,Transition=23)">
<link rel="stylesheet" href="css/main.css" type="text/css">


<link href="css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="./js/cookie.js"></script>
<script type="text/javascript" src="./js/stringUtils.js"></script>
<script type="text/javascript">
	// 页面打开时,光标自动聚焦到第一个输入框
	window.onload = function() {
		if (document.forms.length > 0) {
			for (i = 0; i < document.forms[0].elements.length; i++) {
				var Input = document.forms[0].elements[i];
				if (Input.type != "hidden" && Input.value.length > 0) {
					Input.blur();
					//"return;" was deleted by cjjie on 2010-12-07
					// return;
				} else if (Input.type != "hidden") {
					Input.focus();
					return;
				}
			}
		}
	}

	function MM_reloadPage(init) { //reloads the window if Nav4 resized
		if (init == true)
			with (navigator) {
				if ((appName == "Netscape") && (parseInt(appVersion) == 4)) {
					document.MM_pgW = innerWidth;
					document.MM_pgH = innerHeight;
					onresize = MM_reloadPage;
				}
			}
		else if (innerWidth != document.MM_pgW
				|| innerHeight != document.MM_pgH)
			location.reload();
	}
	MM_reloadPage(true);

	function CheckForm() {
		TrimFormText(loginForm);
		if (loginForm.userid.value == "") {
			alert('<s:text  name="index.checkForm.loginName"/>');
			loginForm.userid.focus();
			return false;
		}
		if (loginForm.userpwd.value == "") {
			alert('<s:text  name="index.checkForm.password"/>');
			loginForm.userpwd.focus();
			return false;
		}
		loginForm.oldUser;
		loginForm.oldCount;
		loginForm.submit();
		return false;
	}
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0" style="overflow: hidden">
	<form action="login!checkuser.action" method="post" name="loginForm">
		<%
			String oldUserTemp = (String) request.getAttribute("oldUserTemp");
			String oldCountTemp = (String) request.getAttribute("oldCountTemp");
		%>
		<input type="hidden" name="oldUserTemp" id="oldUserTemp" value="<%=oldUserTemp%>" /> <input type="hidden" name="oldCountTemp" id="oldCountTemp" value="<%=oldCountTemp%>" />
		<table width="100%" style="min-width:1152" height="100%" border="0" cellspacing="0" cellpadding="0">
			<tr width="100%" align="center" valign="middle">
				<td valign="middle">
					<table width="1152" height="720" border="0" cellspacing="0" cellpadding="0" background="images_new/login/bgMain.jpg">
						<tr height="45%"><td></td></tr> <!-- 登录按钮上侧 -->
						<tr>
							<td>
								<span style="margin-left: 50%;color:red;text-align: center;font-weight: bold;font-size:15px;"><s:property value="message"/></span>
								<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="56%"></td><!-- 登录按钮左侧 -->
										<td width="39%">
											
											<!-- 登录按钮的table -->
											<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="50%">
														<!-- 用户名密码table -->
														<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
															<tr>
																<td style="font-size:16px;font-weight:bold;font-family:'宋体'; text-align:right;" ><font color="#214E93">用户名:</font></td>
																<td style="text-align:left;">
																	<input name="userid" type="text" onFocus="this.select()" style="height:25px;" size="20" maxlength="20">
																</td>
															</tr>
															<tr>
																<td style="font-size:16px;font-weight:bold;font-family:'宋体'; text-align:right;"><font color="#214E93">密码:</font></td>
																<td style="text-align:left;">
																	<input name="userpwd" id="userpwd" type="password" onFocus="this.select()" style="height:25px;" size="20" maxlength="20">
																</td>
															</tr>
														</table>
													</td>
													<td width="5%"></td><!-- 间隔符 -->
													<td>
														<input type="image" src="images_new/login/loginDefault.png" border="0" onmousedown="this.src='images_new/login/loginClick.png'" onmouseup="this.src='images_new/login/loginDefault.png'" onclick="return CheckForm();" />
													</td>
													<td width="5%"></td><!-- 间隔符 -->
													<td>
														<img style="cursor: hand" src="images_new/login/resetDefault.png" onmousedown="this.src='images_new/login/resetClick.png'" onmouseup="this.src='images_new/login/resetDefault.png'" onClick="loginForm.reset()" border="0">
													</td>
												</tr>
											</table>
										</td>
										<td width="5%"></td><!-- 登录按钮右侧 -->
									</tr>
								</table>
							</td>
						</tr>
						<tr height="37%">
					        <td>
					        	<table  width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
					        		<tr>
					        			<td>
					        			</td>
					        			<td height="30" align="center" valign="middle" style="color:red;text-align: center;font-weight: bold;font-size:15px;">
											<%-- <s:property value="message"/> --%>
										</td>
										<td>
					        			</td>
					        		</tr>
					        	</table>
					        </td>
						</tr><!-- 登录按钮下侧 -->
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
