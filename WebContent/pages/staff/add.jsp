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
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript">
	function validateInfo() {
		var emailregd = new RegExp(
				/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/);
		var idcardreg = new RegExp(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/);
		var useridreg = new RegExp(/(^[a-zA-Z][a-zA-Z0-9_]{1,15}$)/);
		var telephone = new RegExp(/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/);
		var telreg = new RegExp(/^([0-9]){1}([0-9]|-){0,12}$/);
		if (!Validate('staff.userid', 'Require')) {
			alert('<s:text name="staff.add.check.userid"/>');
			document.getElementById("userid").focus();
			return false;
		}
		var userid = document.getElementById("userid").value;
		if (!useridreg.test(userid)) {
			alert("员工标识不合法,只能是由1-15个字母或数字或_组成并字母开头");
			return false;
		}
		if (!Validate('staff.username', 'Require')) {
			alert('<s:text name="staff.add.check.username"/>');
			document.getElementById("username").focus();
			return false;
		}
		/*if (!Validate('staff.idCardNo', 'Require')) {
			alert('<s:text name="staff.add.check.idCardNo"/>');
			document.getElementById("idCardNo").focus();
			return false;
		}*/
		var idCardNo = document.getElementById("idCardNo").value;
		if (!idcardreg.test(idCardNo) && idCardNo!="") {
			alert('身份证号不合法,请正确填写');
			document.getElementById("idCardNo").focus();
			return false;
		}
		if (!Validate('staff.birthDate', 'Require')) {
			alert('<s:text name="staff.add.check.birthDate"/>');
			return false;
		}
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
		if (!Validate('staff.workBegindate', 'Require')) {
			alert('<s:text name="staff.add.check.workBegindate"/>');
			return false;
		}
		if(!Validate('staff.mobile','Require')){
			alert('手机号码必填');
			document.getElementById("mobile").focus();
			return false;
		}
		var mobile = document.getElementById("mobile").value;
		if(!telephone.test(mobile)){
			alert("请填写合法的手机号码,手机号码应该以13或15或18开头");
			document.getElementById("mobile").focus();
			return false;
		}
		var tel = document.getElementById("tel").value;
		if(!telreg.test(tel) && tel!=""){
			alert("请填写合法的电话号码,如020-12345678");
			document.getElementById("tel").focus();
			return false;
		}
		if (!Validate('staff.grgBegindate', 'Require')) {
			alert('<s:text name="staff.add.check.grgBegindate"/>');
			return false;
		}
		if (!Validate('staff.level', 'Require')) {
			alert('<s:text name="staff.add.check.level"/>');
			document.getElementById("level").focus();
			return false;
		}
		/*if (!Validate('staff.postLevel', 'Require')) {
			alert('<s:text name="staff.add.check.postLevel"/>');
			document.getElementById("postLevel").focus();
			return false;
		}*/
		if (!Validate('staff.deptName', 'Require')) {
			alert('<s:text name="staff.add.check.deptName"/>');
			document.getElementById("deptName").focus();
			return false;
		}
		if (!Validate('staff.status', 'Require')) {
			alert('<s:text name="staff.add.check.status"/>');
			document.getElementById("status").focus();
			return false;
		}
		var relativeTel = document.getElementById("relativeTel").value;
		if (relativeTel != "" && !telreg.test(relativeTel)) {
			alert("请填写合法的联系人的电话或手机号码");
			document.getElementById("relativeTel").focus();
			return false;
		}
		if (!Validate('staff.jobNumber', 'Require')) {
			alert('请填写公司工号！');
			document.getElementById("jobNumber").focus();
			return false;
		}
		window.returnValue = true;
		return true;
	}

	function save() {
		if (validateInfo()) {
			document.getElementById("ok").disabled = true;
			window.returnValue = true;
			$("#ok").attr("disabled","disabled");
			reportInfoForm.submit();
		}

	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/staff/staffInfo!save.action"
		method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table class="input_table">
							<tr>
								<td class="input_tablehead">
								<s:text name="staff.add.staffinfo" />
								</td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td rowspan="8" class="input_label2"><div
										align="center">
										<s:text name="staff.add.baseinfo" />
									</div></td>
								<td class="input_label2"><s:text
										name="staff.add.userid" /><font color="#FF0000">*</font></td>
								<td><input name="staff.userid" type="text"
									id="userid" size="16" maxlength="16"></td>
								<td class="input_label2"><s:text
										name="staff.add.username" /><font color="#FF0000">*</font></td>
								<td><input
									name="staff.username" type="text" id="username" size="16"
									maxlength="16"></td>
							</tr>
							<tr  bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.birthDate" /><font color="#FF0000">*</font></td>
								<td><input name="staff.birthDate" type="text"
									id="birthDate" class="MyInput" /></td>
								<td  class="input_label2"><s:text
										name="staff.add.idCardNo" /></td>
								<td><input name="staff.idCardNo"
									type="text" id="idCardNo" size="25" maxlength="18"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2" align="right"><s:text
										name="staff.add.email" /><font color="#FF0000">*</font></td>
								<td ><input name="staff.email" type="text"
									id="email" size="30"></td>
								<td class="input_label2" ><s:text
										name="staff.add.education" /></td>
								<td ><select name="staff.education" tabindex="4"
									id="education" style="width: 100px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.education" id="dept">
											<option value="<s:property value='key'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.major" /></td>
								<td><input name="staff.major" type="text" id="major"
									size="16" maxlength="16"></td>
								<td class="input_label2"><s:text
										name="staff.add.workBegindate" /><font color="#FF0000">*</font></td>
								<td><input name="staff.workBegindate" type="text"
									id="workBegindate" class="MyInput" 
									value='<s:date name="staff.startworddtae" format="yyyy-MM-dd"/>'></td>
								</tr>
								<tr  bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.graduateDate" /></td>
								<td><input name="staff.graduateDate" type="text"
									id="graduatedate" size="12" maxlength="12" class="MyInput"
									value='<s:date name="staff.graduateDate" format="yyyy-MM-dd"/>'></td>
								<td class="input_label2"><s:text
										name="staff.add.graduateschool" /></td>
								<td><input name="staff.graduateSchool"
									size="50" type="text" id="graduateschool" isSel="true"></td>
							</tr>
								<tr  bgcolor="#FFFFFF">
								<td class="input_label2">手机号码<font color="#FF0000">*</font></td>
								<td><input name="staff.mobile" type="text" id="mobile"
									size="16" maxlength="11"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')"></td>
								<td class="input_label2">电话</td>
								<td><input name="staff.tel" type="text" id="tel" onkeyup="this.value=this.value.replace(/[^0-9\-]+/,'')" maxlength="12"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.relativeName" /></td>
								<td><input name="staff.relativeName" type="text" id="major"
									size="16" maxlength="16"></td>
								<td class="input_label2"><s:text
										name="staff.add.relativeTel" /></td>
								<td><input name="staff.relativeTel" type="text"
									id="relativeTel" maxlength="12"
									onkeyup="this.value=this.value.replace(/[^0-9\-]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\-]+/,'')"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.address" /></td>
								<td colspan="3">
								<input name="staff.address" size="100" maxlength="50"
									type="text" id="address" isSel="true"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td rowspan="4" class="input_label2"><div
										align="center">
										<s:text name="staff.add.entryInfo" />
										<font color="#FF0000"> </font>
									</div></td>
								<td class="input_label2"><s:text
										name="staff.add.grgBegindate" /><font color="#FF0000">*</font></td>
								<td><input name="staff.grgBegindate" type="text"
									id="grgBegindate" size="12" maxlength="12" class="MyInput"
									value='<s:date name="staff.grgBegindate" format="yyyy-MM-dd"/>'></td>
								<td class="input_label2"><s:text
										name="staff.add.level" /><font color="#FF0000">*</font></td>
								<td><select name="staff.level" id="level"
									style="width: 115px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.userLevel">
											<option value="<s:property value='key'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select></td>
								</tr>
								<tr  bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.postLevel" /></td>
								<td><select name="staff.postLevel" id="postLevel"
									style="width: 115px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.postlevel" id="dept">
											<option value="<s:property value='key'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select></td>
								<td class="input_label2"><s:text
										name="staff.add.status" /><font color="#FF0000">*</font></td>
								<td><select name="staff.status" id="status"
									style="width: 90px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.status" id="dept">
											<option value="<s:property value='key'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2"><s:text
										name="staff.add.deptName" /><font color="#FF0000">*</font></td>
								<td><select name="staff.deptName" id="deptName"
									style="width: 90px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.deptMap" id="dept">
											<option value="<s:property value='key'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select></td>
								<td class="input_label2">
									公司工号
									<font color="#FF0000">*</font>
								</td>
								<td>
									<input name="staff.jobNumber" size="20" maxlength="50"
									type="text" id="jobNumber" isSel="true">
								</td>
								<%-- <td bgcolor="#999999" align="right">项目组:</td>
								<td><select name="project" id="group"
									style="width: 90px">
										<option value=""><s:text name="staff.add.option" /></option>
										<s:iterator value="#request.group" id="group">
											<option value="<s:property value='code'/>"><s:property
													value="name" /></option>
										</s:iterator>
								</select></td> --%>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="input_label2">
									外派工号
									<!-- <font color="#FF0000">*</font> -->
								</td>
								<td>
									<input name="staff.outNumber" size="20" maxlength="50"
									type="text" id="outNumber" isSel="true">
								</td>
								<td>
								</td>
								<td>
								</td>
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
