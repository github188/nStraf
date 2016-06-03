<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
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
	<%-- <script type="text/javascript" src="../../js/jquery.js"></script> --%>
	<script type="text/javascript" >
	function save(){
	
		 if(document.getElementById("uname").value.trim()=="")
			{
				alert("请输入姓名");
				return;
			}	
			 if(document.getElementById("entryDate").value.trim()=="")
			{
				alert("请输入入职日期");
				return;
			}	
			 if(document.getElementById("changeDate").value.trim()=="")
			{
				alert("请输入转正日期");
				return;
			}	
			 if(document.getElementById("groupName").value.trim()=="")
			{
				alert("请输入项目组");
				return;
			}	
			 if(document.getElementById("position").value.trim()=="")
			{
				alert("请输入岗位信息");
				return;
			}	
				 if(document.getElementById("teacher").value.trim()=="")
			{
				alert("请输入督导师");
				return;
			}	
			 
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	
	function check()
	{
	   if(document.getElementById("certificationNo").value.trim()=="")
		{
			alert("请输入认定编号");
			$("#certificationNo").focus();
			return;
		}
		var url="cerinfo!check.action";
		var params={deviceNo:$("#certificationNo").val()};
		jQuery.post(url, params, $(document).callbackFun1, 'json');
	}
	$.fn.callbackFun1=function (json)
	 {	
		
	  	if(json!=null&&json==false)
		{	
		//	$("#deviceNo").focus();
			document.getElementById("ok").disabled = true;
			alert("认定编号与之前输入的相同，请重新输入");
			return;
		}else{
			document.getElementById("ok").disabled = false;
			return;
		}	
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/newEmployeeManage/newEmployeeManageinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="课程信息" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    
 
                   <tr>
                    <td align="center" width="9%" bgcolor="#999999"><div align="center">姓名<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="21%"><input name="em.uname" type="text" id="uname" size="20" maxlength="20" value=''></td>
                     <td align="center"   width="10%" bgcolor="#999999"><div align="center">入职日期<font color="#FF0000">*</font></div></td>
                     <td width="24%" bgcolor="#FFFFFF">
                     <input name="em.entryDate" type="text" id="entryDate" size="22" maxlength="20"  class="MyInput" readonly="readonly" />
                     </td> 
                        <td align="center"   width="13%" bgcolor="#999999"><div align="center">转正日期<font color="#FF0000">*</font></div></td>
                     <td width="23%" bgcolor="#FFFFFF"><input name="em.changeDate" type="text" id="changeDate" size="20" maxlength="20" class="MyInput" readonly="readonly" /></td> 
                  </tr>
                  <tr> 
                   <td align="center"  width="9%" bgcolor="#999999"><div align="center">项目组<font color="#FF0000">*</font></div></td>
              <td bgcolor="#FFFFFF">
                    	<select  name="em.groupName"  style="width:141px;" id="groupName" >
								<option value="" >----</option>
                				<option value="质量管理组">质量管理组</option>
								<option  value="黑盒测试组">黑盒测试组</option>
								<option value="白盒测试组">白盒测试组</option>
								<option  value="自动化测试组">自动化测试组</option>
              			</select > 
                    </td> 
                     <td align="center"  width="10%" bgcolor="#999999"><div align="center">岗位<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF">
                      <div align="left">
                        <input name="em.position" type="text" id="position" size="24" maxlength="24" value=''>
                    </div></td> 
                    <td bgcolor="#999999">  <div align="center">督导师<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="em.teacher" type="text" id="teacher" size="22" maxlength="20" value='<s:property value="em.teacher"/>'></td>
                  </tr>
                  <tr>
                    <td align="center" bgcolor="#999999">学习状态<font color="#FF0000">*</font></td>
                    <td bgcolor="#FFFFFF">
                    	<select  name="em.studyStatus"  style="width:141px;" id="studyStatus" >
                				<option  value="待学习">待学习</option>
                				<option value="学习中">学习中</option>
                				<option  value="完成学习">完成学习</option>
              			</select > 
                    </td>
                    <td bgcolor="#999999"><div align="center">黑盒成绩</div></td>
                    <td bgcolor="#FFFFFF"><input name="em.blackScore" type="text" id="blackScore" size="24" maxlength="24" ></td>
                    <td bgcolor="#999999"><div align="center">白盒成绩</div></td>
                    <td bgcolor="#FFFFFF"><input name="em.whiteScore" type="text" id="whiteScore" size="22" maxlength="20" ></td>
                  </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 