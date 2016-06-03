<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%><html>
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
function f1(){
	var specialType =document.getElementById("specialType").value;
	var r1 = document.getElementById("r1");
	var r2 = document.getElementById("r2");
	if(specialType==1){
		r1.style.visibility="hidden";
		r1.style.display="none";
		r2.style.visibility="visible";
		r2.style.display="";
	}
	if(specialType==0){
		r1.style.visibility="visible";
		r1.style.display="";
		r2.style.visibility="hidden";
		r2.style.display="none";
	}
}
function validateSpecialRegulation(){
	var result = document.getElementById("result").value;
	var regulation = document.getElementById("regulation").value;
	var regulation1 = document.getElementById("regulation1").value;
	var regulation2 = document.getElementById("regulation2").value;
	var regulation3 = document.getElementById("regulation3").value;
	regulation = regulation1+regulation2+regulation3;
	if(result==""||result==null){
		alert('<s:text name="rule.resultNotNull"/>');
		return false;
	}
	if(regulation==""||result==null){
		alert('<s:text name="rule.regulationNotNull"/>');
		return false;
	}
	var a1 = regulation1.length;
	var a2 = regulation2.length;
	var a3 = regulation3.length;
	var a = a1+a2;
	if(a!=20&&a3!=10){
		alert('<s:text name="rule.specialRegulationLength"/>');
		return false;
	}
	return true;
}
function save(){
	//window.returnValue=true;
	
	blackRegulationForm.submit();
}
function SubmitForm(){
if(validateSpecialRegulation()){
	var specialType=document.getElementById("specialType").value;
	var regulation = document.getElementById("regulation");
	if(specialType==1){
		var regulation1 = document.getElementById("regulation1");
		var regulation2 = document.getElementById("regulation2");
		if(regulation1.value>=regulation2.value){
			alert('<s:text name="rule.specialRegulationRaise"/>');
			return;
		}
		regulation.value=regulation1.value+"-"+regulation2.value;
	}
	if(specialType==0){
		var regulation3 = document.getElementById("regulation3");
		regulation.value=regulation3.value;
	}
var newAnOther=specialRegulationForm.newAnOther.checked;
specialRegulationForm.action="<%=request.getContextPath()%>/pages/rule/specialRegulation!save.action?newAnOther="+newAnOther;
specialRegulationForm.submit();
}
}
</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="specialRegulationForm"
			action="/pages/rule/specialRegulation!save.action" method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0"
				class="popnewdialog1">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="rule.addSpecialRegulation" />
							</legend>
							<table width="100%" align="center">

								<tr>
									<td>
										<s:text name="rule.moneyType" />
										<s:text name="label.colon" />
									</td>
									<td>
										<tm:tmSelect name="moneyType" id="moneyType" selType="dataDir"
											path="ruleMgr.moneyType" />
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
											selType="dataDir" path="ruleMgr.moneyDenomination" />
										<font color="#FF0000">*</font>
									</td>
								</tr>
								<tr>
									<td>
										<s:text name="rule.source" />
										<s:text name="label.colon" />
									</td>
									<td>
										<tm:tmSelect name="source" id="source" selType="dataDir"
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
										<input name="result" type="text" id="result" size="30" />
										<font color="#FF0000">*</font>
									</td>
								</tr>
								<tr>
									<td>
										<s:text name="rule.specialType" />
										<s:text name="label.colon" />
									</td>
									<td>
										<tm:tmSelect name="specialType" id="specialType"
											onchange="f1()" selType="dataDir" path="ruleMgr.specialType" />
										<font color="#FF0000">*</font>
									</td>
								</tr>
								<tr>
									<td>
										<s:text name="rule.regulation" />
										<s:text name="label.colon" />
									</td>
									<td>
										<div id="r1" style="visibility: none;">
											<input name="regulation3" type="text" id="regulation3"
												size="30" maxlength="10" />
											<font color="#FF0000">*</font>
										</div>
										<div id="r2" style="visibility: hidden; display: none;">
											<input name="regulation1" type="text" id="regulation1"
												size="12" maxlength="10" />
											-
											<input name="regulation2" type="text" id="regulation2"
												size="12" maxlength="10" />
											<font color="#FF0000">*</font>
										</div>

										<input name="regulation" type="hidden" id="regulation" />
									</td>
								</tr>

							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<br />
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="label.title" />
							</legend>
							<table width="100%">
								<tr>
									<td>
										<s:text name="label.content" />
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td align="center">
						<br>
						<input type="button" name="ok"
							value='<s:text name="grpInfo.ok" />' class="MyButton"
							onclick="SubmitForm()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
						<input type="checkbox" name="newAnOther" id="newAnOther">
						<label for=newAnOther>
							<s:text name="label.addAnOther" />
						</label>
					</td>
				</tr>
			</table>
		</form>
		<%@ include file="/inc/showActionMessage.inc"%>
	</body>
</html>

