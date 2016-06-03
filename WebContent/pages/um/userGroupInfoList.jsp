<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head><title>group info</title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../calendar/fixDate.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>	
<script language="JavaScript">
function query()
{
	var actionUrl = "<%=request.getContextPath()%>/pages/um/userGroup!refresh.action?from=refresh";
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	<%
	int j = 0;//记录的索引
	int k = 1;
	%>
	
	sendAjaxRequest(actionUrl,method,"",false);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
      
   
        html+=' <tr id="tr<%=++k%>" class="trClass<%=j%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
        html+='<td nowrap width="5%"> <div align="center">';
        html+='<input name="id" type="checkbox" id="id" value="'+entryInfo['grpcode']+'"/>';
        html+='</div></td>';
        html+='<td nowrap width="25%">'+entryInfo['grpcode']+'</td>';
        html+='<td nowrap width="30%">'+entryInfo['grpname']+'</td>';
        html+='<td nowrap width="20%"> <div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick=\'GroupUserMenu("'+entryInfo['grpcode']+'")\'/></div></td>';
        html+='<td nowrap width="20%"> <div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick=\'GroupMenu("'+entryInfo['grpcode']+'")\'/></div></td>';
        html+='</tr>';
        
        <% j ++;%>//每调用一次该方法，索引值加1

	return html;
}



function isChecked(){
    var idList=""
    var em = userGroupForm.elements
	for(var i=0;i<em.length;i++)
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
		alert('<s:text name="del.selectNone"/>');
		return false;
	}
	return idList;
}	

function ShowTitleModal(theURL,features){
	OpenModal(theURL,features);
	window.location.href="<%=request.getContextPath()%>/pages/um/userGroup.do";
} 

function del(){
	var id = isChecked();
	if (id==false) return false;
	if (confirm("<s:text name="grpInfo.removeConfirm"/>"))
    {
		var strUrl="/pages/um/userGroup!delete.action?id="+id;
 		var returnValue=OpenModal(strUrl,"520,380,back.title.del,org"); 
 		/* query(); */
 		refreshList();
    }
}

function GroupMenu(groupcode){
	var strUrl="/pages/um/userGroupRole!find.action?grpcode="+groupcode;
	var feature="520,380,grpInfo.updateTitle,um";
 	var returnValue=OpenModal(strUrl,feature);
}
function GroupUserMenu(groupcode){
	var strUrl="/pages/um/sysUserGroup!findAllUser.action?grpcode="+groupcode;
	var feature="520,380,grpInfo.updateTitle,um";
 	var returnValue=OpenModal(strUrl,feature);
}
function add(){	
    var strUrl="/pages/um/userGroup!addpage.action";
	var feature="460,360,grpInfo.addGrp,um";
 	var returnValue=OpenModal(strUrl,feature);
 	/* query(); */
 	refreshList();
	
}

function modify(){
   var aa=document.getElementsByName("id");
   var grpcode;
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			grpcode=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<s:text name="operator.update"/>')
 	else if (j>1)
 		alert('<s:text name="operator.updateone"/>')
	else{
		var strUrl="/pages/um/userGroup!modify.action?id="+grpcode;
		var features="500,400,grpInfo.updateTitle,um";
		OpenModal(strUrl,features);
		/* query(); */
		refreshList();
	}
}

</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
	<%@include file="/inc/navigationBar.inc"%>
		<form name="userGroupForm" >
		<input type="hidden" name="action" >
		<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">
					&nbsp;
					<img src="../../images/share/list.gif" width="14" height="16">
				</td>
				<td class="orarowhead">
					<s:text name="grpInfo.table2title"/>
				</td>
				<td	align="right" width="50%">
    				<tm:button site="2"></tm:button>
    			</td>
			</tr>
		</table>		
		
  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
    <tr> 
      <td width="5%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"><s:text name="label.select"/>
        </div>
      </td>
      <td width="25%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> <s:text name="grpInfoForm.grpcode"/>
        </div>
       </td>
      <td width="30%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> <s:text name="grpInfoForm.grpname"/>
        </div>
      </td>
     <%--  <td width="25%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> <s:text name="grpInfoForm.deptName"/>
        </div>
      </td> --%>
      <td width="20%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> <s:text name="grpInfoForm.peopleSetting"/>
        </div>
      </td>
      <td width="20%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> <s:text name="grpInfoForm.purviewSetting"/>
        </div>
      </td>
    </tr>
    <tbody id="formlist">
    <%
    int i=1;
    int index = 0;
    %>
   
    <s:iterator id="grp" value="grp.groupList"> 
    <tr id="<%="tr"+ ++i%>" class="trClass<%=(index%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
      <td nowrap width="5%"> <div align="center"> 
          <input name="id" type="checkbox" id="id" value='<s:property value="grpcode"/>'/>
        </div></td>
      <td nowrap width="25%"> <s:property value="grpcode"/> </td>
      <td nowrap width="30%"> <s:property value="grpname"/> </td>
     <%--  <td nowrap width="25%"> <s:property value="deptName"/> </td> --%>
      <td nowrap width="20%"> <div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick='GroupUserMenu("<s:property value='grpcode'/>")'></div></td>
      <td nowrap width="20%"> <div align="center"><img style="cursor: hand" src="../../images/share/setting.gif" onClick='GroupMenu("<s:property value='grpcode'/>")'></div></td>
    </tr>
    <%
    index ++;
     %>
    </s:iterator> 
</tbody>
  </table>
</form>		

	</body>
</html>
