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
	function save() {
		var projectId=$("#projectId").val();
		var startDate=$("#startDate").val();
		var endDate =$("#endDate").val();
		var isTraineeCheck=$("input[name='includeTrainee']").attr('checked')==true||$("input[name='includeTrainee']").prop('checked')==true;
		var includeTrainee=(isTraineeCheck||isTraineeCheck=='true')?"checked":"none";
		if(projectId==""){
			alert("请选择项目");
			return;
		}
		if(startDate==""){
			alert("请填写开始日期");
			return;
		}
		if(endDate==""){
			alert("请选择结束日期");
			return;
		}
		var actionUrl="<%=request.getContextPath()%>/pages/costControl/detail!exportData_ProjectPersonDay.action?projectId="+projectId+"&startDate="+startDate+"&endDate="+endDate+"&includeTrainee="+includeTrainee;
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		setTimeout(function(){
			closeModal(false);
		},500);
	}

</script>
<body id="bodyid" leftmargin="0" topmargin="10">
<div style="height:200px;overflow-y:auto;">
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/costControl/detail!exportData_ProjectPersonDay.action"  method="post">
		<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1" >
			<tr>
				<td>
					<table style="background-color:#A5A5A5">
						<tr>
							<td align="left" width="10%" nowrap="nowrap"><b>
								选择项目：
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="95%" typeGroup="checkTable" align="center" cellPadding="1" cellSpacing="1" >
			<tr>
				<td style="size:25px;text-align: left;background-color:#E3E3E3">
					<select name="projectId" id="projectId" style="width:50%">
						<option value=""><s:text name="staff.add.option"/></option>
						<s:iterator value="#request.projects" id="project">
					    	<option value="<s:property value='id'/>"><s:property value="name"/></option>
						</s:iterator>
					</select>
					<input name="includeTrainee" type="checkbox" />包含实习生数据
				</td>
			</tr>
		</table>
		<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1" >
			<tr>
				<td>
					<table style="background-color:#A5A5A5">
						<tr>
							<td align="left" width="10%" nowrap="nowrap"><b>
								选择时间：
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="95%" typeGroup="checkTable" align="center" cellPadding="1" cellSpacing="1" >
			<tr>
				<td style="size:25px;text-align: left;background-color:#E3E3E3">
					 <input name="startDate" type="text" id="startDate"  
                				class="MyInput" style="width:50%;" size="25" value="<s:date name='#request.startDate' format='yyyy-MM-dd'/>" />
                	至
                	<input name="endDate" type="text" id="endDate"  
                				class="MyInput" style="width:50%;" size="25" value="<s:date name='#request.endDate' format='yyyy-MM-dd'/>" />
				</td>
			</tr>
		</table>
		<br/>
		<table width="95%" cellpadding="0" cellspacing="0" align="center" >
			<tr>
				<td align="center"><input type="button" name="ok"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>

