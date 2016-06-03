<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript">
function validateInfo(){
if (confirm('<s:text  name="confirm.modify.commit"/>')){
        validateUserid();
	 if (!Validate('userid','Require'))  {
       alert('<s:text  name="operInfo.check.useridentifiernull"/>');
	  // sysUserForm.username.focus();
	   return false;
	 }		  
     if (!Validate('username','Require'))  {
       alert('<s:text  name="roleInfo.check.username"/>');
	  // sysUserForm.username.focus();
	   return false;
	 }
	 if (!Validate('userpwd','Require'))  {
       alert('<s:text  name="operInfo.check.pwdnull"/>')
	  // sysUserForm.userpwd.focus();
	     return false;
	   }
	 if (!Validate('userpwd2','Require'))  {
       alert('<s:text  name="operInfo.check.pwd2null" />')
	   // sysUserForm.userpwd2.focus();
	     return false;
	   }
	if (sysUserForm.userpwd.value!=sysUserForm.userpwd2.value){
	   		alert('<s:text  name="pwdsetting.differ"/>')
	    // sysUserForm.userpwd2.focus();
	     	return false;
	   }
	if (!Validate('workid','integer')){
		alert('<s:text  name="operAdd.check.workid"/>');
		//sysUserForm.workid.focus();
		return false;	
	}
	if (!Validate('email','Require')){
		alert("电子邮箱不能为空");
		return false;	
	}
	 if (sysUserForm.email.value.length>0){
	if (!Validate('email','Email')){
		alert('<s:text  name="operInfo.check.email" />');
		//sysUserForm.email.focus();
		return false;	
	}
	 }
	 if (sysUserForm.endDate.value.length>0){
	   		var thisDate = sysUserForm.thisDate.value;
	   		var endDate = sysUserForm.endDate.value;
	   		var   re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   
		    var v = re.test(endDate);
		    if(!v){
			alert('');
			return false
			}
	  		if (thisDate>endDate){
				alert('<s:text  name="operInfo.check.endDateERR"/>');
				return false;	
			}
		
	 	}
	 if(!DateValidate('beginDate','endDate'))
	 {
	 	alert('<s:text  name="operInfoform.error.invaliddate"/>');
	 	return false;
	 }	 	 
	  return true;
    }   
 }


/* function closeRoleModal(){
	  window.close();
} */
	
function formsubmit(){
	if (validateInfo()){
        window.returnValue=true;
        
        var newAnOther=sysUserForm.newAnOther.checked;
        sysUserForm.action="<%=request.getContextPath() %>/pages/um/sysUserInfo!add.action?newAnOther="+newAnOther;
		sysUserForm.submit();
	}
}
	
function setFlag()
{
	var flag = sysUserForm.addFlag.value;
	if( flag == "add")
		sysUserForm.newAnOther.checked = true;
}
	
function validateUserid(){
	sysUserForm.userid.value=sysUserForm.userid.value.trim();
	var userid = sysUserForm.userid.value;
	var userid1 = userid.replace(/[\W]/g,'');
	if(userid != userid1 )
	 {
	   alert('用户标识输入不正确，请重新输入');
	   sysUserForm.userid.focus();	  
	 }
	}
	
</script>

<body id="bodyid"  leftmargin="0" topmargin="10" >

<form name="sysUserForm" action="/pages/um/sysUser!add.action" method="post">
	<input type="hidden" name="thisDate" value="<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>"/>
	<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
   	 <tr>
  		<td> 
    		<fieldset class="jui_fieldset" width="100%">
        		<legend><s:text name="operInfo.addOperator"/></legend>
        		<table width="100%" align="center" >
          			 <tr> 
           				 <td width="30%" align="right"> 
           				 	<div align="right"><s:text name="queryCondition.operIden"/></div>
           				 </td>
           				 <td width="5%"  align="left"><s:text name="label.colon"/></td>
           				 <td width="65%"  align="left"> <input name="userid" type="text" id="userid" size="30" maxlength="15" onBlur="validateUserid()" value=""> 
             				<font color="#FF0000">*</font> 
              			 </td>
          			</tr>
        		    <tr> 
           				<td width="30%" align="right"> 
           					<div align="right"><s:text name="operInfoform.username"/></div>
           				</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="username" type="text" id="username" size="30" maxlength="20" value=""> 
              				<font color="#FF0000">*</font> 
              			</td>
          			</tr>
         		    <tr> 
            			<td width="30%" height="23" align="right"> <div align="right"><s:text name="operInfoform.pwd"/></div></td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="userpwd" type="password" id="userpwd" size="30" maxlength="20" value=""> 
             				 <font color="#FF0000"> *</font> 
              			</td>
         			</tr>
          			<tr> 
            			<td width="30%" height="23" align="right"> 
            				<div align="right"><s:text name="operInfoform.pwdconfirm"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="userpwd2" type="password" id="userpwd2" size="30" maxlength="20" value=""> 
              				<font color="#FF0000"> *</font> 
             			</td>
          			</tr>
         			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.number"/></div>
            			</td>
           				<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> 
            				<input name="workid" type="text" id="workid" size="30" maxlength="20" onKeyUp="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"  value=""> 
              				<font color="#FF0000">*</font> 
              			</td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.company"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="workcompany" type="text" id="workcompany" size="30" maxlength="20" value=""> </td>
          			</tr>
          			<tr> 
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.department"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="deptName" type="text" id="deptName" size="30" maxlength="20" value=""> </td>
          			</tr>
          			<tr> 
           				 <td width="30%" align="right"> 
           					<div align="right"><s:text name="operInfoform.phoneNum"/></div>
           				 </td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> <input name="tel" type="text" id="tel" size="30" maxlength="20" value="" onKeyUp="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"> </td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.mobile"/></div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<td width="65%" align="left"> <input name="mobile" type="text" id="mobile" size="30" maxlength="20" value="" onKeyUp="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"> </td>
         			 </tr>
        			 <tr>
           				 <td width="30%" align="right"> 
           				 	<div align="right"><s:text name="operInfoform.email"/></div>
           				 </td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<td width="65%" align="left"> <input name="email" type="text" id="email" size="30" maxlength="100" value=""><font color="#FF0000">*</font></td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><%--<s:text name="queryCondition.org"/>--%>所属组别</div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/>
            			</td>
            			<td width="65%" align="left">
							<bean:define id="sorgid" name="tm.loginUser" property="orgid"/>
							<!--
							<tm:tmSelectNext name="orgid" id="orgid" style="width:50%" selType="orgid" path='<%=String.valueOf(sorgid)%>'/><font color="#FF0000">*</font> 
			 				-->
			 				<input type="hidden" name="orgid" id="orgid" value="0000000000"/>
			 				<select  name="groupName" style="width:50%" id="groupName">
                				<option value="质量管理组">质量管理组</option>
                				<option value="应用软件测试组">应用软件测试组</option>
                				<option value="基础软件测试组">基础软件测试组</option>
                				<option value="技术支持组">技术支持组</option>
			 					<option value="测试部门经理">测试部门经理</option>
           				  </select> 
			 			</td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right">级别</div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/>
            			</td>
            			<td width="65%" align="left">
			 				<select  name="level" style="width:50%" id="level">
                				<option value="0">部门经理</option>
                				<option  value="1">组长</option>
                				<option value="2">普通员工</option>
                                <option value="3">总工</option>
              				</select > 
			 			</td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.invaliddate"/></div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td><input type="hidden" name="beginDate" value='<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>'>
            			<td width="65%" align="left"> <input name="endDate" type="text" id="endDate" size="29" class="MyInput" /> </td>
         		    </tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><s:text name="operInfoform.userflag"/></div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<td width="65%" align="left"> 
            				<select  name="isvalid" style="width:50%">
                				<option  value="Y"><s:text name="operInfo.yes"/></option>
                				<option value="N"><s:text name="operInfo.no"/></option>
              				</select > 
              			</td>
          			</tr>
          			<!--  <tr> 
            			<td width="30%" align="right"><s:text name="um.acceptionemailtype"/></td>
           		 		<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<td width="65%" align="left">
            				<tm:tmSelect name="acceptemailType" id="acceptemailType" style="width:50%" selType="dataDir" path="tmlMgr.acceptEmailType"  beforeOption='select' />
            			</td>
          			</tr>-->
        	</table>
           </fieldset>
       </td>
    </tr>
    </table>
    <br />
    <table width="90%" align="center" cellPadding="0" cellSpacing="0">
    <tr>  
       <td>
             <fieldset  width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%" >
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>  
  </tr>
  
  <tr>
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
        <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
        <input type="checkbox" name="newAnOther" id="newAnOther" value="1" >
        <label for="newAnOther"><s:text name="label.addAnOther"/></label>
      </td>    
  </tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
