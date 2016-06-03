<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ page import="java.util.List" %> 
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head>
		<title>role purview</title>
	</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="JavaScript">
function checkline(num){
	var em = roleInfoForm.elements;
	var name = "checkbox"+num;
	var linename = "check"+num
	for (var i=0;i<em.length;i++){
		if (em[i].name==name){
		em[i].checked=document.getElementById(linename).checked;
		}
	}
}

function saveRoleFuncs(){
	roleInfoForm.action="<%=request.getContextPath()%>/pages/um/userRolePurview!save.action";
	roleInfoForm.submit();
}

/* function gotoBack() {     
     window.close();
	} */
</script>
	<body id="bodyid" leftmargin="10" topmargin="10">
		<form name="roleInfoForm" id="roleInfoForm" method="post">
		<input type="hidden" name="rolecode" value='<%=request.getAttribute("rolecode")%>'>
		<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">
					&nbsp;
					<img src="../../images/share/list.gif" width="14" height="16">
				</td>
				<td class="orarowhead">
						<s:text name="roleInfo.table2title" />
				</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td width="5%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
						<s:text name="label.select" />
					</div>
				</td>
				<td width="19%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
							<s:text name="roleinfo.rolefunc.parentModule" />
					</div>
				</td>
				<td width="12%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
							<s:text name="roleinfo.rolefunc.modulename" />
					</div>
				</td>
				<td width="64%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
							<s:text name="roleinfo.rolefunc.operLst" />
					</div>
				</td>
			</tr>
            <%
            int i=0;
            int index = 0;
            %>		
			<s:iterator value="menuList">
			<tr id="<%="tr"+ ++i%>" class="trClass<%=(index%2)%>"  oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
				<td width="5%">
					 <div align="center"> 
                          <input type="checkbox" name="check" id="check<%=i%>" value="1" onClick="checkline(<%=i%>)"/>
                     </div>
				</td>
				<td width="19%">
					<s:property value="parentMenu"/>
				</td>
				<td width="12%">
					<s:property value="menunname"/>
				</td>
				<td width="64%">
				
				<s:iterator value="menuFucList">
					<input type="checkbox" name="checkbox<%=i%>"  <s:property value='checked'/>  value='<s:property value="funcid"/>' /><s:property value="opername"/>
				</s:iterator>
				</td>
			</tr>
			<%
			index++;
			 %>
			</s:iterator>		
		</table>
		<input type="hidden" name="count" value='<%=i%>'>
		<br>
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
						<legend>
								<s:text name="roleInfo.table1title" />
						</legend>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr bgcolor="#FFFFFF">
							  <td align="center">              
							    <input type="button" name="save" value='<s:text name="button.ok" />' class="MyButton"  onclick="saveRoleFuncs()" image="../../images/share/yes1.gif">
<%-- 								<input type="button" name="btnReturn" value='<s:text name="button.close" />' class="MyButton"  onClick="gotoBack()" image="../../images/share/f_closed.gif">
 --%>								<input type="button" name="btnReturn" value='<s:text name="button.close" />' class="MyButton"  onClick="closeModal()" image="../../images/share/f_closed.gif">
								
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
