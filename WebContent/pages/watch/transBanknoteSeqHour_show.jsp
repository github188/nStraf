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
<form action="/pages/watch/transBanknoteSeqHour!show.action"   method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> <s:text name="wacth.check"/></legend>
  <s:iterator value="transBanknoteSeqHour" id="transBanknoteSeqHour"> 
<table width="100%" align="center">
 <tr>
  <td><s:text name="watch.termID"/>:</td> 
  <td><s:property value="termid"/></td> 
 </tr>

 <tr>
  <td><s:text name="watch.journal_no"/>:</td> 
  <td><s:property value="journalNo"/> </td> 
 </tr>
 
 <tr>
  <td><s:text name="watch.currency"/>:</td> 
  <td><s:property value="currency"/> </td> 
 </tr>
 <tr>
 	<td><s:text name="watch.sequence"/>:</td> 
    <td><s:property value="sequence"/> </td> 
 </tr>
 
 <tr>
  <td><s:text name="watch.denomination"/>:</td> 
  <td><s:property value="denomination"/></td> 	
 </tr>
 <tr>
<td><s:text name="watch.seriaNo"/>:</td> 
  <td><s:property value="seriaNo"/> </td> 
 </tr>
 <tr>
  <td><s:text name="watch.trans_date"/>:</td> 
  <td><s:property value="transDate"/> </td> 
 </tr>
 <tr>
  <td><s:text name="watch.trans_time"/>:</td> 
  <td><s:property value="tranTime"/></td> 
 </tr>
</table>
</s:iterator>
</fieldset></td> 
</tr>
<tr>
</tr> 
<tr><td align="center"> 
<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

