<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	var iTaskIndex = 1; // the number of the task tables
	var selAllFlag = true;
	var tableTaskIndex = 2; // table in table's index
	var iTaskTotalpd = 20; // the total number of tasks
	var clickFlag = false;
	var oldStartTime, oldEndTime;
	var submitOK = true;

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
		var start=document.getElementById("startDate").value;
		var end=document.getElementById("endDate").value;
		dateDiff(end,start);
		var result = document.getElementById("result").value.trim();
		var content = document.getElementById("content").value.trim();
		var day=document.getElementById("hiddenday").value;
		if(day>0){
			day=day-1;
		}
		var hour=document.getElementById("hiddenhour").value;
		var sumtime=document.getElementById("sumtime").value;
		if (start == "") {
			alert('加班开始日期不能为空');
			return;
		}
		if (end == "") {
			alert('加班结束日期不能为空');
			return;
		}
		/*if(compareDate()){
			alert("开始时间不能大于结束时间");
			return;
		}*/
		if(sumtime==""){
			alert("调休时长不能为空");
			return;
		}
		
		if(parseInt(sumtime)>(parseInt(day)+1)*8){
			alert("调休时长一天按8小时计算,你加班的最大调休时长为："+(parseInt(day)+1)*8);
			//return;
		}
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
		var prjnamev = document.getElementById("prjnamev").value;
		document.getElementById("prjname").value = prjnamev;
	}
	function getTimeLong(){
		/*var start=document.getElementById("startDate").value;
		var end=document.getElementById("endDate").value;
		//alert(dateDiff(end,start));
		var sumtime = dateDiff(end,start);
		document.getElementById("sumtime").value=sumtime;*/
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
	    var day1=parseInt(between/(24*3600));
		var hour1=parseInt(between%(24*3600)/3600);
		var minute1=parseInt(between%3600/60);
		var second1=parseInt(between%60/60);
		var sumtime = 0;
		if(day1>0)
			sumtime += day1*8;
		if(hour1>8)
			sumtime += 8;
		else
			sumtime += hour1;
		if(minute1>45){
			if(hour1<8){
				sumtime += 1;
			}
		}
		document.getElementById("hiddenday").value=day1;
		document.getElementById("hiddenhour").value=hour1;
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
		action="<%=request.getContextPath()%>/pages/overtime/overtimeinfo!audit.action"
		method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="90%"
						style="margin-left: 10px">
						<legend>加班登记</legend>
						<div style="margin-top:10px;margin-left:28px;">名字：<s:property value="overtime.username"/>&nbsp;&nbsp;&nbsp;&nbsp;部门：<s:property value="overtime.detname"/></div>
						<table width="80%" align="center" style="display:none;">
							<tr>
								<td>部门</td>
								<td><input type="text" name="overtime.detname"
									id="deptname" value="<s:property value="overtime.detname"/>"
									readonly="readonly" /></td>
								<td>项目组</td>
								<td><input type="text" name="overtime.groupname"
									id="groupname" value="<s:property value="overtime.groupname"/>"
									readonly="readonly" /></td>
								<td>名字</td>
								<td><input type="text" name="overtime.username"
									id="username" value="<s:property value="overtime.username"/>"
									readonly="readonly" /></td>
							</tr>
						</table>
						<table width="90%" align="center" border="0" cellspacing="1"
							cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
							<br />
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="20%"><font
									color="#000000">开始日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><div align="left">
										<input name="overtime.startdate" type="text" id="startDate"
											readonly=true size="22" class="dateTimeInput" 
											value='<s:date name="overtime.startdate" format="yyyy-MM-dd HH:mm:ss"/>'>
									</div></td>
								<td align="center" bgcolor="#A5A5A5" width="20%"><font
									color="#000000">结束日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><div align="left">
										<div align="left">
											<input name="overtime.enddate" type="text" id="endDate"
												readonly=true size="22" class="dateTimeInput" 
												value='<s:date name="overtime.enddate" format="yyyy-MM-dd HH:mm:ss"/>'>
										</div></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#A5A5A5" width="20%"><font
									color="#000000">调休时长</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="4">
									 <div style="float:left;"><input name="overtime.sumtime" type="text" id="sumtime" style="width:30%;border:0px;" value='<s:property value="overtime.sumtime"/>'/><span style="margin-bottom:5px;">小时</span>
									 	<input type="button" id="timelong" name="timelong" onclick="getTimeLong();" value="计算"/>
									 	<input type="hidden" name="hiddenday" id="hiddenday" value="0"/>
									 	<input type="hidden" name="hiddenhour" id="hiddenhour" value="0"/>
									 </div>
								</td>
							</tr>
							<tr>
								<input type="hidden" id="prjnamev"
									value="<s:property value='overtime.prjname'/>">
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">项目名称</font><font color="#FF0000">*</font></td>
								<td colspan="4" bgcolor="#ffffff"><tm:tmSelect 
										name="overtime.prjname" id="prjname" selType="projectName" style="width:85%;" /> 
									<script type="text/javascript">
									if($("#prjName").value=""){
										$("#prjName").append("<option value=' '></option>");
						            	document.getElementById("prjname").value = "";
					                }
									</script></td>

							</tr>
							<tr>
								<td height="44" align="center" bgcolor="#A5A5A5" col>加班原因</font></td>
								<td bgcolor="#FFFFFF" colspan="4"><textarea
										name="overtime.reason" type="text" id="note" rows="3"
										cols="50" style="word-break:break-all;word-wrap: break-word;width:100%;"><s:property
											value="overtime.reason" /></textarea></td>
							</tr>
							<tr>
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td colspan="4" bgcolor="#FFFFFF">待审核
									<input type="hidden" name="overtime.status"
									id="status" value="<s:property value="overtime.status"/>"/>
								</td>
							</tr>
							<tr>
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审核人</font><font color="#FF0000">*</font></td>
				                <td colspan="4" bgcolor="#FFFFFF">
								   	<select name="overtime.auditing_man"
										id="auditing_man" style="width: 50%">
											<option value="">请选择</option>
											<s:iterator value="#request.auditing_man" var="list">
												<option value="<s:property value='userid'/>"
													<s:if test="userid==overtime.auditing_man">selected</s:if>><s:property
														value="username" />
												</option>
											</s:iterator>
									</select>
							    </td>
							</tr>
							<tr>
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审批意见</font><font color="#FF0000">*</font></td>
								<td colspan="4" bgcolor="#FFFFFF">
									<select name="overtime.result" id="result" style="width:50%;" onchange="changeValue();">
							    		<option value="">请选择</option>
							    		<option value="0">通过</option>
							    		<option value="1">不通过</option>
							    	</select>
								</td>
							</tr>
							<tr>
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审批内容</font><font color="#FF0000">*</font></td>
								<td colspan="4" bgcolor="#FFFFFF">
									<textarea name="overtime.content" id="content" cols="50" rows="5" style="word-break:break-all;work-wrap:break-word;width:100%"></textarea>
								</td>
							</tr>
							<tr>
								<td width="20%" align="center" bgcolor="#A5A5A5"><font
									color="#000000">审核记录</font></td>
								<td colspan="5" bgcolor="#ffffff"><textarea
										name="overtime.auditlog" cols="50" rows="8"
										readonly="readonly" style="word-break:break-all;word-wrap: break-word;width:100%;"><s:property
											value="#request.record" /></textarea></td>
							</tr>
							<input type="hidden" id="id" name="overtime.id" value="<s:property value="overtime.id"/>" />
							<input type="hidden" id="createdate" name="overtime.createdate" value="<s:property value="overtime.createdate"/>"/>
 							<input type="hidden" id="userid" name="overtime.userid" value="<s:property value="overtime.userid"/>"/>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td>
					<fieldset width="100%">
						<legend>
							<s:text name="label.tips.title" />
						</legend>
						<table width="100%">
							<tr>
								<td><s:text name="label.admin.content" /></td>
							</tr>
						</table>
					</fieldset> <br />
				</td>
			</tr>
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
