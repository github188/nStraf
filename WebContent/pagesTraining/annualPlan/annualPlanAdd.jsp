<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date currentDate = new java.util.Date();
	String str = sf.format(currentDate);
	String year = str.split("-")[0];
	String month = str.split("-")[1];
%>
<html>
<head>
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript">
	function closeModal() {
		window.close();
	}
	function save() {
		window.returnValue = true;
		reportInfoForm.submit();
	}
	$(function() {
		$("textarea").text("本次更新内容:\r");
	});
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/annualPlan/annualPlanInfo!save.action"
		method="post" enctype="multipart/form-data">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
					<table class="user_info_banner" height="10%">
						<tr height="20%" class="user_info_banner">
							<td align="left" width="13%" nowrap="nowrap"><b>编辑年度计划信息</b>
							</td>
						</tr>
					</table>
					<table class="input_table" style="word-break: break-all; word-wrap: break-word;" border="1">
						<tr>
							<td class="input_label2">
								<div align="right">
									<font color="#000000">计划年度:</font>
								</div>
							</td>
							<td nowrap bgcolor="#FFFFFF" width="100%">
								<select id="planYear" name="planYear">
									<%
										for (int i = 10; i >= 0; i--) {
											int curYear = Integer.parseInt(year);
											int valYear = curYear - i;
											String selectOption = "";
											if (valYear == curYear) {
												selectOption = "selected";
											}
									%>
									<option value="<%=valYear%>" <%=selectOption%>><%=valYear%></option>
									<%
										}
									%>
									<%
										for (int j = 1; j <= 10; j++) {
											int curYear = Integer.parseInt(year);
											int valYear = curYear + j;
											String selectOption = "";
											if (valYear == curYear) {
												selectOption = "selected";
											}
									%>
									<option value="<%=valYear%>" <%=selectOption%>><%=valYear%></option>
									<%
										}
									%>
							</select>年</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="right">
									<font color="#000000">计划文件:</font>
								</div>
							</td>
							<td nowrap bgcolor="#FFFFFF" width="100%">
								<s:file name="upload" id="file" cssStyle="MyButton" size="45"></s:file>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><br /> <input type="button" name="ok"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>