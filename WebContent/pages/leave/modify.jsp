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
	var whole;
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
				document.getElementById("startTime").focus();
				alert("请填写开始时间");
				return;
			}
			if(document.getElementById("endTime").value=="")
			{
				document.getElementById("endTime").focus();
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
					alert("请假时间大于可休时间,请修改后再确认");
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
				 if(data.id==1){
					 alert("时间选择不正确,开始时间小于大于结束时间");
					 return;
				 }
				document.getElementById("sumtime").value=data.id;
				whole = document.getElementById("whole").value;
			 },
			 error:function(e){
				 alert(e);
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
					 document.getElementById("whole").value=whole;
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
					 document.getElementById("whole").value=whole;
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
	
	function ShowDateTime(ctrlobj)
	{ 
	  resettime();
	  ShowCalendar(ctrlobj,1);
	}
	
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
		//	for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementById("reason").value
						.trim();
				if (tmpStr.length > 200) {
					document.getElementById("reason").value = tmpStr
							.substr(0, 200);
					alert("你输入的字数超过200个了");
					document.getElementById("reason").focus();
				}
		//	}
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
<title>修改页面</title>
</head>

<body id="bodyid">
	<form name="leaveForm" action="<%=request.getContextPath() %>/pages/leave/leaveInfo!save.action"   method="post">
	<input type="hidden" name="id" value="<s:property value='#request.leave.id'/>">
<table width="550" class="input_table" style="word-wrap: break-word; word-break: break-all;">
<tr>
	<td class="input_tablehead">修改请假信息</td>
</tr>
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.department"/></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.leave.deptName"/> 
    </td>
  </tr>
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.group"/></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.leave.grpName"/>    	
    </td>
  </tr>
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.username"/></div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.leave.username"/>    	
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.type"/>
    <font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
      <select name="leave.type"  id="type" onchange="gethols()">
      	<option><s:text name="notify.option"></s:text></option>
      	<s:iterator value="#request.typeMap">
      		<option value="<s:property value='value'/>" <s:if test='value.trim()==#request.leave.type'>selected</s:if>><s:property value="value"/></option>
      	</s:iterator>
      </select>
      <input type="hidden" id="whole" value="<s:property value='#request.time'/>">
      <span id="holsspan">
      	<s:if test='#request.leave.type.equals("年假")'>可休年假为：<s:property value='#request.time'/>小时</s:if>
      	<s:if test='#request.leave.type.equals("调休")'>可调休时间为：<s:property value='#request.time'/>小时</s:if>
      </span>
      </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.reason"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    <textarea maxlength="200" name="leave.reason" class="text_area" id="reason" cols="50" rows="5" style="width: 100%"><s:property value="#request.leave.reason"/></textarea></td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.start"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<input  name="leave.startTime" type="text"
			id="startTime" size="20" maxlength="12" class="dateTimeInput" onclick="resettime();"
			value="<s:date name='leave.startTime' format='yyyy-MM-dd HH:mm:ss'/>"
			>
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.end"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<input  name="leave.endTime" type="text"
			id="endTime" size="20" maxlength="12" class="dateTimeInput"
			value="<s:date name='leave.endTime' format='yyyy-MM-dd HH:mm:ss'/>" />
			<input type="button" id="calculate" value="点击计算请假时间" onclick="calculates();">
    </td>
  </tr>
     <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.timelong"/></div></td>
    <td style="background-color: #FFFFFF">
    	<input type="text" name="leave.sumtime" id="sumtime" readonly="readonly" value="<s:property value='#request.leave.sumtime'/>">
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.status"/></div></td>
    <td style="background-color: #FFFFFF">
    	<s:property value="#request.leave.status"/>
    	<input type="hidden" name="leave.status" value="<s:property value='#request.leave.status'/>" readonly="readonly">
    </td>
  </tr>
   <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.approver"/><font color="#FF0000">*</font></div></td>
    <td style="background-color: #FFFFFF">
    	<select name="leave.approver" id="approver">
    		<option value="">&nbsp;<s:text name="notify.option"></s:text>&nbsp;</option>
    		<s:iterator value="#request.approverlist" var="list">
    			<option value="<s:property value='userid'/>" <s:if test='userid==#request.leave.approver'>selected</s:if>><s:property value="username"/> </option>
    		</s:iterator>
    	</select>
    </td>
  </tr>
  
  <s:if test="#request.record.length()>0">
  
   <tr height="25px">
    <td class="input_label2"><div ><s:text name="notify.approval.record"/></div></td>
    <td style="background-color: #FFFFFF">
    	<textarea name="record" class="text_area" cols="50" rows="10" readonly="readonly"  style="width: 100%;"><s:property value="#request.record"/></textarea>
    </td></tr>
    
    </s:if>
    
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
