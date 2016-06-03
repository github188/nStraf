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
<style>
	.input_label2{
		text-align:right;
		padding-top:8px;
		padding-bottom:8px;
		vertical-align: middle;
	}
</style>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript">
	function validateInfo() {
		var emailregd = new RegExp(
				/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/);
		var telephone = new RegExp(/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/);
		if (!Validate('staff.email', 'Require')) {
			alert('<s:text name="staff.add.check.email"/>');
			document.getElementById("email").focus();
			return false;
		}
		var email = document.getElementById("email").value;
		if (!emailregd.test(email)) {
			alert('邮箱格式不正确,请正确填写邮箱');
			document.getElementById("email").focus();
			return false;
		}
		if (!Validate('staff.mobile', 'Require')) {
			alert('手机号码必填');
			return false;
		}
		var mobile = document.getElementById("mobile").value;
		if (!telephone.test(mobile)) {
			alert("请填写合法的手机号码,手机号码应该以13或15或18开头");
			document.getElementById("mobile").focus();
			return false;
		}
		window.returnValue = true;
		return true;
	}

	function save() {
		if (validateInfo()) {
			document.getElementById("ok").disabled = true;
			window.returnValue = true;
			$("#ok").attr("disabled", "disabled");
			reportInfoForm.submit();
		}

	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/staff/staffInfo!modify.action"
		method="post">
		<input type="hidden" name="flag" value="<s:property value='#request.flag'/>">
		<input type="hidden" name="userid" value="<s:property value='staff.userid' />">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead"><s:text
									name="staff.add.staffinfo" /></td>
						</tr>
						<!-- 员工只读 -->
						<tr bgcolor="#FFFFFF">
							<td rowspan="10" class="input_label2"><div align="center">在职信息</div></td>
							<td class="input_label2">用户标识:</td>
							<td><s:property value='staff.userid' /></td>
							<td class="input_label2">用户姓名:</td>
							<td><s:property value='staff.username' /></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">公司工号:</td>
							<td><s:property value='staff.jobNumber' /></td>
							<td class="input_label2">外派工号:</td>
							<td><s:property value='staff.outNumber' /></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">入职时间:</td>
							<td><s:date name="staff.grgBegindate" format="yyyy-MM-dd" /></td>
							<td class="input_label2">参加工作时间:</td>
							<td><s:date name="staff.workBegindate" format="yyyy-MM-dd" /></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">出生日期:</td>
							<td><s:date name="staff.birthDate" format="yyyy-MM-dd" /></td>
							<td class="input_label2">员工部门:</td>
							<td><tm:dataKeyValue beanName="staff" property="deptName" path="staffManager.department" /></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">岗位名称:</td>
							<td><s:property value="staff.postLevel" /></td>
							<td class="input_label2">员工状态:</td>
							<td><tm:dataKeyValue beanName="user" property="status" path="staffManager.status" /></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">用户级别:</td>
							<td><tm:dataKeyValue beanName="user" property="level" path="staffManager.userlevel" /></td>
							<td class="input_label2">权限组别:</td>
							<td><s:property value='#request.grpNames'/></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">毕业时间:</td>
							<td><s:date name="staff.graduateDate" format="yyyy-MM-dd" /></td>
							<td class="input_label2">身份证号:</td>
							<td><s:property value='staff.idCardNo'/></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">学历:</td>
							<td><s:property value='staff.education'/></td>
							<td class="input_label2">专业:</td>
							<td><s:property value='staff.major'/></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">紧急联系人:</td>
							<td><s:property value='staff.relativeName'/></td>
							<td class="input_label2">联系人电话:</td>
							<td><s:property value='staff.relativeTel'/></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2">毕业院校:</td>
							<td><s:property value='staff.graduateSchool'/></td>
							<td class="input_label2">家庭住址:</td>
							<td><s:property value='staff.address'/></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="input_label2" width="6%" rowspan="8"><div align="center">个人修改</div></td>
							<td class="input_label2">工作邮箱:</td>
							<td><input name="staff.email" type="text" id="email" size="25" value="<s:property value='staff.email'/>"></td>
							<td class="input_label2">手机号码:</td>
							<td><input name="staff.mobile" type="text" id="mobile" size="25" maxlength="11" value="<s:property value='staff.mobile'/>"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br /> <br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
