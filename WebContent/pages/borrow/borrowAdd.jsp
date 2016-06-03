<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%
java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
String applydate = formatter.format(currentTime); //将日期时间格式化 
%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {
		if ($('#expectedRepaydate').val().trim() =='') {
			alert("请输入预计还款日期");
			return;
		}
		if ($('#amount').val().trim() =='') {
			alert("请输入申请借款金额");
			return;
		}
		if (Number($('#amount').val())==0) {
			alert("申请借款金额不能为0");
			return;
		}
		var testmoney = /^\d+(\.\d{1,2})?$/;
		if (!testmoney.test($('#amount').val().trim())){
			alert("请输入正确的申请借款金额 ");
			return;
		}
		if ($('#borrowReason').val().trim() =='') {
			alert("请输入借款原因");
			return;
		}
		if ($('#type').val()=='0') {
			if ($('#tripcity').val().trim() ==''){
				alert("出差借款请填写出差地点");
				return;
			}
		}
		var expectedrepaydate = $('#expectedRepaydate').val();
		if(!validateInputInfo()){
			return;
		}
		
		window.returnValue = true;
		reportInfoForm.submit();
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 150) {
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 150);
					alert("你输入的字数超过150个了");
					document.getElementsByTagName("textarea")[i].focus();
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
	
	$(function(){
		var applydate = "<%=applydate%>";
		$("#applaydate").val(applydate);
		var isFinancial = "<%=request.getAttribute("isFinancial")%>";
		if(isFinancial=="true"){
			$("#isFinanTr").show();
			//$("#approveTr").remove();
			var child=document.getElementById("approveTr");
			child.parentNode.removeChild(child);
		}else{
			//$("#isFinanTr").remove();
			var child=document.getElementById("isFinanTr");
			child.parentNode.removeChild(child);
		}
		$("#updatemanid option[value='lqmin']").attr("selected","selected");
	});
	
	function borrowAmounta(){
		var borrowAmount = $("#borrowAmount").val();
		$("#amount").val(borrowAmount);
		if(borrowAmount==""){
			return;
		}
		var getsum = fmoney(borrowAmount,2);
		$("#borrowAmount").val(getsum);
	}
	
	//金额千分号分隔
	function fmoney(s, n){   
	   n = n > 0 && n <= 20 ? n : 2;   
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
	   var l = s.split(".")[0].split("").reverse(),   
	   r = s.split(".")[1];   
	   t = "";   
	   for(i = 0; i < l.length; i ++ )   
	   {   
	      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
	   }   
	   return t.split("").reverse().join("") + "." + r;   
	} 
	
	function validateInputInfo() {
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var thisDate = "<%=applydate%>";
		var endDate = $("#expectedRepaydate").val();
		if (thisDate.length > 0 && endDate.length > 0) {
			var v = re.test(endDate);
			var a = re1.test(thisDate);
			if (!v || !a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			if (!DateValidate()) {
				alert('预计还款日期不能小于申请日期，请重新输入！');
				return false;
			}
		} else if (thisDate.length > 0 && endDate.length == 0) {
			var a = re1.test(thisDate);
			if (!a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		} else if (thisDate.length == 0 && endDate.length > 0) {
			var v = re.test(endDate);
			if (!v) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		}
		return true;
	}
	function DateValidate() {
		var Require = /.+/;
		var begin = "<%=applydate%>";
		var end = $("#expectedRepaydate").val();
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
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/borrow/borrowInfo!save.action"
		method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>	
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">编辑借款信息</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="6">
								姓名：<s:property value="#request.usr.username"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								部门：<s:property value="#request.usr.deptName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								借款状态：新增&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								申请日期：<%=applydate %>
							</td>
						</tr>
						<tr>
							<td class="input_label2">申请借款金额:</td>
							<td bgcolor="#FFFFFF" style="width:18%;">
								<input name="borrow.amount" type="hidden" id="amount" value='' />
								<input name="borrowAmount" type="text" id="borrowAmount" value='' maxLength="7" style="width:90%" onBlur="borrowAmounta();" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')"/>元
							</td>
							<td class="input_label2">出差地:</td>
							<td bgcolor="#FFFFFF" style="width:19%;">
								<input name="borrow.tripcity" type="text" id="tripcity" maxLength="16" value='<s:property value="borrow.tripcity"/>'/>
							</td>
							<td class="input_label2">借款类型:</td>
			                <td bgcolor="#ffffff" style="width:18%;">
			                	<select id="type" name="borrow.type" style="width:100%;">
			                		<option value="0">出差</option>
			                		<option value="1">备用金</option>
			                	</select>
						   	</td>
						</tr>
						<tr id="isFinanTr" style="display:none;">
							<td width="8%" class="input_label2">预计还款日期:</td>
			                <td bgcolor="#ffffff">
			                	<input name="applaydate" id="applaydate" type="hidden" value="" /> 
			                	<input name="borrow.expectedRepaydate" id="expectedRepaydate" type="text" id="start" size="14" style="width:100%;" class="MyInput" readonly="readonly" />
						   	</td>
						   	<td class="input_label2">&nbsp;</td>
						   	<td bgcolor="#ffffff">&nbsp;</td>
						   	<td class="input_label2">&nbsp;</td>
						   	<td bgcolor="#ffffff">
						   		<div id="updatemaniddiv" style="display:none;"><s:select name="borrow.updatemanid" id="updatemanid"  list="#request.approvePerson" listKey="userid" listValue="username" style="width:100%;"></s:select></div>
						   	</td>
						</tr>
						<tr id="approveTr">
							<td class="input_label2" id="approveTd" width="9%">财务审核人员:</td>
							<td bgcolor="#FFFFFF">
								<s:select name="borrow.updatemanid" id="updatemanid"  list="#request.approvePerson" listKey="userid" listValue="username" style="width:100%;"></s:select>
							</td>
							<td width="8%" class="input_label2">预计还款日期:</td>
			                <td bgcolor="#ffffff">
			                	<input name="borrow.expectedRepaydate" id="expectedRepaydate" type="text" id="start" size="14" style="width:100%;" class="MyInput" readonly="readonly" />
						   	</td>
						   	<td class="input_label2">&nbsp;</td>
						   	<td bgcolor="#ffffff">&nbsp;</td>
						</tr>
						<tr>
							<td class="input_label2">借款原因：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="borrow.borrowReason" id="borrowReason" cols="77" rows="3" style="width:100%" ><s:property value="borrow.borrowReason"/></textarea>
							</td>
						</tr>
					</table> 
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
					<input type="button" name="return"
					value='<s:text  name="grpInfo.ok"/>' class="MyButton"
					onclick="save();" image="../../images/share/f_closed.gif">
					<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
