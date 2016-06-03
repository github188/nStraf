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
		 if(document.getElementById("start").value.trim()=="")
			{
				alert("请输入开始日期");
				return;
			}	
			 if(document.getElementById("end").value.trim()=="")
			{
				alert("请输入结束日期");
				return;
			}	
			 if(document.getElementById("tripMan").value.trim()=="")
			{
				alert("请输入出差人");
				return;
			}	
			 if(document.getElementById("destination").value.trim()=="")
			{
				alert("请输入出差地址");
				return;
			}	
			 if(document.getElementById("oa_seqno").value.trim()=="")
			{
				alert("请输入OA流水号");
				return;
			}	
			 if(document.getElementById("prjName").value.trim()=="")
			{
				alert("请输入项目名称");
				return;
			}			
			if(document.getElementById("customer").value.trim()=="")
			{
				alert("请输入客户名称");
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/trip/tripinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                    
                    <table width="95%" class="input_table">
                    <tr><td class="input_tablehead"><s:text name="课程信息" /></td></tr>
                   <tr>
                    <td width="16%" class="input_label2"><div align="center">课程名称<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="21%">
                    	<input name="course.courseName" type="text" id="courseName" size="24" maxlength="24" value="<s:property value='course.courseName'/>" readonly>
                    </td>
                     <td   width="9%" class="input_label2"><div align="center">分类<font color="#FF0000">*</font></div></td>
                     <td width="23%" bgcolor="#FFFFFF"><input name="course.category" type="text" id="category" size="24" maxlength="24" value="<s:property value='course.category'/>" readonly>
                     </td> 
                        <td  width="11%" class="input_label2"><div align="center">讲师<font color="#FF0000">*</font></div></td>
                     <td width="20%" bgcolor="#FFFFFF"><input name="course.teacher" type="text" id="teacher" size="20" maxlength="20" value='<s:property value="course.teacher"/>' readonly></td> 
                  </tr>
                   <tr> 
                   <td  width="16%" class="input_label2"><div align="center">资料名称<font color="#FF0000"></font><font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="course.resourceName" type="text" id="resourceName" size="24" maxlength="100" value='<s:property value="course.resourceName"/>' readonly></td> 
                     <td  width="9%" class="input_label2"><div align="center">链接地址<font color="#FF0000">*</font></div></td>
                    <td colspan="3" bgcolor="#FFFFFF"><input name="course.path" type="text" id="path" size="63" maxlength="200" value='<s:property value="course.path"/>' readonly>                      <div align="center"></div>               </td> 
                    </tr>
                  <tr>
                  	  <td width="16%" class="input_label2"><div align="center">课程简介<font color="#FF0000"></font></div></td> 
					<td colspan="5" bgcolor="#FFFFFF">
                  	  <textarea name="course.summary" cols="90" rows="3" readonly><s:property value="course.summary"/></textarea>                  
                     </td>
                  </tr>
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