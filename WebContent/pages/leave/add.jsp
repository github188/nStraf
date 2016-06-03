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

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" >
	var whole = 0;
	function save(){
			 if(document.getElementById("type").value=="")
			{
				alert("请填写请假类型");
				document.getElementById("type").focus();
				return;
			}
			if(document.getElementById("reason").value=="")
			{
				alert("请填写请假原因");
				document.getElementById("reason").focus();
				return;
			}
/*			if(document.getElementById("reason").value.length>200){
				alert("请假原因字数应限制在200字以内");
				document.getElementById("reason").focus();
				return;
			}
*/			if(document.getElementById("startTime").value=="")
			{
				alert("请填写开始时间");
				return;
			}
			if(document.getElementById("endTime").value=="")
			{
				alert("请选择结束时间");
				return;
			}
			if(document.getElementById("approver").value=="")
			{
				document.getElementById("approver").focus();
				alert("请选择审批人");
				return;
			}
			if(document.getElementById("sumtime").value=="")
			{
				document.getElementById("sumtime").focus();
				alert("请先计算请假时长");
				return;
			}
			if(document.getElementById("sumtime").value=="0.0"){
				document.getElementById("sumtime").focus();
				alert("请假时间不能为0，请重新输入请假时间");
				return;
			}
			var time = document.getElementById("sumtime").value;
			var type = document.getElementById("type").value;
			if(type=="年假" || type=="调休"){
				if(Number(time)>Number(whole)){
					alert("请假时间大于可调休时间,请修改后再确认或请事假");
					return;
				}
			}
			window.returnValue=true;
			$("#ok").attr("disabled","disabled");
			leaveForm.submit();
		}
	
	function calculates(){
		var start = document.getElementById("startTime").value;
		var end = document.getElementById("endTime").value;
		if(start=="" || end==""){
			alert("请选择开始时间或结束时间");
			return false;
		}
		var actionUrl = "<%=request.getContextPath()%>/pages/leave/leaveInfo!calculate.action?method=0&startTime="+start+"&endTime="+end;
		$.ajax({
		     type:"post",
		     url:actionUrl,
			 dataType:"json",
			 cache: false,
			 async:true,
			 success:function(data){
				 if(data.id==-1){
					 alert("时间选择不正确,开始时间大于等于结束时间");
					 return;
				 }
				document.getElementById("sumtime").value=data.id;
			 },
			 error:function(xhr, textStatus, er) {
				 alert("textStatus:" + textStatus)
				 alert("xhr:" + xhr)
				 alert("er:" + er);
			 }
		   });  
	}
	
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	
	function resettime(){
		document.getElementById("sumtime").value="";
	}
	
	function gethols(){
		var type = document.getElementById("type").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/leave/leaveInfo!calculate.action";
		if(type=="年假"){
			actionUrl+="?method=1";
			$.ajax({
			     type:"post",
			     url:actionUrl,
				 dataType:"json",
				 cache: false,
				 async:true,
				 success:function(data){
					 whole = data.id;
					 $("#holsspan").html("可休年假："+whole+"小时");
				 },
				 error:function(e){
					 alert(e);
				 }
			   });  
		}
		else if(type=="调休"){
			actionUrl+="?method=2";
			$.ajax({
			     type:"post",
			     url:actionUrl,
				 dataType:"json",
				 cache: false,
				 async:true,
				 success:function(data){
					 whole = data.id;
					 $("#holsspan").html("可调休假："+whole+"小时");
				 },
				 error:function(e){
					 alert(e);
				 }
			   });  
		}
		else{
			$("#holsspan").html("");
		}
	}
	
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 200) {
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 200);
					alert("你输入的字数超过200个了");
					document.getElementsByTagName("textarea")[i].focus();
				}
			}
		}
	}

	function listenKey() {
		if (document.addEventListener) {
			document.addEventListener("keyup", getKey, false);
		} else if (document.attachEvent) {
			document.attachEvent("onkeyup", getKey);
		} else {
			document.onkeyup = getKey;
		}
	}
	listenKey();
	</script>
<title>新增页面</title>
</head>

<body id="bodyid">
	<form name="leaveForm" action="<%=request.getContextPath() %>/pages/leave/leaveInfo!save.action"   method="post">
<table width="550" height="232" class="input_table">
<tr>
	<td class="input_tablehead">新增请假信息</td>
</tr>
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.department"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.deptname"/> 
		<input name="leave.deptName" type="hidden" value="<s:property value='#request.deptname'/> ">   	
    </td>
  </tr>
<%--  <tr height="25px">
    <td width="25%" class="input_label2"><div >项目组<font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.grpname"/>    	
		<input name="leave.grpName" type="hidden" value="<s:property value='#request.grpname'/> ">   	
    </td>
  </tr> --%> 
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.username"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.username"/>    	
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.type"/>
    <font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
      <label></label>  
      <select name="leave.type" style="width:150" id="type" onchange="gethols()">
      	<option><s:text name="notify.option"></s:text></option>
      	<s:iterator value="#request.typeMap">
      		<option value="<s:property value='value'/>"><s:property value="value"/></option>
      	</s:iterator>
     </select>
	      	<span id="holsspan"></span>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.reason"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    <textarea name="leave.reason" class="text_area" id="reason" cols="50" rows="5" maxlength="200"  style="width: 100%"></textarea></td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.start"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<input  name="leave.startTime" type="text"
			id="startTime" size="20" maxlength="12" class="dateTimeInput" />
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div><s:text name="user.leave.end"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<input  name="leave.endTime" type="text"
			id="endTime" size="20" maxlength="12" class="dateTimeInput" />
			<input type="button" id="calculate" value="点击计算请假时间" onclick="calculates();">
    </td>
  </tr>
     <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.timelong"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<input type="text" name="leave.sumtime" id="sumtime" readonly="readonly">
    </td>
  </tr>
     <tr height="25px">
    <td class="input_label2"><div >状态</div></td>
    <td style="background-color: #FFFFFF">
    	新增
    </td>
  </tr>
  <input type="hidden" name="leave.status" value="新增" readonly="readonly">
   <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.approver"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<select name="leave.approver" id="approver">
    		<option value="">&nbsp;<s:text name="notify.option"></s:text>&nbsp;</option>
    		<s:iterator value="#request.approverlist" var="list">
    			<option value="<s:property value='userid'/>"><s:property value="username"/> </option>
    		</s:iterator>
    	</select>
    </td>
  </tr>
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
    </form>
</body>
</html>
