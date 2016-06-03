<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
</head>

<script language="javascript">
	function closeRoleModal(){
	  window.close();
	}
	
	
  function modify(){
    var userid = document.getElementById("userid").value;
  	var strUrl="/pages/um/sysUserInfo!modify.action?userid="+userid;
	var features="600,500,operInfo.updateTitle,um";
	var resultvalue = OpenModal(strUrl,features);
	window.close();
  }
</script>

<body id="bodyid"  leftmargin="0" topmargin="10">
 	 
<form action="/pages/um/sysUserInfo!modify.action" method="post"  name="sysUserForm">	
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
			<input type="hidden"  name="userid" id="userid" value="<s:property value='userid'/>"/>
  <tr>
  		<td> 
    <fieldset class="jui_fieldset" width="100%">
        <legend> <s:text name="operInfo.viewOperator" /> </legend>
        
      <table width="100%" align="center" class="popnewdialog1">
        <s:iterator value="sysUser" id="user">    
        <tr> 
          <td width="30%" align="right"> <div align="right"> <s:text name="operInfoform.userIdentifier" /></div></td>
          <td width="5%"  align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20"  align="left"> <s:property value="userid"/> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right"> <div align="right"> <s:text name="operInfoform.username" /></div></td>
          <td width="5%"  align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20"  align="left"> <s:property  value="username" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right"> <div align="right"> <s:text name="operInfoform.number" /></div></td>
          <td width="5%"  align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20"  align="left"> <s:property   value="workid" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right"> <div align="right"> <s:text name="operInfoform.company" /></div></td>
          <td width="5%"  align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20"  align="left"> <s:property value="workcompany" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right"> <div align="right"> <s:text name="operInfoform.phoneNum" /></div></td>
          <td width="5%"  align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20"  align="left"> <s:property value="tel" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="operInfoform.mobile" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left"> <s:property value="mobile" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="operInfoform.email" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left"> <s:property value="email" /> 
          </td>
        </tr>
        <tr> 
          <!--<td width="30%" align="right">  <s:text name="queryCondition.org" /></td>-->
          <td width="30%" align="right">  所属组别</td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left">
         	<!--<s:property value="orgid" /> -->
            <s:property value="groupName"/>
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="operInfoform.createdate" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left"> <s:property value="createdate" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="operInfoform.invaliddate" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left"> <s:property value="invaliddate" /> 
          </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="um.delflag" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left">
          	<tm:dataDir beanName="sysUser" property="flag"  path="operator.delflag"/>
		   </td>
        </tr>
        <tr> 
          <td width="30%" align="right">  <s:text name="operInfoform.userflag" /></td>
          <td width="5%" align="left"> <s:text name="label.colon" /></td>
          <td width="65%" height="20" align="left"> 
          	 <tm:dataDir beanName="sysUser" property="isvalid"  path="operator.userflag"/> 
          </td>
        </tr>
        </s:iterator>
      </table>
        </fieldset></td>   
  </tr>
  <tr>
     <td align="center"> <br>
	    <input type="button" name="return" value='修改' class="MyButton"  onclick="modify();" image="../../images/share/refu.gif"> 
        <input type="button" name="return" value='<s:text name="roleInfo.return" />' class="MyButton"  onclick="closeRoleModal();" image="../../images/share/refu.gif"> 
      </td>    
  </tr>
</table>
<form>

</body>
</html>