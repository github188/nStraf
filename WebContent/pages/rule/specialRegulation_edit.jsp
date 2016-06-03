<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*,cn.grgbanking.feeltm.domain.SpecialRegulation,java.lang.*" %>
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
function validate(){
	var result = document.getElementById("result").value;
	if(result==null||result==""){
		alert('<s:text name="rule.resultNotNull"/>');
		return false;
	}
	
	//moneyType moneyDenomination source result regulation specialType
	//var moneyType = document.getElementById("moneyType").value;
	//var moneyDenomination = document.getElementById("moneyDenomination").value;
	//var source = document.getElementById("source").value;
	//var result = document.getElementById("result").value;
	//var regulation = document.getElementById("regulation").value;
	//var specialType = document.getElementById("specialType").value;
	//if(moneyType==""){
	//	alert('<s:text name="rule.pleaseSelectMoneyType"/>');
	//	return false;
	//}
	//if(moneyDenomination==""){
	//	alert('<s:text name="rule.pleaseSelectmoneyDenomination"/>');
	//	return false;
	//}
	//if(source==""){
	//	alert('<s:text name="rule.pleaseSelectSource"/>');
	//	return false;
	//}
	//if(result==""){
	//	alert('<s:text name="rule.pleaseInputResult"/>');
	//	return false;
	//}
	//if(regulation==""){
	//	alert('<s:text name="rule.pleaseInputRegulation"/>');
	//	return false;
	//}
	//if(specialType==""){
	//	alert('<s:text name="rule.pleaseSelectSpecialType"/>');
	//	return false
	//}
	return true;
}

function update(){
	//alert("it is a test1");
	if(!validate()){
		//alert("it is a test2");
	return false;
	}
	//alert("it is a test3");
	var specialType=document.getElementById("specialType").value;
	//alert("it is a test4");
	var regulation3 = document.getElementById("regulation3");
	//alert("it is a test5");
	if(specialType=="单规则"){
	var regulation = document.getElementById("regulation");
	regulation.value=regulation3.value;
	if(regulation.value.length!=10){
		alert('<s:text name="rule.specialRegulationLength"/>');
		return false;
	}
	}
	if(specialType=="范围规则"){
	var regulation1 = document.getElementById("regulation1");
	var regulation2 = document.getElementById("regulation2");
	var regulation = document.getElementById("regulation");
	//alert("it is a test6");
	regulation.value=regulation1.value+"-"+regulation2.value;
	//alert("it is a test7");
	if(regulation.value.length!=21){
		alert('<s:text name="rule.specialRegulationLength"/>');
		return false;
	}
	}
specialRegulationForm.submit();
}
</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name=specialRegulationForm
			action="<%=request.getContextPath()%>/pages/rule/specialRegulation!update.action"
			method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0"
				class="popnewdialog1">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="rule.updateSpecialRegulaton" />
							</legend>
							<s:iterator value="specialRegulation" id="specialRegulation">
								<input type="hidden" name="id" value="<s:property value='id' />" />
								<table width="100%" align="center">
									<tr>
										<td>
											<s:text name="rule.moneyType" />
											<s:text name="label.colon" />
										</td>
										<td>
											<tm:tmSelect name="moneyType" id="moneyType"
												selType="dataDir" path="ruleMgr.moneyType"
												beanName="specialRegulation" />
											<font color="#FF0000">*</font>
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="rule.moneyDenomination" />
											<s:text name="label.colon" />
										</td>
										<td>
											<tm:tmSelect name="moneyDenomination" id="moneyDenomination"
												selType="dataDir" beanName="specialRegulation"
												path="ruleMgr.moneyDenomination" />
											<font color="#FF0000">*</font>
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="rule.source" />
											<s:text name="label.colon" />
										</td>
										<td>
											<tm:tmSelect name="source" id="source"
												beanName="specialRegulation" selType="dataDir"
												path="transMgr.cometype" />
											<font color="#FF0000">*</font>
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="rule.result" />
											<s:text name="label.colon" />
										</td>
										<td>
											<input name="result" type="text"
												value='<s:property value="result"/>' id="result" size="30" />
											<font color="#FF0000">*</font>
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="rule.specialType" />
											<s:text name="label.colon" />
										</td>
										<td>
											<input name="specialType" type="text"
											value='<tm:dataDir 
												beanName="specialRegulation" property="specialType" 
												path="ruleMgr.specialType" />' id="specialType" size="30" readonly="readonly"/>
											
										</td>
									</tr>


									<tr>
										<td>
											<s:text name="rule.regulation" />
											<s:text name="label.colon" />
											<input type="hidden" id="regulation" name="regulation"/>
										</td>
										<%
										String specialType=request.getAttribute("specialType").toString();
										String regulation = request.getAttribute("regulation").toString();
										if("0".equals(specialType)){
											
										%>
										<td>
				
										<input name="regulation3" type="text" id="regulation3"
											size="30" maxlength="10"
											value="<%=regulation %>"/>
										</td>
						
										<%
										}
										if("1".equals(specialType)){
											String[] arr = regulation.split("-");
											String str1 = arr[0];
											String str2 = arr[1];
										%>
										<td>
				
										<input name="regulation1" type="text" id="regulation1"
											size="13" maxlength="10"
											value="<%=str1 %>" />-
										<input name="regulation2" type="text" id="regulation2"
											size="13" maxlength="10"
											value="<%=str2 %>" />
										</td>
										<%
										}
										%>
			
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
						<input type="button" name="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onclick="update()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
		</form>

	</body>
</html>

