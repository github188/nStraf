<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@page import="cn.grgbanking.feeltm.domain.*"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>modify advances and responses</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>

<script type="text/javascript">
	//下拉列表   意见和反馈的状态
	$(function() {
		$("#adviceMan").css({
			color : "#000000",
			background : "#D3D3D3"
		});
		$("#time").css({
			color : "#000000",
			background : "#D3D3D3"
		});
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

		$("#status").change(function() {
			var statusinput = document.getElementById("statusinput");//隐藏域 input标签
			var select = document.getElementById("status");
			var index = select.selectedIndex; // 选中索引
			var text = select.options[index].text; // 选中文本
			var value = select.options[index].value; // 选中值
			statusinput.value = value;
			var newstatus = statusinput.value;
			//alert(newstatus);
		});

		//设置用户修改数据的权限
		/*当前用户与意见提出者不一致的时候   所有的信息都不能修改  */
		/*当前用户与意见提出者不一致的时候   是  系统管理员   tel  content不可用 */
		var curUser = $("#curUser").val();//当前用户名
		var aduser = $("#adviceMan").val();//当前意见的意见人的id
		var hasEditStatuRight = $("#hasEditStatuRight").val();//判断是否有管理员权限
		if (hasEditStatuRight == "no" && curUser == aduser) {
			$("#status").attr("disabled", "disabled");
			$("#plantime").attr("readonly", "readonly");
			$("#reply").attr("readonly", "readonly");
			$("#plantime,#reply").css({
				color : "#000000",
				background : "#D3D3D3"
			});
			$("#tel").removeAttr("readonly")
			$("#content").removeAttr("readonly")

		}
		if (hasEditStatuRight == "no" && curUser != aduser) {
			$("#tel,#content,#email").attr({
				readonly : "readonly"
			});
			$("#status").attr("disabled", "disabled");
			$("#plantime").attr("readonly", "readonly");
			$("#tel,#content,#plantime,#email").css({
				color : "#000000",
				background : "#D3D3D3"
			});

		}

		if (hasEditStatuRight == "yes" && curUser != aduser) {
			$("#tel,#content,#email").attr({
				readonly : "readonly"
			});
			$("#tel,#content,#email").css({
				color : "#000000",
				background : "#D3D3D3"
			});
			$("#status").removeAttr("disabled");
			$("#plantime").datepicker({
				dateFormat : 'yy-mm-dd', //更改时间显示模式  
				changeMonth : true, //是否显示月份的下拉菜单，默认为false  
				changeYear : true
			//是否显示年份的下拉菜单，默认为false  
			});
		}
		if (hasEditStatuRight == "yes" && curUser == aduser) {
			$("#tel,#content").removeAttr("readonly");
			$("#content").removeAttr("readonly");
			$("#status").removeAttr("disabled");
			$("#plantime").datepicker({
				dateFormat : 'yy-mm-dd', //更改时间显示模式  
				changeMonth : true, //是否显示月份的下拉菜单，默认为false  
				changeYear : true
			//是否显示年份的下拉菜单，默认为false  
			});

		}

	});

	/* //关闭窗口
	function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */
	//确认修改
	function savemodify() {

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

		var Email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
		var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
		var phoneormobile = document.getElementById("tel").value.trim();
		var email = document.getElementById("email").value.trim();
		if (!isPhone.test(phoneormobile) && !isMob.test(phoneormobile)) {
			alert("请填写正确的联系方式！");
			return;
		}
		if (!Email.test(email)) {
			alert("请填写正确的邮箱！");
			return;
		}

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

		<%-- <table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
	</table> --%>
		<br /> <br />
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">意见反馈
							<td width="25%">
								<%-- <input id= "curuserid" type="hidden"  value="<%=((SysUser)request.getAttribute("curUser")).getUserid()%>" > --%>
								<input name="curUser" type="hidden" id="curUser"
								value="<%=((SysUser) request.getAttribute("curUser")).getUsername()
					.trim()%>">
								<input name="hasEditStatuRight" type="hidden"
								id="hasEditStatuRight"
								value="<%=(String) request.getAttribute("hasEditStatuRight")%>">

							</td>
						</tr>


						<s:if test="#request.advice!=null">
							<s:iterator value="#request.advice" var="advice">
								<td class="input_label2" width="13%">
									<div align="center">
										意见提出人：<font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="25%"><input
									name="adviceandresponse.id" maxlength="16" size="50" id="id"
									value="<s:property value="id"/>" type="hidden"> <input
									name="adviceandresponse.adviceMan" maxlength="16" size="50"
									id="adviceMan" value="<s:property value="adviceMan"/>"
									readonly="readonly"></td>
								<td width="13%" class="input_label2">
									<div align="center">
										时间：<font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="25%"><input
									name="adviceandresponse.time" maxlength="16" size="50"
									id="time" value="<s:date name="time" format="yyyy-MM-dd"/>"
									readonly="readonly"></td>
								</tr>

								<tr>
									<td class="input_label2" width="13%">
										<div align="center">
											联系电话：<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF"><input name="adviceandresponse.tel"
										maxlength="20" size="50" id="tel"
										value="<s:property value="tel"/>"></td>
									<td class="input_label2">
										<div align="center">
											邮箱：<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF" maxlength="20" size="50"><input
										name="adviceandresponse.email" maxlength="20" size="50"
										id="email" value="<s:property value="email"/>"></td>
								</tr>


								<tr>
									<td class="input_label2">
										<div align="center">
											处理状态： <font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF" maxlength="20" size="50">
										<%-- <input name="adviceandresponse.status" type="text" id="status" class="MyInput" 
										readonly="readonly"  
										value="<s:property value="status"/>"> --%> <input
										type="hidden" id="statusinput" name="adviceandresponse.status"
										value='<s:property value="status"/>'> <!-- 数据的状态 --> <select
										id="status" name="status" style="width: 100%")>
											<s:iterator value="#request.adviceARStautsMap" id="ele">
												<option value="<s:property value='#ele.key'/>">
													<s:property value="#ele.value" />
												</option>
											</s:iterator>
									</select>
									</td>

									<td class="input_label2" width="13%" maxlength="20" size="50">
										<div align="center">
											计划执行时间： <font color="#FF0000">*</font>
										</div>
									</td>
									<td width="14%" bgcolor="#FFFFFF"><input
										name="adviceandresponse.plantime" class=".plantimeinput"
										type="date" id="plantime" maxlength="15" size="50" id="tel"
										onkeyup="this.value=this.value.replace(/[^0-9\-]+/,'')"
										onKeyDown="this.value=this.value.replace(/[^0-9\-]+/,'')"
										value="<s:date name="plantime" format="yyyy-MM-dd"/>">
									</td>

								</tr>



								<td class="input_label2" width="13%"></td>
								<td bgcolor="#FFFFFF"><input readonly="readonly"
									type="hidden" name="adviceandresponse.userId" maxlength="16"
									size="40" id="userId" value="<s:property value="userId"/>"
									readonly="readonly"></td>
								<tr>
									<td class="input_label2">
										<div align="center">
											意见内容<font color="#FF0000"></font>
										</div>
									</td>
									<td colspan="5">
										<div>
											<textarea name="adviceandresponse.content" cols="100" rows="10" id="content"><s:property value="content" /></textarea>
										</div>
									</td>
								</tr>
								<tr>
									<td class="input_label2">
										<div align="center">
											意见回复<font color="#FF0000"></font>
										</div>
									</td>
									<td colspan="5">
										<div>
											<textarea name="adviceandresponse.reply" cols="100" rows="10" id="reply"><s:property value="reply" /></textarea>
										</div>
									</td>
								</tr>

							</s:iterator>
						</s:if>

					</table>
				</td>
			</tr>
		</table>


		<br /> <br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="savemodify()" image="../../images/share/yes1.gif">
					<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal(true);" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>


	</form>
</body>
</html>
