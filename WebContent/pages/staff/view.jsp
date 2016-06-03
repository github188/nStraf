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
  	var strUrl="/pages/staff/staffInfo!modifyPage.action?userid="+userid;
	var features="1000,500,operInfo.updateTitle,um";
	var resultvalue = OpenModal(strUrl,features);
	window.close();
  }
</script>

<body id="bodyid">
 	 
<form action="/pages/staff/staffInfo!modifyPage.action" method="post"  name="sysUserForm">	
<table width="60%" align="center" cellPadding="0" cellSpacing="0">
<input type="hidden"  name="userid" id="userid" value="<s:property value='userid'/>"/>
  <tr>
  	<td>     
      <table   width="100%" class="input_table">
        <tr>
        	 <td class="input_tablehead"> <s:text name="staff.list.detail" /> </td>
        </tr>
        <tr height="20"> 
          <td width="20%" class="input_label2"> <div > <s:text name="staff.add.userid" /> <s:text name="label.colon" /></div></td>
          <td width="30%"   align="left" class="input_readonly" > <s:property value="#request.sysUser.userid"/> 
          </td>
          <td width="20%"  class="input_label2"> <div > <s:text name="staff.add.username" /> <s:text name="label.colon" /></div></td>
          <td width="30%"  align="left" class="input_readonly"> <s:property  value="#request.sysUser.username" /> 
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2"> <div > <s:text name="staff.add.idCardNo" /> <s:text name="label.colon" /></div></td>
          <td  align="left" class="input_readonly"> <s:property   value="#request.sysUser.idCardNo" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.education" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          	<tm:dataKeyValue beanName="sysUser" property="education"  path="staffManager.education"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2"> <div > <s:text name="staff.add.birthDate" /> <s:text name="label.colon" /></div></td>
          <td   align="left" class="input_readonly"> <s:date name="#request.sysUser.birthDate" format="yyyy-MM-dd"/> 
          </td>
          <td   class="input_label2"> <div > <s:text name="staff.add.deptName" /> <s:text name="label.colon" /></div></td>
          <td   align="left" class="input_readonly"> 
           <tm:dataKeyValue beanName="sysUser" property="deptName"  path="staffManager.department"/> 
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.status" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          <tm:dataKeyValue beanName="sysUser" property="status"  path="staffManager.status"/>
          </td>
          <td   class="input_label2">  <s:text name="staff.add.level" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          <tm:dataKeyValue beanName="sysUser" property="level"  path="staffManager.userlevel"/>
          </td>
        </tr>
         <tr height="20"> 
          <td   class="input_label2"> 公司工号 <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          	<s:property value="#request.sysUser.jobNumber"/>
          </td>
          <td   class="input_label2"> 外派工号 <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          	<s:property value="#request.sysUser.outNumber"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.workBegindate" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:date name="#request.sysUser.workBegindate" format="yyyy-MM-dd"/>
          </td>
          <td   class="input_label2">  <s:text name="staff.add.grgBegindate" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:date name="#request.sysUser.grgBegindate" format="yyyy-MM-dd"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.sumworkage" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sumage"/>
          </td>
          <td   class="input_label2">  <s:text name="staff.grgworkage" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.grgage"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.historyage" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.historyage"/>
          </td>
          <td   class="input_label2">  项目组<s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly">
            <s:property value="#request.groupNames"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.graduateschool" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.graduateSchool" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.graduateDate" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:date name="#request.sysUser.graduateDate" format="yyyy-MM-dd"/> 
          </td>
        </tr>
        <tr height="20" > 
          <td   class="input_label2">  <s:text name="staff.add.major" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.major" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.postLevel" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> 
          <tm:dataKeyValue beanName="sysUser" property="postLevel"  path="staffManager.postlevel"/> 
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.mobile" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.mobile" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.email" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.email" /> 
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.tel" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.tel" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.isvalid" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly">
          	<tm:dataKeyValue beanName="sysUser" property="isvalid"  path="staffManager.validate"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.relativeName" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly"> <s:property value="#request.sysUser.relativeName" /> 
          </td>
          <td   class="input_label2">  <s:text name="staff.add.relativeTel" /> <s:text name="label.colon" /></td>
          <td  align="left" class="input_readonly">
          	<s:property value="#request.sysUser.relativeTel"/>
          </td>
        </tr>
        <tr height="20"> 
          <td   class="input_label2">  <s:text name="staff.add.address" /> <s:text name="label.colon" /></td>
          <td  align="left" colspan="3" class="input_readonly"> <s:property value="#request.sysUser.address" /> 
          </td>
        </tr>
      </table>
        </td>   
  </tr>
</table>
<form>

</body>
</html>