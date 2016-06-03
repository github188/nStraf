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
		 if(document.getElementById("finishPercent").value.trim()=="")
			{
				alert("请选择完成百分比");
				return;
			}	
			 if(document.getElementById("actualFinishDate").value.trim()=="")
			{
				alert("请输入实际完成日期");
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/ec/ecinfo!update.action"   method="post">
     <input name="ec.id" type="hidden" id="id" value='<s:property value="ec.id"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                    <table width="95%" class="input_table">
                   <tr>
                   		<td class="input_tablehead"><s:text name="课程信息" /></td>
                   </tr>
                   <tr>
                    <td width="11%" class="input_label2"><div align="center">姓名</div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="16%"><input name="ec.uname" type="text" class="input_readonly" id="uname" value='<s:property value="ec.uname"/>' size="12" maxlength="20" readonly></td>
                     <td  width="14%" class="input_label2"><div align="center">课程编号</div></td>
                     <td width="22%" bgcolor="#FFFFFF">
                     <input name="ec.cid" type="text" id="cid" size="20" maxlength="20"  class="input_readonly" value='<s:property value="ec.cid"/>' readonly>                     </td> 
                     
                     <td  width="16%" class="input_label2"><div align="center">课程名称</div></td>
                     <td width="21%" bgcolor="#FFFFFF"><input name="ec.courseName" type="text" id="courseName" size="20" maxlength="20" class="input_readonly"  value='<s:property value="ec.courseName"/>' readonly></td> 
                  </tr>
                  <tr> 
                   <td  width="11%" class="input_label2"><div align="center">类别</div></td>
      <td bgcolor="#FFFFFF">
            <input type="text" name="ec.category" value="<s:property value='ec.category'/>" class="input_readonly" readonly/>
            </td> 
                     <td width="14%" class="input_label2"><div align="center">优先级</div></td>
                    <td bgcolor="#FFFFFF">
                        <input type="text" name="ec.prioryLevel" value="<s:property value='ec.prioryLevel'/>" class="input_readonly" readonly/>
                     </td> 
                    <td class="input_label2">  <div align="center">掌握标准</div></td>
                    <td bgcolor="#FFFFFF">
                    	<input type="text" name="ec.graspStandard" value="<s:property value='ec.graspStandard'/>" class="input_readonly" readonly/>
                    </td>
                  </tr>
                  <tr>
                    <td class="input_label2">完成百分比<font color="#FF0000">*</font></td>
                    <td bgcolor="#FFFFFF">
                               <select name="ec.finishPercent"  id="finishPercent"  style="width:90px">
                                <option value="100%">100%</option>
                                <option value="95%">95%</option>
                                <option value="90%">90%</option>
                                <option value="85%">85%</option>
                                <option value="80%">80%</option>
                                <option value="75%">75%</option>
                                <option value="70%">70%</option>
                                <option value="65%">65%</option>
                                <option value="60%">60%</option>
                                <option value="55%">55%</option>
                                <option value="50%">50%</option>
                                <option value="45%">45%</option>
                                <option value="40%">40%</option>
                                <option value="35%">35%</option>
                                <option value="30%">30%</option>
                                <option value="25%">25%</option>
                                <option value="20%">20%</option>
                                <option value="15%">15%</option>
                                <option value="10%">10%</option>
                                <option value="5%">5%</option>
                                <option value="0%" selected="selected">0%</option>
                              </select>          
                     </td>
                     <script>
                     	$("#finishPercent").val("<s:property value='ec.finishPercent'/>");
                     </script>
                    <td class="input_label2"><div align="center">计划完成日期</div></td>
                    <td bgcolor="#FFFFFF"><input name="ec.planFinishDate" type="text" id="planFinishDate" size="21" maxlength="20"  class="MyInput"  value='<s:date name="ec.planFinishDate" format="yyyy-MM-dd" />' ></td>
                    <td class="input_label2" ><div align="center">实际完成日期<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="ec.actualFinishDate" type="text" id="actualFinishDate" size="17" maxlength="20" class="MyInput"  value='<s:date name="ec.actualFinishDate" format="yyyy-MM-dd"/>'></td>
                  </tr>
                  <tr>
                  	<td class="input_label2">
                    	<div align="center">完成效果<font color="#FF0000"></font></div>
                    </td>
                    <td bgcolor="#FFFFFF">
                    	 <s:select list="{'熟练掌握','基本掌握','有待提高'}" name="ec.finishEffect" value="ec.finishEffect" theme="simple" cssStyle="width:90px;"   headerKey="" headerValue="" id="category" disabled="true"></s:select> 
                   		<input type="hidden" name="ec.finishEffect" value="<s:property value='ec.finishEffect'/>"/>
                    </td>
                    <td bgcolor="#FFFFFF" colspan="4"></td>
                  </tr>
                    </table>
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