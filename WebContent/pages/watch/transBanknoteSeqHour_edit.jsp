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
transBanknoteSeqHourForm.submit();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name=transBanknoteSeqHourForm  action="<%=request.getContextPath()%>/pages/watch/transBanknoteSeqHour!update.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add TransBanknoteSeqHour </legend>
 <s:iterator value="transBanknoteSeqHour" id="transBanknoteSeqHour"> 
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value='<s:property value="id"/>' >  </td> 
<td>c_termid</td> 
  <td><input name="termid" type="text" id="termid"  size="30" maxlength="20" value='<s:property value="termid"/>'>  </td> 
 </tr><tr><td>c_journal_no</td> 
  <td><input name="journalNo" type="text" id="journalNo"  size="30" maxlength="20" value='<s:property value="journalNo"/>' >  </td> 
<td>c_sequence</td> 
  <td><input name="sequence" type="text" id="sequence"  size="30" maxlength="20" value='<s:property value="sequence"/>'>  </td> 
 </tr><tr><td>c_currency</td> 
  <td><input name="currency" type="text" id="currency"  size="30" maxlength="20" value='<s:property value="currency"/>' >  </td> 
<td>c_denomination</td> 
  <td><input name="denomination" type="text" id="denomination"  size="30" maxlength="20" value='<s:property value="denomination"/>'>  </td> 
 </tr><tr><td>c_cash_box_id</td> 
  <td><input name="cashBoxId" type="text" id="cashBoxId"  size="30" maxlength="20" value='<s:property value="cashBoxId"/>' >  </td> 
<td>c_seria_no</td> 
  <td><input name="seriaNo" type="text" id="seriaNo"  size="30" maxlength="20" value='<s:property value="seriaNo"/>'>  </td> 
 </tr><tr><td>c_picture_name</td> 
  <td><input name="pictureName" type="text" id="pictureName"  size="30" maxlength="20" value='<s:property value="pictureName"/>' >  </td> 
<td>c_verify_no</td> 
  <td><input name="verifyNo" type="text" id="verifyNo"  size="30" maxlength="20" value='<s:property value="verifyNo"/>'>  </td> 
 </tr><tr><td>c_url_name</td> 
  <td><input name="urlName" type="text" id="urlName"  size="30" maxlength="20" value='<s:property value="urlName"/>' >  </td> 
<td>c_trans_date</td> 
  <td><input name="transDate" type="text" id="transDate"  size="30" maxlength="20" value='<s:property value="transDate"/>'>  </td> 
 </tr><tr><td>c_tran_time</td> 
  <td><input name="tranTime" type="text" id="tranTime"  size="30" maxlength="20" value='<s:property value="tranTime"/>' >  </td> 
</table>
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

