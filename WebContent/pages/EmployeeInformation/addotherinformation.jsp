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
                    <legend><s:text name="其它信息" /></legend>
                    <input name="trip.start2" type="text" id="trip.start"  class="MyInput" />
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    <tr>
					  <tr>
					  <td rowspan="3" align="center"  bgcolor="#999999"><div align="center">其它信息<font color="#FF0000">
					  </font></div> </td>
					  <td>户口所在地</td>
					  <td colspan="3"><input name="employssInformation.residentcity" size="70" type="text" id="residentcity"  class="MyInput" ></td>
					  <td> 邮政编码</td>
					  <td><input name="employeeInformation.postalcode_city" type="text" id="postalcode_city"  class="MyInput" ></td>
					  </tr>
					  
					  <tr>
					  <td>家庭地址</td>
					  <td colspan="3"><input name="employssInformation.address" size="70" type="text" id="address"  class="MyInput" ></td>
					  <td> 邮政编码</td>
					  <td><input name="employeeInformation.postalcode_address" type="text" id="postalcode_address"  class="MyInput" ></td>
					  </tr>
					  
					  <tr>

					  <td>紧急联系人</td>
					  <td><input name="employssInformation.emergencycontacts"  type="text" id="emergencycontacts"  class="MyInput" ></td>
					  <td>关系</td>
					  <td><input name="employssInformation.relation" type="text" id="relation"  class="MyInput" /></td>
					  <td>电话</td>
					  <td ><input  name="employssInformation.contactstelnum" type="text" id="employssInformation.contactstelnum"  class="MyInput" ></input></td>
					 
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