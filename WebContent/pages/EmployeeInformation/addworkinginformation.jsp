<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.domain.SysUser"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>certification query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" >
	function save(){
		 if(document.getElementById("username").value.trim()=="")
			{
				alert("请输入姓名");
				return;
			}	
			document.getElementById("ok").disabled = true;
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}

	function selDate(id)
    {
    	var obj=document.getElementById(id);
    	ShowDate(obj);
    } 
	
	
	String.prototype.trim = function()
	{
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
	
	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/EmployeeInformation/EmployeeInformationinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="在职信息" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    <tr>
					  <tr>
					  <td rowspan="3" align="center"  bgcolor="#999999"><div align="center">在职信息<font color="#FF0000">
					  </font></div> </td>
					  <td>入职日期</td>
					  <td><input name="employssInformation.hiredate" type="text" id="hiredate"  class="MyInput" /></td>
					  <td >项目组</td>
					  <td><input name="employeeInformation.groupname" type="text" id="groupname"  class="MyInput" ></td>
					  <td >手机号码</td>
					  <td><input name="employeeInformation.mobile" type="text" id="mobile"  class="MyInput" ></td>
					  </tr>
					  
					  <tr>

					  <td>状态</td>
					  <td><select name="status" style="width:63px;" id="status"  >		
                      <option value='在职'>在职</option>
                      <option value='离职'>离职</option>
                      </select></td>
					  <td width="10%">离职日期</td>
					  <td><input name="employssInformation.leavedate" type="text" id="leavedate"  class="MyInput" /></td>
					  <td width="10%">离职原因</td>
					  <td  colspan="3"><input name="employssInformation.leavereason" type="text" class="MyInput" id="employssInformation.leavereason" value="" size="60"></td>
					  <tr>
					  <td>备注</td>
					  <td  colspan="7">
					  <textarea cols="130" name="employssInformation.remark"
					 id="remark" ><s:property value="employssInformation.remark"/></textarea>					 </td>
					  <tr>
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" id="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		  </td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 