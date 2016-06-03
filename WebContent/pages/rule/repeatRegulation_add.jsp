<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
	function closeModal(){
	 	window.close();
	}
	function save(){
		window.returnValue=true
		blackRegulationForm.submit();
	}
	
	function validateRepeatRegulationForm() {
		var repeatNum = document.getElementById("repeatnum").value;
		var reversionName = document.getElementById("reversionName").value;
		var termid = document.getElementById("termid").value;
		//var actionto = document.getElementById("actionto").value;
		if(repeatNum == "") {
			alert("最多重号数量不能为空！");
			return false;
		}
		if(reversionName == "") {
			alert("请选择下一步处理人！");
			return false;
		}
		if(termid == "") {
			alert("请选择终端！");
			return false;
		}
		return true;
    }
	
	function SubmitForm() {
		if (validateRepeatRegulationForm()){
	        window.returnValue=true;
	        
	        var newAnOther=repeatRegulationForm.newAnOther.checked;
	        repeatRegulationForm.action="<%=request.getContextPath() %>/pages/rule/repeatRegulation!save.action?newAnOther="+newAnOther;
			repeatRegulationForm.submit();
		}
	 }

	//选择流程下一步的OWNER
	function selectOneUser(obj){
		  var objValue=obj.value;
		  //var strUrl="/pages/um/sysUser.do?action=selectOneUser&userid="+objValue;
		  var strUrl="/pages/um/sysUserInfo!getUsrUsrgrp.action";
			var features="80,200,rule.nextUser,rule";
			var returnValue=OpenModal(strUrl,features);
			if(returnValue != null){
				try{
				obj.value = returnValue[0];
				//alert(returnValue.length);
				//blackRegulationForm.reversionName.value=returnValue[0];
				}catch(e){}
			}
	}

	//选择终端
	function selectTml(obj){
		  var objValue=obj.value;
		  //var strUrl="/pages/um/sysUser.do?action=selectOneUser&userid="+objValue;
		  var strUrl="/pages/tmlInfo/tmlInfo!selectTml.action";
			var features="600,400,rule.selectTml,rule";
			var returnValue=OpenModal(strUrl,features);
			if(returnValue != null){
				try{
				obj.value = returnValue[0];
				//blackRegulationForm.termId.value=returnValue[0];
				}catch(e){}
			}
	}
	</script>
	
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="repeatRegulationForm" id="repeatRegulationForm" action="/pages/rule/repeatRegulation!save.action" method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
	<fieldset class="jui_fieldset" width="100%">
		<legend><s:text name="label.new.repeatRegulation" /></legend>
		<table width="100%" align="center" class="popnewdialog1">
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="repeat.info.num"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"> <input name="repeatnum" type="text" id="repeatnum" size="30" maxlength="2"> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="repeat.info.dealMode"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left">
           		<tm:tmSelect name="dealwithMode" id="dealwithMode" selType="dataDir" path="ruleMgr.processModel" /> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="repeat.info.screenMode"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left">
				<tm:tmSelect name="creenMode" id="creenMode" selType="dataDir" path="ruleMgr.greenModel" /> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
        <tr>
			<td width="30%" align="right"> 
				<div align="right"><s:text name="repeat.info.accountMode"/></div>
			</td>
			<td width="5%"  align="left"><s:text name="label.colon"/></td>
			<td width="65%"  align="left"> 
				<tm:tmSelect name="enterAccountMode" id="enterAccountMode" selType="dataDir" path="ruleMgr.accountModel" /> 
				<font color="#FF0000">*</font> 
			</td>
		</tr> 
		<tr>
			<td width="30%" align="right"> 
				<div align="right"><s:text name="repeat.info.logMode"/></div>
			</td>
			<td width="5%"  align="left"><s:text name="label.colon"/></td>
				<td width="65%"  align="left">
					<tm:tmSelect name="logMode" id="logMode" selType="dataDir" path="ruleMgr.logModel" /> 
				</td>
		</tr>
		
		<tr id="selectHandler">
			 <td width="30%" align="right"> 
           		<div align="right"><s:text name="rule.nextUser" /></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
			 <td nowrap colspan="3" width="65%"  align="left">
			 <input type="text" name="reversionName" id="reversionName" readonly="readonly" class="InputReadOnly"/>
					<img src="../../images/share/view.gif" width="18"
					height="17"  
					style="CURSOR: hand" 
					onclick="selectOneUser(repeatRegulationForm.reversionName)"><font color="#FF0000">*</font>
			<!-- <input type="hidden" name="reversionName" id="reversionName" /> -->
			 </td>
		</tr>
		
		<tr id="selectTml">
			 <td width="30%" align="right"> 
           		<div align="right"><s:text name="rule.selectTml" /></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
			 <td nowrap colspan="3" width="65%"  align="left">
			 <input type="text" name="termid" id="termid" readonly="readonly" size="30" class="InputReadOnly"/>
					<img src="../../images/share/view.gif" width="18"
					height="17"  
					style="CURSOR: hand" 
					onclick="selectTml(repeatRegulationForm.termid)"><font color="#FF0000">*</font>
			 <!-- <input type="text" name="termId" id="termId" /> -->
			 </td>
		</tr>
		
		</table>
	</fieldset>
</td> 
</tr>
<tr>
     <td>
        <fieldset class="jui_fieldset" width="100%">
            <legend><s:text name="label.title"/></legend>
                  <table width="100%">
                      <tr>
                          <td><s:text name="label.content"/></td>
                      </tr>
                      <tr style="padding-bottom: 10px;">
                      	  <td><s:text name="tips.regualtion" /></td>
                      </tr>
                  </table>
              </fieldset>
          </td>
</tr>
<tr>
	<td align="center"> 
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' onclick="SubmitForm()" class="MyButton" image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		<input type="checkbox" name="newAnOther" id="newAnOther">
        <label for=newAnOther><s:text name="label.addAnOther"/></label>
	</td> 
</tr>  
</table> 
</form>

<%@ include file="/inc/showActionMessage.inc"%>
</body>  
</html>