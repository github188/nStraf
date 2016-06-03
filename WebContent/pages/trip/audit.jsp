<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	function save() {
		var startdate = document.getElementById("startdate").value.trim();
		var enddate = document.getElementById("enddate").value.trim();
//		var prjname = document.getElementById("prjname").value.trim();
		var tripcity = document.getElementById("tripcity").value.trim();
		var clientname = document.getElementById("clientname").value.trim();
		var taskdesc = document.getElementById("taskdesc").value.trim();
		var auditing_man = document.getElementById("auditing_man").value.trim();
		var result = document.getElementById("result").value.trim();
		var content = document.getElementById("content").value.trim();
		if(startdate==""){
			alert("开始日期不能为空");
			return;
		}
		if(enddate==""){
			alert("结束日期不能为空");
			return;
		}
		if(!compareDate(startdate,enddate)){
			return;
		}
//		if(prjname==""){
//			alert("项目名称不能为空");
//			return;
//		}
		if(tripcity==""){
			alert("出差地不能为空");
			return;
		}
		if(clientname==""){
			alert("客户名称不能为空");
			return;
		}
		if(taskdesc==""){
			alert("任务描述不能为空");
			return;
		}
		if(auditing_man==""){
			alert("审核人不能为空");
			return;
		}
		if(result==""){
			alert("审核意见不能为空");
			return;
		}
		if(content==""){
			alert("审核内容不能为空");
			return;
		}
		window.returnValue = true;
		reportInfoForm.submit();
	}
	function compareDate(startDate,endDate){
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		if (startDate.length > 0 && endDate.length > 0) {
			var v = re.test(endDate);
			var a = re1.test(startDate);
			if (!v || !a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			if (!DateValidate(startDate,endDate)) {
				alert('结束日期不能小于等于开始日期，请重新输入！');
				return false;
			}
		} else if (startDate.length > 0 && endDate.length == 0) {
			var a = re1.test(startDate);
			if (!a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		} else if (startDate.length == 0 && endDate.length > 0) {
			var v = re.test(endDate);
			if (!v) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		}
		return true;
	}

	function DateValidate(begin, end) {
		var Require = /.+/;
		var flag = true;
		if (Require.test(begin) && Require.test(end)) {
			var beginStr = begin.split("-");
			var endStr = end.split("-");
			if (parseInt(beginStr[0], 10) > parseInt(endStr[0], 10)) {
				flag = false;
			} else if (parseInt(beginStr[0], 10) == parseInt(endStr[0], 10)) {
				if (parseInt(beginStr[1], 10) > parseInt(endStr[1], 10)) {
					flag = false;
				} else if (parseInt(beginStr[1], 10) == parseInt(endStr[1], 10)) {
					if (parseInt(beginStr[2], 10) > parseInt(endStr[2], 10)) {
						flag = false;
					}
				}
			}
			if(begin==end){
				flag = false;
			}
		}
		return flag;
	}
	function dateDiff(date1, date2){ 
	    date1 = stringToTime(date1); 
	    date2 = stringToTime(date2); 
	    var between = (date1 - date2) / 1000;//结果是秒 
	    var day1=between/(24*3600);
		var hour1=between%(24*3600)/3600;
		var minute1=between%3600/60;
		var second1=between%60/60;
		return day1;
		//return ""+day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒";
	}
	function stringToTime(string){ 
	    var f = string.split(' ', 2); 
	    var d = (f[0] ? f[0] : '').split('-', 3); 
	    var t = (f[1] ? f[1] : '').split(':', 3); 
	    return (new Date( 
	    parseInt(d[0], 10) || null, 
	    (parseInt(d[1], 10) || 1)-1, 
	    parseInt(d[2], 10) || null, 
	    parseInt(t[0], 10) || null, 
	    parseInt(t[1], 10) || null, 
	    parseInt(t[2], 10) || null 
	    )).getTime(); 

	} 
	function getTimeLong(){
		var startdate = document.getElementById("startdate").value.trim()+" 00:00:00";
		var enddate = document.getElementById("enddate").value.trim()+" 23:59:59";
		if(!compareDate(document.getElementById("startdate").value.trim(),document.getElementById("enddate").value.trim())){
			return;
		}
		/*var day = parseInt(dateDiff(enddate,startdate));
		var count = day/30;
		var restday = 0;
		for(var i=0;i<=count;i++){
			if(day>0 && day<=14)
				restday += 1;
			
			if(day>=14 && day <=28)
				restday += 2;
			if(day>=28)
				restday += 3;
			day -= 30;
		}
		document.getElementById("sumtime").value=restday;*/
		var actionUrl = "<%=request.getContextPath()%>/pages/trip/tripinfo!getPageSumtime.action?startdate="+startdate+"&enddate="+enddate;
		$.ajax({
		     type:"post",
		     url:actionUrl,
			 dataType:"json",
			 cache: false,
			 async:true,
			 success:function(data){
				document.getElementById("sumtime").value=data.sumtime;
			 },
			 error:function(e){
				 alert(e);
			 }
		   });
	}
	/* function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function init(){
		//var prjname=document.getElementById("editprjname").value;
		//document.getElementById("prjname").value=prjname;
	}
	function changeValue(){
		var result = document.getElementById("result").value;
		if(result=="0"){
			document.getElementById("content").value="同意";
		}else{
			document.getElementById("content").value="";
		}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10" onload="init()">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/trip/tripinfo!audit.action"
		method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
						<legend>
							<s:text name="出差信息" />
						</legend>
						<div style="margin-top:10px;margin-left:28px;">名字：<s:property value="trip.username"/>&nbsp;&nbsp;&nbsp;&nbsp;部门：<s:property value="trip.detname"/></div>
		                <table width="80%" align="center" style="display:none;">
		                    <tr>
		                        <td>部门</td>
		                        <td><input type="text" name="trip.detname" id="deptname" value="<s:property value="trip.detname"/>" readonly="readonly"/></td>
		                        <td>项目组</td>
		                        <td><input type="text" name="trip.groupname" id="groupname" value="<s:property value="trip.groupname"/>" readonly="readonly"/></td>
		                        <td>名字</td>
		                        <td><input type="text" name="trip.username" id="username" value="<s:property value="trip.username"/>" readonly="readonly"/></td>
		                    </tr>
		                </table>
						<table width="95%" align="center" border="0" cellspacing="1"
							cellpadding="1" bgcolor="#583F70">
							<br />
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">开始日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="23%"><div align="left">
										<input name="trip.startdate" type="text" id="startDate"
											readonly=true size="18" class="MyInput" 
											value='<s:date name="trip.startdate" format="yyyy-MM-dd"/>'>
									</div></td>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">结束日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="23%"><div align="left">
										<div align="left">
											<input name="trip.enddate" type="text" id="endDate"
												readonly=true size="18" class="MyInput" 
												value='<s:date name="trip.enddate" format="yyyy-MM-dd"/>'>
										</div></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><input name="trip.status"
									type="text" id="status"
									value='<s:property value="trip.status"/>'
									style="width: 100%;" /></td>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">调休时长</font><font color="#FF0000">*</font>
								<td bgcolor="#FFFFFF" width="24%">
									<div style="float: left;">
										<input name="trip.sumtime" type="text" id="sumtime"
											style="width: 30%; border: 0px;" readonly="readonly"
											value='<s:property value="trip.sumtime"/>' /><span
											style="margin-bottom: 5px;">时</span> <input type="button"
											id="timelong" name="timelong" onclick="getTimeLong();"
											value="计算" /> <input type="hidden" name="hiddenday"
											id="hiddenday" value="0" /> <input type="hidden"
											name="hiddenhour" id="hiddenhour" value="0" />
									</div>
								</td>
							</tr>
							
							<tr>
							<input type="text" id="editprjname"  style="display: none" name="trip.prjname" value='<s:property value="trip.prjname"/>'>
							<!--  	<td width="10%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">项目名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#ffffff"><input type="hidden" id="editprjname"
									name="editprjname" value='<s:property value="trip.prjname"/>'>
									<tm:tmSelect name="trip.prjname" id="prjname" selType="projectName"
										path="systemConfig.projectname" style="width:100%;" /> <script
										type="text/javascript">
											$("#prjName")
													.append(
															"<option value=' '></option>");
											document.getElementById("prjname").value = "";
										</script></td> -->
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">出差地</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><input name="trip.tripcity"
									type="text" id="tripcity"
									value='<s:property value="trip.tripcity"/>'
									style="width: 100%;" /></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">客户名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><input name="trip.clientname"
									type="text" id="clientname"
									value='<s:property value="trip.clientname"/>'
									style="width: 100%;" /></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">任务描述</font><font color="#FF0000">*</font>
									</div></td>
								<td colspan="3" bgcolor="#FFFFFF"><textarea
										name="trip.taskdesc" id="taskdesc" cols="77" rows="3"
										style="word-break:break-all;word-wrap:break-word;width: 100%"><s:property
											value="trip.taskdesc" /></textarea></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="10%"><font
									color="#000000">经验/总结</font>
									</div></td>
								<td colspan="3" bgcolor="#FFFFFF"><textarea
										name="trip.sumup" id="sumup" cols="77" rows="3"
										style="word-break:break-all;word-wrap:break-word;width: 100%"><s:property value="trip.sumup" /></textarea>
								</td>
							</tr>
							<tr>
								<td width="10%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审核人</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><select name="trip.auditing_man"
									id="auditing_man" style="width: 100%">
										<option value="">请选择</option>
										<s:iterator value="#request.auditing_man" var="list">
											<option value="<s:property value='userid'/>"
												<s:if test="userid==trip.auditing_man">selected</s:if>><s:property
													value="username" />
											</option>
										</s:iterator>
								</select></td>
								<td width="10%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审批意见</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><select name="trip.result"
									id="result" style="width: 100%;" onchange="changeValue();">
										<option value="">请选择</option>
										<option value="0">通过</option>
										<option value="1">不通过</option>
								</select></td>
							</tr>
							<tr>
								<td width="10%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审批内容</font><font color="#FF0000">*</font></td>
								<td colspan="3" bgcolor="#FFFFFF"><textarea onkeyup="limitLen(500,this)"
										name="trip.content" id="content" cols="50" rows="5"
										style="word-break:break-all;word-wrap:break-word;width: 100%"></textarea></td>
							</tr>
							<tr>
								<td width="10%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审核记录</font></td>
								<td colspan="3" bgcolor="#ffffff"><textarea
										name="trip.auditlog" cols="50" rows="8" readonly="readonly"
										style="word-break:break-all;word-wrap:break-word;width: 100%"><s:property
											value="#request.record" /></textarea></td>
							</tr>
							<input type="hidden" name="trip.id"
								value='<s:property value="trip.id"/>'>
							<input type="hidden" name="trip.createdate" id="createdate"
								value='<s:property value="trip.createdate"/>'>
							<input type="hidden" name="trip.userid" id="userid"
								value='<s:property value="trip.userid"/>'>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onclick="save();" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>

	</form>
</body>
</html>
