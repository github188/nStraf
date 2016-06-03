<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>

<script type="text/javascript">
function validateInfo(){
if (confirm('<s:text name="confirm.modify.commit"/>')){
	 var reg = /[^\x00-\xff]/;
	 var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
     if (!Validate('username','Require'))  {
       alert('<s:text name="roleInfo.check.username"/>');
	  // sysUserForm.username.focus();
	   return false;
	 }
	if (!Validate('workid','integer')){
		alert('<s:text name="operAdd.check.workid"/>');
		//sysUserForm.workid.focus();
		return false;	
	}
	if (sysUserForm.endDate.value.length>0){
	
	   		var thisDate = sysUserForm.thisDate.value;
	   		var endDate = sysUserForm.endDate.value;
			var v = re.test(endDate);
		    if(!v){
			alert('日期格式不正确');
			return false
			}
	   		if (thisDate>endDate){
	   			alert('<s:text name="operInfo.check.endDateERR"/>');
				return false;	
			}
		
	 	}
	 	
	 if (!Validate('email','Require')){
		alert("电子邮箱不能为空");
		return false;	
	}
	if (sysUserForm.email.value.length>0){
		if (!Validate('email','Email')){
			alert('<s:text name="operInfo.check.email"/>');
			//sysUserForm.email.focus();
			return false;	
		}
	}
	  return true;
  } 
}

	/* function closeRoleModal(){
	  window.close();
	} */
	
	function formsubmit(){
		if (validateInfo()){
	     window.returnValue=true
		 document.forms[0].submit();
		}
	}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10">	 
<s:form  name="sysUserForm" id="sysUserForm" action="/pages/um/sysUserInfo!update.action" method="post" >	
<input type="hidden" name="thisDate" value="<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
  <tr>
  	<td> 
  		<fieldset class="jui_fieldset" width="100%">
  			 <legend> <s:text  name="operInfo.updateOperator"/> </legend>
  			 <table width="100%" align="center" class="popnewdialog1">
  			   <s:iterator value="sysUser" id="user">      
  			 	<tr> 
		          <td width="30%" align="right"> 
			          <div align="right">
			          	<s:text  name="operInfoform.userIdentifier"/>
			          </div>
		          </td>
		          <td width="5%"  align="left"><s:text  name="label.colon"/></td>
		          <td width="65%"  align="left">
		          	<input type="hidden" value="<s:property value='userid'/>"  name="userid"/>
		          	<s:property value="userid"/> 
		          </td>
       			 </tr>
       			 
       			 <tr> 
			         <td width="30%" align="right"> 
			          <div align="right">
				          <s:text  name="operInfoform.username"/>
				      </div>
			          </td>
			          <td width="5%"  align="left"><s:text  name="label.colon"/></td>
			          <td width="65%"  align="left"> 
				          <input type="hidden" value="<s:property value='userid'/>" /> 
				          <input type="text" name="username" maxlength="20" value="<s:property value='username'/>"/> 
				          <font color="#FF0000">*</font> 
			          </td>
                  </tr>
                   
			      <tr> 
			          <td width="30%" align="right"> 
				          <div align="right">
				          	<s:text  name="operInfoform.number"/>
				          </div>
			          </td>
			          <td width="5%"  align="left"><s:text  name="label.colon"/></td>
			          <td width="65%"  align="left"> <input type="text" name="workid" value="<s:property value='workid'/>" maxlength="20"  onkeyup="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"/> <font color="#FF0000">*</font> 
			          </td>
			      </tr>
			        
			      <tr> 
				      <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.company"/></div></td>
				      <td width="5%"  align="left"><s:text  name="label.colon"/></td>
				      <td width="65%"  align="left"> <input type="text" value="<s:property value='workcompany'/>" name="workcompany" maxlength="20"/> 
				      </td>
			      </tr>
   
			     <tr> 
				      <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.phoneNum"/></div></td>
				      <td width="5%"  align="left"><s:text  name="label.colon"/></td>
				      <td width="65%"  align="left"> <input type="text" name="tel" value="<s:property value='tel'/>" maxlength="15" onKeyUp="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"/> </td>
				 </tr>
 
		        <tr> 
			          <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.mobile"/></div></td>
			          <td width="5%" align="left"><s:text  name="label.colon"/></td>
			          <td width="65%" align="left"> <input type="text" name="mobile" value="<s:property value='mobile'/>" maxlength="15" onKeyUp="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"/> </td>
			    </tr>
				
				
				<tr> 
			          <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.email"/></div></td>
			          <td width="5%" align="left"><s:text  name="label.colon"/></td>
			          <td width="65%" align="left"> <input type="text" name="email" value="<s:property value='email'/>" maxlength="100" /> <font color="#FF0000">*</font></td>
			   </tr>	
			   
			   <tr> 
			          <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.invaliddate"/></div></td>
			          <td width="5%" align="left"><s:text  name="label.colon"/></td>
			          <td width="65%" align="left"> 
			          <%
			            String endDate="";
			            endDate=(String)request.getAttribute("endDate");
			            if(endDate==null)
			            	endDate="";
			          %>
			            <input type="text" name="endDate" value='<%=endDate %>'  class="MyInput" /> 
			          </td>
			  </tr>
			  
			  <tr> 
			          <td width="30%" align="right"> <div align="right"><s:text  name="operInfoform.userflag"/></div></td>
			          <td width="5%" align="left"><s:text  name="label.colon"/></td>
			          <td width="65%" align="left"> 
			            <select  name="isvalid">					     
			              <option  value="Y"><s:text  name="operInfo.yes"/></option>
			                <s:if test='isvalid=="N"'>
								 <option value="N" selected>
						    </s:if>     
						    <s:else>
								 <option value="N" >
						    </s:else>  
			              	<s:text  name="operInfo.no"/>
			              </option>
			            </select > 
			          </td>
              </tr>
       
       		 <!--   <tr> 
					<td width="30%" align="right"><s:text  name="um.acceptionemailtype"/></td>
					<td width="5%" align="left"><s:text  name="label.colon"/></td>
					<td width="65%" align="left">
						<tm:tmSelect name="acceptemailType"  id="acceptemailType" style="width:50%" selType="dataDir" path="tmlMgr.acceptEmailType"  beforeOption='select'/> 	
	               		<s:if test="acceptemailType!=''">
	               			<script language="javascript">
	                            sysUserForm.acceptemailType.value='<s:property value="acceptemailType"/>';
	                        </script>
	                    </s:if>
					</td>
             </tr>-->
             <tr> 
            			<td width="30%" align="right"> 
            				<div align="right"><!--<s:text name="queryCondition.org"/>-->所属组别</div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/></td>
            			<td width="65%" align="left">
							<bean:define id="sorgid" name="tm.loginUser" property="orgid"/>
							<input type="hidden" name="orgid" id="orgid" value="0000000000"/>
                            <input type="hidden" name="stemp" id="stemp" value='<s:property value="groupName"/>'/>
			 				<select  name="groupName" style="width:50%" id="groupName">
                				<option value="质量管理组">质量管理组</option>
                				<option value="应用软件测试组">应用软件测试组</option>
                				<option value="基础软件测试组">基础软件测试组</option>
                				<option value="技术支持组">技术支持组</option>
			 					<option value="测试部门经理">测试部门经理</option>
   				          </select > 
							<%--
							<tm:tmSelectNext name="orgid" id="orgid" style="width:50%" selType="orgid" path='<%=String.valueOf(sorgid)%>'/>--%> <font color="#FF0000">*</font>
							<script type="text/javascript">
								var szStmp = document.getElementById("stemp").value.trim();
								if(szStmp=="")
								{
									szStmp = " ";
								}
								document.getElementById("groupName").value = szStmp;
							</script>
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
              				<script type="text/javascript">
								document.getElementById("level").value = <s:property value='level'/>;
							</script>
			 			</td>
          			</tr>
             </s:iterator>
			</table>
	   </fieldset>
	</td>
  </tr>
  <br />
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
  
  <tr>
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
        <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
      </td>    
  </tr>
</table>
</s:form>
</body>
</html>