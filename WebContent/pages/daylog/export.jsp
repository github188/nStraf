<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@ page contentType="text/html; charset=UTF-8"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet"
	href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css"
	type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head></head>
<script type="text/javascript">
	var globalTaskInProject;
	var iTaskIndex = 1; // the number of the task tables
	var selAllFlag = true;
	var iTaskTotalpd = 20; // the total number of tasks
	var timeGroup = "",areaGroup = "";

	var initialTable;

	/* function closeModal() {
		if(window==window.parent){
           window.close();
		}else{
		   parent.close();  
		}
	} */

	function save() {
		var actionUrl="<%=request.getContextPath()%>/pages/daylog/logInfo!exportData.action";
		if(timeGroup==""){
			timeGroup="week";
		}
		if(areaGroup==""){
			areaGroup="person";
		}
		actionUrl+="?timeGroup="+timeGroup+"&areaGroup="+areaGroup;
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		setTimeout(function(){
			closeModal(true);
		},500);
	}

	$(function() {
		$("input[typeGroup='timeGroupRadio']").change(function(){
			timeGroup = $(this).val();
		});
		$("input[typeGroup='areaGroupRadio']").change(function(){
			areaGroup = $(this).val();
		});
	});
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/daylog/logInfo!exportData.action" method="post">
		<table width="99%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
			<tr>
				<td>
					<table style="background-color:#A5A5A5">
						<tr>
							<td align="left" width="10%" nowrap="nowrap"><b>
								导出时间：
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="99%" typeGroup="checkTable" align="center" cellPadding="1" cellSpacing="1" >
			<tr>
				<td width="33%" style="size:25px;text-align: left;background-color:#E3E3E3">
					 <input type="radio" name="timeGroup"  value='day' typeGroup='timeGroupRadio'/>本日
				</td>
				<td width="33%" style="size:25px;text-align: left;background-color:#E3E3E3">
					<input type="radio" name="timeGroup" checked value='week' typeGroup='timeGroupRadio'/>本周
				</td>
				<td style="size:25px;text-align: left;background-color:#E3E3E3">
					<input type="radio" name="timeGroup"  value='month' typeGroup='timeGroupRadio'/>本月
				</td>
			</tr>
		</table>
		<table width="99%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
			<tr>
				<td>
					<table style="background-color:#A5A5A5">
						<tr>
							<td align="left" width="10%" nowrap="nowrap"><b>
								导出范围：
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="99%" typeGroup="checkTable" align="center" cellPadding="1" cellSpacing="1" >
			<tr>
				<td width="33%" style="size:25px;text-align: left;background-color:#E3E3E3">
					 <input type="radio" name="areaGroup" checked value='person' typeGroup='areaGroupRadio'/>本人
				</td>
				<td width="33%" style="size:25px;text-align: left;background-color:#E3E3E3">
					<input type="radio" name="areaGroup" value='project' typeGroup='areaGroupRadio'/>本项目
				</td>
				<td style="size:25px;text-align: left;background-color:#E3E3E3">
					<input type="radio" name="areaGroup" value='dept' typeGroup='areaGroupRadio'/>本部门
				</td>
			</tr>
		</table>
		<br />
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>

</body>
</html>

