<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head>
	<title>List</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/aa.js"></script>


<%@ include file="/inc/pagination.inc"%>

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
     

function query()
{
	var pageNum = document.getElementById("pageNum").value;

	var userid =  document.getElementById("userid").value;

	var actionUrl = "<%=request.getContextPath()%>/LoginLog!refresh.action?from=refresh&pageNum="+pageNum+"&userid="+userid;

	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
		html+='<tr id="<%=k%>" class="trClass<%=k%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> ';
		html+='<td nowrap width="12%"><div id="hostip" align="center">'+entryInfo['hostip']+'</div></td>';				
		html+='<td nowrap width="8%" align="center">'+entryInfo['userid']+' </td>';
		html+='<td nowrap width="8%" align="center">'+entryInfo['username']+' </td>';
		html+='<td nowrap width="20%" align="center">'+entryInfo['logintime'].substring(0,19)+'</td>';													
		html+='<td nowrap width="20%" align="center">'+entryInfo['logouttime'].substring(0,19)+' </td>';
		html+='<td nowrap width="10%" align="center">'+entryInfo['result']+'</td>';	
		html+='<td nowrap width="10%" align="center">'+entryInfo['type']+'</td>';	
		html+='</tr>'
 			<% k++;%>
	return html;
}


</script>

<body id="bodyid"  leftmargin="0" topmargin="0" >

<!-- <form name="loginlogForm" action="<%=request.getContextPath()%>/pages/login/Loginlog.do?action=list" method="post">	-->
<form name="loginlogForm" >
<%@include file="/inc/navigationBar.inc"%>
<table width="100%" cellspacing="0" cellpadding="0"  class="selectTableBackground">
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
  <tr> 
    <td> 
		<fieldset  width="100%"><legend><s:text name="label.condition"/></legend> 
		<table width="100%" align="center">
				<tr>
            		<td width="15%" align="right"><s:text name="operInfoform.userIdentifier"/><s:text name="label.colon"/>
       		  	  	</td>
            		<td>            		          	
                  		<input width="15%" id="userid" name="userid" type="text" size="20"   onKeyUp="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" onafterpaste="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')"  value='<s:property value="userid"/>'>                  		
            		</td>
            		<td width="15%"></td>
            		<td width="15%"></td>
            		<td width="15%"></td>
            		<td width="15%"></td>
            		<td width="15%">
            			<tm:button site="1"></tm:button>
            		</td>
          		</tr>           		
		</table>
		</fieldset>
	</td>
  </tr>
</table>

</form>
<form name="formN" method="post">
 <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
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
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>	
		<table width="100%" cellSpacing="0" cellPadding="0">
          <tr>
  			<td width="83%"	align="right">
  			<div id="pagetag"><tm:pagetag pageName="currPage" formName="loginlogForm"/></div>
			</td>				
  		  </tr>
		</table>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
			  <td width="12%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="loginlog.ip"/>
					</div>
		  	  </td>
				<td width="8%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="operInfoform.userIdentifier"/>
					</div>
		  	  </td>
				<td width="8%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="operInfoform.username"/>
					</div>
		  	  </td>
				<td width="20%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="loginlog.logintime"/>
					</div>
		  	  </td>
				<td width="20%" class="oracolumncenterheader">
					<div align="center">
						<s:text name="loginlog.logouttime"/>
					</div>
		  	  </td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">
						状态
					</div>
		  	  </td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">
						登录口
					</div>
		  	  </td>
		</tr>
		<%
			int index = 0;
		 %>
		<tbody name="formlist" id="formlist">   
		<s:iterator id="loglist" value="loglist" >
			<tr id="<%=index%>" class="trClass<%=index%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
				<td nowrap width="12%" align="center">				
				  <s:property value="hostip"/>
				</td>				
				<td nowrap width="8%" align="center">
					<s:property value="userid"/>
			  </td>
				<td nowrap width="8%" align="center">				
				  <s:property value="username"/>
			  </td>
				<td nowrap width="20%" align="center">	
				  <s:date name="logintime" format="yyyy-MM-dd HH:mm:ss"/>			
			  </td>														
				<td nowrap width="20%" align="center">				
				  <s:date name="logouttime" format="yyyy-MM-dd HH:mm:ss"/>			
			  </td>
				<td nowrap width="10%" align="center">				
				  <tm:dataDir beanName="loglist" property="result" path="operator.loginlogtype"/>
			  </td>		
				<td nowrap width="20%" align="center">				
				  <s:property value="type"/>
			  </td>
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