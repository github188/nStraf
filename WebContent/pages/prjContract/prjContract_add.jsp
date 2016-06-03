<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.lang.Integer"%>
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
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
		/* function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？')){
				return;				
			}
		 	window.close();
		} */
		function save(){
			if(document.getElementById("prjName").value==""){
				alert("请填写项目名称");
				document.getElementById("prjName").focus();
				return ;
			}
			if(document.getElementById("prjManager").value==""){
				alert("请填写项目经理");
				document.getElementById("prjManager").focus();
				return ;
			}
			if(document.getElementById("total").value==""){
				alert("请填写项目金额");
				document.getElementById("total").focus();
				return;
			}
			if(document.getElementById("payment").value==""){
				alert("请填写项目交付金额");
				document.getElementById("payment").focus();
				return;
			}
			if(document.getElementById("status").value==""){
				alert("请填写合同状态");
				document.getElementById("status").focus();
				return;
			}
			if(document.getElementById("client").value==""){
				alert("请填写甲方人员");
				document.getElementById("client").focus();
				return;
			}
			if(document.getElementById("fare").value==""){
				alert("请填写合同进度");
				document.getElementById("fare").focus();
				return;
			}
			if(document.getElementById("startDate").value==""){
				alert("请填写项目实施时间");
				return;
			}
			if(document.getElementById("endDate").value==""){
				alert("请填写项目结束时间");
				return;
			}
			if(document.getElementById("finishDate").value==""){
				alert("请填写项目交付时间");
				return;
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var finishDate = $("#finishDate").val();
			var total = $("#total").val()-0;
			var payment = $("#payment").val()-0;
			if(startDate>endDate){
				alert("项目实施时间不能大于项目结束时间！");
				return ;
			}
			if(startDate>finishDate){
				alert("项目实施时间不能大于项目交付时间！");
				return;
			}
			 if(Number(total)<Number(payment)){
				 document.getElementById("payment").focus();
				alert("交付金额不能大于合同金额，请填写正确的金额数");
				return ;
			} 
			document.getElementById("ok").disabled=true;
			window.returnValue=true;
			prjContractInfoForm.submit();
		}	
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
<div style="height:450px;overflow-y:auto;">
	<form name="prjContractInfoForm"
		action="<%=request.getContextPath()%>/pages/prjContract/prjContractInfo!save.action"
		method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" /> <input
			type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">添加合同信息</td>
						</tr>
						<input name="prjContract.Id" type="hidden"
							value='<s:property value="prjContract.Id"/>'>
						<tr>
							<td class="input_label2" width="13%">
								<div align="center">
									项目名称<font color="#FF0000">*</font>
								</div></td>
							<td align="left" bgcolor="#FFFFFF" width="25%"><s:select list="groupNameList" id="prjName" name="prjContract.prjName"
									headerKey="" headerValue="请选择"></s:select>
							</td>
							<td width="13%" class="input_label2">
								<div align="center">
									项目经理<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF" width="25%">
								<%-- <s:textfield name="prjContract.prjManager" size="25" id="prjManager"/> --%>
								<select name="prjContract.prjManager" id="prjManager">
									<option value="">
										&nbsp;
										<s:text name="notify.option"></s:text>
										&nbsp;
									</option>
									<s:iterator value="#request.groupManager" var="list">
										<option value="<s:property value='username'/>">
											<s:property value="username" />
										</option>
									</s:iterator>
							</select></td>

						</tr>

						<tr>
							<td align="center" class="input_label2">
								<div align="center">
									合同金额<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><s:textfield name="prjContract.total"
									size="20" id="total"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')"
									onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" />&nbsp;元</td>
							<td class="input_label2">
								<div align="center">
									已付金额 <font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF" colspan="3"><s:textfield
									name="prjContract.payment" size="20" id="payment"
									onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')"
									onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" />&nbsp;元</td>
						</tr>




						<tr>
							<td align="center" class="input_label2">
								<div align="center">
									合同状态<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><select name="prjContract.status"
								id="status" align="left" style="width: 60%" disabled="true">
									<option value='新增' selected="true">新增</option>
									<option value='执行中'>执行中</option>
									<option value='意外中止'>意外中止</option>
									<option value='结束'>结束</option>
							</select></td>
							<%-- <td bgcolor="#FFFFFF">
								<s:textfield name="prjContract.status" size="20%" id="status"/>
							</td> --%>
							<td class="input_label2" width="13%">
								<div align="center">
									甲方 <font color="#FF0000">*</font>
								</div></td>
							<td width="14%" bgcolor="#FFFFFF"><s:textfield
									name="prjContract.client" size="20%" maxlength="50" id="client" />
							</td>
						</tr>

						<tr>
							<td class="input_label2">
								<div align="center">
									合同进度<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><s:textfield name="prjContract.fare"
									maxlength="20" size="20" id="fare" /></td>
							<td class="input_label2">
								<div align="center">
									项目开始时间<font color="#FF0000">*</font>
								</div></td>
							<td colspan="3" bgcolor="#FFFFFF"><input
								name="prjContract.startDate" type="text" id="startDate"
								class="MyInput"
								value='<s:date name="prjContract.startDate" format="yyyy-MM-dd"/>'>
							</td>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									项目结束时间<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><input name="prjContract.endDate"
								type="text" id="endDate" class="MyInput" 
								value='<s:date name="prjContract.endDate" format="yyyy-MM-dd"/>'>
							</td>
							<td class="input_label2">
								<div align="center">
									项目交付时间<font color="#FF0000">*</font>
								</div></td>
							<td colspan="3" bgcolor="#FFFFFF"><input
								name="prjContract.finishDate" type="text" id="finishDate"
								class="MyInput" 
								value='<s:date name="prjContract.finishDate" format="yyyy-MM-dd"/>'>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									项目签订时间<font color="#FF0000">*</font>
								</div></td>
							<td colspan="3" bgcolor="#FFFFFF"><input
								name="prjContract.signDate" type="text" id="signDate"
								class="MyInput" 
								value='<s:date name="prjContract.signDate" format="yyyy-MM-dd"/>'>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									合同风险<font color="#FF0000"></font>
								</div></td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<s:textarea name="prjContract.risk" cols="81" rows="3"
										onkeyup="limitLen(500,this)" id="risk" />
								</div></td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>
								</div></td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<s:textarea name="prjContract.note"
										onkeyup="limitLen(500,this)" cols="81" rows="3" id="note" />
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
		<br /> <br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>
