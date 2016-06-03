<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head>   </head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript">
function closeModal(){
 window.close();
}
function update(){
blackRegulationForm.submit();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name=blackRegulationForm  action="<%=request.getContextPath()%>/pages/rule/blackRegulation!update.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add BlackRegulation </legend>
 <s:iterator value="blackRegulation" id="blackRegulation"> 
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value='<s:property value="id"/>' >  </td> 
<td>c_money_type</td> 
  <td><input name="moneyType" type="text" id="moneyType"  size="30" maxlength="20" value='<s:property value="moneyType"/>'>  </td> 
 </tr><tr><td>c_money_denomination</td> 
  <td><input name="moneyDenomination" type="text" id="moneyDenomination"  size="30" maxlength="20" value='<s:property value="moneyDenomination"/>' >  </td> 
<td>c_regulation</td> 
  <td><input name="regulation" type="text" id="regulation"  size="30" maxlength="20" value='<s:property value="regulation"/>'>  </td> 
 </tr><tr><td>c_regulation_status</td> 
  <td><input name="regulationStatus" type="text" id="regulationStatus"  size="30" maxlength="20" value='<s:property value="regulationStatus"/>' >  </td> 
<td>c_termtype</td> 
  <td><input name="termtype" type="text" id="termtype"  size="30" maxlength="20" value='<s:property value="termtype"/>'>  </td> 
 </tr><tr><td>d_create_date</td> 
  <td><input name="createDate" type="text" id="createDate"  size="30" maxlength="20" value='<s:property value="createDate"/>' >  </td> 
<td>c_create_name</td> 
  <td><input name="createName" type="text" id="createName"  size="30" maxlength="20" value='<s:property value="createName"/>'>  </td> 
 </tr><tr><td>d_reversion_date</td> 
  <td><input name="reversionDate" type="text" id="reversionDate"  size="30" maxlength="20" value='<s:property value="reversionDate"/>' >  </td> 
<td>c_reversion_name</td> 
  <td><input name="reversionName" type="text" id="reversionName"  size="30" maxlength="20" value='<s:property value="reversionName"/>'>  </td> 
 </tr></table>
   </s:iterator>
</fieldset></td> </tr> <tr><td align="center"> 
 <input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

