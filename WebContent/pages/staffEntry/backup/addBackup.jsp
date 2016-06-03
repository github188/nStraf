<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%
	String ctxPath= "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
	
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript" src="../../js/jquery-1.11.0.js"></script>
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


function closeRoleModal(){
	  window.close();
}
	
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


function changeDept(key,value){
	if(key!='-1'){
		$.ajax({
			url: '<%=ctxPath%>/pages/staffEntry/entryInfo!getUsersByDept.action',
			data:{getUserByDept_DeptName:value},
			type: 'POST',
			dataType: 'JSON',
			timeout: 10000,
			error: function(data,error){
				alert("获取部门人员错误");
			},
			success: function(result){
				alert(result);
			}
		});
	}
}
	
</script>

<body id="bodyid"  leftmargin="0" topmargin="10" >

<form name="addEntryInfoForm" action="/pages/staffEntry/entryInfo!doAdd.action" method="post">
	<input type="hidden" name="thisDate" value="<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>"/>
	<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
   	 <tr>
  		<td> 
    		<fieldset class="jui_fieldset" width="100%">
        		<legend><s:text name="staffEntry.add_jsp.addOpDsc"/></legend>
        		<table width="100%" align="center" >
        			<!-- 选择部门  -->
        			 <tr> 
            			<td width="10%" height="23" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.detName"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="85%"  align="left"> 
            				  <select  name="deptName" style="width:50%" id="deptName" onChange="changeDept(this.options[this.options.selectedIndex].value,this.options[this.options.selectedIndex].text)">
            				    <option value='-1'><s:text name="staffEntry.add_jsp.selectDet"/></option>
            					<s:iterator value="deptValues" id="column">
                					<option value='<s:property value="key"/>'><s:property value="value"/></option>
                				</s:iterator>
           				     </select > 
             				 <font color="#FF0000"> *</font> 
              			</td>
         			</tr>
         			<!-- 用户选择 -->
         			<tr>
         				<td width="100%" colspan="3"> 
            				<div align="right" id="selectDeptUserDiv"></div>
            			</td>
         			</tr>
        			<!-- 用户id -->
          			 <tr> 
           				 <td width="10%" align="right"> 
           				 	<div align="right"><s:text name="staffEntry.add_jsp.userId"/></div>
           				 </td>
           				 <td width="5%"  align="left"><s:text name="label.colon"/></td>
           				 <td width="85%"  align="left"> <input name="userId" type="text" id="userId" readonly="readonly" size="30" maxlength="15" onBlur="validateUserid()" value=""> 
             				<font color="#FF0000">*</font> 
              			 </td>
          			</tr>
          			<!-- 用户姓名 -->
        		    <tr> 
           				<td width="10%" align="right"> 
           					<div align="right"><s:text name="staffEntry.add_jsp.userName"/></div>
           				</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="85%"  align="left"> <input name="userName" type="text" id="userName" readonly="readonly" size="30" maxlength="20" value=""> 
              				<font color="#FF0000">*</font> 
              			</td>
          			</tr>
          			<!-- 所属组别 -->
          			<tr> 
            			<td width="15%" height="23" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.groupName"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="80%"  align="left"> 
            				<input name="groupName" type="text" id="groupName"  readonly="readonly" size="30" maxlength="20" value=""> 
              				<font color="#FF0000"> *</font> 
             			</td>
          			</tr>
          			<tr> 
            			<td width="15%" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.grgBeginDate"/></div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<input type="hidden" name="beginDate" value='<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>'>
            			<td width="80%" align="left"> 
            				<input name="grgBeginDate" type="text" id="grgBeginDate" size="29" class="MyInput" issel="true" isdate="true" dofun="ShowDate(this)" > 
            			</td>
         		    </tr>
          			<tr> 
            			<td width="15%" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.regularDate"/></div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td><input type="hidden" name="beginDate" value='<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>'>
            			<td width="80%" align="left"> <input name="regularDate" type="text" id="regularDate" size="29" class="MyInput" issel="true" isdate="true" dofun="ShowDate(this)" > </td>
         		    </tr>
         			<tr> 
            			<td width="15%" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.note"/></div>
            			</td>
           				<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="80%"  align="left"> 
            				<input name="workid" type="note" id="workid" size="30" maxlength="20"  value=""> 
              				<font color="#FF0000">*</font> 
              			</td>
          			</tr>
          			<tr> 
            			<td width="15%" align="right"> 
            				<div align="right"><s:text name="staffEntry.add_jsp.extendStatus"/></div>
            			</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="80%"  align="left"> <input name="extendStatus" type="text" id="extendStatus" size="30" maxlength="20" value=""> </td>
          		   </tr>
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
        <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeRoleModal();" image="../../images/share/f_closed.gif"> 
        <input type="checkbox" name="newAnOther" id="newAnOther" value="1" >
        <label for="newAnOther"><s:text name="label.addAnOther"/></label>
      </td>    
  </tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
