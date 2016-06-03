<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>

<script type="text/javascript">
function validateInfo(){
if (confirm('<s:text name="confirm.modify.commit"/>')){
	// var reg = /[^\x00-\xff]/;
	// var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
	if (!Validate('conName','Require'))  {
       alert('<s:text name="roleInfo.check.username"/>');
	   return false;
	 }
	  return true;
  } 
}
	
	function formsubmit(){
		if (validateInfo()){
	     window.returnValue=true
		 document.forms[0].submit();
		}
	}
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 300) {
					alert("你输入的字数超过300个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 300);
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
	listenKey();
</script>
<body id="bodyid"  leftmargin="0" topmargin="10">	 
<s:form  name="contactForm" id="contactForm" action="/pages/contact/contactAction!update.action" method="post" >	
<input type="hidden" name="update" value="<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>">
<table  width="90%" align="center" cellPadding="0" cellSpacing="0">
  <tr>
  	<td> 
  			 <table width="90%" class="input_table">
  			 <s:hidden name="userId"></s:hidden> <s:hidden name="id"></s:hidden>
  			 	<tr>
  			 		<td class="input_tablehead"> <s:text  name="operInfo.updateOperator"/> </td>
  			 	</tr>
  			 	<tr  bgcolor="#FFFFFF"> 
		          <td class="input_label2"> <div align="center">
			          	<s:text  name="form.userId"/>
			          <s:text name="label.colon"/></div></td>
		          <td  bgcolor="#FFFFFF"> 
		          	<s:property value="userId"/> 
		          </td>
       			
			         <td class="input_label2"> <div align="center">
				          <s:text  name="label.userName"/>
				      <s:text name="label.colon"/></div></td>
		          <td  bgcolor="#FFFFFF"> 
				          <s:label name="conName" id="conName"/>
			      </td>
                  </tr>
                  
                   <tr> 
            			<td class="input_label2"> <div align="center"><s:text name="form.Tel" /><s:text name="label.colon"/></div></td>
            			<td  bgcolor="#FFFFFF"> 
            				<s:textfield name="conTel" id="conTel" size="30" maxlength="15" onkeyup="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')" /> 
              			</td>
            			<td class="input_label2"> <div align="center"><s:text name="form.Mobile" /><s:text name="label.colon"/></div></td>
            			<td  bgcolor="#FFFFFF"> 
            				<s:textfield name="conMobile" id="conMobile" size="30" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')" />
              			</td>
          			</tr>
          			<tr>
          				 <td   class="input_label2"> <div align="center"><s:text name="form.note" /><s:text name="label.colon"/></div></td>
            			<td  colspan="4"> 
            				<textarea name="note" id="note" cols="90" rows="2" maxLength="150"><s:property value="note"/></textarea>
              			</td> 
              		</tr>
			</table>
	</td>
  </tr>
  <br />
  
  <tr>
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
        <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
      </td>    
  </tr>
</table>
</s:form>
<body>
</body>
</html> 

