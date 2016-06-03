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
transHourInfoForm.submit();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name=transHourInfoForm  action="<%=request.getContextPath()%>/pages/transHourInfo/transHourInfo!update.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add TransHourInfo </legend>
 <s:iterator value="transHourInfo" id="transHourInfo"> 
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value='<s:property value="id"/>' >  </td> 
<td><s:text name="watch.trans_code"/></td> 
  <td><input name="transCode" type="text" id="transCode"  size="30" maxlength="20" value='<s:property value="transCode"/>'>  </td> 
 </tr><tr><td><s:text name="watch.trans_orgid"/></td> 
  <td><input name="transOrgid" type="text" id="transOrgid"  size="30" maxlength="20" value='<s:property value="transOrgid"/>' >  </td> 
<td><s:text name="watch.termid"/></td> 
  <td><input name="termid" type="text" id="termid"  size="30" maxlength="20" value='<s:property value="termid"/>'>  </td> 
 </tr><tr><td><s:text name="watch.trans_date"/></td> 
  <td><input name="transDate" type="text" id="transDate"  size="30" maxlength="20" value='<s:property value="transDate"/>' >  </td> 
<td><s:text name="watch.trans_time"/></td> 
  <td><input name="transTime" type="text" id="transTime"  size="30" maxlength="20" value='<s:property value="transTime"/>'>  </td> 
 </tr><tr><td><s:text name="watch.account_no"/></td> 
  <td><input name="accountNo" type="text" id="accountNo"  size="30" maxlength="20" value='<s:property value="accountNo"/>' >  </td> 
<td><s:text name="watch.journal_no"/></td> 
  <td><input name="journalNo" type="text" id="journalNo"  size="30" maxlength="20" value='<s:property value="journalNo"/>'>  </td> 
 </tr><tr><td><s:text name="watch.trans_amt"/></td> 
  <td><input name="transAmt" type="text" id="transAmt"  size="30" maxlength="20" value='<s:property value="transAmt"/>' >  </td> 
<td><s:text name="watch.trans_result"/></td> 
  <td><input name="transResult" type="text" id="transResult"  size="30" maxlength="20" value='<s:property value="transResult"/>'>  </td> 
 </tr><tr><td><s:text name="watch.trans_status"/></td> 
  <td><input name="transStatus" type="text" id="transStatus"  size="30" maxlength="20" value='<s:property value="transStatus"/>' >  </td> 
<td><s:text name="watch.note_count"/></td> 
  <td><input name="noteCount" type="text" id="noteCount"  size="30" maxlength="20" value='<s:property value="noteCount"/>'>  </td> 
 </tr><tr><td><s:text name="watch.reserve1"/></td> 
  <td><input name="reserve1" type="text" id="reserve1"  size="30" maxlength="20" value='<s:property value="reserve1"/>' >  </td> 
<td><s:text name="watch.reserve2"/></td> 
  <td><input name="reserve2" type="text" id="reserve2"  size="30" maxlength="20" value='<s:property value="reserve2"/>'>  </td> 
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

