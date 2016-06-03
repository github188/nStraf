<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript">
	function closeModal() {
		window.close();
	}
	
	function save() {
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		addCheckCondtionForm.submit();
		window.close();
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="addCheckCondtionForm" action="<%=request.getContextPath()%>/pages/staffEntry/entryInfo!doEditCheckCondition.action" method="post">
		<!-- 主键 -->
		<input 	name="checkCondition.id" type="hidden" id="checkCondition.id"  size="40" class="MyInput" value='<s:property value="checkCondition.id"/>' >
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			<tr>
				<td align="center" width="20%" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.addCheckCondition_jsp.type" />
					</div>
				</td>
				<td bgcolor="#FFFFFF" width="60%">
					<input 	name="checkCondition.type" type="text" id="checkCondition.type" maxlength="25" size="40" class="MyInput" value='<s:property value="checkCondition.type"/>'>
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.addCheckCondition_jsp.content" />
					</div>
				</td>
				<td bgcolor="#FFFFFF">
					<input 	name="checkCondition.content" type="text" id="checkCondition.content" maxlength="50" size="40" class="MyInput" value='<s:property value="checkCondition.content"/>'>
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.addCheckCondition_jsp.jointDept" />
					</div>
				</td>
				<td bgcolor="#FFFFFF" >
					<input 	name="checkCondition.jointDept" type="text" id="checkCondition.jointDept" maxlength="16" size="40" class="MyInput" value='<s:property value="checkCondition.jointDept"/>'>
				</td>
			</tr>
			<tr>
				<td  height="67" align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.addCheckCondition_jsp.note" />
					</div>
				</td>
				<td bgcolor="#FFFFFF" >
					<div>
						<textarea cols="30" rows="3" name="checkCondition.note" id="checkCondition.note"><s:property value="checkCondition.note"/></textarea>
					</div>
				</td>
			</tr>
		</table>
		<br /> 
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
				<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
