<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head>
		<title>set group role</title>
	</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="JavaScript">
	 function saveGrprole(){
		grpRoleForm.action="<%=request.getContextPath()%>/pages/um/userGroupRole!save.action";
		grpRoleForm.submit();
	}

	/* function returnBefore(){
		window.close();
	} */
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<%@include file="/inc/navigationBar.inc"%>
		<form name="grpRoleForm" method="post" id="grpRoleForm">
			<table width="100%" align="center" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
				<tr>
					<td width="25" valign="middle">
						&nbsp;
						<img src="../../images/share/list.gif" width="14" height="16">
					</td>
					<td class="orarowhead">
						<s:text name="roleInfo.table2title"/>
					</td>
				</tr>
			</table>

			<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
				<tr>
					<td width="10%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
						<div align="center"><s:text name="label.select"/></div>
				  	</td>
					<td width="45%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
						<div align="center">
							<s:text name="roleInfoForm.roleCode"/>
						</div>
					</td>
					<td width="45%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
						<div align="center">
							<s:text name="roleInfoForm.roleName"/>
						</div>
					</td>					
				</tr>
		
				<%
					int index = 0 ;
			    %>
				<s:iterator value="grpRoleLst">
				<tr id="tr<s:property value="rolecode"/>" class="trClass<%=(index%2)%>"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
						<td width="10%">
							<div align="center">
								<input type="checkbox" name="rolecode" value='<s:property value="rolecode"/>' <s:property value="checked"/>/>
							</div>
					  </td>
						<td width="45%"><s:property value="rolecode"/></td>
						<td width="45%"><s:property value="rolename" /></td>
					</tr>
					<%index++; %>
				</s:iterator>
		
		  </table>
		  <br>
			<table width="100%"  align="center" cellSpacing="0" cellPadding="0">
				<tr><td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="roleInfo.table1title"/>
							</legend>
							<table  align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr bgcolor="#FFFFFF">
									<td align="center">
										<input type="button" name="save" value='<s:text name="button.ok"/>' class="MyButton"  onclick="saveGrprole()" image="../../images/share/yes1.gif">
										<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal()" image="../../images/share/f_closed.gif">
									    <input name="grpcode" type="hidden" value='<s:property value="grpcode"/>'>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>