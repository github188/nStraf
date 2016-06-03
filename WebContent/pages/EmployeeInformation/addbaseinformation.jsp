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
                    <legend><s:text name="员工基本资料" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    <tr>
					  <tr>
					  <td rowspan="3" align="center"  bgcolor="#999999"><div align="center">开始日期<font color="#FF0000">
					  </font></div> </td>
					  <td width="9%">姓名</td>
					  <td><input name="employeeInformation.username" type="text" id="username"  class="MyInput" ></td>
					  <td width="8%">性别</td>
					  <td><select name="sex" style="width:63px;" id="sex"  >		
                      <option value='男'>男</option>
                      <option value='女'>女</option>
                      </select></td>
					  <td width="8%">出生日期</td>
					  <td><input name="trip.end" type="text" id="end"  class="MyInput" /></td>
					  <td width="9%">民族</td>
					  <td><select name="national" style="width:83px;" id="national"  >		
                      <option value='汉族'>汉族</option>
                      <option value='满族'>满族</option>
					  <option value='藏族'>藏族</option>
					  <option value='维吾尔族'>维吾尔族</option>
					  <option value='蒙古族'>蒙古族</option>
                      </select></td>
					  </tr>
					  
					  <tr>

					  <td width="10%">政治面貌</td>
					  <td><select name="politicastatus" style="width:63px;" id="politicastatus"  >		
                      <option value='团员'>团员</option>
                      <option value='党员'>党员</option>
					  <option value='群众'>群众</option>
                      </select></td>
					  <td width="8%">籍贯</td>
					  <td><select name="birthplace" style="width:63px;" id="birthplace"  >		
                      <option value='广东'>广东</option>
                      <option value='广西'>广西</option>
					  <option value='江西'>江西</option>
					  <option value='湖南'>湖南</option>
                      <option value='湖北'>湖北</option>
					  <option value='安徽'>安徽</option>
                      </select></td>
					  <td>外语水平</td>
					  <td><select name="englishskill" style="width:63px;" id="englishskill"  >		
                      <option value='一般'>一般</option>
                      <option value='CET4'>CET4</option>
					  <option value='CET6'>CET6</option>
					  <option value='CET8'>CET8</option>
                      </select></td>
					  <td width="9%">技术职称</td>
					  <td><input name="employeeInformation.technicaltitle" type="text" id="technicaltitle"  class="MyInput" ></td>
					  </tr>
					  
					  <tr>
					  <td width="9%">学历</td>
					  <td><select name="education" style="width:63px;" tabindex="4" id="education"  >		
                      <option value='初中'>初中</option>
                      <option value='高中/中专'>高中/中专</option>
					  <option value='大专'>大专</option>
					  <option value='本科'>本科</option>
					  <option value='硕士'>硕士</option>
					  <option value="博士">博士</option>
                      </select></td>
					  <td width="8%">毕业院校</td>
					  <td  colspan="3"><input name="employssInformation.graduateschool" size="60" type="text" id="graduateschool"  class="MyInput" isSel="true">                      </td>
                      
					  <td width="9%">所学专业</td>
					  <td><input name="employssInformation.major" type="text" id="major"  class="MyInput" ></td>
				      </tr>
					   
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