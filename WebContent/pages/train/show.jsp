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
	
		 if(document.getElementById("courseName").value.trim()=="")
			{
				alert("请输入课程名称");
				return;
			}	
			 if(document.getElementById("category").value.trim()=="")
			{
				alert("请输入类别");
				return;
			}	
			 if(document.getElementById("teacher").value.trim()=="")
			{
				alert("请输入讲师");
				return;
			}	
			 if(document.getElementById("trainDate").value.trim()=="")
			{
				alert("请输入培训日期");
				return;
			}	
			 if(document.getElementById("trainHour").value.trim()=="")
			{
				alert("请输入培训时长");
				return;
			}	
			 if(document.getElementById("addr").value.trim()=="")
			{
				alert("请输入地点");
				return;
			}	
			 if(document.getElementById("student").value.trim()=="")
			{
				alert("请输入参训人员");
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/course/courseinfo!save.action"   method="post">
    	<input type="hidden" name="train.id"  value="<s:property value='train.id'/>">
<table width="80%" align="center" cellPadding="0" cellSpacing="0">
	</table>
<br/>
<table width="80%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    
                    <table width="95%" class="input_table">
                    <tr><td class="input_tablehead"><s:text name="培训课程" /></td></tr>
                   <tr>
                    <td  width="14%" class="input_label2"><div >课程名称：</div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="24%"><input name="train.courseName" type="text" id="courseName" value='<s:property value="train.courseName"/>' size="22" maxlength="24" readonly></td>
                     <td  width="10%" class="input_label2"><div >分类：</div></td>
                   <td width="18%" bgcolor="#FFFFFF">
                   <input name="train.category" type="text" id="category" size="16" maxlength="24" value='<s:property value="train.category"/>' readonly>
	             </td> 
                     <td  width="11%" class="input_label2"><div >讲师：</div></td>
                     <td width="23%" bgcolor="#FFFFFF"><input name="train.teacher" type="text" id="teacher" size="25" maxlength="20" value='<s:property value="train.teacher"/>' readonly></td> 
                  </tr>
                  <tr> 
                   <td width="14%"class="input_label2"><div >培训日期<font color="#FF0000"></font>：</div></td>
                    <td bgcolor="#FFFFFF"><input name="train.trainDate" type="text" id="trainDate"  value="<s:date name='train.trainDate' format='yyyy-MM-dd'/>" size="22" maxlength="100" readonly></td> 
                     <td width="10%" class="input_label2"><div >培训时长：</div></td>
<td bgcolor="#FFFFFF">
                    	<input name="train.trainHour" type="text" id="trainHour" size="16" maxlength="200" value='<s:property value="train.trainHour"/>' readonly>                             
                    </td> 
                    <td class="input_label2"><div >地点：</div></td>
                    <td bgcolor="#FFFFFF">
                    <input name="train.addr" type="text" id="addr" size="25" maxlength="200" value="<s:property value='train.addr'/>" readonly>
                    </td>
                  </tr>
                  <tr>
                    <td class="input_label2">培训时间：</td>
                    <td bgcolor="#FFFFFF">
                   <input name="train.start" type="text" id="start"  value='<s:property value="train.start"/>' size="8" readonly>----<input name="train.end" type="text" id="end"  value='<s:property value="train.end"/>' size="8" maxlength="100" readonly>
                    </td>
                    <td class="input_label2"><div >参训人员：</div></td>
                    <td bgcolor="#FFFFFF" colspan="3">
               	    <textarea name="train.student" id="student" cols="52" rows="3" readonly style="word-break:break-all;word-wrap:break-word;width:100%;"><s:property value='train.student'/></textarea>  </td>
                  </tr>
                  <tr>
                  	  <td  width="14%" class="input_label2"><div >备注：</div></td> 
					<td colspan="5" bgcolor="#FFFFFF" style="white-space:pre-wrap;word-break:break-all;word-wrap:break-word;width:100%;">
                  	  <textarea name="train.note" cols="91" rows="3" readonly ><s:property value='train.note'/></textarea>  </td>

                  </tr>
                  <tr>
                  <td  class="input_label2"  colspan="6">
                  <div style="float: left;">
                   <input type="checkbox" name="anon_flag" id="anon_flag" value="1" onclick="return false;"/><span>跨部门</span><input type='hidden' id="bound" name="train.bound" value="<s:property value='train.bound'/>"/></td>
                  </div>
                   </tr>
                    <script>
                      if(document.getElementById("bound").value == "跨部门")
						{
							document.getElementById("anon_flag").checked = true;
						}
						else
						{
							document.getElementById("anon_flag").checked = false;
						}
						</script>
                    </table>
                </td> 
            </tr> 
    </table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 