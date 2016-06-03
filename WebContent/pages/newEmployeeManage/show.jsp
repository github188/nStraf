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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/newEmployeeManage/newEmployeeManageinfo!update.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                   
                    <table width="95%"class="input_table">
                    <tr> <td class="input_tablehead"><s:text name="新员工学习信息" /></td></tr>
 
                   <tr>
				    <td class="input_label2"width="9%"><div >姓名：</div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="21%"><input name="em.uname" type="text" id="uname" size="20" maxlength="20"  value='<s:property value="em.uname"/>' readonly></td> 
					<td  width="9%" class="input_label2"><div >部门：</div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="21%"><input name="em.detName" type="text" id="detName" size="20" maxlength="20"  value='<s:property value="em.detName"/>' readonly></td> 
					<td width="9%" class="input_label2"><div >项目组：</div></td>
      <td bgcolor="#FFFFFF">
     <input name="em.groupName" type="text" id="groupName" size="20" maxlength="20"  value='<s:property value="em.groupName"/>' readonly>
                  </td> 
				  </tr>
				  <tr>
                     <td width="10%" class="input_label2"><div >入职日期：</div></td>
                     <td width="24%" bgcolor="#FFFFFF">
                     <input name="em.entryDate" type="text" id="entryDate" size="24" maxlength="20"  value='<s:date name="em.entryDate" format="yyyy-MM-dd"/>'  readonly>
                     </td> 
                     <td width="13%" class="input_label2"><div >转正日期：</div></td>
                     <td width="23%" bgcolor="#FFFFFF" colspan="3"><input name="em.changeDate" type="text" id="changeDate" size="22" maxlength="20"  value='<s:date name="em.changeDate" format="yyyy-MM-dd"/>' readonly></td> 
                  </tr>
                  <tr> 
                     <td width="10%" class="input_label2"><div >岗位：</div></td>
                    <td bgcolor="#FFFFFF">
                      <div align="left">
                        <input name="em.position" type="text" id="position" size="24" maxlength="24" value='<s:property value="em.position"/>' readonly>
                    </div></td> 
                    <td class="input_label2">  <div >督导师：</div></td>
                    <td bgcolor="#FFFFFF"><input name="em.teacher" type="text" id="teacher" size="22" maxlength="20" value='<s:property value="em.teacher"/>' readonly></td>
					<td class="input_label2">学习状态：</td>
                    <td bgcolor="#FFFFFF">
                    	<input name="em.studyStatus" type="text" id="uname" size="20" maxlength="20" value='<s:property value="em.studyStatus"/>' readonly>
                    </td>
                  </tr>
                  <!-- 
                  <tr>
                    <td class="input_label2"><div align="center">笔试成绩</div></td>
                    <td bgcolor="#FFFFFF"><input name="em.blackScore" type="text" id="blackScore" size="24" maxlength="24"  value='<s:property value="em.blackScore==0.0?'':em.blackScore"/>' readonly></td>
                    <td class="input_label2"><div align="center">实操成绩</div></td>
                    <td bgcolor="#FFFFFF" colspan="3"><input name="em.whiteScore" type="text" id="whiteScore" size="22" maxlength="20"  value='<s:property value="em.whiteScore==0.0?'':em.whiteScore"/>' readonly></td>
                  </tr> -->
                    </table>
                </td> 
            </tr> 
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 