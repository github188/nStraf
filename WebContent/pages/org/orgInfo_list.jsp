<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.domain.OrgInfo" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head>
	<title>org info list</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
</head>
<script language="javascript" src="js/orgPages.js"></script>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/aa.js"></script>

<%@ include file="/inc/pagination.inc"%>
<script language="JavaScript" type="text/JavaScript">
function query()
{
	var parentid = document.getElementById("parentid").value;
	var actionUrl = "<%=request.getContextPath() %>/pages/org/orgMgr!refresh.action?from=refresh&parentid="+parentid;
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,"",false);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
	 	html+='<tr  class="trClass<%=(k%2)%>"  oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> '
     	html+='<td nowrap width="8%"> '
        html+='<div align="center"> '
	    html+='<input  type="checkbox" name="deleteOrgid" id="deleteOrgid"  value="'+entryInfo["orgid"]+'"/>';
      	html+=' </div>'
    	html+='</td>'
    	html+='<td nowrap width="14%">'
        html+='<div align="center">'
   var childnum = entryInfo["childnum"];
   
   if(childnum>0)
		html+=' <img src="../../images/share/foldericon.gif" > '
   else
		html+=' <img src="../../images/share/modify.gif" > '
        
        html+=' </div>'
        html+='</td>'
        html+=' <td nowrap width="11%">'
        html+=' <div align="center">'
		html+='<a href="<%=request.getContextPath() %>/pages/org/orgMgr!query.action?parentid='+entryInfo["orgid"]+'">'
		html+=entryInfo["orgname"];
    	html+='</a>'
	    html+='</div>'
	    html+='</td>'
        html+=' <td nowrap width="12%"> '
        html+=' <div align="center">'
        html+=entryInfo["orgid"];
		
        html+='</div>'
        html+='</td>'
        html+=' <td nowrap width="13%"> <div align="center">'+entryInfo["contact"]+'</div></td>'
        html+=' <td nowrap width="11%"> <div align="center">'+entryInfo["tel"]+'</div></td>'
        html+='<td nowrap width="12%"> '
        html+='<div align="center">'
        html+= entryInfo["ifreckon"];

	  	html+='</div>'
	 	html+='</td>'
   		html+=' <td width="19%"> <div align="center">'+entryInfo["remark"]+'</div></td>'
   	    html+='</tr>'
 			<% k++;%>
 			
	return html;
}
   
function  GetSelIds(){
     var idList=""
	  var aa = document.getElementsByName("deleteOrgid");
    for (var i=0; i<aa.length; i++){
     if(aa[i].checked){ 
	 idList+=aa[i].value;
	 if (i<aa.length-1)
  		idList+=",";
	}
  }
  if(idList=="")
	    return ""
	 return idList
}
function deleteInfo(formName) {
	var idList=GetSelIds();
  if(IsDeleteCheckValues(this,'deleteOrgid')){
  	document.forms[formName].action.value="delete";
 	doyou = confirm('<s:text name="back.deleteinfo"/>'); //Your question.
  if (doyou == true){
  		var parentid = document.getElementById("parentid").value; 
 	  	var strUrl="/pages/org/orgMgr!delete.action?deleteOrgid="+idList+"&parentid="+parentid;
 		var returnValue=OpenModal(strUrl,"520,380,back.title.del,org");
		query();
   	}
   }
	else{   
    	alert('<s:text name="del.selectNone"/> ');
	}
	
}

function modify(){
   var aa=document.getElementsByName("deleteOrgid");
   var id
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			id=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<s:text name="operator.update"/>')
 	else if (j>1)
 		alert('<s:text name="operator.updateone"/>')
	else{
		var strUrl="/pages/org/orgMgr!modify.action?orgid="+id;
		var features="600,500,operator.modify,org";
		var retValue = OpenModal(strUrl,features);
		
		query();
	}
}



function add(){
	 <%
  	 OrgInfo orgInfo =((OrgInfo) request.getSession().getAttribute("orgInfoForms"));
  	 String parentid = orgInfo.getParentid();
  	 %>
	var parentid ='<%=parentid%>';
	if(parentid=='Top_Parentid'){
		alert('<s:text name="org.pleaseSelectedNoTop"/>');
		return false;
	}
	var retValue = OpenModal("/pages/org/orgInfo_insert.jsp","650,550,orginfo.addinfo,org")
		query();
}
function del() {
	var parentid = '<%=parentid%>';
	if(parentid =='Top_Parentid'){
		alert('<s:text name="org.TopNotDel"/>');
		return false;
	}
	deleteInfo('orgInfoForm')
}
</script>

<body id="bodyid"  leftmargin="0" topmargin="0" >

<form method="post" action="" name="orgInfoForm" id="orgInfoForm">
	<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" background="../../images/main/bgtop.gif"> 
	  <tr>
	    <td width="25" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
	    <td class="orarowhead"><s:text name="orginfo.postiton"/> ：<s:property value="parentName"/></td>
	    <td	align="right" width="50%">
	    	 <tm:button  menuid="org.menuid" type="session"/>	
	    </td>
	  </tr>
	</table>
	
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
	  <tr bgcolor="#FFFFFF"> 
	  	<td>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0">
	  <tr bgcolor="#FFFFFF"> 
	    <td width="14%"> 
	    <div align="left">
			<input type="checkbox" name="all"  id="all"   value="CheckAll"  onClick="checkAll(this,'deleteOrgid','orgInfoForm')">
			<s:text name="operator.checkAll"/> 
		</div>
		</td>   
	  </tr>
	</table>
	
	 <input type="hidden" name="parentid" id="parentid" value="<%=parentid %>"/>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
	  <tr> 
	    <td onClick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand" ><s:text name="operator.checkAll"/> </td>
	    <td  onclick="sortTable(downloadList,1)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="label.label"/></td>
	    <td onClick="sortTable(downloadList,2)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.orgname"/></td>
	    <td onClick="sortTable(downloadList,3)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.orgid"/></td>
	    <td onClick="sortTable(downloadList,4)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.contact"/></td>
	    <td onClick="sortTable(downloadList,5)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.tel"/></td>
	    <td onClick="sortTable(downloadList,5)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.nowStatus"/></td>
	    <td onClick="sortTable(downloadList,5)" class="oracolumncenterheader" style="CURSOR: hand"><s:text name="orginfo.Remark"/></td>   
	  </tr>
	
	 <%
	  	if(parentid!=null && !parentid.equals("Top_Parentid")){//最顶层不显示“返回上一级”
	     %>
	  <tr id="tr18" class="oracletdtwo" onClick=""> 
	    <td width="8%"> <div align="left"> </div></td>
	    <td colspan="7" > 
	    <div align="left">
	 		<a href="<%=request.getContextPath() %>/pages/org/orgMgr!back.action?parentid=<%=parentid %>">
	       	 <s:text name="operator.backup"/>
	        </a>
	    </div>
	    <div align="center"></div>
	    <div align="center"></div>
	    <div align="center"></div>
	    <div align="center"></div>
	    <div align="left"></div></td>
	  </tr>
	  <%} %>
	   <%
	  	int index = 0;
	   %>
	   <tbody id="formlist" name="formlist">
	    <s:iterator value="orgInfoList" id="orgInfo" > 
	   <tr  class="trClass<%=(index%2)%>"  oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
	    <td nowrap width="8%"> 
	       <div align="center"> 
		      <input  type="checkbox" name="deleteOrgid" id="deleteOrgid"   value='<s:property  value="orgid"/>'>
	      </div>
	    </td>
	    <td nowrap width="14%" >
	      <div align="center">
		    <s:if test="childnum>0" >
			 <img src="../../images/share/foldericon.gif" > 
	        </s:if>
			 <s:if  test="childnum==0">
			 <img src="../../images/share/modify.gif" > 
	        </s:if>
	      </div>
	    </td>
	    <td nowrap width="11%">
	     <div align="center">
			<a href="<%=request.getContextPath() %>/pages/org/orgMgr!query.action?parentid=<s:property  value="orgid"/>">
				<s:property value="orgname"/>
	    	</a>
		</div>
		</td>
	    <td nowrap width="12%"> 
		    <div align="center">
		    	<tm:orgId beanName="orgInfo" property="orgid" length="20" scope="request"/>
		    </div>
	    </td>
	    <td nowrap width="13%"> <div align="center"><s:property  value="contact"/></div></td>
	    <td nowrap width="11%"> <div align="center"><s:property  value="tel"/></div></td>
	    <td nowrap width="12%"> 
			<s:if  test="ifreckon == 1" >
			<div align="center">
				<tm:dataDir beanName="orgInfo" property="ifreckon" path="tmlMgr.ifreckon" scope="request"/>
			</div>
			</s:if>
			<s:if  test="ifreckon != 1">
				<div align="center">
					<font color="#FF0000">
						<tm:dataDir beanName="orgInfo" property="ifreckon" path="tmlMgr.ifreckon" scope="request"/>
					</font>
				</div>
			</s:if>
		</td>
	    <td width="19%"><div align="center"><s:property  value="remark"/></div></td>
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