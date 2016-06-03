<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>

<script type="text/javascript">
	var listIndex = 0;
	var paytotal = 0;
	$(function(){
		paytotal = parseFloat($("#paytotal").val());
	});
/* 	function addRow() {
		$("#paymentTable").append(
			"<tr><td width='2%' bgcolor='#FFFFFF'><input type='checkBox' /></td>"
				+ "<td style='width:8%'><input type='text' value=<s:property value='prjContract.client' /> name='paymentList["+ listIndex +  "].client'/></td>"
				+ "<td style='width:5%'><input type='text' value=<s:property value='prjContract.prjName' /> name='paymentList["+ listIndex +  "].prjName' class='MyInput'/></td>"
				+"<td style='white-space:nowrap;width:5%'><input class='paymentDate'  name='paymentList["+ listIndex +  "].paymentDate' type='text' id='startDate' class='MyInput' isSel='true' isDate='true'"
				+ "	onFocus='ShowDate(this)' dofun='ShowDate(this)' value='<s:date name='paymentList["+ listIndex +  "].paymentDate' format='yyyy-MM-dd'/>' /></td>"
				+ "<td style='width:3%'><input class='payment' onblur='count()' type='text' name='paymentList["+ listIndex +  "].payment'/></td>"
				+ "<td style='width:3%'><input class='updateman' type='text' name='paymentList["+ listIndex +  "].updateMan'/></td>"
				+ "<td style='width:8%;white-space:nowrap'><input type='text' class='input_readonly' readonly /></td>" 
				+ "<td style='width:10%'><input type='text'name='paymentList["+ listIndex +  "].note'/></td></tr>");
		listIndex++;
	} */
	function save() {
			if(isNull()==true){
				return;
			}
			if(isNum($("#payment").val())==false){
				return;
			}
			
			var total = $("#total").val()-0;
			var payment = $("#paytotal").val()-0;
			if(Number(total)<Number(payment)){
				alert("交付金额不能大于合同金额，请填写正确的金额数");
				document.getElementById("payment").focus();
				return ;
			} 
				document.getElementById("ok").disabled = true;
				window.returnValue = true;
				entryInfoForm.submit();	
	}
/* 	function delRow(){
		$("input:checkbox:checked").each(function(){
			$(this).parent().parent().remove();
		});
		count();
	} */
	function count(){
		var total = $("#total").val();
		var paytotalTemp = paytotal;
		var flag = true;
		$(".payment").each(function(){
			flag = isNum($(this).val());	
			if(flag==false){
				$(this).focus();
				return false;
			}
			var payment = parseFloat($(this).val());
			if(payment!=""){
				paytotalTemp+=payment;
			}
		});
		if(flag==true){			
		 	$("#paytotal").val(paytotalTemp.toFixed(2));
		}
	}
	/* function closeModal(){
 		if(!confirm('您确认关闭此页面吗？')){
			return;				
		}
	 	window.close();
	} */
	function isNum(val){
		var reg =/^\d+(\.\d+)?$/;//非负实数
		var flag = reg.test(val); 
		if(flag==false){
			alert("回款金额错误：请输入非负实数！");
		}
		return flag;
	}
	function isNull(){
		if($("#paymentDate").val()==""){
			alert("回款日期不能为空!");
			return true;
		}else if($("#payment").val()==""){
			alert("回款金额不能为空!");
			return true;
		}else if($("#updateMan").val()==""){
			alert("收款人不能为空!");
			return true;
		}
		return false;
	}
	
	function cal(){
		var payment = $("#payment").val();
		if(payment!=""){
			var temp =  parseFloat($("#payment").val()) + parseFloat( paytotal);
			$("#paytotal").val(temp.toFixed(2));
		}else{
			$("#paytotal").val(paytotal);
		}

	}
</script>
</head>
<body>
	<form name="entryInfoForm"
		action="<%=request.getContextPath()%>/pages/prjContract/prjContractInfo!addPayment.action"
		method="post">
		<input name="prjContractPayment.contractId" type="hidden"
			value='<s:property value="prjContract.id"/>' />
		<table align="center" cellPadding="0" cellSpacing="0" width="90%">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">合同信息</td>
						</tr>
						<input name="prjContract.Id" type="hidden"
							value='<s:property value="prjContract.Id"/>'>
						<tr>
							<td class="input_label2" width="13%">
								<div align="center">
									项目名称<font color="#FF0000">*</font>
								</div>
							</td>
							<td class="input_label2" width="25%"><s:textfield
									readonly="true" name="prjContract.prjName" maxlength="16"
									id="prjName" size="25%" />
							</td>
							<td width="13%" class="input_label2">
								<div align="center">
									项目经理<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%"><s:textfield
									name="prjContract.prjManager" size="25" readonly="true"
									id="prjManager" />
							</td>

						</tr>

						<tr>
							<td class="input_label2">
								<div align="center">
									合同金额<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF"><s:textfield name="prjContract.total"
									readonly="true" size="20%" id="total"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')"
									onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" /> <span>元</span>
							</td>
							<td class="input_label2">
								<div align="center">
									已付金额 <font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="3"><s:textfield
									readonly="true" name="prjContract.payment" size="20%"
									id="paytotal"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')"
									onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" /> <span>元</span>
							</td>
						</tr>




						<tr>
							<td class="input_label2">
								<div align="center">
									合同状态<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF"><s:textfield name="prjContract.status"
									readonly="true" size="20%" id="status" />
							</td>
							<td class="input_label2" width="13%">
								<div align="center">
									甲方 <font color="#FF0000">*</font>
								</div>
							</td>
							<td width="14%" bgcolor="#FFFFFF"><s:textfield
									readonly="true" name="prjContract.client" maxlength="50"
									size="20%" id="client" />
							</td>
						</tr>

						<tr>
							<td class="input_label2">
								<div align="center">
									合同进度<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF"><s:textfield name="prjContract.fare"
									readonly="true" maxlength="20" size="20" id="fare" />
							</td>
							<td class="input_label2">
								<div align="center">
									项目开始时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="3" bgcolor="#FFFFFF"><input type="text"
								id="startDate" readonly="true" name="prjContract.startDate"
								value='<s:date name="prjContract.startDate" format="yyyy-MM-dd"/>'>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									项目结束时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF"><input name="prjContract.endDate"
								readonly="true" type="text" id="endDate"
								value='<s:date name="prjContract.endDate" format="yyyy-MM-dd"/>'>
							</td>
							<td class="input_label2">
								<div align="center">
									项目交付时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="3" bgcolor="#FFFFFF"><input
								name="prjContract.finishDate" type="text" id="finishDate"
								readonly="true"
								value='<s:date name="prjContract.finishDate" format="yyyy-MM-dd"/>'>
							</td>
						</tr>
						<tr> 
						<td class="input_label2">
								<div align="center">
									项目签订时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="3" bgcolor="#FFFFFF"><input
								name="prjContract.signDate" type="text" id="finishDate"
								readonly="true"
								value='<s:date name="prjContract.signDate" format="yyyy-MM-dd"/>'>
							</td>
        				</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									风险<font color="#FF0000"></font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<s:textarea name="prjContract.risk" readonly="true" cols="77"
										rows="3" id="risk" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<s:textarea name="prjContract.note" readonly="true" cols="77"
										rows="3" id="note" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table align="center" width="90%" cellPadding="1" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">新增回款信息</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									回款日期&nbsp;<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF" nowrap="nowrap"><input
								name='prjContractPayment.paymentDate' type="text" id="paymentDate"
								class="MyInput" size="18"
								value='<s:date name='prjContractPayment.paymentDate' format='yyyy-MM-dd'/>'>
							</td>
							<td class="input_label2">
								<div align="center">
									本次回款金额&nbsp;<span>(元)</span><font color="#FF0000">*</font>
								</div> 
							</td>
							<td nowrap="nowrap" bgcolor="#FFFFFF"><s:textfield
									name="prjContractPayment.payment" size="20%" id="payment"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'');cal()"
									onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" /></td>
							<td class="input_label2">
								<div align="center">
									收款人&nbsp;<font color="#FF0000">*</font>
								</div></td>
							</td>
							<td bgcolor="#FFFFFF"><s:textfield id="updateMan"
									name="prjContractPayment.updateMan" /></td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<s:textarea name="prjContractPayment.note" cols="83" rows="3"
										id="note" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
			<table width="90%"
			style="border: 1px solid #CCCCCC; margin-top: 10px;" align="center"
			padding="1" cellspacing="1" bgcolor="#CCCCCC" id="paymentTable">
			<tr>
				<td class="input_tablehead2" colspan="15">回款明细表</td>
			</tr>
			<tr>
				<%-- 				<td width="2%" class="column_label"><span>序号</span>
				</td> --%>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">客户名称</div></td>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">项目名称</div></td>
				<td  width="10%" class="oracolumncenterheader"><div
						align="center">
						回款日期&nbsp;<font color="#FF0000">*</font>
					</div></td>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">
						回款金额&nbsp;<span>(元)</span><font color="#FF0000">*</font>
					</div></td>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">
						收款人&nbsp;<font color="#FF0000">*</font>
					</div></td>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">更新时间</div></td>
				<td  width="5%" class="oracolumncenterheader"><div
						align="center">备注</div></td>
			</tr>
			<s:iterator  value="paymentList" id="paymentList" status="row">
				<tr>
					<td width="5%" style="background-color: #FFFFFF" align="center"><s:property value="client" />
					</td>
					<td width="5%"  style="background-color: #FFFFFF" align="center"><s:property value="prjName" />
					</td>
					<td width="10%"  style="background-color: #FFFFFF" align="center"><s:date name="paymentDate" format="yyyy-MM-dd" />
					</td>
					<td width="5%"  style="background-color: #FFFFFF" align="center"><s:property value="payment" />
					</td>
					<td width="5%"  style="background-color: #FFFFFF" align="center"><s:property value="updateMan" />
					</td>
					<td width="5%"  style="background-color: #FFFFFF" align="center"><s:date name="upDateTime" format="yyyy-MM-dd"/></td>
					<td width="5%"  style="background-color: #FFFFFF" align="center"><s:property value="note" />
					</td>
				</tr>
			</s:iterator>
		</table>
	</form>
	
</body>
</html>