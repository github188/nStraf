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
	var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
				
			window.returnValue=true;
			reportInfoForm.submit();
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

		  function init()
	      {
	      	var id=$("#id").val();
	      	if(""!=id&&null!=id)
	      	{   
	      		parent.document.getElementById("id").value=id;
	      		if(parent.showLi)
	      			parent.showLi();
	      	}
	      	return false;
	      }   
	      
		 
		function selAgain(){
			var taskDivPPtr = document.getElementsByTagName("table")[0];
			for(var ind=0; ind<taskDivPPtr.getElementsByTagName("input").length; ind++){
				if(taskDivPPtr.getElementsByTagName("input")[ind].type.toLowerCase()=="checkbox"){
					taskDivPPtr.getElementsByTagName("input")[ind].checked=selAllFlag;
				}
			}
			selAllFlag = !selAllFlag;
		}
		  
	       function selDate(id)
	      {
	      	var obj=document.getElementById(id);
	      	ShowDate(obj);
	      }  
	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/SysUser/SysUserinfo!updateOtherInformaton.action"   method="post">
	<input type="hidden" id="sysuser.id" name="sysuser.id"
				value='<s:property value="sysuser.id"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="其它信息" /></legend>
                                      <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                   
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" rowspan="3" align="center"  ><div align="center">其它信息<font color="#FF0000">
					  </font></div> </td>
					  <td bgcolor="#999999">户口所在地</td>
					  <td colspan="3"><input name="sysuser.residentcity" size="82" type="text" id="residentcity"  class="MyInput" value='<s:property value="sysuser.residentcity"/>'></td>
					  <td bgcolor="#999999"> 邮政编码</td>
					  <td><input name="sysuser.postalcode_city" type="text" id="postalcode_city"  class="MyInput" value='<s:property value="sysuser.postalcode_city"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999">家庭地址</td>
					  <td colspan="3"><input name="sysuser.address" size="82" type="text" id="address"  class="MyInput" value='<s:property value="sysuser.address"/>'></td>
					  <td bgcolor="#999999"> 邮政编码</td>
					  <td><input name="sysuser.postalcode_address" type="text" id="postalcode_address"  class="MyInput" value='<s:property value="sysuser.postalcode_address"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999">紧急联系人</td>
					  <td><input name="sysuser.emengencycontacts"  type="text" id="emengencycontacts"  class="MyInput" value='<s:property value="sysuser.emengencycontacts"/>'></td>
					  <td bgcolor="#999999">关系</td>
					  <td><input name="sysuser.relation" type="text" id="relation"  class="MyInput" value='<s:property value="sysuser.relation"/>'></td>
					  <td bgcolor="#999999">电话</td>
					  <td ><input  name="sysuser.contactstelnum" type="text" id="contactstelnum"  class="MyInput" value='<s:property value="sysuser.contactstelnum"/>'></input></td>
					 </tr>
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
		  <td align="center"><input type="button" name="return" id="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"></td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 