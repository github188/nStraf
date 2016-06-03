<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../js/Validator.js"></script>
	<script language="javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript">
function closeModal(){
 window.close();
}
</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form action="/pages/rule/specialRegulation!show.action" method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0"
				class="popnewdialog1">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="rule.showSpecialRegulation"/>
							</legend>
							<s:iterator value="specialRegulation" id="specialRegulation">
								<table width="100%" align="center">
									<tr>
									<td>
									<s:text name="rule.moneyType"/>
									<s:text name="label.colon"/>
									</td>
									<td>
									<tm:dataDir beanName="specialRegulation" property="moneyType"
										path="ruleMgr.moneyType" />
									</td>
									</tr>
									<tr>
									<td>
									<s:text name="rule.moneyDenomination"/>
									<s:text name="label.colon"/>
									</td>
									<td>
									<tm:dataDir beanName="specialRegulation" property="moneyDenomination"
										path="ruleMgr.moneyDenomination"/>
									</td>
									</tr>
									<tr>
									<td>
									<s:text name="rule.source"/>
									<s:text name="label.colon"/>
									</td>
									<td>
									<tm:dataDir beanName="specialRegulation" property="source"
										path="transMgr.cometype"/>
									</td>
									</tr>
									<tr>
									<td>
									<s:text name="rule.result"/>
									<s:text name="label.colon"/>
									</td>
									<td>
									<s:property value="result"/>
									</td>
									</tr>
									<tr>
									<td>
									<s:text name="rule.specialType"/>
									<s:text name="label.colon"/>
									</td>
									<td>
									<tm:dataDir beanName="specialRegulation" property="specialType"
										path="ruleMgr.specialType"/>
									</td>
									</tr>
									<tr>
										<td>
											<s:text name="rule.regulation"/>
											<s:text name="label.colon"/>
										</td>
										<td>
											<s:property value="regulation"/>
										</td>
								</table>
							</s:iterator>
						</fieldset>
					</td>
				</tr>
			</table>
			<br />
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td align="center">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
		</form>

	</body>
</html>

