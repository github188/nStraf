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
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
function save() {
	if ($('#status').val()=='1'){//财务审核不通过
		if($("#approveContent").val()==""){
			alert("请输入审核结论");
			return;
		}
	}else{//财务审核通过
		var approveSum = $("#approveSum").val().trim();
		if(approveSum==""){
			alert("请输入核准借款金额");
			return;
		}
		var testmoney = /^\d+(\.\d{1,2})?$/;
		if (!testmoney.test(approveSum)){
			alert("请输入正确的核准借款金额 ");
			return;
		}
		if(Number(approveSum)==0){
			alert("核准借款金额不能为0");
			return;
		}
		if($("#approveContent").val()==""){
			$("#approveContent").val("通过");
		}
		var amount = $("#amount").val();
		var approveSum = $("#approveSum").val();
		if(Number(amount)!=Number(approveSum)){
			if(!confirm("申请借款金额不等于核准借款金额，确定通过吗")){
				return;
			}
		}
	}
	if(!validateInputInfo()){
		return;
	}
	
	window.returnValue = true;
	reportInfoForm.submit();
}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	/*function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 50) {
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0,50);
					alert("你输入的字数超过50个了");
					document.getElementsByTagName("textarea")[i].focus();
				}
			}
		}
	}*/

	/*function listenKey() {
		if (document.addEventListener) {
			document.addEventListener("keyup", getKey, false);
		} else if (document.attachEvent) {
			document.attachEvent("onkeyup", getKey);
		} else {
			document.onkeyup = getKey;
		}
	}
	listenKey();*/
	
	$(function(){
		var getsum=$("#showamount").val();
		var showsum=fmoney(getsum,2);
		if(getsum!=""){
			$("#showamount").val(showsum);
		}
		if($("#approveSum").val()==""){
			$("#approveSum").val(getsum);
			$("#borrowApproveSum").val(showsum);
		}
		$("select[id='status']").change(function(){
			if($(this).val()=="1"){
				$("#approveSum").val('');
				$("#borrowApproveSum").val('');
			}else{
				if($("#approveSum").val()==""){
					var getsum=$("#showamount").val();
					var showsum=fmoney(getsum,2);
					$("#approveSum").val(getsum);
					if(getsum!=""){
						$("#borrowApproveSum").val(showsum);
					}
				}
			}
		});
	});
	
	function borrowAmounta(){
		var borrowAmount = $("#borrowApproveSum").val();
		$("#approveSum").val(borrowAmount);
		if(borrowAmount==""){
			return;
		}
		var getsum = fmoney(borrowAmount,2);
		$("#borrowApproveSum").val(getsum);
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
		var thisDate = $("#createdate").val();
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
		var begin = $("#createdate").val();
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
		action="<%=request.getContextPath()%>/pages/borrow/borrowInfo!update.action?type=audit" method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>	
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">借款财务审核</td>
							<input type="hidden" id="id" name="borrow.id" value='<s:property value="borrow.id"/>' />
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="4">
								姓名：<s:property value="borrow.userman"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								部门：<s:property value="borrow.detname"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								借款状态：
								<s:if test='#request.borrow.status=="0"'>财务审核通过，已发款，代还款</s:if>
								<s:elseif test='#request.borrow.status=="1"'>财务审核未通过</s:elseif>
								<s:elseif test='#request.borrow.status=="2"'>待审核</s:elseif>
								<s:elseif test='#request.borrow.status=="3"'>新增</s:elseif>
								<s:elseif test='#request.borrow.status=="4"'>还款结束</s:elseif>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								借款类型：
								<s:if test='#request.borrow.type=="0"'>出差</s:if>
								<s:elseif test='#request.borrow.type=="1"'>备用金</s:elseif>
								<input type="hidden" id="borrowId" name="borrowId" value="<s:property value="borrow.id"/>" />
								<input type="hidden" id="borrow.id" name="id" value="<s:property value="borrow.id"/>" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								申请日期：<s:date name="borrow.createdate" format="yyyy-MM-dd"/>
							</td>
						</tr>
						<tr>
							<td class="input_label2">预计还款日期:</td>
			                <td bgcolor="#ffffff" style="width:30%;">
			                	<input name="createdate" id="createdate" type="hidden" value='<s:date name="borrow.createdate" format="yyyy-MM-dd"/>' />
			                	<input name="borrow.expectedRepaydate" id="expectedRepaydate" type="text" id="start" size="14" style="width:100%;" class="MyInput" readonly="readonly" value='<s:date name="borrow.expectedRepaydate" format="yyyy-MM-dd"/>' />
						   	</td>
						   	<td class="input_label2">出差地:</td>
							<td bgcolor="#FFFFFF" style="width:25%;">
								<input name="borrow.tripcity" type="text" id="tripcity" readonly="readonly" value='<s:property value="borrow.tripcity"/>'/>
							</td>
						</tr>					
						<tr>
							<td class="input_label2">申请借款金额:</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<input name="showamount" type="text" id="showamount" value='<s:property value="borrow.amount"/>' readonly="readonly"/>元
								<input name="borrow.amount" type="hidden" id="amount" value='<s:property value="borrow.amount"/>'/>
							</td>
						</tr>
						<tr>
							<td class="input_label2">借款原因：</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<textarea name="borrow.borrowReason" id="borrowReason" cols="77" rows="3" style="width:100%" readonly="readonly"><s:property value="borrow.borrowReason"/></textarea>
							</td>
						</tr>
						<tr>
							<td class="input_label2">审核结果：</td>
							<td bgcolor="#FFFFFF">
								<select id="status" name="borrow.status" style="width:100%;">
			                		<option value="0">财务审核通过，已发款，待还款</option>
			                		<option value="1">财务审核不通过</option>
			                	</select>
							</td>
							<td class="input_label2">核准借款金额：</td>
							<td bgcolor="#FFFFFF">
								<input name="borrow.approveSum" type="hidden" id="approveSum" value='<s:property value="borrow.approveSum"/>'/>
								<input name="borrowApproveSum" type="text" id="borrowApproveSum" onBlur="borrowAmounta();" value='' maxLength="7" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')"/>元
							</td>
						</tr>
						<tr>
							<td class="input_label2">审核结论：</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<textarea name="approveContent" cols="73" rows="4" id="approveContent" style="width:100%"></textarea>
							</td>
						</tr>
						<tr>
							<td class="input_label2">审核记录：</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<textarea name="record" cols="73" rows="4" id="record" style="width:100%" readonly="readonly"><s:property value="#request.record"/></textarea>
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
