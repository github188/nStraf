<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.util.UUID"%>
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
<title>打印报销费用单</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function printPage(){
		$("#buttonTable").hide();
		window.print();
		if(window.print()==true){
			$("#buttonTable").show();
		}
	}
	
	function save(){
		document.getElementById("ok").disabled=true;
		window.returnValue=true;
		expenseInfoForm.submit();
	}
	
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="expenseInfoForm" action="<%=request.getContextPath()%>/pages/expense/expenseInfo!saveExpensePrint.action" method="post">
		<table width="100%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="7" align="center">软件体系费用说明表 </td>
				<input name="expenseId" id="expenseId" type="hidden" value='<s:property value="#request.expenseAccount.id"/>'>
				<input name="printType" id="printType" type="hidden" value='cost'>
			</tr>
			<tr>
				<td colsapn="4">报销人： <s:property value="#request.expenseAccount.userName"/></td>
				<td colsapn="3">填写日期： <s:date name="#request.expenseAccount.submitDate" format="yyyy-MM-dd"/></td>
			</tr>
		</table>
		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="1">
			<tr>
				<td align="center" width="12%">类别/日期</td>
				<td align="center" width="8%">地点</td>
				<td align="center" width="20%">项目名称</td>
				<td align="center" width="8%">业务费</td>
				<td align="center" width="8%">市内交通费</td>
				<td align="center" width="8%">其他费用</td>
				<td align="center" width="36%">业务及其他费用说明（业务费的人名、职位、目的）</td>
			</tr>
			<s:iterator value="#request.travelDetailList" id="expenseInfo" status="row">
			<tr>
				<td align="center"><s:property value="ddTime" /></td>
				<td align="center">
					<s:if test="didian.length()>0"> 
						<s:property value="didian" />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td align="center">
					<s:if test="prjname.length()>0"> 
						<s:property value="prjname" />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td align="center"><s:property value="yewu" /></td>
				<td align="center"><s:property value="traffic" /></td>
				<td align="center"><s:property value="another" /></td>
				<td align="center">
					<s:if test="note.length()>0"> 
						<s:property value="note" />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
			</tr>
			</s:iterator>
			<tr id="travelSumTr">
				<td align="center"><s:property value="#request.travelSum.ddTime" /></td>
				<td align="center">&nbsp;</td>
				<td align="center">&nbsp;</td>
				<td align="center"><s:property value="#request.travelSum.yewu" /></td>
				<td align="center"><s:property value="#request.travelSum.traffic" /></td>
				<td align="center"><s:property value="#request.travelSum.another" /></td>
				<td align="center">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="3">部门经理审核：</td>
				<td colspan="3">主管领导审批：</td>
				<td>财务会计审核：</td>
			</tr>
		</table>
		<br />
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="import"
					id="import" value='打印' class="MyButton"
					onClick="printPage()" image="../../images/share/import.gif">
					<input type="button" name="ok" id="ok"
					value='确认是否打印' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
