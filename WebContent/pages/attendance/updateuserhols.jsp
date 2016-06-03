<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="cn.grgbanking.feeltm.staff.service.StaffInfoService"%>
<%@page import="cn.grgbanking.feeltm.project.service.ProjectService"%>
<%@page import="cn.grgbanking.feeltm.context.BaseApplicationContext" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page import="java.util.UUID,java.net.URLDecoder" %>
<%
	String getV=request.getParameter("ids");
	String userid=getV.split(",")[0];
	String username=getV.split(",")[1];
	username = URLDecoder.decode(username, "UTF-8");
%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">
	function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	}
	function save() {
		//var userid="<%=userid%>";
		//var deferred=document.getElementById("deferred").value();
		//var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!updateUserRols.action?userid="+userid;
		//actionUrl += "&deferred="+deferred;
		reportInfoForm.submit();
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/attendance/attendanceAction!updateUserRols.action" method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
					<td class="input_label2" width="20%"><font color="#000000">用户名：</font></td>
					<td bgcolor="#FFFFFF">
						<input type="text" id="username" name="username" value="<%=username%>"/>
						<input type="hidden" id="userid" name="userid" value="<%=userid%>"/>
					</td>
					<td class="input_label2" width="20%"><font color="#000000">本月剩余调休天数：</font></td>
					<td bgcolor="#FFFFFF">
						<input type="text" id="deferred" name="deferred" value=""/>
					</td>
				</td>
			</tr>
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="okButton"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onclick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
