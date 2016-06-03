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
<head>
<title></title>

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript"> 
	function save() {
		var startdate = document.getElementById("startdate").value.trim();
		var enddate = document.getElementById("enddate").value.trim();
		var prjname = document.getElementById("prjname").value.trim();
		var tripcity = document.getElementById("tripcity").value.trim();
		var clientname = document.getElementById("clientname").value.trim();
		var taskdesc = document.getElementById("taskdesc").value.trim();
		var auditing_man = document.getElementById("auditing_man").value.trim();
		var sumtime = document.getElementById("sumtime").value.trim();
		var sumup = document.getElementById("sumup").value.trim();
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
		if(sumtime==""){
			alert("调休时长不能不空");
			return;
		}
		/*if(sumtime=="0"){
			alert("调休时长不能等于0");
			return;
		}*/
		if(prjname==""){
			alert("项目名称不能为空");
			return;
		}
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
		
		document.getElementById("ok").disabled = true;
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

	/* function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function init() {
		document.getElementById("tripdeptname").value="<%=deptname%>";
		document.getElementById("tripgroupname").value="<%=groupName%>";
		document.getElementById("tripusername").value="<%=username%>";
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
			if(day>=7 && day<14)
				restday += 1;
			
			if(day>=14 && day <28)
				restday += 2;
			if(day>=28)
				restday += 3;
			day -= 30;
		}*/
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
</script>
</head>
<body id="bodyid" onload="init()">
	<form name="reportInfoForm" 
		action="<%=request.getContextPath()%>/pages/trip/tripinfo!save.action"
		method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						
						<div class="user_info_banner">名字:<%=username %>&nbsp;&nbsp;&nbsp;&nbsp;部门:<%=deptname %></div>
						<input name="trip.detname" type="hidden" id="tripdeptname">
						<input name="trip.groupname" type="hidden" id="tripgroupname">
						<input name="trip.username" type="hidden" id="tripusername">
						<table width="95%" class="input_table">
							<tr>
								<td class="input_tablehead"><s:text name="出差信息" /></td>
							</tr>
							<tr>
								<td class="input_label2" width="10%"><font
									color="#000000">开始日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="23%"><div align="left">
										<input name="trip.startdate" type="text" id="startdate"
											readonly=true size="18" class="MyInput" 
											value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'>
									</div></td>
								<td class="input_label2" width="10%"><font
									color="#000000">结束日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="23%"><div align="left">
										<div align="left">
											<input name="trip.enddate" type="text" id="enddate"
												readonly=true size="18" class="MyInput" 
												value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'>
										</div></td>
							</tr>
							<tr>
								<td class="input_label2" width="10%"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><input name="trip.status"
									type="text" id="status" style="width: 100%;" readonly value="新增"/></td>
								<td class="input_label2" width="10%"><font
									color="#000000">调休时长</font>
								<td bgcolor="#FFFFFF" width="10%" style="text-align: left;">
									<div >
										<input name="trip.sumtime" type="text" id="sumtime"
											style="width: 12%; border: 0px;" readonly="readonly" /><span
											style="margin-bottom: 5px;">&nbsp;&nbsp;时</span> <input type="button"
											id="timelong" name="timelong" onclick="getTimeLong();"
											value="计算" /> <input type="hidden" name="hiddenday"
											id="hiddenday" value="0" /> <input type="hidden"
											name="hiddenhour" id="hiddenhour" value="0" />
									</div>
								</td>
							</tr>
							<tr>
								<td width="10%" class="input_label2"><font
									color="#000000">项目名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#ffffff"><tm:tmSelect name="trip.prjname"
										id="prjname" selType="projectName" path="systemConfig.projectname"
										style="width:100%;" /> <script type="text/javascript">
							   		$("#prjName").append("<option value=' '></option>");
				               		document.getElementById("prjname").value = "";
							   	</script></td>
								<td class="input_label2" width="10%"><font
									color="#000000">出差地</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><input name="trip.tripcity"
									type="text" id="tripcity" style="width: 100%;" onkeyup="limitLen(16,this)"/></td>
							</tr>
							<tr>
								<td class="input_label2" width="10%"><font
									color="#000000">客户名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><input name="trip.clientname"
									type="text" id="clientname" style="width: 100%;" onkeyup="limitLen(16,this)"/>
									</td>
							</tr>
							<tr>
								<td class="input_label2" width="10%"><font
									color="#000000">任务描述</font><font color="#FF0000">*</font></td>
								<td colspan="5" bgcolor="#FFFFFF"><textarea
										name="trip.taskdesc" id="taskdesc" cols="80" rows="3"
										onkeyup="limitLen(200,this)"></textarea></td>
							</tr>
							<tr>
								<td class="input_label2" width="10%"><font
									color="#000000">经验/总结</font>
									</div></td>
								<td colspan="5" bgcolor="#FFFFFF"><textarea
										name="trip.sumup" id="sumup" cols="80" rows="7"
										onkeyup="limitLen(500,this)"></textarea></td>
							</tr>
							<tr>
								<td width="10%" class="input_label2"><font
									color="#000000">审核人</font><font color="#FF0000">*</font></td>
								<td colspan="5" bgcolor="#FFFFFF"><select
									name="trip.auditing_man" id="auditing_man" style="width: 20%">
										<option value="">请选择</option>
										<s:iterator value="#request.auditing_man" var="list">
											<option value="<s:property value='userid'/>"><s:property
													value="username" />
											</option>
										</s:iterator>
								</select></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onclick="save();" image="../../images/share/yes1.gif"> <input
					type="button" name="return" id="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal(true);" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
