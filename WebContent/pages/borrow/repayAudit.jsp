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
		if($("replydate").val()==""){
			alert("请填写还款日期");
			return;
		}
		if($("#replyamount").val()==""){
			alert("请填写还款金额");
			return;
		}
		if(Number($("#replyamount").val())==0){
			alert("还款金额不能为0");
			return;
		}
		var testmoney = /^\d+(\.\d{1,2})?$/;
		if (!testmoney.test($('#replyamount').val().trim())){
			alert("请输入正确的还款金额 ");
			return;
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
				if (tmpStr.length > 1000) {
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0,1000);
					alert("你输入的字数超过1000个了");
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
	listenKey();*/
	
	$(function(){
		var getsum=$("#amount").val();
		var showsum=fmoney(getsum,2);
		if(getsum!=""){
			$("#showamount").html(showsum);
		}
		var getsum=$("#approveSum").val();
		var showsum=fmoney(getsum,2);
		if(getsum!=""){
			$("#showapproveSum").html(showsum);
		}
		var replyAmount="<%=request.getAttribute("replyAmount")%>";
		$("#reply_amount").val(replyAmount);
	});
	
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
	
	function calResidueAmount(){
		var amount = $("#approveSum").val();
		var replyamount = $("#replyamount").val();
		var reply_amount = $("#reply_amount").val();
		var residueAmount = Number(amount)-Number(replyamount)-Number(reply_amount);
		if(Number(residueAmount)<0){
			alert("还款金额和超出核准借款金额");
			$("#replyamount").val('');
			$("#replyamount").focus();
			return;
		}
		$("#residueamount").val(residueAmount);
		residueAmount = fmoney(residueAmount,2);
		$("#residue_amount").val(residueAmount);
	}
	
	function validateInputInfo() {
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var thisDate = $("#createdate").val();
		var endDate = $("#replydate").val();
		if (thisDate.length > 0 && endDate.length > 0) {
			var v = re.test(endDate);
			var a = re1.test(thisDate);
			if (!v || !a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			if (!DateValidate()) {
				alert('还款日期不能小于借款日期，请重新输入！');
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
		var end = $("#replydate").val();
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
		action="<%=request.getContextPath()%>/pages/borrow/borrowInfo!updateAudit.action" method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>	
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">编辑还款信息</td>
							<input type="hidden" id="id" name="borrow.id" value='<s:property value="borrow.id"/>' />
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="6">
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
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								申请日期：
								<s:date name="borrow.createdate" format="yyyy-MM-dd"/>
							</td>
						</tr>
						<tr>
							<td class="input_label2">申请借款金额:</td>
							<td bgcolor="#FFFFFF" style="width:18%;"">
								<input name="amount" type="hidden" id="amount" value='<s:property value="borrow.amount"/>'/>
								<span id="showamount"></span>元
							</td>
						   	<td class="input_label2">出差地:</td>
							<td bgcolor="#FFFFFF" style="width:19%;">
								<s:property value="borrow.tripcity"/>
							</td>
							<td class="input_label2">借款类型:</td>
			                <td bgcolor="#ffffff" style="width:18%;">
			                	<s:if test='#request.borrow.type=="0"'>出差</s:if>
								<s:elseif test='#request.borrow.type=="1"'>备用金</s:elseif>
						   	</td>
						</tr>					
						<tr>
							<td class="input_label2">核准借款金额:</td>
							<td bgcolor="#FFFFFF">
								<input name="approveSum" type="hidden" id="approveSum" value='<s:property value="borrow.approveSum"/>'/>
								<span id="showapproveSum"></span>元
							</td>
							<td class="input_label2">借款日期:</td>
			                <td bgcolor="#ffffff">
			                	<s:date name="borrow.borrowdate" format="yyyy-MM-dd"/>
						   	</td>
							<td class="input_label2">预计还款日期:</td>
			                <td bgcolor="#ffffff">
			                	<input name="createdate" id="createdate" type="hidden" value='<s:date name="borrow.borrowdate" format="yyyy-MM-dd"/>' />
			                	<s:date name="borrow.expectedRepaydate" format="yyyy-MM-dd"/>
						   	</td>
						</tr>
						<tr>
							<td class="input_label2">借款原因：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="borrow.borrowReason" id="borrowReason" cols="77" rows="3" style="width:100%" readonly="readonly"><s:property value="borrow.borrowReason"/></textarea>
							</td>
						</tr>
					</table> 
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">分期还款明细</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr> 
							<td nowrap width="10%" class="oracolumncenterheader"><div align="center">还款日期</div></td>
							<td nowrap width="10%" class="oracolumncenterheader"><div align="center">金额</div></td>
							<td nowrap width="80%" class="oracolumncenterheader"><div align="center">备注</div></td>
						</tr>
						<s:iterator value="#request.detailList" id="detailinfo" status="row">
							<tr>
								<td nowrap width="10%" align="center" bgcolor="#ffffff"><s:date name="replydate" format="yyyy-MM-dd" /></td>
							 	<td nowrap width="10%" align="center" bgcolor="#ffffff"><s:property value="amount" /></td>
							 	<td nowrap width="80%" align="left" title='<s:property value="note" />' bgcolor="#ffffff">
							 		<s:if test="note.length()>50"><s:property value="note.substring(0,50)+'...'" /> </s:if>
									<s:else><s:property value="note"/>&nbsp;</s:else>
							 	</td>
							 </tr>
						</s:iterator>
					</table> 
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">新增分期还款</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_label2">还款日期:</td>
							<td bgcolor="#FFFFFF" style="width:18%;">
								<input name="replydate" type="text" id="replydate" value='' class="MyInput" readonly="readonly"/>
							</td>
						   	<td class="input_label2">还款金额:</td>
							<td bgcolor="#FFFFFF" style="width:19%;">
								<input name="replyamount" type="text" id="replyamount" value='' maxLength="7" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" onBlur="calResidueAmount()"/>
								<input name="reply_amount" type="hidden" id="reply_amount" value="" />
							</td>
							<td class="input_label2">剩余待还金额:</td>
							<td bgcolor="#FFFFFF" style="width:18%;">
								<input name="residue_amount" type="text" id="residue_amount" value='' readonly="readonly" />
								<input name="residueamount" type="hidden" id="residueamount" value='' readonly="readonly" />
							</td>
						</tr>					
						<tr>
							<td class="input_label2">备注：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="replynote" id="replynote" cols="77" rows="3" style="width:100%" ></textarea>
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
