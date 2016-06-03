<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head>
		<title>user group</title>
	</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="JavaScript" type="text/javascript">
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
function saveUserGroup(){
	var grpNamesStr = "";
	$("input[name=grpcode]").each(function(){
		var td = $(this).parent().parent().parent().find("td[class=grpNameTd]");
		if(this.checked==true){
			if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
				grpNamesStr += $(td).text() + ",";	
			}else{
				grpNamesStr += $(td).text().trim() + ",";
			}
		}
	})
	//调用父窗口的回调函数，将选择的组别插入父页面
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
		if(window.parent.dialogArguments.selUsrGrpCallBack){
			window.parent.dialogArguments.selUsrGrpCallBack(grpNamesStr.substr(0,grpNamesStr.length-1));		
		}
	}else{
		if(window.parent.parent.opener.selUsrGrpCallBack==undefined){
			
		}else{
			sysUserGroupFrom.action.value="save";
			window.parent.parent.opener.selUsrGrpCallBack(grpNamesStr.substr(0,grpNamesStr.length-1));
		}
	}
	sysUserGroupFrom.submit();
}

</script>
<body id="bodyid" leftmargin="0" topmargin="0">
		<form name="sysUserGroupFrom" action="<%=request.getContextPath()%>/pages/um/sysUserGroup!saveSysUserGroup.action" >
		<input name="action" id="action" type="hidden">
			<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
				<tr>
					<td width="25" valign="middle"><img src="../../images/share/list.gif" width="14" height="16"></td>
					<td class="orarowhead">
						<s:text name="roleinfo.grproleRelation"/>
					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
					<%
						int index = 0;
					 %>
					<s:iterator value="usrGrpLst">
						<tr id="tr<s:property value='grpcode'/>" class="trClass<%=index%2%>"  oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
							<td width="10%" >
								<div align="center">
									<input  type="checkbox" name="grpcode" <s:property  value="checked"/>  value='<s:property  value="grpcode"/>'/>
								</div>
							</td>
							<td width="90%" class="grpNameTd">
								<s:property  value="grpname"/>
							</td>
						</tr>
						<%
							index++;
						 %>
					</s:iterator>
				
			</table>
			<br>
			<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="roleInfo.table1title"/>
							</legend>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr bgcolor="#FFFFFF">
									<td align="center">
										<input type="button" name="save" value='<s:text name="button.ok"/>' class="MyButton"  onclick="saveUserGroup()" image="../../images/share/yes1.gif" />
										<input type="button" name="btnReturn" value='<s:text name="button.close"/>' class="MyButton"  onClick="closeModal()" image="../../images/share/f_closed.gif" />
										<input name="userid" type="hidden" id="userid" value='<s:property value="userid"/>' />
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