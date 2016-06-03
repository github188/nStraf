<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="cn.grgbanking.feeltm.staff.service.StaffInfoService"%>
<%@page import="cn.grgbanking.feeltm.project.service.ProjectService"%>
<%@page import="cn.grgbanking.feeltm.context.BaseApplicationContext" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%
	UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
	String username = userModel.getUserid();
	ProjectService projectService =  (ProjectService) BaseApplicationContext.getAppContext().getBean("projectService");
	String groupName=projectService.getProjectNameByUserid(userModel.getUserid());
	StaffInfoService staffInfoService =  (StaffInfoService) BaseApplicationContext.getAppContext().getBean("staffInfoService");
	String deptname=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">
	function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	}
	function save() {
		var prjname=document.getElementById("prjname").value.trim();
		var auditing_man=document.getElementById("auditing_man").value.trim();
		var reason = document.getElementById("note").value.trim();
		var start=document.getElementById("startDate").value.trim();
		var end=document.getElementById("endDate").value.trim();
		var day=document.getElementById("hiddenday").value.trim();
		var hour=document.getElementById("hiddenhour").value.trim();
		var sumtime=document.getElementById("sumtime").value.trim();
		if (start == "") {
			alert('加班开始时间不能为空');
			return;
		}
		if (end == "") {
			alert('加班结束时间不能为空');
			return;
		}
		if(!compareDate(start,end)){
			alert("开始时间不能大于等于结束时间");
			return;
		}
		if(sumtime==""){
			alert("调休时长不能为空");
			return;
		}
		/*if(parseInt(sumtime)>(parseInt(day)+1)*8){
			alert("调休时长一天按8小时计算,你加班的最大调休时长为："+(parseInt(day)+1)*8);
			return;
		}*/
		if(sumtime=="0.0" || sumtime=="0"){
			alert("调休时长不能等于0");
			return;
		}
		
		var re = /[^\d\.]/g;
		if(re.test(sumtime)){
			alert("请输入数字");
			return;
		}
		if(prjname==""){
			alert("必须填写项目名称");
			return;
		}
		if (reason == "") {
			alert('必须填写加班原因');
			return;
		}
		if(auditing_man==""){
			alert("必须填写审核人");
			return;
		}
		window.returnValue = true;
		reportInfoForm.submit();
	}
	
	function compareDate(startTime, endTime) {
		var startTimeArr = startTime.split(" ");
		var endTimeArr = endTime.split(" ");
		var dateArr1 = startTimeArr[0].split("-");
		var dateArr2 = endTimeArr[0].split("-");
		var timeArr1 = startTimeArr[1].split(":");
		var timeArr2 = endTimeArr[1].split(":");
		var date1 = new Date(dateArr1[0], dateArr1[1], dateArr1[2], timeArr1[0], timeArr1[1], timeArr1[2]);   
		var date2 = new Date(dateArr2[0], dateArr2[1], dateArr2[2], timeArr2[0], timeArr2[1], timeArr2[2]);  
		if (date1.getTime() >= date2.getTime()) {
			return false;
		} else {
			return true;
		}
		return false;
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
				if (tmpStr.length > 250) {
					alert("你输入的字数超过250个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 250);
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
	function init() {
		document.getElementById("overdeptname").value = "<%=deptname%>";
		document.getElementById("overgroupname").value="<%=groupName%>";
		document.getElementById("overusername").value="<%=username%>";
	}
	function getTimeLong(){
		var start=document.getElementById("startDate").value;
		var end=document.getElementById("endDate").value;
		if(start==""){
			alert("开始时间不能为空");
			return;
		}
		if(end==""){
			alert("结束时间不能为空");
			return;
		}
		var startdate=start.split(" ")[0];
		var enddate=end.split(" ")[0];
		if(startdate!=enddate){
			alert("开始时间与结束时间的日期必须是同一天");
			return;
		}
		if(!compareDate(start,end)){
			alert("开始时间不能大于等于结束时间");
			return;
		}
		//alert(dateDiff(end,start));
		//var sumtime = dateDiff(end,start);
		//document.getElementById("sumtime").value=sumtime;
		
		var actionUrl = "<%=request.getContextPath()%>/pages/overtime/overtimeinfo!getSumtime.action?startdate="+start+"&enddate="+end;
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
	function dateDiff(date1, date2){ 
	    date1 = stringToTime(date1); 
	    date2 = stringToTime(date2); 
	    var between = (date1 - date2) / 1000;//结果是秒 
	    var day=parseInt(between/(24*3600));
		var hour=parseInt(between%(24*3600)/3600);
		var minute=parseInt(between%3600/60);
		var second=parseInt(between%60/60);
		var sumtime = 0;
		if(day>0)
			sumtime += day*8;
		sumtime += hour;
		minute = parseFloat(minute/60);
		sumtime += minute;
		/*if(hour1>8)
			sumtime += 8;
		else
			sumtime += hour1;
		if(minute1>45){
			if(hour1<8){
				sumtime += 1;
			}
		}*/
		document.getElementById("hiddenday").value=day;
		document.getElementById("hiddenhour").value=hour;
		return sumtime;
		//return ""+day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒" +",调休时长："+sumtime;
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
</script>
<body id="bodyid" leftmargin="0" topmargin="10" onload="init()">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/overtime/overtimeinfo!save.action"
		method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
						
						<div class="user_info_banner">名字：<%=username %>&nbsp;&nbsp;&nbsp;&nbsp;部门：<%=deptname %></div>
						<input name="overtime.detname" type="hidden" id="overdeptname">
						<input name="overtime.groupname" type="hidden" id="overgroupname">
						<input name="overtime.username" type="hidden" id="overusername">
						<table width="90%" class="input_table">
							<tr>
								<td class="input_tablehead"> 加班登记 </td>
							</tr>
							<tr>
								<td  class="input_label2" width="20%"><font
									color="#000000">开始时间</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><div align="left">
										<input name="overtime.startdate" type="text" id="startDate" readonly=true
											size="22" class="dateTimeInput" 
											value='<s:date name="new java.util.Date()" format="yyyy-MM-dd HH:mm:ss"/>'>
									</div>
								</td>
								<td class="input_label2" width="20%"><font
									color="#000000">结束时间</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><div align="left">
									<div align="left">
										<input name="overtime.enddate" type="text" id="endDate" readonly=true
											size="22" class="dateTimeInput" 
											value='<s:date name="new java.util.Date()" format="yyyy-MM-dd HH:mm:ss"/>'>
									</div>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="20%"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF">
									 <input name="overtime.status" type="text" id="status" readonly value="新增"/>
								</td>
								<td class="input_label2" width="20%"><font
									color="#000000">调休时长</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF">
									 <div style="float:left;"><input name="overtime.sumtime" type="text" id="sumtime" style="width:30%;border:0px;"/><span style="margin-bottom:5px;">小时</span>
									 	<input type="button" id="timelong" name="timelong" onclick="getTimeLong();" value="计算"/>
									 	<input type="hidden" name="hiddenday" id="hiddenday" value="0"/>
									 	<input type="hidden" name="hiddenhour" id="hiddenhour" value="0"/>
									 </div>
								</td>
							</tr>
							<tr>
								<td width="20%" class="input_label2"><font
									color="#000000">项目名称</font><font color="#FF0000">*</font></td>
				                <td colspan="4" bgcolor="#ffffff"><tm:tmSelect name="overtime.prjname" id="prjname" selType="projectName"  defaultValue="<%=username %>" style="width:85%;" />
				                <script type="text/javascript">
				                if($("#prjname").value=""){
									$("#prjname").append("<option value=' '></option>");
					            	document.getElementById("prjname").value = "";
				                }
							   	</script></td>
							</tr>
							<tr>
								<td height="44" class="input_label2">加班原因</font><font
									color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="4"><textarea class="text_area"
										name="overtime.reason" type="text" id="note" rows="3" maxlength="200"
										cols="50" style="width:100%"></textarea></td>
							</tr>
							<tr>
								<td width="20%" class="input_label2"><font
									color="#000000">审核人</font><font color="#FF0000">*</font></td>
				                <td colspan="4" bgcolor="#FFFFFF">
								   	<select name="overtime.auditing_man" id="auditing_man" style="width:50%">
							    		<option value="">请选择</option>
							    		<s:iterator value="#request.auditing_man" var="list">
							    			<option value="<s:property value='userid'/>"><s:property value="username"/> </option>
							    		</s:iterator>
							    	</select>
							    </td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="okButton"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onclick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
