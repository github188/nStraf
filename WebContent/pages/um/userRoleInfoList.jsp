<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head>
		<title>role info</title>
	</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	
	<%@ include file="/inc/pagination.inc"%>
	<script language="JavaScript">

	String.prototype.trim  =  function()
	{
        //  用正则表达式将前后空格


        //  用空字符串替代。


        return  this.replace(/(^\s*)|(\s*$)/g,  "");
	} 	
	

function query()
{
	var actionUrl = "<%=request.getContextPath()%>/pages/um/userRole!refresh.action?from=refresh";
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	<%
	int j = 0;//记录的索引
	%>
	
	sendAjaxRequest(actionUrl,method,"",false);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
		html+='<tr id="tr'+entryInfo['rolecode']+'/>" class="trClass<%=j%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)">';
		html+='<td nowrap width="7%">';
		html+=' <div align="center"> ';       
	    html+='<input name="id" type="checkbox" id="id" value="'+entryInfo['rolecode']+'"/>';
	    html+='</div>';
		html+='</td>';
		html+='<td nowrap width="27%"  >';
		html+= entryInfo["rolecode"];
		html+='</td>';
		html+='<td nowrap width="50%">';
		html+=entryInfo["rolename"];
		html+='</td>'
		html+='<td nowrap width="16%">'
		html+='<div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick=GroupMenu("'+entryInfo['rolecode']+'")></div>'
		html+='</td>';
		html+='</tr>';
		
		<%
	      j++;
	    %>
	return html;
}

function isChecked(){
    var idList="";
    var  em= userRoleForm.elements;
	for(var  i=0;i<em.length;i++)
	{
	       if(em[i].type=="checkbox")
		   {
		       if(em[i].checked){
			       idList+=","+em[i].value;
			   }
		   }
	}
	if(idList=="")
	{
	       alert("<s:text name="del.selectNone"/>");
		   return false;
	}
	return idList;
 }
	
function ShowTitleModal(theURL,features){
	OpenModal(theURL,features);
	window.location.href="<%=request.getContextPath()%>/pages/um/userRole.do";
}
 

function modify(){
   var aa=document.getElementsByName("id");
   var rolcode
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			rolcode=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<s:text name="operator.update"/>')
 	else if (j>1)
 		alert('<s:text name="operator.updateone"/>')
	else{
		var strUrl="/pages/um/userRole!modify.action?id="+rolcode;
		var features="500,400,roleInfo.updateRole,um";
		var returnValue = OpenModal(strUrl,features);
	
		/* query(); */
		refreshList();
	}
}

function del() {
	var id = isChecked();
	if (id==false)  return false;
	if (confirm('<s:text name="roleInfo.removeConfirm"/>'))
    {
		var strUrl="/pages/um/userRole!delete.action?id="+id;
 		var returnValue=OpenModal(strUrl,"520,380,back.title.del,org"); 
 		/* query(); */
 		refreshList();
    }
}

function GroupMenu(rolecode){
	var strUrl="/pages/um/userRolePurview!saveRoleFunc.action?rolecode="+rolecode;
	var features="1024,600,purview.role.purviewSetting,um";
	var resultvalue = OpenModal(strUrl,features);
	
	//if(resultvalue != null)
		//query();
}


function add(){
	var strUrl="pages/um/userRole!addpage.action";
	var features="500,400,roleInfo.addRole,um";
	var resultvalue =  OpenModal(strUrl,features);	
	
	/* query(); */
	refreshList();
}

function submitInfo()
{
	userRoleForm.action.value="list";	
	ajaxAnywhere.submitAJAX();
}
</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
	<%@include file="/inc/navigationBar.inc"%>
	<form name="userRoleForm" id="userRoleForm">
		<input type="hidden" name="action" value="">
		<input type="hidden" name="roleid" value="">
		
		<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">
					&nbsp;
					<img src="../../images/share/list.gif" width="14" height="16">
				</td>
				<td class="orarowhead">
					<s:text name="roleInfo.table2title" />
				</td>
				<td	align="right" width="50%">
    				<tm:button site="2"></tm:button>
    			</td>
			</tr>
		</table>
		
		
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td width="7%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center"><s:text name="label.select" /></div>
				</td>
				<td width="27%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
						<s:text name="roleInfoForm.roleCode" />
					</div>
				</td>
				<td width="50%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
						<s:text name="roleInfoForm.roleName" />
					</div>
				</td>
				<td width="16%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">
						<s:text name="roleInfoForm.purviewSetting" />
					</div>
				</td>
			</tr>
			
			
			<%
				int i = 0;
			 %>
			<tbody name="formlist" id="formlist">
			<s:iterator value="role.roleList">
			<tr id="tr<s:property value='rolecode'/>" class="trClass<%=i%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
				<td nowrap width="7%">
					 <div align="center">          
          				<input name="id" type="checkbox"  id="id" value='<s:property value="rolecode"/>'>
                     </div>
				</td>
					<td nowrap width="27%"  >
					<s:property value="rolecode"/>
				</td>
				<td nowrap width="50%">
					<s:property value="rolename"/>
				</td>
				<td nowrap width="16%">
					<div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick='GroupMenu("<s:property value='rolecode'/>")'></div>
				</td>
			</tr>
			<%
				i++;
			 %>
			</s:iterator>
			</tbody>
		</table>
		</form>
	</body>
</html>
