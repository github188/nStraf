<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript">

function closeModal(){
		if(!confirm('您确认关闭此页面吗？'))
	{
		return;				
	}
 	window.close();
}

function save(){
		var reg = new RegExp("/^-?\d+\.?\d{0,1}$/");
		var patten = /^-?\d+\.?\d{0,2}?$/;
		var yeartime = document.getElementsByName("yearholsTime");
		for(var i=0;i<yeartime.length;i++){
			if(yeartime[i].value==""){
				alert("部分年假时间未填");
				return false;
			}
			if(!patten.test(yeartime[i].value)){
				alert("年假时间应该为整数或2位小数");
				return false;
			}
		}
		var deferredTime = document.getElementsByName("deferredTime");
		for(var i=0;i<deferredTime.length;i++){
			if(deferredTime[i].value==""){
				alert("部分可调休时间未填");
				return false;
			}
			if(!patten.test(deferredTime[i].value)){
				alert("调休时间应该为整数或2位小数");
				return false;
			}
		}
		window.returnValue=true;
		//$("#oks").attr("disabled","true");
		modifyForm.submit();
	}
	
	function yearadds(){
		var yeartime = document.getElementsByName("yearholsTime");
		for(var i=0;i<yeartime.length;i++){
			var temp = yeartime[i].value;
			if(temp<9999)
				document.getElementById("yearholsTime"+i).value=Number(temp)+Number(1);
		}
	}
	
	function yearminuss(){
		var yeartime = document.getElementsByName("yearholsTime");
		for(var i=0;i<yeartime.length;i++){
			var temp = yeartime[i].value;
			if(temp>0)
				document.getElementById("yearholsTime"+i).value=Number(temp)-Number(1);
		}
	}
	
	function defreeadds(){
		var yeartime = document.getElementsByName("deferredTime");
		for(var i=0;i<yeartime.length;i++){
			var temp = yeartime[i].value;
			if(temp<9999)
				document.getElementById("deferredTime"+i).value=Number(temp)+Number(1);
		}
	}
	
	function defreeminuss(){
		var yeartime = document.getElementsByName("deferredTime");
		for(var i=0;i<yeartime.length;i++){
			var temp = yeartime[i].value;
			if(temp>0)
				document.getElementById("deferredTime"+i).value=Number(temp)-Number(1);
		}
	}
	
</script>

<body id="bodyid"  leftmargin="0" topmargin="10" >

<form name="modifyForm" action="<%=request.getContextPath() %>/pages/hols/holsInfo!modify.action" method="post">
<table width="80%" border="1" cellspacing="0" cellpadding="3" align="center">
<caption>修改人员</caption>
<tr>
	<td>
		姓名</td>
	<td>年假
		<input type="button" id="yearadd" onclick="yearadds();" value=" + ">
		<input type="button" id="yearminus" onclick="yearminuss();" value=" - ">小时
		</td>
	<td>调休时间
	<input type="button" id="defreeadd" onclick="defreeadds();" value=" + ">
	<input type="button" id="defreeminus" onclick="defreeminuss();" value=" - ">小时</td>
</tr>
<s:iterator value="#request.userhols" id="user" status="s">
<tr>
	<td>
		<input type="hidden" name="userid" value="<s:property value='#request.user.userid'/>">
		<input type="hidden" name="holsid" value="<s:property value='#request.user.id'/>">
		<s:property value="#request.user.username"/></td>
	<td>
		<input type="text" id="yearholsTime<s:property value='#s.index'/>" maxlength="7" name="yearholsTime" value="<s:property value='#request.user.yearholsTime'/>" 
		onblur="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeyup="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeypress="this.value=this.value.replace(/[^\d+\.]/g,'')">
		</td>
	<td>
		<input type="text" id="deferredTime<s:property value='#s.index'/>" maxlength="7" name="deferredTime" value="<s:property value='#request.user.deferredTime'/>" 
			onblur="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeyup="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeypress="this.value=this.value.replace(/[^\d+\.]/g,'')" >
	</td>
		
	</tr>
</s:iterator>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
 <tr>
  <td align="center">
   <input type="button" name="ok" id="oks" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
   <input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
  </td>
 </tr>
</table>
</form>
</body>
</html>
