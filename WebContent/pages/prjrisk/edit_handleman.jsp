<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	/* function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */
	function save() {
		var status=document.getElementById("status").value.trim();
		if(status=="新建" || status=="处理中"){
			alert("状态不能为新增或处理中");
			return;
		}
		if (document.getElementById("factdate").value.trim() == "") {
			alert("请输入实际完成日期");
			return;
		}
		if (document.getElementById("fruit").value.trim() == "") {
			alert("请输入解决情况");
			return;
		}
		var sdate=document.getElementById("createdate").value.trim();
		var edate=document.getElementById("factdate").value.trim();
		if(!compareDate(sdate,edate)){
			return;
		}
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		reportInfoForm.submit();

	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function selDate(id) {
		var obj = document.getElementById(id);
		ShowDate(obj);
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function checkHdl(val) {
		if (val.value.trim() == "" || val.value == null) {
			alert("必填项不能为空");
		}
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
				alert('实际完成日期不能小于提出日期，请重新输入！');
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
		}
		return flag;
	}
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 600) {
					alert("你输入的字数超过600个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 600);
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
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!update.action"
		method="post">
		<input type="hidden" name="prjRisk.id"
			value='<s:property value="prjRisk.id"/>'> <input
			name="usernameHidden" type="hidden" id="usernameHidden"
			value="<%=((cn.grgbanking.feeltm.login.domain.UserModel) session
					.getAttribute("tm.loginUser")).getUsername()%>" />
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table class="input_table">
							<tr>
								<td class="input_tablehead"> 项目风险</td>
							</tr>
							<tr>
								<td class="input_label2" width="13%">
									<div align="center">提出日期</div>
								</td>
								<td bgcolor="#FFFFFF" width="27%" colspan="3"><input
									name="prjRisk.createdate" type="text"
									id="createdate"
									value='<s:date name="prjRisk.createdate" format="yyyy-MM-dd"/>'
									readonly="readonly" class="input_readonly"></td>
								<td width="9%" class="input_label2">
									<div align="center">提出者</div>
								</td>
								<td bgcolor="#FFFFFF" width="26%"><input
									name="prjRisk.createman" type="text" id="createman"
									maxlength="20" size="20"
									value='<s:property value="prjRisk.createman"/>' readonly="readonly" class="input_readonly"></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										风险编号<font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="3" nowrap bgcolor="#FFFFFF">
									<input name="riskno" type="text" id="riskno"
									value='<s:property value="prjRisk.rno"/>' class="input_readonly" readonly="readonly">
								</td>
								<td class="input_label2" width="11%"><div align="center">
										状态</div></td>
								<td width="14%" bgcolor="#FFFFFF"><select
									name="prjRisk.status" id="status" align="left"
									style="width: 77px">
										<option value='新建' style="width: 105px">新建</option>
										<option value='处理中'>处理中</option>
										<option value='已解决'>已解决</option>
										<option value="不解决">不解决</option>
								</select></td>
								<input type='hidden' id="st1" name="st1"
									value="<s:property value='prjRisk.status'/>" />
								<s:if test="prjRisk.status!=null&&!prjRisk.status.equals('')">
									<script language="javascript">
										var status = $("#st1").val();
										status = decodeURI(status);
										$("#status").val(status);
									</script>
								</s:if>
							</tr>
							

							<tr>
								<td class="input_label2">
									<div align="center">项目名称</div>
								</td>
								<td colspan="3" nowrap bgcolor="#FFFFFF"><input
									name="prjRisk.prjname" type="text" id="prjname"
									maxlength="20" size="20" style="width: 100%"
									value='<s:property value="prjRisk.prjname"/>' readonly="readonly" class="input_readonly"></td>
								<td class="input_label2"><div align="center">类别</div></td>
								<td bgcolor="#FFFFFF"><input name="prjRisk.type"
									type="text"  id="type" maxlength="20" size="20"
									value='<s:property value="prjRisk.type"/>' readonly="readonly" class="input_readonly"></td>


							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">严重性</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="3"><input
									name="prjRisk.pond" type="text"  id="pond"
									maxlength="20" size="20"
									value='<s:property value="prjRisk.pond"/>' readonly="readonly" class="input_readonly"></td>

								<td width="11%" class="input_label2"><div align="center">
										紧急程度</div></td>
								<td bgcolor="#FFFFFF"><input name="prjRisk.urgent"
									type="text" id="urgent" maxlength="20" size="20"
									value='<s:property value="prjRisk.urgent"/>' readonly="readonly" class="input_readonly">
								</td>

							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">概要</div>
								</td>
								<td colspan="5" bgcolor="#FFFFFF"><input
									name="prjRisk.summary" type="text" id="summary" maxlength="70"
									size="74" value='<s:property value="prjRisk.summary"/>'
									readonly="readonly" class="input_readonly"></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">风险描述</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea cols="74" rows="3" name="prjRisk.riskdesc"
											id="riskdesc" readonly="readonly" style="width:100%"><s:property
												value="prjRisk.riskdesc" /></textarea>
									</div>
								</td>
							</tr>

							<tr>
								<td class="input_label2">建议方案</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea cols="74"
										rows="3" name="prjRisk.suggest" id="suggest" readonly="readonly"
										style="width:100%"><s:property value="prjRisk.suggest" /></textarea>
								</td>
							</tr>
							<tr>
								<td width="13%" class="input_label2">行动计划</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea
										name="prjRisk.plan" cols="74" rows="3" readonly="readonly" 
										id="plan" style="width:100%"><s:property value="prjRisk.plan" /></textarea></td>
							</tr>


							<tr>
								<td width="13%" class="input_label2">
									<div align="center">计划完成日期</div>
								</td>
								<td width="27%" bgcolor="#FFFFFF"><input
									name="prjRisk.handleterm" type="text" readonly="readonly" class="input_readonly"
									id="handleterm"
									value='<s:date name="prjRisk.handleterm" format="yyyy-MM-dd"/>'>
								</td>
								<td width="13%" class="input_label2">
									<div align="center">
										实际完成日期<font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF"><input name="prjRisk.factdate"
									type="text" id="factdate" class="MyInput" 
									value='<s:date name="prjRisk.factdate" format="yyyy-MM-dd"/>'></td>
								<td class="input_label2" width="9%">
									<div align="center">处理者</div>
								</td>
								<td width="26%" bgcolor="#FFFFFF"><input
									name="prjRisk.handleman" type="text" readonly="readonly" class="input_readonly"
									id="handleman" value='<s:property value="prjRisk.handleman"/>'
									size="20" maxlength="10"></td>
							</tr>

							<tr>
								<td class="input_label2">解决情况<font
									color="#FF0000">*</font>
								</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea
										name="prjRisk.fruit" cols="74" rows="3" id="fruit"><s:property
											value="prjRisk.fruit" /></textarea></td>
							</tr>
						<input type="hidden" name="prjRisk.userid" id="userid" value="<s:property value='prjRisk.userid'/>"/>
						<input type="hidden" name="prjRisk.deptname" id="deptname" value="<s:property value='prjRisk.deptname'/>"/>
						<input type="hidden" name="prjRisk.groupname" id="groupname" value="<s:property value='prjRisk.groupname'/>"/>

						</table>
				</td>
			</tr>

		</table>
		<br /> <br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>

</body>
</html>
