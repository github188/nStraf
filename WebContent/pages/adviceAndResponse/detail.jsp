<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="cn.grgbanking.feeltm.domain.*"%>

<html>
<head>
<title>mdetails advances and responses</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript">
	//下拉列表   意见和反馈的状态
	$(function() {
		$("#status").each(function() {
			var status = $(this).prev().val();
			$(this).find("option").each(function() {
				if ($(this).val() == status) {
					$(this).attr("selected", "selected");
				} else {
					$(this).removeAttr("selected");
				}
			});
		});

	});

	//关闭窗口
	function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	}

	//确认修改
	function save() {
		if (document.getElementById("adviceMan").value == "") {
			alert("请填写建议提出人");
			document.getElementById("adviceMan").focus();
			return;
		}
		if (document.getElementById("content").value == "") {
			alert("请填写意见内容");
			document.getElementById("content").focus();
			return;
		}

		if (document.getElementById("tel").value == "") {
			alert("请填写联系电话");
			document.getElementById("tel").focus();
			return;
		}

		var h = document.getElementsByName("status");//隐藏域 input标签
		var obj = document.getElementById("status");
		var index = obj.selectedIndex; // 选中索引
		var text = obj.options[index].text; // 选中文本
		var value = obj.options[index].value; // 选中值
		alert(index);
		alert(text);
		alert(value);
		h.value = value;
		var s = h.value;
		var h1 = document.getElementsByName("status");//

		document.getElementById("ok").disabled = true;
		window.returnValue = true;

		addAdvanceAndResponseForm.submit();
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<%-- <%@include file="/inc/navigationBar.inc"%> --%>

	<form name="addAdvanceAndResponseForm"
		action="myAdviceandResponse!update.action"
		namespace="/pages/adviceAndResponse" method="post">

		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead" colspan="4">
							意见反馈
							<input type="hidden" id="islogin" name="islogin" value='<s:property value="requset.get"/>'>
							</td>
						</tr>
						<!-- 数据的状态 -->
						<s:if test="#request.advice!=null">
							<s:iterator value="#request.advice" var="advice">
								<tr bgcolor="#FFFFFF">
									<td class="input_label2" width="13%">
										<div align="center">
											意见提出人：<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF" width="25%"><s:property
											value="adviceMan" /></td>
									<td width="13%" class="input_label2">
										<div align="center">
											时间：<font color="#FF0000">*</font>
										</div>
									</td>
									<td width="25%"><s:date name="time" format="yyyy-MM-dd" /></td>
								</tr>

								<tr bgcolor="#FFFFFF">
									<td class="input_label2">
										<div align="center">
											联系电话：<font color="#FF0000">*</font>
										</div>
									</td>
									<td ><s:property value="tel" /></td>
									<td class="input_label2">
										<div align="center">
											处理状态： <font color="#FF0000">*</font>
										</div>

									</td>
									<td bgcolor="#FFFFFF"><s:property value="status" /></td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td class="input_label2" width="13%">
										<div align="center">
											计划执行时间： <font color="#FF0000">*</font>
										</div>
									</td>
									<td width="14%"><s:date name="plantime"
											format="yyyy-MM-dd" /></td>

									<td class="input_label2" width="13%">
										<div align="center">
											邮箱： <font color="#FF0000">*</font>
										</div>
									</td>
									<td width="14%"><s:property
											value="email" /></td>
								<tr bgcolor="#FFFFFF">
									<td class="input_label2">
										<div align="center">
											意见内容<font color="#FF0000"></font>
										</div>
									</td>
									<td colspan="3"><textarea style="border-color:transparent;" readonly="readonly" type="text"  rows="8" cols="100" ><s:property value='content'/></textarea></td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td class="input_label2">
										<div align="center">
											意见回复<font color="#FF0000"></font>
										</div>
									</td>
									<td colspan="3"><textarea style="border-color:transparent;" readonly="readonly" type="text"  rows="8" cols="100" ><s:property value='reply'/></textarea></td>
								</tr>
							</s:iterator>
						</s:if>
					</table>
				</td>
			</tr>
		</table>
		<br /> <br />
	</form>
</body>
</html>
