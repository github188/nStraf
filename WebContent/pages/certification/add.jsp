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
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" >
	function save(){
		if(document.getElementById("cerName").value.trim()=="")
		{
			alert("请输入证书名称");
			return;
		}
		 if(document.getElementById("certificationNo").value.trim()=="")
			{
				alert("请输入认定编号");
				return;
			}			
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/certification/cerinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="证书信息" /></legend>
                    <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                        
                          <td align="center" width="10%" bgcolor="#999999"><div align="center" style="width: 63px">证书名称<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><input name="cer.cerName" type="text" id="cerName" size="30" maxlength="30" value=''>&nbsp;</td> 
                          <td align="center" width="10%" bgcolor="#999999"><div align="center" style="width: 63px">认定编号<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><input name="cer.certificationNo" type="text" id="certificationNo" size="30" maxlength="30" value=''>&nbsp;</td> 
                          <td align="center" width="10%" bgcolor="#999999"><div align="center" style="width: 63px">产品名称&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF" width="30%"><input name="cer.productName" type="text" id="productName" size="50" maxlength="50" value=''>&nbsp;</td>
                      </tr>
                      <tr>
                      <td align="center" width="10%" bgcolor="#999999"><div align="center">有效日期&nbsp;&nbsp;</div></td>  
                      <td bgcolor="#FFFFFF"  width="10%">
                      <input name="cer.validTime" type="text" class="MyInput" id="validTime" maxlength="30" size="28"  isSel="true" isDate="true"  
                     onFocus="ShowDate(this)" dofun="ShowDate(this)" size="18" value='<s:date name='cer.validTime' format="yyyy-MM-dd"/>'>&nbsp;</td> 
                      <td align="center" width="10%" bgcolor="#999999"><div align="center">颁发日期&nbsp;</div></td>  
                      <td bgcolor="#FFFFFF" nowrap="nowrap" width="10%">
                  <input name="cer.awardDate" type="text" class="MyInput"  id=" awardDate" maxlength="30" size="28" isSel="true" isDate="true"
		         onFocus="ShowDate(this)" dofun="ShowDate(this)" size="18"  value='<s:date name="cer.awardDate" format="yyyy-MM-dd"/>'>						   
		             </td>
                     <td align="center" width="10%" bgcolor="#999999"><div align="center">颁发组织&nbsp;</div></td>  
                    <td bgcolor="#FFFFFF" width="30%"><input name="cer.awardOgnization" type="text" id="version" size="50" maxlength="50" value=''>&nbsp;</td> 
                  </tr> 
                   <tr>
                    <td align="center" width="10%" bgcolor="#999999"><div align="center">申请日期&nbsp;&nbsp;</div></td>
                     <td bgcolor="#FFFFFF" nowrap="nowrap" width="10%">
                  <input name="cer.applyDate" type="text" class="MyInput"  id=" applyDate" maxlength="30" size="28" isSel="true" isDate="true"
		onFocus="ShowDate(this)" dofun="ShowDate(this)" size="18"  value='<s:date name="cer.applyDate" format="yyyy-MM-dd"/>'>						   
		</td>
                        <td align="center"   width="10%" bgcolor="#999999"><div align="center">申请部门&nbsp;&nbsp;</div></td>
                        <td bgcolor="#FFFFFF"><input name="cer.applyDepartment" type="text" id="applyDepartment" size="30" maxlength="30" value=''>&nbsp;</td> 
                        <td align="center"   width="10%" bgcolor="#999999"><div align="center">申请人&nbsp;&nbsp;</div></td>
                        <td bgcolor="#FFFFFF"><input name="cer.applyMan" type="text" id="applyMan" size="50" maxlength="50" value=''>&nbsp;</td> 
                  </tr>
                  <tr> 
                   <td align="center"  width="10%" bgcolor="#999999"><div align="center">证书办理人<font color="#FF0000"></font></div></td>
                     <td bgcolor="#FFFFFF"><input name="cer.transactionMan" type="text" id="transactionMan" size="30" maxlength="30" value=''>&nbsp;</td> 
                     <td align="center"  width="10%" bgcolor="#999999"><div align="center">归档部门&nbsp;&nbsp;</div></td>
                        <td bgcolor="#FFFFFF"><input name="cer.fileDepartment" type="text" id="fileDepartmente" size="30" maxlength="30" value=''>&nbsp;</td> 
                      <td align="center" width="10%" bgcolor="#999999"><div align="center">备注&nbsp;&nbsp;</div></td>
                       <td  bgcolor="#FFFFFF"><input name="cer.remark" type="text" id="remark" size="50" maxlength="50" value=''>&nbsp;</td>     
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
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 