<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>update role</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
     if (!Validate('rolename','Require'))  {
       alert('<s:text name="roleInfo.check.rolename"/>')  
	   return false;
	   }
	  return true;	   
	 }
	 

/* function closeRoleModal(){
  //window.returnValue=true
  window.close();
} */

function returnRoleInfo(){
	userRoleForm.action="<%=request.getContextPath()%>/pages/um/userRole.do";
	userRoleForm.submit();
}

function formsubmit(){
if (validateInfo()){
	userRoleForm.submit();
}
}


</script>
<body id="bodyid"  leftmargin="10" topmargin="10">
<form action="<%=request.getContextPath() %>/pages/um/userRole!update.action" name="userRoleForm" method="post" >
<table width="100%" border="0" align="center">
  <tr  align="center">
  <td> <fieldset class="jui_fieldset" width="100%">
        <legend> <s:text name="roleInfo.updateRole"/> </legend>
       
        <s:iterator value="usrRole">
        <table align="center">
          <tr> 
            <td width="35%" align="right"><s:text name="roleInfoForm.roleCode"/><s:text name="label.colon"/></td>        
            <td width="65%">
            <input type="hidden" name="rolecode" value='<s:property value="rolecode"/>'/>
			<s:property value="rolecode"/> 			
			</td>
          </tr>			
          <tr> 
            <td width="35%" align="right"><s:text name="roleInfoForm.roleName"/><s:text name="label.colon"/></td>        
            <td width="65%">	
            <input type="text" name="rolename" value='<s:property value="rolename"/>'  maxlength="50"/>
			<font color="#FF0000">*</font>
			</td>
          </tr>
        </table>
        </s:iterator>
        </fieldset></td>   
  </tr>
  <tr><td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%">
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>
  
  </tr>
  <tr align="center">
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="button.ok" />' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
        <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
      </td>
    
  </tr>
</table>
</form>
<form>
<logic:messagesPresent message="true" >
      <fieldset class="jui_fieldset" width="100%">
            <legend><s:text  name="operator.backinfo"/></legend>
         <table width="100%">
          <tr> 
            <td width="10%" align="left">&nbsp;</td>
          <td width="90%" align="left">
		    <font color="#FF0000">
				<html:messages id="msg" message="true" >
				   <bean:write name="msg"  filter="false"/>
				</html:messages>
			</font>
			  </td>
          </tr>
        </table>
		    </fieldset>
	 </logic:messagesPresent>
	</form>
</body>
</html>