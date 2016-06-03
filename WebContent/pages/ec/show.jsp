<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>certification query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script language='JavaScript' src="../../js/jquery-1.11.0.js"></script>
	<script type="text/javascript" >
	function save(){
	
		 if(document.getElementById("category").value.trim()=="")
			{
				alert("请选择类别");
				return;
			}	
			 if(document.getElementById("prioryLevel").value.trim()=="")
			{
				alert("请选择优先级");
				return;
			}	
			 if(document.getElementById("graspStandard").value.trim()=="")
			{
				alert("请选择掌握标准");
				return;
			}	
			 if(document.getElementById("planFinishDate").value.trim()=="")
			{
				alert("请输入计划完成日期");
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
                    <tr><td class="input_tablehead"><s:text name="课程信息" /></td></tr>
                   <tr>
                    <td  width="11%" class="input_label2"><div >姓名：</div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="16%"><input name="ec.uname" type="text" id="uname" value='<s:property value="ec.uname"/>' size="20" maxlength="20" readonly></td>
                     <td   width="14%" class="input_label2"><div >课程编号：</div></td>
                     <td width="22%" bgcolor="#FFFFFF">
                     <input name="ec.cid" type="text" id="cid" size="20" maxlength="20" value='<s:property value="ec.cid"/>'readonly >                     </td> 
                     
                     <td  width="16%" class="input_label2"><div >课程名称：</div></td>
                     <td width="21%" bgcolor="#FFFFFF"><input name="ec.courseName" type="text" id="courseName" size="20" maxlength="20"  value='<s:property value="ec.courseName"/>' readonly></td> 
                  </tr>
                  <tr> 
                   <td  width="11%" class="input_label2"><div >类别：</div></td>
<td bgcolor="#FFFFFF">
                     <input name="uname" type="text" id="uname2" value='<s:property value="ec.category"/>' size="20" maxlength="20" readonly></td> 
                     <td width="14%" class="input_label2"><div >优先级：</div></td>
                    <td bgcolor="#FFFFFF">
                      <input name="uname2" type="text" id="uname3" value='<s:property value="ec.prioryLevel"/>' size="20" maxlength="20" readonly></td> 
                    <td class="input_label2">  <div >掌握标准：</div></td>
                    <td bgcolor="#FFFFFF">
                 
                        <input name="uname3" type="text" id="uname4" value='<s:property value="ec.graspStandard"/>' size="20" maxlength="20" readonly></td>
                  </tr>
                  <tr>
                    <td class="input_label2">完成百分比：</td>
                    <td bgcolor="#FFFFFF"><input name="uname4" type="text" id="uname5" value='<s:property value="ec.finishPercent"/>' size="20" maxlength="20" readonly></td>
                    <td class="input_label2"><div >计划完成日期：</div></td>
                    <td bgcolor="#FFFFFF">
                    <input name="ec.planFinishDate" type="text" id="planFinishDate" size="20" maxlength="20"  value='<s:date name="ec.planFinishDate" format="yyyy-MM-dd"/>'  readonly>
                    </td>
                    <td class="input_label2"><div >实际完成日期:<font color="#FF0000"></font></div></td>
                    <td bgcolor="#FFFFFF">
                    <input name="ec.actualFinishDate"  type="text" id="actualFinishDate" size="20" maxlength="20"  value='<s:date 						                              name="ec.actualFinishDate" format="yyyy-MM-dd"/>' readonly>
                    </td>
                  </tr>
                  <tr>
                  	<td class="input_label2">
                    	<div >完成效果:<font color="#FF0000"></font></div>
                    </td>
                    <td bgcolor="#FFFFFF">
                         <input name="uname5" type="text" id="uname6" value='<s:property value="ec.finishEffect"/>' size="20" maxlength="20" readonly></td>
                          <td bgcolor="#FFFFFF" colspan="4"></td>
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