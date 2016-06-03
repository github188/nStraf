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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/postremove/postremoveinfo!save.action" method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	</table>
<br/>
<table width="90%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="轮岗计划" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                  
                  <tr>
                    <td width="15%" bgcolor="#999999"><div align="center" >姓名</div></td>
    <td width="10%" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
  <input name="postremove.name" type="text" id="name" size="10"  maxlength="100" value='<s:property value="postremove.name"/>' readonly="readonly">
    	</label>
</td>
    <td width="10%" align="center"  bgcolor="#999999"><div align="center">提出日期</div></td>
    <td width="15%" bgcolor="#FFFFFF">
<label>
<input name="postremove.advencedate" type="text" id="advencedate" size="17" maxlength="100"  value='<s:date name="postremove.advencedate" format="yyyy-MM-dd"/>' readonly="readonly">
</label></td>
          <td width="10%" align="center"  bgcolor="#999999"><div align="center">轮岗部门</div></td>
                    <td width="15%" bgcolor="#FFFFFF">
                      <input name="postremove.groupname" type="text" id="groupname" size="18" maxlength="100" value='<s:property value="postremove.groupname"/>' readonly="readonly"></td>
<td align="center" width="10%" bgcolor="#999999"><div align="center">轮岗领域</div></td>
                    <td width="15%" bgcolor="#FFFFFF">
                      <input name="postremove.domain" type="text" id="domain" size="18" maxlength="100" value='<s:property value="postremove.domain"/>' readonly="readonly"></td>  
                  </tr> 
                  <tr> 
                   <td  align="center" bgcolor="#999999">轮岗计划时长</td>
<td  bgcolor="#FFFFFF">
              <input name="postremove.plantotaltime" type="text" id="plantotaltime" size="4" maxlength="100" value='<s:property value="postremove.plantotaltime"/>' readonly="readonly">
              个月</td>  
                    
<td  bgcolor="#999999"><div align="center">计划开始日期</div></td>
    <td  bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="postremove.planstartdate" type="text" id="planstartdate" size="17" maxlength="100" value='<s:date name="postremove.planstartdate" format="yyyy-MM-dd"/>' readonly="readonly">
</label></td>
                    <td   bgcolor="#999999"><div align="center">计划结束日期</div></td>
    <td  colspan="3" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="postremove.planfinishdate" type="text" id="planfinishdate" size="17" maxlength="100" value='<s:date name="postremove.planfinishdate" format="yyyy-MM-dd"/>' readonly="readonly">
</label></td>
                  </tr>
                   <tr>
                  	  <td align="center"  width="13%" bgcolor="#999999"><div align="center">轮岗目标</div></td> 
					<td colspan="7" bgcolor="#FFFFFF"> <textarea name="postremove.wishgut" id="wishgut" cols="88" rows="5" readonly><s:property value="postremove.wishgut"/></textarea></td>
                  </tr>
                          <tr> 
                    <td  align="center" bgcolor="#999999">轮岗实际时长</td>
<td  bgcolor="#FFFFFF">
              <input name="postremove.endtotaltime" type="text" id="endtotaltime" size="4" maxlength="100" value='<s:property value="postremove.endtotaltime"/>' readonly="readonly">
              个月</td>
<td  bgcolor="#999999"><div align="center">实际开始日期</div></td>
    <td  bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="postremove.endstartdate" type="text" id="endstartdate" size="14" maxlength="100"  value='<s:date name="postremove.endstartdate" format="yyyy-MM-dd"/>' readonly="readonly">
</label></td>
    <td bgcolor="#999999"><div align="center">实际结束日期</div></td>
    <td bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="postremove.endfinishdate" type="text" id="endfinishdate" size="14" maxlength="100" value='<s:date name="postremove.endfinishdate" format="yyyy-MM-dd"/>' readonly="readonly">
</label></td>

<td align="center"  bgcolor="#999999"><div align="center">状态</div></td>
                    <td  bgcolor="#FFFFFF">
                      <input name="postremove.status" type="text" id="status" size="18" maxlength="100" value='<s:property value="postremove.status"/>' readonly="readonly"></td>
                  </tr>
                  <tr>
                  	  <td align="center"  width="13%" bgcolor="#999999"><div align="center">自评</div></td> 
					<td colspan="7" bgcolor="#FFFFFF">
                  	  <textarea name="postremove.selfscore" id="selfscore" cols="88" rows="5" readonly><s:property value="postremove.selfscore"/></textarea></td>
                  </tr>
                                    <tr>
                  	  <td align="center"  width="13%" bgcolor="#999999"><div align="center">上级评价</div></td> 
					<td colspan="7" bgcolor="#FFFFFF">
                  	  <textarea name="postremove.headmanscore" id="headmanscore" cols="88" rows="5" readonly><s:property value="postremove.headmanscore"/></textarea>                    </td>                 </tr>
                  <tr>
                  	  <td align="center"  width="13%" bgcolor="#999999"><div align="center">上上级评价</div></td> 
					  <td colspan="7" bgcolor="#FFFFFF"><textarea name="postremove.managescore" id="managescore" cols="88" rows="5" readonly><s:property value="postremove.managescore"/></textarea></td>
                  </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 