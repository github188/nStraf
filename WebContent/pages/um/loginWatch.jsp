<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@page import="java.util.HashMap,java.util.Iterator"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>Login Watch List</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<script language="javascript" type="text/javascript">  
function document.onkeydown()
    {
        	var e=event.srcElement;        	
        	if(event.keyCode==13) 
        	{
        		if(event.srcElement.id=="userid" && loginlogForm.userid.value.length > 0 )
					{
						query();
					} 
     		}
     }
     
   
function refurbish()
{
	loginlogForm.action="<%=request.getContextPath()%>/LoginLog!onLine.action";
	
	loginlogForm.submit();
}

function reGetData(){ 			
	//refurbish();
	//window.setInterval("reGetData()",5000);
} 

function isChecked(){
    var idList="";
    var  em = formList.elements;
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
	       alert('<s:text name="del.selectNone"/>');
		   return false;
	}
	return idList;
}

function logout(){
	var idList = isChecked();
	if (idList){
	if (confirm("Logout this user!")){
		loginlogForm.action="<%=request.getContextPath()%>/LoginLog!logoutLog.action?id="+idList;
 		loginlogForm.submit();
 		//ajaxAnywhere.submitAJAX();
   }
  }
}
</script>

<body id="bodyid"  leftmargin="10" topmargin="0" onload="reGetData()">
<form name="loginlogForm" action="<%=request.getContextPath()%>/pages/login/LoginLog!loginLog.action" method="post">	
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">

<table width="100%" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
      <fieldset class="jui_fieldset" width="100%">
        <legend><s:text name="label.operate"/></legend>	  
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="center"> 
        	<input type="button" name="Submit1" value='注销用户' class="MyButton" onclick="logout()" >&nbsp;&nbsp;
        	<input type="button" name="Submit1" value='刷新' class="MyButton" onclick="refurbish()" >
              </td>
          </tr>
        </table>		
      </fieldset>
    </td>
  </tr>
</table>
<input type="hidden" name="pageSize" value="10">
<input type="hidden" name="pageNum" value="1">
</form>
<form name="formList" method="post">

<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">
					&nbsp;
					<img src="../../images/share/list.gif" width="14" height="16">
				</td>
				<td class="orarowhead">
					<s:text name="label.query.result"/>
				</td>
			</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td width="7%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center"><s:text name="label.select"/></div>
				</td>
			  <td width="30%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="loginlog.ip"/>
					</div>
		  	  </td>
				<td width="23%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="operInfoform.userIdentifier"/>
					</div>
		  	  </td>
				<td width="40%" class="oracolumncenterheader">
					<div align="center">
						Session ID
					</div>
		  	  </td>
		</tr>
		<%
		int indexid = 0;
		
		HashMap userMap = (HashMap)request.getSession().getAttribute("userMap");
		HashMap userAndIPMap = (HashMap)request.getSession().getAttribute("userAndIPMap");
		Iterator i = userMap.entrySet().iterator();
		while(i.hasNext()){
		java.util.Map.Entry entry = (java.util.Map.Entry)i.next();
    //return the key
    String key = (String)entry.getKey();
    indexid++;
		%>
			<tr id="<%=indexid%>" class="trClass<%=(indexid%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
				<td width="7%"><div align="center">                          
          <input name="indexid" type="checkbox" id="id" value='<%=entry.getValue()%>'></div>
        </td>
				<td width="30%" align="center">				
				  <%=userAndIPMap.get(key)%>	
				</td>				
				<td width="23%" align="center">
					<%=key%>
			  </td>
				<td width="40%" align="center">			
				  <%=entry.getValue()%>
			  </td>
			</tr>
		<%
		}
		%>	
</table>

</form>
</body>
</html>