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
		 if(document.getElementById("ProjectName").value.trim()=="")
			{
				alert("请输入项目名称");
				return;
			}	
			 if(document.getElementById("ProjectType").value.trim()=="")
			{
				alert("请选择项目类型");
				return;
			}	
			 if(document.getElementById("ProjectManager").value.trim()=="")
			{
				alert("请输入项目经理");
				return;
			}	
			 if(document.getElementById("TestVer").value.trim()=="")
			{
				alert("请输入测试版本");
				return;
			}	
			 if(document.getElementById("VerType").value.trim()=="")
			{
				alert("请选择版本类型");
				return;
			}	
			 if(document.getElementById("TestStatus").value.trim()=="")
			{
				alert("请选择测试状态");
				return;
			}			
			if(document.getElementById("SumbitProcess").value.trim()=="")
			{
				alert("请选择提交进度");
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/TestRecord/TestRecordinfo!update.action"   method="post">
    <input type="hidden" name="testRecord.id" value='<s:property value="testRecord.id"/>'>
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="软件产品测试情况记录" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                      <br/>
                      <tr>
                        <td align="center" width="9%" bgcolor="#999999"><div align="center">项目名称<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF"  width="12%"><input name="testRecord.ProjectName" type="text" id="ProjectName" size="20" maxlength="50" value='<s:property value="testRecord.ProjectName"/>' readonly="readonly"></td>
                        <td align="center" width="8%" bgcolor="#999999"><div align="center">项目类型<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF" nowrap="nowrap" width="12%"><input name="testRecord.ProjectType" type="text" id="ProjectType" size="20" maxlength="10" value='<s:property value="testRecord.ProjectType"/>' readonly="readonly"></td>
                        <td align="center" width="8%" bgcolor="#999999"><div align="center">项目经理<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF" width="10%"><input name="testRecord.ProjectManager" type="text" id="ProjectManager" size="20" maxlength="50" value='<s:property value="testRecord.ProjectManager"/>' readonly="readonly"></td>
                      </tr>
                      <tr>
                        <td align="center"  bgcolor="#999999"><div align="center">测试版本<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF" nowrap="nowrap" ><input name="testRecord.TestVer" type="text" id="TestVer" size="20" maxlength="100" value='<s:property value="testRecord.TestVer"/>' readonly="readonly"></td>
                        <td align="center"    bgcolor="#999999"><div align="center">版本类型<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.VerType" type="text" id="VerType" size="20" maxlength="8" value='<s:property value="testRecord.VerType"/>' readonly="readonly"></td>
                        <td align="center" width="8%" bgcolor="#999999"><div align="center">测试状态<font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.TestStatus" type="text" id="TestStatus" size="20" maxlength="50" value='<s:property value="testRecord.TestStatus"/>' readonly="readonly"></td>
                      </tr>
                      <tr>
                        <td align="center"   bgcolor="#999999"><div align="center">计划提交日期<font color="#FF0000"></font><font color="#FF0000">*</font></div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.SumbitPlanDate" type="text" id="SumbitPlanDate"    value='<s:date name="testRecord.SumbitPlanDate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
                        <td align="center"  bgcolor="#999999"><div align="center">实际提交日期</div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.ActualSumbitDate" type="text" id="ActualSumbitDate"  value='<s:date name="testRecord.ActualSumbitDate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
                        <td align="center" bgcolor="#999999"><div align="center">提交进度<font color="#FF0000">*</font></div></td>
                        <td  bgcolor="#FFFFFF"><input name="testRecord.SumbitProcess" type="text" id="SumbitProcess" size="20" maxlength="50" value='<s:property value="testRecord.SumbitProcess"/>' readonly="readonly">                        </td>
                      </tr>
                      <tr>
                      <td align="center"   bgcolor="#999999"><div align="center">测试开始日期<font color="#FF0000"></font></div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.TestStartDate" type="text" id="TestStartDate"  value='<s:date name="testRecord.TestStartDate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
                        <td align="center"   bgcolor="#999999"><div align="center">测试完成日期<font color="#FF0000"></font></div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.TestFinishDate" type="text" id="TestFinishDate"  value='<s:date name="testRecord.TestFinishDate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
                      
                        <td align="center"  bgcolor="#999999"><div align="center">测试人员数</div></td>
                        <td  bgcolor="#FFFFFF"><input name="testRecord.TesterSum" type="text" id="TesterSum" size="20" maxlength="20" value='<s:property value="testRecord.TesterSum"/>' readonly="readonly">                        </td>
                      </tr>
                      <tr>
                       
                        <td align="center"  bgcolor="#999999"><div align="center">测试总工作量</div></td>
                        <td  bgcolor="#FFFFFF"><input name="testRecord.WorkLoad" type="text" id="WorkLoad" size="5" maxlength="10" value='<s:property value="testRecord.WorkLoad"/>' readonly="readonly">
                          (单位：人时)<font color="#FF0000">&nbsp;</font>
                          <div align="left"></div></td>
                           <td align="center"   bgcolor="#999999"><div align="center">测试人均工时<br>
                        </div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.TestTimeSum" type="text" id="TestTimeSum" size="5" maxlength="20" value='<s:property value="testRecord.TestTimeSum"/>' readonly="readonly">
                          (单位：时/人)<font color="#FF0000">&nbsp;</font></td>
                            <td align="center"  bgcolor="#999999"><div align="center">发现缺陷数</div></td>
                        <td bgcolor="#FFFFFF"><input name="testRecord.FindBugSum" type="text" id="FindBugSum" size="20" maxlength="20" value='<s:property value="testRecord.FindBugSum"/>' readonly="readonly"></td>
                      </tr>
                      <tr>
                        <td align="center" bgcolor="#999999"><div align="center">测试人员</div></td>
                        <td colspan="3" bgcolor="#FFFFFF"><input name="testRecord.Tester" type="text" id="Tester"  size="72" value='<s:property value="testRecord.Tester"/>' readonly="readonly"></td>
                         <td align="center"  bgcolor="#999999"><div align="center">OA流水号</div></td>
                        <td  bgcolor="#FFFFFF"><input name="testRecord.OaNum" type="text" id="OaNum" size="20"  maxlength="20" value='<s:property value="testRecord.OaNum"/>' readonly="readonly"></td>
                      </tr>
                        <tr>
                        <td align="center" bgcolor="#999999"><div align="center">测试范围与重点</div></td>
                        <td colspan="5" bgcolor="#FFFFFF"><textarea cols="86" rows="5" name="testRecord.Remark"
												id="Remark" readonly><s:property value="testRecord.Remark"/></textarea></td>
                      </tr>
                       <tr>
                        <td align="center" bgcolor="#999999"><div align="center">备注</div></td>
                        <td colspan="5" bgcolor="#FFFFFF"><textarea cols="86" rows="3" name="testRecord.Remark2"
												id="Remark2" readonly><s:property value="testRecord.Remark2"/></textarea></td>
                      </tr>
                    </table>
                </fieldset></td> 
            </tr> 
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
      <td align="left" valign="middle">&nbsp;*测试人均工时=测试总工作量/测试人员数</td>
        
    </tr>
		<tr>
			<td align="center"><input type="button" name="return" id="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 