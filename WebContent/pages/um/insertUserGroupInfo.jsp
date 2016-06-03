<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>add group</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
    userGroupForm.grpcode.value=userGroupForm.grpcode.value.trim();
    userGroupForm.grpname.value=userGroupForm.grpname.value.trim();
	if (!Validate('grpcode','Require')){  
		alert('<s:text name="roleInfo.check.grpcode"/>');
		return false;
	}
	if (!Validate('grpname','Require'))  {
		alert('<s:text name="roleInfo.check.grpname"/>');
		return false;
	}
	/* if (!Validate('grpLevel','Require'))  {
		alert('请选择数据级别');
		return false;
	} */
	window.returnValue=true;
	return true;
}	 

<%-- function closeGrpModal(){
	var theUrl='<%=request.getContextPath()%>/pages/um/userGroup.do';
	window.returnValue=theUrl
	window.close();
} --%>

function formsubmit(){
	if (validateInfo()){
	 var newAnOther=userGroupForm.newAnOther.checked;
     userGroupForm.action="<%=request.getContextPath() %>/pages/um/userGroup!insert?newAnOther="+newAnOther;
	 userGroupForm.submit();
	}
}

function setFlag()
{
	var flag = userGroupForm.flag.value;
	if( flag == "add")
		userGroupForm.newAnOther.checked = true;
}


</script>
<body id="bodyid" leftmargin="10" topmargin="10" onLoad="setFlag()">
<form action="/pages/um/userGroup.do?action=insert" method="post" name="userGroupForm" id="userGroupForm">
<table width="90%" border="0" align="center" class="popnewdialog1">
  <tr >
  <td><fieldset class="jui_fieldset" width="100%">
        <legend> <s:text name="grpInfo.addGrp"/> </legend>
        <table align="center">
		 <tr> 
            <td width="30%" align="right">组号:</td>        
            <td width="70%">
			<input name="grpcode" type="text"  size="20" maxlength="32" onblur="this.value=this.value.trim()"><font color="#FF0000">*</font>
			</td>
          </tr>
          <tr> 
            <td width="30%" align="right">组名:</td>        
            <td width="70%">
			<input name="grpname" type="text"  size="20" maxlength="50" onblur="this.value=this.value.trim()"><font color="#FF0000">*</font>			
			</td>
          </tr>
          <%-- <tr> 
            <td width="30%" align="right">数据级别：</td>        
            <td width="70%">
            	<select name="grpLevel" id="grpLevel">
            		<option value="">请选择...</option>
            		<option value="0">1级(查看所有数据)</option>
            		<option value="1">2级(查看部门数据)</option>
            		<option value="2">3级(查看项目内数据)</option>
            		<option value="3">4级(查看个人数据)</option>
            	</select><font color="#FF0000">*</font>
			</td>
          </tr>  --%>
          
          <tr>
          	<td width="35%" align="center"><input type="hidden" name="flag"/></td>
          </tr>
        </table>
        </fieldset></td>   
  </tr>
  </table>
  <br />
  <table width="90%" align="center" cellPadding="0" cellSpacing="0">
  <tr>  
  		<td>
             <fieldset width="100%">
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%">
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>
  	</tr>
  	<tr>
    	<td align="center">
	  		<table width="100%" border="0">
		  		<tr>
			     <td align="right"> <br> 
			        <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton" onclick="formsubmit()" image="../../images/share/yes1.gif"> 
<%-- 			        <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeGrpModal()" image="../../images/share/f_closed.gif"> 
 --%>			        <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal()" image="../../images/share/f_closed.gif"> 

			       
			        <input type="checkbox" name="newAnOther" id="newAnOther" value="1">
			        <label for=newAnOther><s:text name="label.addAnOther"/></label>     
			      </td>
        </table>
	</tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>