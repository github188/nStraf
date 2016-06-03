<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>add role</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
     document.getElementById("rolecode").value=document.getElementById("rolecode").value.trim();
     document.getElementById("rolename").value=document.getElementById("rolename").value.trim();
     if (!Validate('rolecode','Require')){  
       alert('<s:text name="roleInfo.check.rolecode"/>')
	   
	   return false;
	   }
     if (!Validate('rolename','Require'))  {
       alert('<s:text name="roleInfo.check.rolename"/>')
	  
	   return false;
	   }
	   window.returnValue = true;
	  return true;
	   
	 }
	 

/* function closeRoleModal(){
  window.returnValue=true;
  window.close();
} */

function returnRoleInfo(){
	userRoleForm.action="/pages/um/userRole!";
	userRoleForm.submit();
}

function formsubmit(){
if (validateInfo()){
   var newAnOther=document.getElementById("newAnOther").checked;	
	  window.returnValue=true;

        updateUserRoleForm.action="<%=request.getContextPath() %>/pages/um/userRole!add.action?newAnOther="+newAnOther;
		updateUserRoleForm.submit();
}
}

function setFlag()
{
	var flag = document.getElementById("flag").value;
	if( flag == "add")
		updateUserRoleForm.newAnOther.checked = true;
}

</script>
<body id="bodyid"  leftmargin="10" topmargin="10" onLoad="setFlag()">
<form action="/pages/um/userRole!insert.action" method="post" name="updateUserRoleForm" id="updateUserRoleForm">
<table width="90%" border="0" align="center" class="popnewdialog1">
  <tr  align="center">
  <td> <fieldset class="jui_fieldset" width="100%">
        <legend><s:text name="roleInfo.addRole"/>  </legend>
        <table align="center">
		 <tr> 
            <td width="35%" align="right"><s:text name="roleInfo.addRoleCode"/></td>        
            <td width="65%"><input name="rolecode"  id="rolecode" type="text"  onblur="this.value=this.value.trim()"  size="20" maxlength="32"><font color="#FF0000">*</font></td>          
          </tr>
          <tr> 
            <td width="35%" align="right"><s:text name="roleInfo.addRoleName"/></td>        
            <td width="65%"><input name="rolename" id="rolename" type="text"  size="20" maxlength="50" onblur="this.value=this.value.trim()"><font color="#FF0000">*</font></td>        
          </tr>
          <tr>
          	<td width="35%" align="center"><input type="hidden" name="flag" id="flag" value="0"/></td>
          </tr>
          
          <tr>
         <%-- <td width="35%" align="right">组级别<s:text name="label.colon"/>
			 <div align="left">
		  	   <tm:tmSelect name="grpLevel" id="grpLevel" style="width:100%" selType="dataDir" path="tmlMgr.orglevel" />
		     </div>
			</td> --%>
          </tr>
          
        </table>
        </fieldset></td>   
  </tr>
  </table>
  <br/>
  <table width="90%" border="0" align="center" >
  <tr>
          <td>
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

    <td height="69" align="center">
	 <!-- <fieldset class="jui_fieldset" width="100%">
     <legend><s:text name="label.operator"/></legend> -->
	  <table width="90%" border="0">
  	<tr>
     <td align="right"> <br> 
        <input type="button" name="ok"  value='<s:text name="roleInfo.ok"/>' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
<%--         <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeRoleModal();" image="../../images/share/f_closed.gif"> 
 --%>        <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
       
        <input type="checkbox" name="newAnOther" id="newAnOther" value="1">
        <label for=newAnOther><s:text name="label.addAnOther"/></label>      
      </td>    
   </tr>
        </table>
	    <!-- </fieldset> -->
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>