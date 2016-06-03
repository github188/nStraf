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
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form action="/pages/watch/tranCallbackAbnoinfo!show.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add TranCallbackAbnoinfo </legend>
  <s:iterator value="tranCallbackAbnoinfo" id="tranCallbackAbnoinfo"> 
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value='<s:property value="id"/>' >  </td> 
<td>c_tran_id</td> 
  <td><input name="tranId" type="text" id="tranId"  size="30" maxlength="20" value='<s:property value="tranId"/>' >  </td> 
 </tr><tr><td>c_trans_orgid</td> 
  <td><input name="transOrgid" type="text" id="transOrgid"  size="30" maxlength="20" value='<s:property value="transOrgid"/>' >  </td> 
<td>c_termid</td> 
  <td><input name="termid" type="text" id="termid"  size="30" maxlength="20" value='<s:property value="termid"/>' >  </td> 
 </tr><tr><td>c_trans_date</td> 
  <td><input name="transDate" type="text" id="transDate"  size="30" maxlength="20" value='<s:property value="transDate"/>' >  </td> 
<td>c_trans_time</td> 
  <td><input name="transTime" type="text" id="transTime"  size="30" maxlength="20" value='<s:property value="transTime"/>' >  </td> 
 </tr><tr><td>c_account_no</td> 
  <td><input name="accountNo" type="text" id="accountNo"  size="30" maxlength="20" value='<s:property value="accountNo"/>' >  </td> 
<td>c_journal_no</td> 
  <td><input name="journalNo" type="text" id="journalNo"  size="30" maxlength="20" value='<s:property value="journalNo"/>' >  </td> 
 </tr><tr><td>i_trans_amt</td> 
  <td><input name="transAmt" type="text" id="transAmt"  size="30" maxlength="20" value='<s:property value="transAmt"/>' >  </td> 
<td>i_trans_result</td> 
  <td><input name="transResult" type="text" id="transResult"  size="30" maxlength="20" value='<s:property value="transResult"/>' >  </td> 
 </tr><tr><td>i_trans_notenum</td> 
  <td><input name="transNotenum" type="text" id="transNotenum"  size="30" maxlength="20" value='<s:property value="transNotenum"/>' >  </td> 
<td>i_black_notenum</td> 
  <td><input name="blackNotenum" type="text" id="blackNotenum"  size="30" maxlength="20" value='<s:property value="blackNotenum"/>' >  </td> 
 </tr><tr><td>i_repeat_notenum</td> 
  <td><input name="repeatNotenum" type="text" id="repeatNotenum"  size="30" maxlength="20" value='<s:property value="repeatNotenum"/>' >  </td> 
<td>i_callback_notenum</td> 
  <td><input name="callbackNotenum" type="text" id="callbackNotenum"  size="30" maxlength="20" value='<s:property value="callbackNotenum"/>' >  </td> 
 </tr><tr><td>c_reserve1</td> 
  <td><input name="reserve1" type="text" id="reserve1"  size="30" maxlength="20" value='<s:property value="reserve1"/>' >  </td> 
<td>c_reserve2</td> 
  <td><input name="reserve2" type="text" id="reserve2"  size="30" maxlength="20" value='<s:property value="reserve2"/>' >  </td> 
 </tr><tr><td>c_reserve3</td> 
  <td><input name="reserve3" type="text" id="reserve3"  size="30" maxlength="20" value='<s:property value="reserve3"/>' >  </td> 
<td>c_abno_callback_type</td> 
  <td><input name="abnoCallbackType" type="text" id="abnoCallbackType"  size="30" maxlength="20" value='<s:property value="abnoCallbackType"/>' >  </td> 
 </tr><tr><td>i_out_count</td> 
  <td><input name="outCount" type="text" id="outCount"  size="30" maxlength="20" value='<s:property value="outCount"/>' >  </td> 
<td>i_in_count</td> 
  <td><input name="inCount" type="text" id="inCount"  size="30" maxlength="20" value='<s:property value="inCount"/>' >  </td> 
 </tr><tr><td>c_note</td> 
  <td><input name="note" type="text" id="note"  size="30" maxlength="20" value='<s:property value="note"/>' >  </td> 
<td>c_create_date</td> 
  <td><input name="createDate" type="text" id="createDate"  size="30" maxlength="20" value='<s:property value="createDate"/>' >  </td> 
 </tr><tr><td>d_any_date</td> 
  <td><input name="anyDate" type="text" id="anyDate"  size="30" maxlength="20" value='<s:property value="anyDate"/>' >  </td> 
<td>c_trans_code</td> 
  <td><input name="transCode" type="text" id="transCode"  size="30" maxlength="20" value='<s:property value="transCode"/>' >  </td> 
 </tr></table>
</s:iterator>
</fieldset></td> </tr> 
 </table> <br/> <table width="90%" align="center" cellPadding="0" cellSpacing="0">  <tr><td align="center"> 
<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

