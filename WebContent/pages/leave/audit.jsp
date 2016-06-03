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
	<script type="text/javascript" src="../../js/common.js"></script>
	<script type="text/javascript" >
	String.prototype.trim = function()
	{
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function save(){
		if(document.getElementById("result").value=="")
		{
			alert("请填写审批结果");
			document.getElementById("result").focus();
			return;
		}else if(document.getElementById("result").value=="1"){
			if(document.getElementById("option").value.trim()==""){
				alert("请填写审批不通过意见");
				document.getElementById("result").focus();
				return;
			}
		}
/*		if(document.getElementById("option").value.length>100) {
			alert("审批意见不能超过100个汉字！");
			document.getElementById("option").focus();
			return;
		}
*/		window.returnValue=true;
		$("#ok").attr("disabled","disabled");
		leaveForm.submit();
	}
			
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	
	$(function(){
		$("#result").change(function(){
			if($(this).val()==0){
				$("#option").html("同意");
			}else{
				$("#option").html("");
			}
		});
	});
	
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
		//	for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementById("option").value
						.trim();
				if (tmpStr.length > 200) {
					document.getElementById("option").value = tmpStr
							.substr(0, 200);
					alert("你输入的字数超过200个了");
					document.getElementById("option").focus();
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
<title>审批页面</title>
</head>

<body id="bodyid">
	<form name="leaveForm" action="<%=request.getContextPath() %>/pages/leave/leaveInfo!audit.action"   method="post">
	<input type="hidden" name="id" value="<s:property value='#request.leave.id'/>">
<table width="550"  border="1" style="word-wrap: break-word; word-break: break-all;">
<tr height="25px">
    <td width="25%" bgcolor="#999999"><div align="right"><s:text name="user.hols.department"/></div></td>
    <td>
		<s:property value="#request.leave.deptName"/> 
    </td>
  </tr>
<tr height="25px">
    <td width="25%" bgcolor="#999999"><div align="right"><s:text name="user.hols.group"/></div></td>
    <td>
		<s:property value="#request.leave.grpName"/>    	
    </td>
  </tr>
<tr height="25px">
    <td width="25%" bgcolor="#999999"><div align="right"><s:text name="user.hols.username"/></div></td>
    <td >
		<s:property value="#request.leave.username"/>    	
    </td>
  </tr>
  <tr height="25px">
    <td  bgcolor="#999999"><div align="right"><s:text name="user.leave.type"/></div></td>
    <td >
    	<s:property value="#request.leave.type"/>
  </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.reason"/></div></td>
    <td >
    <textarea name="leave.reason" id="reason" cols="50" rows="5" style="width: 100%" readonly="readonly"><s:property value="#request.leave.reason"/></textarea></td>
  </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.start"/></div></td>
    <td >
    	<s:date name="#request.leave.startTime" format="yyyy-MM-dd HH:mm:ss"/>
    </td>
  </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.end"/></div></td>
    <td >
    	<s:date name="#request.leave.endTime" format="yyyy-MM-dd HH:mm:ss"/>
    </td>
  </tr>
     <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.timelong"/></div></td>
    <td >
    	<input type="text" name="leave.sumtime" id="sumtime" readonly="readonly" value="<s:property value='#request.leave.sumtime'/>">
    </td>
  </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.status"/></div></td>
    <td >
    	<s:property value='#request.leave.status'/>
    </td>
  </tr>
   <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="user.leave.approver"/></div></td>
    <td >
    	<s:property value="#request.leave.approverName"/>
    </td>
  </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.result"/></div></td>
	    <td >
	    	<select name="result" id="result">
	    		<option value="">请选择</option>
	    		<option value="0">通过</option>
	    		<option value="1">不通过</option>
	    	</select>
	    </td>
    </tr>
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.option"/></div></td>
    <td >
    	<textarea id="option" name="option" cols="50" rows="5" style="width: 100%;" onblur="limitLen(200,this)"></textarea>
    </td></tr>
    
  <s:if test="#request.record.length()>0">  
    
  <tr height="25px">
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.record"/></div></td>
    <td >
    	<textarea name="record" cols="50" rows="10" readonly="readonly" maxlength="200"  style="width: 100%;"><s:property value="#request.record"/></textarea>
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
