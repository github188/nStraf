<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>

</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript">
	function validateInfo() {
		if (!Validate('file', 'Require')) {
			alert('请选择文件');
			return false;
		}
		if (!change()) {
			return false;
		}
		if (confirm('<s:text name="confirm.modify.commit"/>')) {
			return true;
		}
	}
	function change() {
		var file = document.getElementById("file");
		if (!filter(file.value)) {
			alert("您上传的文件类型不被支持，本系统只支持.xls格式的文件");
			return false;
		}
		return true;
	}

	function filter(name) {
		var lastvalue = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
		/*if (lastvalue != "XLS" && lastvalue != "XLSX" && lastvalue != "ET"
				&& lastvalue != "XLT" && lastvalue != "ETT") {
			return false
		}*/
		if(lastvalue.toUpperCase() != "XLS"){
			return false;
		}
		return true;
	}

	function formsubmit() {
		if (validateInfo()) {
			window.returnValue = true
			document.forms[0].submit();
		}
	}
</script>
<body>
	<s:form
		action="/pages/signrecord/signRecord!importAttendanceData.action"
		method="post" enctype="multipart/form-data">
		<br>
		<table border="0" width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td class="ta_01" align="center" colspan="4"><font face="宋体"
					size="2"><strong>Excel文件数据导入,系统只支持.xls格式文件的导入</strong></font></td>
			</tr>
			<tr>
				<td width="1%" height=30></td>
				<td width="20%"></td>
				<td width="78%"></td>
				<td width="1%"></td>
			</tr>
			<tr>
				<td width="1%"></td>
				<td width="15%" align="center">请选择文件:</td>
				<td width="83%" align="left"><s:file name="file" id="file"
						cssStyle="MyButton" onchange="change(this);"></s:file></td>
				<td width="1%"></td>
			</tr>
			<tr height=50>
				<td colspan=4></td>
			</tr>
			<tr height=2>
				<td colspan=4></td>
			</tr>
			<tr height=10>
				<td colspan=4></td>
			</tr>
			<tr>
				<td align="center" colspan=4><input type="button" name="ok"
					value='<s:text name="grpInfo.ok"/>' class="MyButton"
					onclick="formsubmit();" image="../../images/share/yes1.gif">
					<input type="button" name="return"
					value='<s:text name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>