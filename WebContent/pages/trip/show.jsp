<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {
		if (document.getElementById("start").value.trim() == "") {
			alert("请输入开始日期");
			return;
		}
		window.returnValue = true;
		reportInfoForm.submit();

	}

	/* function closeModal() {

		window.close();
	} */

	function check() {
		if (document.getElementById("certificationNo").value.trim() == "") {
			alert("请输入认定编号");
			$("#certificationNo").focus();
			return;
		}
		var url = "cerinfo!check.action";
		var params = {
			deviceNo : $("#certificationNo").val()
		};
		jQuery.post(url, params, $(document).callbackFun1, 'json');
	}
	$.fn.callbackFun1 = function(json) {

		if (json != null && json == false) {
			//	$("#deviceNo").focus();
			document.getElementById("ok").disabled = true;
			alert("认定编号与之前输入的相同，请重新输入");
			return;
		} else {
			document.getElementById("ok").disabled = false;
			return;
		}
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
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/trip/tripinfo!update.action"
		method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>

		<div class="user_info_banner">名字：<s:property value="trip.username"/>&nbsp;&nbsp;&nbsp;&nbsp;部门：<s:property value="trip.detname"/>&nbsp;&nbsp;&nbsp;&nbsp;项目组：<s:property value="trip.groupname"/></div>
		<table width="95%" class="input_table">
			<tr>
				<td class="input_tablehead"><s:text name="出差信息" /></td>
			</tr>
			<tr>
				<td class="input_label2" width="10%"><font
					color="#000000">开始日期：</font></td>
				<td class="input_label2" colspan="2"><div align="left">
						<input name="trip.startdate" type="text" id="startdate" readonly=true
							style="width:100%;"
							value='<s:date name="trip.startdate" format="yyyy-MM-dd"/>'>
					</div>
				</td>
				<td class="input_label2" width="10%"><font
					color="#000000">结束日期：</font></td>
				<td bgcolor="#FFFFFF"  colspan="2"><div align="left">
					<div align="left">
						<input name="trip.enddate" type="text" id="enddate" readonly=true
							style="width:100%;"
							value='<s:date name="trip.enddate" format="yyyy-MM-dd"/>'>
					</div>
				</td>
			</tr>
			<tr>
				<td width="10%" class="input_label2"><font
					color="#000000">状态：</font></td>
				<td bgcolor="#FFFFFF" colspan="2">
					<input name="trip.status" type="text" id="status" readonly value='<s:property value="trip.status"/>' style="width:100%;"/>
				</td>
				<td class="input_label2" width="10%"><font
					color="#000000">时长：</font>
				<td bgcolor="#FFFFFF"   colspan="2">
					<div style="float: left;">
						<input name="trip.sumtime" type="text" id="sumtime"
							style="width: 30%; border: 0px;" readonly="readonly"
							value='<s:property value="trip.sumtime"/>' /><span
							style="margin-bottom: 5px;">天</span>
					</div>
				</td>
			</tr>
			<tr>
				<td width="10%" class="input_label2"><font
					color="#000000">项目名称：</font></td>
                <td bgcolor="#ffffff" colspan="2">
                	<input type="text" id="editprjname" readonly style="width:100%;" name="editprjname" value='<s:property value="trip.prjname"/>'>
			   	</td>
			   	<td class="input_label2" width="10%"><font
					color="#000000">出差地：</font>
				<td bgcolor="#FFFFFF" colspan="2">
					<input name="trip.tripcity" type="text" readonly id="tripcity" value='<s:property value="trip.tripcity"/>' style="width:100%;"/>
				</td>
			</tr>
			<tr>
				<td class="input_label2" width="10%"><font
					color="#000000">客户名称：</font>
				<td bgcolor="#FFFFFF" colspan="5">
					<input name="trip.clientname" type="text" id="clientname"  readonly value='<s:property value="trip.clientname"/>' style="width:100%;"/>
				</td>
			</tr>
			<tr>
				<td class="input_label2" width="10%"><font
					color="#000000">任务描述：</font>
					</div></td>
				<td colspan="5" bgcolor="#FFFFFF">
					<textarea name="trip.taskdesc" id="taskdesc" cols="77" rows="3" style="word-break:break-all;word-wrap:break-word;width:100%" readonly><s:property value="trip.taskdesc"/></textarea>
				</td>
			</tr>
			<tr>
				<td class="input_label2" width="10%"><font
					color="#000000">经验/总结：</font>
					</div></td>
				<td colspan="5" bgcolor="#FFFFFF">
					<textarea name="trip.sumup" id="sumup" cols="77" rows="3" style="word-break:break-all;word-wrap:break-word;width:100%" readonly><s:property value="trip.sumup"/></textarea>
				</td>
			</tr>
			<tr>
				<td width="10%" class="input_label2"><font
					color="#000000">审核人：</font></td>
			    <td bgcolor="#FFFFFF" colspan="5"><select name="trip.auditing_man" disabled
					id="auditing_man" style="width: 25%">
						<option value="">请选择</option>
						<s:iterator value="#request.auditing_man" var="list">
							<option value="<s:property value='userid'/>"
								<s:if test="userid==trip.auditing_man">selected</s:if>><s:property
									value="username" />
							</option>
						</s:iterator>
				</select></td>
			</tr>
			<tr>
				<td width="10%" class="input_label2"><font
					color="#000000">审批内容：</font></td>
				<td colspan="5" bgcolor="#FFFFFF">
					<textarea name="trip.content" id="content" readonly cols="50" rows="5" style="word-break:break-all;word-wrap:break-word;width:100%"></textarea>
				</td>
			</tr>
			<tr>
			    <td width="10%" class="input_label2"><font
					color="#000000">审核记录：</font></td>
			    <td colspan="5" bgcolor="#ffffff">
			    	<textarea name="trip.auditlog" cols="50" rows="8" readonly="readonly" style="word-break:break-all;word-wrap:break-word;width:100%"><s:property
											value="#request.record" /></textarea>
			    </td>
			</tr>
			<input type="hidden" name="trip.id" value='<s:property value="trip.id"/>'>
			<input type="hidden" name="trip.status" id="status" value="<s:property value="trip.status"/>"/>
			<input type="hidden" name="trip.createdate" id="createdate" value='<s:property value="trip.createdate"/>'>
			<input type="hidden" name="trip.userid" id="userid" value='<s:property value="trip.userid"/>'>
		</table>
		</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
