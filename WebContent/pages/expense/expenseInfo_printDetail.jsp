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
<title>打印报销明细单</title>
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
		//空回调函数
		function query(){
			//alert(123);
		}
		function save(){
			document.getElementById("ok").disabled=true;
			window.returnValue=true;
			expenseInfoForm.submit();
		}
		
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="expenseInfoForm" action="<%=request.getContextPath()%>/pages/expense/expenseInfo!saveExpensePrint.action" method="post">
		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="14" align="center">软件体系出差费用报销明细表 </td>
				<input name="expenseId" id="expenseId" type="hidden" value='<s:property value="#request.expenseAccount.id"/>'>
				<input name="printType" id="printType" type="hidden" value='detail'>
			</tr>
			<tr>
				<td colspan="14" align="right">填写日期：<s:date name="#request.expenseAccount.submitDate" format="yyyy-MM-dd"/> </td>
			</tr>
		</table>
		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="1">
			<tr style="height:50px;">
				<td align="center" width="8%">类别/日期</td>
				<td align="center" width="5%">地点</td>
				<td align="center" width="12%">项目名称</td>
				<td align="center" width="10%">出差任务</td>
				<td align="center" width="7%">往返车船<br/>飞机费</td>
				<td align="center" width="6%">出租<br/>车</td>
				<td align="center" width="6%">公共汽车<br/></td>
				<td align="center" width="6%">住<br/>宿<br/>费</td>
				<td align="center" width="6%">通<br/>讯<br/>费</td>
				<td align="center" width="6%">业务<br/>费用</td>
				<td align="center" width="6%">其<br/>它</td>
				<td align="center" width="6%">补助</td>
				<td align="center" width="6%">小计</td>
				<td align="center" width="10%">公交<br/>线路</td>
			</tr>
			<s:iterator value="#request.expenselist" id="expenseInfo" status="row">
			<tr>
				<td align="center"><s:property value="dateTime" /></td>
				<td align="center"><s:property value="place" /></td>
				<td align="center">
					<s:if test="prjname.length()>0"> 
						<s:property value="prjname" />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td align="center"><s:property value="task" /></td>
				<td align="center"><s:property value="fly" /></td>
				<td align="center"><s:property value="taxi" /></td>
				<td align="center"><s:property value="bus" /></td>
				<td align="center"><s:property value="living" /></td>
				<td align="center"><s:property value="contact" /></td>
				<td align="center"><s:property value="business" /></td>
				<td align="center"><s:property value="other" /></td>
				<td align="center"><s:property value="buzhu" /></td>
				<td align="center"><s:property value="account" /></td>
				<td align="center">
					<s:if test="path.length()>0"> 
						<s:property value="path" />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
			</tr>
			</s:iterator>
			<tr id="costSumTr">
				<td align="center">合计</td>
				<td align="center">&nbsp;</td>
				<td align="center">&nbsp;</td>
				<td align="center">&nbsp;</td>
				<td align="center"><s:property value="#request.costSum.fly" /></td>
				<td align="center"><s:property value="#request.costSum.taxi" /></td>
				<td align="center"><s:property value="#request.costSum.bus" /></td>
				<td align="center"><s:property value="#request.costSum.living" /></td>
				<td align="center"><s:property value="#request.costSum.contact" /></td>
				<td align="center"><s:property value="#request.costSum.business" /></td>
				<td align="center"><s:property value="#request.costSum.other" /></td>
				<td align="center"><s:property value="#request.costSum.buzhu" /></td>
				<td align="center"><s:property value="#request.costSum.account" /></td>
				<td align="center">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="4">部门经理审核：</td>
				<td colspan="5">财务审核：</td>
				<td>报销人</td>
				<td colsapn="4"><s:property value="#request.expenseAccount.userName"/></td>
			</tr>
			<tr>
				<td colspan="4">日期：</td>
				<td colspan="5">日期：</td>
				<td>日期：</td>
				<td colsapn="4"><s:date name="#request.expenseAccount.submitDate" format="yyyy-MM-dd"/></td>
			</tr>
		</table>
		<br />
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center" id="buttonTable">
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
